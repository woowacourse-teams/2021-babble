#!/usr/bin/python

# Copyright 2014 Amazon.com, Inc. or its affiliates. All Rights Reserved.
#
# Licensed under the Amazon Software License (the "License"). You may not use
# this file except in compliance with the License. A copy of the License is
# located at http://aws.amazon.com/asl/ or in the "license" file accompanying
# this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
# CONDITIONS OF ANY KIND, either express or implied. See the License for the specific
# language governing permissions and limitations under the License.
#
#
# Script Version: 1.4.9
#

from optparse import OptionParser
import os
import platform
import re
import subprocess
import sys
import shutil


from contextlib import contextmanager

try:
    # 3.x imports
    import configparser
    from urllib.request import urlopen
except ImportError:
    # 2.x imports
    import ConfigParser as configparser
    from urllib import urlopen

LAUNCHER_SCRIPT = """#!/bin/sh
# Version: $VERSION$
echo -n $$ > $PIDFILE$
[ -f $AWS_PROXY_CONFIG_FILE$ ] && . $AWS_PROXY_CONFIG_FILE$
/usr/bin/env -i HTTPS_PROXY=$HTTPS_PROXY HTTP_PROXY=$HTTP_PROXY NO_PROXY=$NO_PROXY AWS_CONFIG_FILE=$AWS_CONFIG_FILE$ HOME=$HOME$ $NICE_PATH$ -n 4 $CLIPATH$ logs push --config-file $AGENT_CONFIG_FILE$ --additional-configs-dir $AGENT_ADDITIONAL_CONFIGS_DIR$ >> /var/log/awslogs.log 2>&1
"""

NANNY_SCRIPT = """#!/bin/sh
# Version: $VERSION$
PIDFILE=$PIDFILE$
LOCKFILE=$LOCKFILE$

# Bail out if PID file doesn't exist. It implies that the
# process was never started.
if [ ! -f $PIDFILE ]
then
    exit 0
fi

$PS_PATH$ -C -p `$CAT_PATH$ $PIDFILE` | $GREP_PATH$ "$AGENT_LAUNCHER$" > /dev/null
RETVAL=$?
echo
[ $RETVAL != 0 ] && [ -f $LOCKFILE ] && $SERVICE_PATH$ awslogs restart
"""

AGENT_LOG_ROTATE_CONFIG = """
# Version: $VERSION$
/var/log/awslogs.log {
    $SU_FOR_UBUNTU$
    missingok
    notifempty
    size 100M
    create 0600 root root
    delaycompress
    compress
    rotate 4
    postrotate
        service awslogs restart
    endscript
}
"""

NANNY_CRON_CMD = """#!/bin/bash
# Version: {VERSION}
MAILTO=""
PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin

* * * * * root {AGENT_NANNY_PATH} > /dev/null 2>&1
"""

LOGROTATE_CRON_CMD = """#!/bin/bash
# Version: {VERSION}
MAILTO=""
PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin

30 * * * * root logrotate -s /var/log/logstatus /etc/logrotate.d/awslogs
"""

PROXY_CONFIG = """
# Version: {VERSION}
# Refer to http://docs.aws.amazon.com/cli/latest/userguide/cli-http-proxy.html for details.
HTTP_PROXY={http_proxy}
HTTPS_PROXY={https_proxy}
NO_PROXY={no_proxy}"""

AWSLOGS_VERSION = "1.4.9"
HOME = os.path.expandvars("$HOME")
AWSLOGS_HOME = "/var/awslogs"
AWSLOGS_BIN = AWSLOGS_HOME + "/bin"
AWSCLI_CMD = AWSLOGS_BIN + "/aws"
VIRTUALENV_ACTIVATE_CMD = AWSLOGS_BIN + "/activate"
AGENT_STATE_DIR = AWSLOGS_HOME + "/state"
AGENT_ETC_DIR = AWSLOGS_HOME + "/etc"
AGENT_SETUP_LOG_FILE = "/var/log/awslogs-agent-setup.log"
AGENT_CONFIG_FILE = AGENT_ETC_DIR + "/awslogs.conf"
AGENT_ADDITIONAL_CONFIGS_DIR = AGENT_ETC_DIR + "/config"
AWS_CONFIG_FILE = AGENT_ETC_DIR + "/aws.conf"
AWS_PROXY_CONFIG_FILE = AGENT_ETC_DIR + "/proxy.conf"
AGENT_LAUNCHER = AWSLOGS_BIN + "/awslogs-agent-launcher.sh"
AGENT_NANNY_PATH = AWSLOGS_BIN + "/awslogs-nanny.sh"
AGENT_LOCK_FILE = AGENT_STATE_DIR + "/awslogs.lock"
AGENT_MUTEX_FILE = AGENT_STATE_DIR + "/awslogs.mutex"
AGENT_START_FLAG_FILE = AGENT_STATE_DIR + "/starting.flag"
AGENT_PID_FILE = AGENT_STATE_DIR + "/awslogs.pid"

AWSLOGS_VERSION_SCRIPT_PATH = AWSLOGS_BIN + "/awslogs-version.sh"
AWSLOGS_GENERATED_FILES_LOG = AGENT_STATE_DIR + "/generated-files"
AGENT_DOWNLOAD_STANDALONE_MAX_NUM = 5

DEFAULT_ENV = dict(AWS_CONFIG_FILE=AWS_CONFIG_FILE, HOME=HOME)

DAEMON_SCRIPT = """#!/bin/sh

### BEGIN INIT INFO
# Provides:          awslogs
# Required-Start:    networking
# Required-Stop:     networking
# Default-Start:     2 3 4 5
# Default-Stop:      0 1 6
# Short-Description: Daemon for AWSLogs agent.
# Description:       This script is responsible for running AWSLogs agent
#                    as a daemon.
### END INIT INFO

# Version: $VERSION$

CONFIG_FILE=$AGENT_CONFIG_FILE$
DAEMON=$AGENT_LAUNCHER$
DAEMON_NAME=awslogs

DAEMON_USER=root
PIDFILE=$PIDFILE$
LOCKFILE=$LOCKFILE$
MUTEXFILE=$MUTEXFILE$

START_CMD="start-stop-daemon --start --background --pidfile $PIDFILE --user $DAEMON_USER --chuid $DAEMON_USER --startas $DAEMON"
STATUS_CMD="status_of_proc -p $PIDFILE "$DAEMON_NAME" "$DAEMON" && exit 0 || exit $?"

RETVAL=0

legacy_start() {
    echo "Starting $DAEMON_NAME daemon"
    daemon --pidfile $PIDFILE --user=$DAEMON_USER "nohup $DAEMON > /dev/null 2>&1 &"
}

if [ -e /etc/init.d/functions ]; then
    . /etc/rc.d/init.d/functions
    START_CMD=legacy_start
    STATUS_CMD="status -p $PIDFILE"
elif [ -e /lib/lsb/init-functions ]; then
    . /lib/lsb/init-functions
else
    echo "Failed to detect LSB compliant init-functions."
    exit 1
fi

do_start () {
    (
        flock -w 10 -x 9
        echo "Starting system $DAEMON_NAME daemon"
        $START_CMD
        RETVAL=$?
        touch $LOCKFILE

        for i in {1..5}
        do
            $STATUS_CMD
            if [ $? -ne 0 ];then
                echo "`date +%F_%T_%N` status is not running, sleep 2 second"
                sleep 2
            else
                break
            fi
        done
    ) 9>$MUTEXFILE
    rm -f $MUTEXFILE
}

do_stop () {
    (
        flock -w 10 -x 9
        echo "Stopping system $DAEMON_NAME daemon"
        ppids=`ps axo pid,comm | grep "awslogs-agent-l" | awk '{print $1}'`
        for pid in $ppids; do
            pkill -15 -P $pid > /dev/null 2>&1
        done

        sleep 3
        procList=`ps axo pid,comm | grep "awslogs-agent-l"`
        procsAlive=$?
        ppids=`${procList} | awk '{print $1}'`
        if [ $procsAlive = 0 ]; then
            for pid in $ppids; do
                pkill -9 -P $pid > /dev/null 2>&1
            done
        fi

        procList=`ps axo pid,comm | grep "awslogs-agent-l"`
        if [ $? != 0 ]; then
            RETVAL=0
        else
            RETVAL=1
        fi

        rm -f $LOCKFILE
    ) 9>$MUTEXFILE
    rm -f $MUTEXFILE
}

do_restart () {
    do_stop
    do_start
}

case "$1" in

    start)
        do_start
        ;;
    stop)
        do_stop
        ;;
    restart)
        do_restart
        ;;
    status)
        $STATUS_CMD
        ;;
    *)
        echo "Usage: /etc/init.d/$DAEMON_NAME {start|stop|restart|status}"
        exit 1
        ;;

esac
exit $RETVAL
"""

AWSLOGS_VERSION_SCRIPT = """#!/bin/sh
FILES=$AWSLOGS_GENERATED_FILES_LOG$
for file in `cat ${FILES}`; do
    if [ -e $file ]; then
        echo "${file} version:"
        cat ${file} | grep "# Version: "
    else
        echo "$file not found on system"
    fi
done

echo "CloudWatch Logs Plugin Version:"
/var/awslogs/bin/pip show awscli-cwlogs

echo "AWS CLI Version:"
/var/awslogs/bin/pip show awscli
"""


DEFAULT_CONFIG = """
#
# ------------------------------------------
# CLOUDWATCH LOGS AGENT CONFIGURATION FILE
# ------------------------------------------
#
# --- DESCRIPTION ---
# This file is used by the CloudWatch Logs Agent to specify what log data to send to the service and how.
# You can modify this file at any time to add, remove or change configuration.
#
# NOTE: A running agent must be stopped and restarted for configuration changes to take effect.
#
# --- CLOUDWATCH LOGS DOCUMENTATION ---
# https://aws.amazon.com/documentation/cloudwatch/
#
# --- CLOUDWATCH LOGS CONSOLE ---
# https://console.aws.amazon.com/cloudwatch/home?region=us-east-1#logs:
#
# --- AGENT COMMANDS ---
# To check or change the running status of the CloudWatch Logs Agent, use the following:
#
# To check running status: /etc/init.d/awslogs status
# To stop the agent: /etc/init.d/awslogs stop
# To start the agent: /etc/init.d/awslogs start
#
# --- AGENT LOG OUTPUT ---
# You can find logs for the agent in /var/log/awslogs.log
# You can find logs for the agent script in /var/log/awslogs-agent-setup.log
#

# ------------------------------------------
# CONFIGURATION DETAILS
# ------------------------------------------
# Refer to http://docs.aws.amazon.com/AmazonCloudWatch/latest/DeveloperGuide/AgentReference.html for details.

[general]
# Path to the CloudWatch Logs agent's state file. The agent uses this file to maintain
# client side state across its executions.
state_file = /var/awslogs/state/agent-state

## This queue size determine the max number of event batch loaded into memory. 10 is the default value.
## It can be reduced if the program consumes too much memory. 1 is the valid minimum value.
#queue_size = 10
#
## Each log file is defined in its own section. The section name doesn't
## matter as long as its unique within this file.
#[kern.log]
#
## Path of log file for the agent to monitor and upload.
#file = /var/log/kern.log
#
## Name of the destination log group.
#log_group_name = kern.log
#
## Name of the destination log stream. You may use {hostname} to use target machine's hostname.
#log_stream_name = {instance_id} # Defaults to ec2 instance id
#
## Format specifier for timestamp parsing. Here are some sample formats:
## Use '%b %d %H:%M:%S' for syslog (Apr 24 08:38:42)
## Use '%d/%b/%Y:%H:%M:%S' for apache log (10/Oct/2000:13:55:36)
## Use '%Y-%m-%d %H:%M:%S' for rails log (2008-09-08 11:52:54)
#datetime_format = %b %d %H:%M:%S # Specification details in the table below.
#
## A batch is buffered for buffer-duration amount of time or 32KB of log events.
## Defaults to 5000 ms and its minimum value is 5000 ms.
#buffer_duration = 5000
#
# Use 'end_of_file' to start reading from the end of the file.
# Use 'start_of_file' to start reading from the beginning of the file.
#initial_position = start_of_file
#
## Encoding of file
#encoding = utf-8 # Other supported encodings include: ascii, latin-1
#
#
#
# Following table documents the detailed datetime format specification:
# ----------------------------------------------------------------------------------------------------------------------
# Directive     Meaning                                                                                 Example
# ----------------------------------------------------------------------------------------------------------------------
# %a            Weekday as locale's abbreviated name.                                                   Sun, Mon, ..., Sat (en_US)
# ----------------------------------------------------------------------------------------------------------------------
#  %A           Weekday as locale's full name.                                                          Sunday, Monday, ..., Saturday (en_US)
# ----------------------------------------------------------------------------------------------------------------------
#  %w           Weekday as a decimal number, where 0 is Sunday and 6 is Saturday.                       0, 1, ..., 6
# ----------------------------------------------------------------------------------------------------------------------
#  %d           Day of the month as a zero-padded decimal numbers.                                      01, 02, ..., 31
# ----------------------------------------------------------------------------------------------------------------------
#  %b           Month as locale's abbreviated name.                                                     Jan, Feb, ..., Dec (en_US)
# ----------------------------------------------------------------------------------------------------------------------
#  %B           Month as locale's full name.                                                            January, February, ..., December (en_US)
# ----------------------------------------------------------------------------------------------------------------------
#  %m           Month as a zero-padded decimal number.                                                  01, 02, ..., 12
# ----------------------------------------------------------------------------------------------------------------------
#  %y           Year without century as a zero-padded decimal number.                                   00, 01, ..., 99
# ----------------------------------------------------------------------------------------------------------------------
#  %Y           Year with century as a decimal number.                                                  1970, 1988, 2001, 2013
# ----------------------------------------------------------------------------------------------------------------------
#  %H           Hour (24-hour clock) as a zero-padded decimal number.                                   00, 01, ..., 23
# ----------------------------------------------------------------------------------------------------------------------
#  %I           Hour (12-hour clock) as a zero-padded decimal numbers.                                  01, 02, ..., 12
# ----------------------------------------------------------------------------------------------------------------------
#  %p           Locale's equivalent of either AM or PM.                                                 AM, PM (en_US)
# ----------------------------------------------------------------------------------------------------------------------
#  %M           Minute as a zero-padded decimal number.                                                 00, 01, ..., 59
# ----------------------------------------------------------------------------------------------------------------------
#  %S           Second as a zero-padded decimal numbers.                                                00, 01, ..., 59
# ----------------------------------------------------------------------------------------------------------------------
#  %f           Microsecond as a decimal number, zero-padded on the left.                               000000, 000001, ..., 999999
# ----------------------------------------------------------------------------------------------------------------------
#  %z           UTC offset in the form +HHMM or -HHMM (empty string if the the object is naive).        (empty), +0000, -0400, +1030
# ----------------------------------------------------------------------------------------------------------------------
#  %j           Day of the year as a zero-padded decimal number.                                        001, 002, ..., 365
# ----------------------------------------------------------------------------------------------------------------------
#  %U           Week number of the year (Sunday as the first day of the week) as a zero padded          00, 01, ..., 53
#               decimal number. All days in a new year preceding the first Sunday are considered
#               to be in week 0.
# ----------------------------------------------------------------------------------------------------------------------
#  %W           Week number of the year (Monday as the first day of the week) as a decimal number.      00, 01, ..., 53
#               All days in a new year preceding the first Monday are considered to be in week 0.
# ----------------------------------------------------------------------------------------------------------------------
#  %c           Locale's appropriate date and time representation.                                      Tue Aug 16 21:30:00 1988 (en_US)
# ----------------------------------------------------------------------------------------------------------------------


"""

#
# Return codes used in this script.
#
INVALID_PARAMETERS = 1
NOT_ROOT = 2
PLATFORM_NOT_SUPPORTED = 3
CONFIG_FILE_NOT_FOUND = 4
UNSUPPORTED_REGION = 5
MISSING_DEPENDENCY = 6
STANDALONE_INSTALLATION_FAILURE = 7
STANDALONE_DOWNLOAD_FAILURE = 8

class CloudWatchLogsAgentSetup:
    Rhel, Ubuntu, AmazonLinux, CentOS, Raspbian, Debian = range(6)

    installer_name_map = {Rhel: "yum",
                          Ubuntu: "apt-get",
                          AmazonLinux: "yum",
                          CentOS: "yum",
                          Raspbian: "apt-get",
                          Debian: "apt-get"}

    def __init__(self, log_file, options):
        self.log_file = log_file
        self.non_interactive = options.non_interactive
        self.only_generate_config = options.only_generate_config
        self.region = options.region
        self.config_file = options.config_file
        self.os_flavor = self.get_distro_info()
        self.plugin_url = options.plugin_url
        self.python = options.python
        self.http_proxy = options.http_proxy if options.http_proxy else ""
        self.https_proxy = options.https_proxy if options.https_proxy else ""
        self.no_proxy = options.no_proxy if options.no_proxy else ""
        self.generated_filepaths = []
        self.dependency_path = options.dependency_path if options.dependency_path else ""
        self.is_standalone = not self.dependency_path == ""
        self.ca_bundle = options.ca_bundle if options.ca_bundle else ""
        self.cleanup_tmp_files = False
        # install awslogsplugin from standalone.
        if not self.dependency_path:
            self.init_dependency_path()

    def init_dependency_path(self):
        url = 'https://s3.amazonaws.com/aws-cloudwatch/downloads/latest/AgentDependencies.tar.gz'
        import os
        from datetime import datetime
        self.tmp_dependency_file_dir = "/tmp/AgentDependencies_tmp." + str(os.getpid()) + "." + str(datetime.now().microsecond)
        subprocess.call(["mkdir", '-p', self.tmp_dependency_file_dir], env=DEFAULT_ENV)
        self.tmp_dependency_file_path = self.tmp_dependency_file_dir + "/AgentDependencies.tar.gz"

        for i in range(0, AGENT_DOWNLOAD_STANDALONE_MAX_NUM):
            try:
                try:
                    message("downloading AgentDependencies.tar.gz with urllib\n")
                    import urllib
                    urllib.urlretrieve(url, self.tmp_dependency_file_path)
                except:
                    message("downloading AgentDependencies.tar.gz with urllib.request\n")
                    import urllib.request
                    urllib.request.urlretrieve(url, self.tmp_dependency_file_path)
                break
            except:
                if i >= AGENT_DOWNLOAD_STANDALONE_MAX_NUM - 1 :
                    fail('Failed to download standalone package: %s. Exit.' % url, STANDALONE_DOWNLOAD_FAILURE)
                message("To retry to download standalone packge %s after while \n" % url)
                import time
                time.sleep(2)

        # tar xvf AgentDependencies.tar.gz -C /tmp/
        subprocess.call(["tar", 'xvf', self.tmp_dependency_file_path, '-C', self.tmp_dependency_file_dir], env=DEFAULT_ENV)
        self.dependency_path = self.tmp_dependency_file_dir + "/AgentDependencies"
        self.is_standalone = True
        self.cleanup_tmp_files = True

    def _cleanup_tmp_files(self):
        if self.cleanup_tmp_files:
            subprocess.call(["rm", '-rf', self.tmp_dependency_file_dir], env=DEFAULT_ENV)

    def write_launcher_file(self):
        file_path = AGENT_LAUNCHER
        nicepath = subprocess.Popen(['which', 'nice'], stdout=subprocess.PIPE, universal_newlines=True).communicate()[0].rstrip()
        contents = LAUNCHER_SCRIPT.replace("$REGION$", self.region)
        contents = contents.replace("$HOME$", HOME)
        contents = contents.replace("$NICE_PATH$", nicepath)
        contents = contents.replace("$CLIPATH$", AWSCLI_CMD)
        contents = contents.replace("$AWS_PROXY_CONFIG_FILE$", AWS_PROXY_CONFIG_FILE)
        contents = contents.replace("$PIDFILE$", AGENT_PID_FILE)
        contents = contents.replace("$AGENT_CONFIG_FILE$", AGENT_CONFIG_FILE)
        contents = contents.replace("$AGENT_ADDITIONAL_CONFIGS_DIR$", AGENT_ADDITIONAL_CONFIGS_DIR)
        contents = contents.replace("$AWS_CONFIG_FILE$", AWS_CONFIG_FILE)
        contents = contents.replace("$VERSION$", AWSLOGS_VERSION)
        with open(file_path, "w") as file:
            file.write(contents)
        os.system("chmod 755 " + file_path)
        self.log_generated_file(file_path)

    def write_daemon_script(self):
        file_path = "/etc/init.d/awslogs"
        contents = DAEMON_SCRIPT
        contents = contents.replace("$PIDFILE$", AGENT_PID_FILE)
        contents = contents.replace("$LOCKFILE$", AGENT_LOCK_FILE)
        contents = contents.replace("$MUTEXFILE$", AGENT_MUTEX_FILE)
        contents = contents.replace("$AGENT_LAUNCHER$", AGENT_LAUNCHER)
        contents = contents.replace("$AGENT_CONFIG_FILE$", AGENT_CONFIG_FILE)
        contents = contents.replace("$VERSION$", AWSLOGS_VERSION)
        with open(file_path, "w") as file:
            file.write(contents)
        os.system("chmod 755 " + file_path)
        self.log_generated_file(file_path)

    def get_distro_info(self):
        default_issue_file_path = "/etc/issue"
        redhat_version_file_path = "/etc/redhat-release"
        if os.path.isfile(redhat_version_file_path):
            issue_file_path = redhat_version_file_path
        elif os.path.isfile(default_issue_file_path):
            issue_file_path = default_issue_file_path
        else:
            fail("Failed to determine linux distribution. Exiting.", PLATFORM_NOT_SUPPORTED)

        # Support Amazon Linux, Ubuntu, CentOS, Debian, Raspbian and RHEL.
        with open(issue_file_path, "r") as issue_file:
            line = issue_file.readline()
            if line.startswith("Amazon Linux AMI"):
                return self.AmazonLinux
            elif line.startswith("Ubuntu"):
                os.environ['DEBIAN_FRONTEND'] = 'noninteractive'
                return self.Ubuntu
            elif line.startswith("Red Hat"):
                return self.Rhel
            elif line.startswith("CentOS"):
                return self.CentOS
            elif line.startswith("Raspbian"):
                return self.Raspbian
            elif line.startswith("Debian"):
                os.environ['DEBIAN_FRONTEND'] = 'noninteractive'
                return self.Debian
            else:
                fail("Failed to determine linux distribution. Exiting.", PLATFORM_NOT_SUPPORTED)

    def get_installer_name(self):
        return self.installer_name_map[self.os_flavor]

    def install(self, package):
        installer = self.get_installer_name()
        subprocess.call([installer, 'install', '-y', package], stderr=self.log_file, stdout=self.log_file)

    def install_awslogs_cli(self):

        if self.is_standalone:
            # If this is standalone installation, use the virtual env
            # package in the dependency folder to set up AWSLOGS_HOME as
            # the virtual env.
            self.validate_dependency_path(self.dependency_path)
            self.setup_virtualenv()
        else:
            # No local dependency folder provided, install virtualenv
            # from pip.
            self.do_pip_install('virtualenv', False)

        # Do not make AWSLOGS_HOME virtualenv again if it's done before.
        # The cmd will fail to overwrite running executable, e.g. python.
        if not os.path.exists(VIRTUALENV_ACTIVATE_CMD):

            venv_in_path = (subprocess.call(["which", "virtualenv"], stderr=self.log_file, stdout=self.log_file) == 0)
            if venv_in_path:
                venv_cmd = ["virtualenv"]
            else:
                # Try some explicit paths for venv
                if os.path.exists("/usr/bin/virtualenv"):
                    venv_cmd = ["/usr/bin/virtualenv"]
                elif os.path.exists("/usr/local/bin/virtualenv"):
                    venv_cmd = ["/usr/local/bin/virtualenv"]
                else:
                    fail("The path of the virtualenv command is unknown. Try manually installing with pip and adding it to the sudo user's PATH before running this script.", MISSING_DEPENDENCY)
            if self.python:
                venv_cmd.append('--python=' + self.python)
            venv_cmd.append(AWSLOGS_HOME)
            venv_response = subprocess.call(venv_cmd,
                                            stderr=self.log_file,
                                            stdout=self.log_file)
            if venv_response != 0:
                fail("Failed to create virtualenv. Try manually installing with pip and adding it to the sudo user's PATH before running this script.", MISSING_DEPENDENCY)

        if not self.is_standalone:
            message("Checking version of pip for s3 compatibility ... \n")
            self.do_pip_install('pip<7.0.0')
        python_version = sys.version_info
        need_wheel_29 = False
        if python_version >= (2, 6) and python_version < (2, 7):
            need_wheel_29 = True
        if self.is_standalone:
            plugin_path = os.path.join(self.dependency_path, 'awslogscli')
            # Note: As part of the standalone installation feature, the version
            # of the awscli that the Logs plugin dependes on has been bumped up
            # and so is the version number of the Logs Plugin itself.
            # For standalone installation, the dependency packages will be vended
            # in an S3 bucket, so using the bumped up version here. For standard
            # installation, the pypy repository would need awscli-cwlogs==1.4.1
            # to be made available before we can refer to it in the script.
            if need_wheel_29:
                self.do_pip_install('wheel==0.29.0', True, None, True, self.plugin_url)
            self.do_pip_install('awscli-cwlogs==1.4.6', True, plugin_path, True)
        else:
            # Check for plugin_url if we are not doing Standalone installation
            # For standalone installation we will ignore plugin url and simply
            # install from the dependency folder.
            if self.plugin_url:
                if need_wheel_29:
                    self.do_pip_install('wheel==0.29.0', True, None, True, self.plugin_url)
                self.do_pip_install('awscli-cwlogs==1.4.6', True, None, True, self.plugin_url)
            else:
                if need_wheel_29:
                    self.do_pip_install('wheel==0.29.0', True, None, True)
                self.do_pip_install('awscli-cwlogs==1.4.6', True, None, True)

        # Setup awslogs plugin
        subprocess.call([AWSCLI_CMD, 'configure', 'set', 'plugins.cwlogs', 'cwlogs'], env=DEFAULT_ENV)

        # Setup the default region for the CLI
        subprocess.call([AWSCLI_CMD, 'configure', 'set', 'default.region', self.region], env=DEFAULT_ENV)

        # If the user provided a path to a CA certificate bundle to use when
        # verifying SSL certificates, configure the default ca_bundle on the aws cli.
        if not self.ca_bundle == "":
            subprocess.call([AWSCLI_CMD, 'configure', 'set', 'default.ca_bundle', self.ca_bundle], env=DEFAULT_ENV)

    def cleanup_existing_logs_binaries(self):
        os.system("rm -rf " + AWSLOGS_HOME + "/bin")
        os.system("rm -rf " + AWSLOGS_HOME + "/lib")
        os.system("rm -rf " + AWSLOGS_HOME + "/lib64")
        os.system("rm -rf " + AWSLOGS_HOME + "/include")
        os.system("rm -rf " + AWSLOGS_HOME + "/local")

    @staticmethod
    def aws_configure():
        # Run aws configure to setup credentials.
        subprocess.call([AWSCLI_CMD, 'configure'], env=DEFAULT_ENV)

    def write_agent_config_file(self):
        if self.config_file.startswith("http"):
            file_path = AGENT_CONFIG_FILE
            contents = urlopen(self.config_file).read().decode("utf-8")
            with open(file_path, "w") as file:
                file.write(contents)
            self.config_file = file_path
        elif self.config_file.startswith("s3"):
            # aws s3 cp s3://BUCKET/remote_file local_file
            subprocess.call([AWSCLI_CMD, 's3', 'cp', self.config_file, AGENT_CONFIG_FILE], env=DEFAULT_ENV)
        elif not os.path.isfile(self.config_file):
            fail("Config file %s doesn't exist." % self.config_file, CONFIG_FILE_NOT_FOUND)
        else:
            shutil.copyfile(self.config_file, AGENT_CONFIG_FILE)

    def setup_initd(self):
        os.system("ln -f -s /etc/init.d/awslogs /etc/rc0.d/K99awslogs")
        os.system("ln -f -s /etc/init.d/awslogs /etc/rc1.d/K99awslogs")
        os.system("ln -f -s /etc/init.d/awslogs /etc/rc2.d/K99awslogs")
        os.system("ln -f -s /etc/init.d/awslogs /etc/rc3.d/K99awslogs")
        os.system("ln -f -s /etc/init.d/awslogs /etc/rc4.d/K99awslogs")
        os.system("ln -f -s /etc/init.d/awslogs /etc/rc5.d/K99awslogs")
        os.system("ln -f -s /etc/init.d/awslogs /etc/rc6.d/K99awslogs")

        os.system("ln -f -s /etc/init.d/awslogs /etc/rc0.d/S99awslogs")
        os.system("ln -f -s /etc/init.d/awslogs /etc/rc1.d/S99awslogs")
        os.system("ln -f -s /etc/init.d/awslogs /etc/rc2.d/S99awslogs")
        os.system("ln -f -s /etc/init.d/awslogs /etc/rc3.d/S99awslogs")
        os.system("ln -f -s /etc/init.d/awslogs /etc/rc4.d/S99awslogs")
        os.system("ln -f -s /etc/init.d/awslogs /etc/rc5.d/S99awslogs")
        os.system("ln -f -s /etc/init.d/awslogs /etc/rc6.d/S99awslogs")

        self.log_generated_file("/etc/rc0.d/K99awslogs")
        self.log_generated_file("/etc/rc1.d/K99awslogs")
        self.log_generated_file("/etc/rc2.d/K99awslogs")
        self.log_generated_file("/etc/rc3.d/K99awslogs")
        self.log_generated_file("/etc/rc4.d/K99awslogs")
        self.log_generated_file("/etc/rc5.d/K99awslogs")
        self.log_generated_file("/etc/rc6.d/K99awslogs")

        self.log_generated_file("/etc/rc0.d/S99awslogs")
        self.log_generated_file("/etc/rc1.d/S99awslogs")
        self.log_generated_file("/etc/rc2.d/S99awslogs")
        self.log_generated_file("/etc/rc3.d/S99awslogs")
        self.log_generated_file("/etc/rc4.d/S99awslogs")
        self.log_generated_file("/etc/rc5.d/S99awslogs")
        self.log_generated_file("/etc/rc6.d/S99awslogs")

        if self.os_flavor == self.Debian or self.os_flavor == self.Ubuntu:
            subprocess.call(['/usr/sbin/update-rc.d', 'awslogs', 'defaults'], env=DEFAULT_ENV, stderr=self.log_file, stdout=self.log_file)

    def setup_agent_as_daemon(self):
        # We use init.d for AmazonLinux and Upstart for Ubuntu.
        if self.os_flavor == self.AmazonLinux or self.os_flavor == self.Ubuntu or self.os_flavor == self.Rhel or self.os_flavor == self.CentOS or self.os_flavor == self.Raspbian or self.os_flavor == self.Debian:
            self.setup_initd()
        else:
            fail("Unsupported platform.", PLATFORM_NOT_SUPPORTED)

    @contextmanager
    def cd(self, dirname):
        original = os.getcwd()
        os.chdir(dirname)
        try:
            yield
        finally:
            os.chdir(original)

    def do_pip_install(self, package_descriptor, virtual_env_pip=True, folder_path=None, do_upgrade=False, extra_index_url=None):
        """
        Install packages using pip. Generates the command for the pip
        install based on the parameters provided. For standalone installation
        defaults to the path of the dependency folder unless an explicit
        folder_path is passed in.
        """
        pip_path = AWSLOGS_BIN + '/pip'
        if not virtual_env_pip:
            pip_path = 'pip'

        install_cmd = [pip_path, 'install']

        if do_upgrade:
            install_cmd.append('--upgrade')

        if self.is_standalone:
            install_cmd.extend(['--no-index', '--find-links'])
            if folder_path is None:
                folder_path = self.dependency_path
            install_cmd.append(folder_path)

        install_cmd.append(package_descriptor)

        if extra_index_url and not self.is_standalone:
            install_cmd.append('--extra-index-url=' + extra_index_url)

        subprocess.call(install_cmd,
                        stderr=self.log_file,
                        stdout=self.log_file)

        # Make AWSLOGS_HOME as the virtual env, which will also make
    # pip available in the same folder.
    def setup_virtualenv(self):
        dependency_dir = self.dependency_path
        virtual_env_dir = ""

        with self.cd(dependency_dir):
            for directory in os.listdir('.'):
                if directory.startswith('virtualenv'):
                    virtual_env_dir = directory

        if virtual_env_dir == "":
            fail("Failed to find the virtual env folder in the dependency_path folder", STANDALONE_INSTALLATION_FAILURE)

        virtual_env_script = os.path.join(dependency_dir, virtual_env_dir, "virtualenv.py")

        python_interpreter = sys.executable
        if self.python:
            python_interpreter = self.python

        # Try to stop the awslogs service if it is running.
        # It is OK if this call fails when it is 1st time to install the awslogs.
        subprocess.call(['service', 'awslogs', 'stop'], stderr=self.log_file, stdout=self.log_file)

        p = subprocess.Popen([sys.executable, virtual_env_script, "--python", python_interpreter, AWSLOGS_HOME, "--no-download"], stdout=subprocess.PIPE, stderr=subprocess.PIPE)
        stdout, stderr = p.communicate()
        if p.returncode != 0:
            raise BadRCError("Bad rc (%s) for cmd '%s': %s" % (p.returncode, "cmd", stdout + stderr))

    def validate_dependency_path(self, path):
        if not os.path.exists(path):
            fail("Make sure the dependency_path points to the correct folder. Path doesn't exist: " + path, STANDALONE_INSTALLATION_FAILURE)

    def install_system_dependencies(self):
        self.install_pip()
        self._install_ubuntu_dependencies()

    def _install_ubuntu_dependencies(self):
        if self.os_flavor == self.Ubuntu or self.os_flavor == self.Debian:
            libyaml_dev_installed = self._package_installed("libyaml-dev")
            python_dev_installed = self._package_installed("python-dev")
            if libyaml_dev_installed and (not python_dev_installed):
                self.install("python-dev")

    def install_pip(self):

        # For standalone installation, no need to check for the existence
        # of pip, we will simply be using the virtualenv packages in the
        # dependency folder to activate AWSLOGS_HOME as the virtualenv.
        if not self.is_standalone and not executable_exists("pip"):
            if self.os_flavor == self.Rhel or self.os_flavor == self.CentOS:
                self.install("python-setuptools")
                subprocess.call(['easy_install', 'pip'], stdout=self.log_file, stderr=self.log_file)
            else:
                self.install("python-pip")

        # If this is not standalone installation and pip executable still
        # doesn't exist, we have a problem
        if not self.is_standalone and not executable_exists("pip"):
            fail("Could not install pip. Please try again or see " + AGENT_SETUP_LOG_FILE + " for more details")

    def _package_installed(self, package_name):
        devnull = open(os.devnull, "w")
        if self.os_flavor == self.Ubuntu or self.os_flavor == self.Debian:
            ret_val = subprocess.call(["dpkg", "-s", package_name], stdout=self.log_file, stderr=self.log_file)
        else:
            ret_val = subprocess.call(["rpm", "-q", package_name], stdout=self.log_file, stderr=self.log_file)
        devnull.close()
        if ret_val != 0:
            message(package_name + " does not exist in system ")
            return False
        return True

    def setup_agent_log_file_rotation(self):
        config = AGENT_LOG_ROTATE_CONFIG
        if self.os_flavor == self.Ubuntu or self.os_flavor == self.Debian:
            config = config.replace('$SU_FOR_UBUNTU$','su root root')
        else:
            config = config.replace('$SU_FOR_UBUNTU$','')

        config = config.replace("$VERSION$", AWSLOGS_VERSION)
        file_path = "/etc/logrotate.d/awslogs"
        with open (file_path, "w") as log_rotation_config:
            log_rotation_config.write(config)
        self.log_generated_file(file_path)

    def setup_cron_jobs(self):
        nanny_cron_path = "/etc/cron.d/awslogs"
        with open (nanny_cron_path, "w") as cron_fragment:
            cron_fragment.write(NANNY_CRON_CMD.format(VERSION=AWSLOGS_VERSION, AGENT_NANNY_PATH=AGENT_NANNY_PATH) + "\n")
        self.log_generated_file(nanny_cron_path)

        logrotate_cron_path = "/etc/cron.d/awslogs_log_rotate"
        with open (logrotate_cron_path, "w") as lr_cron:
            lr_cron.write(LOGROTATE_CRON_CMD.format(VERSION=AWSLOGS_VERSION))
        self.log_generated_file(logrotate_cron_path)

    def write_nanny_script(self):
        file_path = AGENT_NANNY_PATH
        ps_path = subprocess.Popen(['which', 'ps'], stdout=subprocess.PIPE, universal_newlines=True).communicate()[0].rstrip()
        grep_path = subprocess.Popen(['which', 'grep'], stdout=subprocess.PIPE, universal_newlines=True).communicate()[0].rstrip()
        cat_path = subprocess.Popen(['which', 'cat'], stdout=subprocess.PIPE, universal_newlines=True).communicate()[0].rstrip()
        service_path = subprocess.Popen(['which', 'service'], stdout=subprocess.PIPE, universal_newlines=True).communicate()[0].rstrip()
        contents = NANNY_SCRIPT.replace("$PIDFILE$", AGENT_PID_FILE)
        contents = contents.replace("$LOCKFILE$", AGENT_LOCK_FILE)
        contents = contents.replace("$AGENT_LAUNCHER$", AGENT_LAUNCHER)
        contents = contents.replace("$PS_PATH$", ps_path)
        contents = contents.replace("$GREP_PATH$", grep_path)
        contents = contents.replace("$CAT_PATH$", cat_path)
        contents = contents.replace("$SERVICE_PATH$", service_path)
        contents = contents.replace("$VERSION$", AWSLOGS_VERSION)
        with open(file_path, "w") as nanny_file:
            nanny_file.write(contents)
        os.system("chmod 755 " + file_path)
        self.log_generated_file(AGENT_NANNY_PATH)

    def setup_agent_nanny(self):
        self.write_nanny_script()
        self.setup_cron_jobs()

    def setup_daemon(self):
        self.write_daemon_script()
        self.write_launcher_file()
        self.setup_agent_as_daemon()
        self.setup_agent_log_file_rotation()
        # Restart the agent.
        subprocess.call(['service', 'awslogs', 'restart'], stderr=self.log_file, stdout=self.log_file)
        self.setup_agent_nanny()

        self.write_generated_file_log()
        self.write_awslogs_version_script()

    def log_generated_file(self, filepath):
        self.generated_filepaths.append(filepath)

    def write_generated_file_log(self):
        with open(AWSLOGS_GENERATED_FILES_LOG, "w") as generated_files_log:
            generated_files_log.write("\n".join(self.generated_filepaths))

    def write_awslogs_version_script(self):
        with open(AWSLOGS_VERSION_SCRIPT_PATH, "w") as version_script:
            version_script.write(AWSLOGS_VERSION_SCRIPT.replace("$AWSLOGS_GENERATED_FILES_LOG$", AWSLOGS_GENERATED_FILES_LOG))

        os.system("chmod 755 " + AWSLOGS_VERSION_SCRIPT_PATH)

    def setup_artifacts(self):
        if not os.path.exists(AWSLOGS_HOME):
            os.mkdir(AWSLOGS_HOME)
        if not os.path.exists(AGENT_STATE_DIR):
            os.mkdir(AGENT_STATE_DIR)
        if not os.path.exists(AGENT_ETC_DIR):
            os.mkdir(AGENT_ETC_DIR)
        if not os.path.exists(AGENT_ADDITIONAL_CONFIGS_DIR):
            os.mkdir(AGENT_ADDITIONAL_CONFIGS_DIR)

        if not self.is_standalone and (self.os_flavor == self.Ubuntu or self.os_flavor == self.Debian):
            subprocess.call(['apt-get', 'update'], stdout=self.log_file, stderr=self.log_file)

        with open(AWS_PROXY_CONFIG_FILE, "w") as config:
            config.write(PROXY_CONFIG.format(http_proxy=self.http_proxy, https_proxy=self.https_proxy, no_proxy=self.no_proxy, VERSION=AWSLOGS_VERSION))
        self.log_generated_file(AWS_PROXY_CONFIG_FILE)

        if not self.only_generate_config:
            self.cleanup_existing_logs_binaries()
            message("\nStep 1 of 5: Installing pip ...")
            self.install_system_dependencies()
            status("DONE")
            message("Step 2 of 5: Downloading the latest CloudWatch Logs agent bits ... ")
            self.install_awslogs_cli()
            status("DONE")
        else:
            message("Skipping downloading and installation of agent bits.")

        if not self.non_interactive:
            message("Step 3 of 5: Configuring AWS CLI ... \n")
            self.aws_configure()
            message("\nStep 4 of 5: Configuring the CloudWatch Logs Agent ... \n")
            self.aws_logs_configure()
        else:
            self.write_agent_config_file()

        message("\nStep 5 of 5: Setting up agent as a daemon ...")
        self.setup_daemon()
        self._cleanup_tmp_files()
        status("DONE")

        rerun_cmd = "sudo python ./awslogs-agent-setup.py --region " + self.region + " --only-generate-config"
        message("\n------------------------------------------------------")
        message("\n- Configuration file successfully saved at: " + AGENT_CONFIG_FILE)
        message("\n- You can begin accessing new log events after a few moments at https://console.aws.amazon.com/cloudwatch/home?region=" + self.region + "#logs:")
        message("\n- You can use 'sudo service awslogs start|stop|status|restart' to control the daemon.")
        message("\n- To see diagnostic information for the CloudWatch Logs Agent, see /var/log/awslogs.log")
        message("\n- You can rerun interactive setup using '" + rerun_cmd + "'");
        message("\n------------------------------------------------------\n")


    def get_system_defaults(self):
        log_file_path = '/var/log/kern.log'
        datetime_format = '%b %d %H:%M:%S'

        if self.os_flavor == self.AmazonLinux or self.os_flavor == self.Rhel or self.os_flavor == self.CentOS:
            log_file_path = '/var/log/messages'
        elif self.os_flavor == self.Ubuntu or self.os_flavor == self.Raspbian or self.os_flavor == self.Debian:
            log_file_path = '/var/log/syslog'

        return {'file_path': log_file_path,
                'datetime_format': datetime_format}

    def get_initial_position(self, prompter):
        message("\nChoose initial position of upload:\n")
        message("  1. From start of file.\n")
        message("  2. From end of file.\n")
        choice = prompter.get_value("1", "Enter choice")

        while choice != "1" and choice != "2":
            choice = prompter.get_value("1", "Enter choice")

        if choice == "1":
            return "start_of_file"
        return "end_of_file"

    def get_timestamp_format(self, prompter):
        syslog_fmt = '%b %d %H:%M:%S'
        access_fmt = '%d/%b/%Y:%H:%M:%S'
        rails_fmt = '%Y-%m-%d %H:%M:%S'

        message("\nChoose Log Event timestamp format:\n")
        message('  1. ' + syslog_fmt + '    (Dec 31 23:59:59)\n')
        message('  2. ' + access_fmt + ' (10/Oct/2000:13:55:36)\n')
        message('  3. ' + rails_fmt + ' (2008-09-08 11:52:54)\n')
        message("  4. Custom\n")
        datetime_fmt = prompter.get_value("1", "Enter choice")

        if datetime_fmt == "4":
            datetime_fmt = None
            while datetime_fmt is None:
                datetime_fmt = prompter.get_value(None, "Enter customer timestamp format")
            return datetime_fmt

        if datetime_fmt == "1":
            return syslog_fmt
        elif datetime_fmt == "2":
            return access_fmt
        else:
            return rails_fmt

    def get_stream_name(self, prompter):
        message("\nChoose Log Stream name:\n")
        message("  1. Use EC2 instance id.\n")
        message("  2. Use hostname.\n")
        message("  3. Custom.\n")
        choice = prompter.get_value("1", "Enter choice")

        if choice == "1":
            return "{instance_id}"
        elif choice == "2":
            return "{hostname}"

        choice = None
        while choice is None:
            choice = prompter.get_value(None, "Enter Log Stream name")

        return choice


    def aws_logs_configure(self):
        prompter = InteractivePrompter()

        # Just to generate the default configuration in case it doesn't already exist.
        LogsConfig(None)

        file_path_msg = 'Path of log file to upload'
        log_group_msg = 'Destination Log Group name'

        defaults = self.get_system_defaults()
        default_log_file_path = defaults['file_path']

        while True:
            log_file_path = prompter.get_value(default_log_file_path, file_path_msg)
            section_name = log_file_path
            # log-group name defaults to the file-path.
            dest_log_group = prompter.get_value(log_file_path, log_group_msg)
            dest_log_stream = self.get_stream_name(prompter)

            datetime_fmt = self.get_timestamp_format(prompter)
            initial_position = self.get_initial_position(prompter)

            section = {'__section__': section_name,
                       'file': log_file_path,
                       'log_group_name': dest_log_group,
                       'log_stream_name': dest_log_stream,
                       'datetime_format': datetime_fmt,
                       'buffer_duration': 5000,
                       'initial_position': initial_position}

            writer = ConfigFileWriter()
            writer.update_config(section, AGENT_CONFIG_FILE)

            response = prompter.get_value("Y", "More log files to configure?")
            if not response.upper().startswith('Y'):
                break

# Utility routine that returns True if the specified file exists and False otherwise.
def executable_exists(exe):
    try:
        devnull = open(os.devnull)
        subprocess.Popen([exe], stdout=devnull, stderr=devnull).communicate()
    except OSError as e:
        if e.errno == os.errno.ENOENT:
            return False
    return True


def parse_args():
    parser = OptionParser()
    parser.add_option("-o", "--only-generate-config", dest="only_generate_config", action="store_true", default=False, help="Only generate configuration without installing new bits.")
    parser.add_option("-n", "--non-interactive", dest="non_interactive", action="store_true", default=False, help="Non interactive mode.")
    parser.add_option("-r", "--region", dest="region", help="AWS region.")
    # --configfile is only required in non-interactive mode.
    parser.add_option("-c", "--configfile", dest="config_file",
                      help="Local path, S3 path or http(s) based URL of the CloudWatch Logs agent's configuration file.")

    # Optional parameter for the custom CLI url. This is only used for testing purposes.
    parser.add_option("-u", "--plugin-url", dest="plugin_url", help="URL of CloudWatch Logs plugin.")
    parser.add_option("-p", "--python", dest="python",
                      help="The Python interpreter to use. The default is the interpreter that virtualenv was installed with (/usr/bin/python)")
    parser.add_option("--http-proxy", dest="http_proxy", help="The http proxy that's used to communicate with CloudWatch Logs service.")
    parser.add_option("--https-proxy", dest="https_proxy", help="The https proxy that's used to communicate with CloudWatch Logs service.")
    parser.add_option("--no-proxy", dest="no_proxy", help="A comma-separated list of domain extensions the proxy should not be used for.")

    # Optional parameter to specify a local folder for standalone installation.
    # Useful if you do not have access to pip repositories or limited external connectivity.
    # Needs necessary dependency packages to be available locally. 
    parser.add_option("--dependency-path", dest="dependency_path", help="Path to local folder that has the dependency packages")
    parser.add_option("--ca-bundle", dest="ca_bundle", help="Path for the CA certificate bundle to use for verifying SSL certificates.")
    options, args = parser.parse_args()
    return options


class BadRCError(Exception):
    pass


#
# Utility methods to deal with console output.
#
class Colors:
    GREY = '\033[90m'
    GREEN = '\033[92m'
    RED = '\033[91m'
    END = '\033[0m'
    CYAN = '\033[96m'


def error(msg):
    print(Colors.RED + 'ERROR: ' + msg + Colors.END)


def fail(msg, errno):
    error(msg)
    exit(errno)


def status(msg):
    if msg == 'DONE':
        print(Colors.GREEN + msg + Colors.END + '\n')
    else:
        print(Colors.GREEN + msg + Colors.END)


def message(msg):
    sys.stdout.write(msg + Colors.END)
    sys.stdout.flush()

############################################################

#
# Helper class that deals with reading the configuration for
# CloudWatch Logs agent.
#
class LogsConfig(object):

    def __init__(self, file_path=None):
        self.config = self._get_config(file_path)

    @staticmethod
    def _get_logs_config_file():
        return AGENT_CONFIG_FILE

    def _get_config(self, file_path):
        """
        Finds and parses the logs configuration file and returns all
        of the data found within the file as a dictionary of
        dictionaries, one for each profile section found in the
        configuration file.

        :returns: A dict with keys for each section found in the config
            file and the value of each key being a dict containing name
            value pairs found in that section.
        """
        config = {}
        path = AGENT_CONFIG_FILE
        if path is not None:
            path = os.path.expandvars(path)
            path = os.path.expanduser(path)
            if not os.path.isfile(path):
                self._generate_config_template(path)

            cp = configparser.RawConfigParser()
            cp.read(path)

            for section in cp.sections():
                config[section] = {}
                for option in cp.options(section):
                    config_value = cp.get(section, option)
                    if config_value.startswith('\n'):
                        # Then we need to parse the inner contents as
                        # hierarchical.  We support a single level
                        # of nesting for now.
                        try:
                            config_value = _parse_nested(config_value)
                        except ValueError:
                            raise botocore.exceptions.ConfigParseError(
                                path=path)
                    config[section][option] = config_value
        return config

    @staticmethod
    def _generate_config_template(path):
        # Create the file as well as the parent dir if needed.
        dirname, basename = os.path.split(path)
        if not os.path.isdir(dirname):
            os.makedirs(dirname)
        with os.fdopen(os.open(path, os.O_WRONLY | os.O_CREAT, 0o600), 'w') as file:
            file.writelines(DEFAULT_CONFIG)

    @staticmethod
    def _parse_nested(config_value):
        # Given a value like this:
        # \n
        # foo = bar
        # bar = baz
        # We need to parse this into
        # {'foo': 'bar', 'bar': 'baz}
        parsed = {}
        for line in config_value.splitlines():
            line = line.strip()
            if not line:
                continue
            # The caller will catch ValueError
            # and raise an appropriate error
            # if this fails.
            key, value = line.split('=', 1)
            parsed[key.strip()] = value.strip()
        return parsed


############################################################

#
# Helper class for reading console inputs
#
class InteractivePrompter(object):
    @staticmethod
    def get_value(current_value, prompt_text=''):
        global input
        try:
            input = raw_input
        except NameError:
            pass
        response = input("%s [%s]: " % (prompt_text, current_value))
        if not response:
            response = current_value
        return response


############################################################

#
# This section contains definition of helper classes that
# are used to write CloudWatch Logs agent's configuration
# file.
#
class SectionNotFoundError(Exception):
    pass


class ConfigFileWriter(object):
    SECTION_REGEX = re.compile(r'\[(?P<header>[^]]+)\]')
    OPTION_REGEX = re.compile(
        r'(?P<option>[^:=\s][^:=]*)'
        r'\s*(?P<vi>[:=])\s*'
        r'(?P<value>.*)$'
    )

    def update_config(self, new_values, config_filename):
        section_name = new_values.pop('__section__', 'default')
        if not os.path.isfile(config_filename):
            self._create_file(config_filename)
            self._write_new_section(section_name, new_values, config_filename)
            return
        with open(config_filename, 'r') as f:
            contents = f.readlines()
        # We can only update a single section at a time so we first need
        # to find the section in question
        try:
            self._update_section_contents(contents, section_name, new_values)
            with open(config_filename, 'w') as f:
                f.write(''.join(contents))
        except SectionNotFoundError:
            self._write_new_section(section_name, new_values, config_filename)

    def _create_file(self, config_filename):
        # Create the file as well as the parent dir if needed.
        dirname, basename = os.path.split(config_filename)
        if not os.path.isdir(dirname):
            os.makedirs(dirname)
        with os.fdopen(os.open(config_filename, os.O_WRONLY|os.O_CREAT, 0o600), 'w'):
            pass

    def _write_new_section(self, section_name, new_values, config_filename):
        with open(config_filename, 'a') as f:
            f.write('[%s]\n' % section_name)
            for key, value in new_values.items():
                f.write('%s = %s\n' % (key, value))

    def _update_section_contents(self, contents, section_name, new_values):
        new_values = new_values.copy()
        # contents is a list of file line contents.
        for i in range(len(contents)):
            line = contents[i]
            if line.strip().startswith(('#', ';')):
                # This is a comment, so we can safely ignore this line.
                continue
            match = self.SECTION_REGEX.search(line)
            if match is not None and self._matches_section(match,
                                                           section_name):
                break
        else:
            raise SectionNotFoundError(section_name)
        # If we get here, then we've found the section.  We now need
        # to figure out if we're updating a value or adding a new value.
        i += 1
        last_matching_line = i
        for j in range(i, len(contents)):
            line = contents[j]
            match = self.OPTION_REGEX.search(line)
            if match is not None:
                last_matching_line = j
                key_name = match.group(1).strip()
                if key_name in new_values:
                    new_line = '%s = %s\n' % (key_name, new_values[key_name])
                    contents[j] = new_line
                    del new_values[key_name]
            elif self.SECTION_REGEX.search(line) is not None:
                # We've hit a new section which means the config key is
                # not in the section.  We need to add it here.
                self._insert_new_values(line_number=last_matching_line,
                                        contents=contents,
                                        new_values=new_values)
                return

        if new_values:
            if not contents[-1].endswith('\n'):
                contents.append('\n')
            self._insert_new_values(line_number=last_matching_line + 1,
                                    contents=contents,
                                    new_values=new_values)

    def _insert_new_values(self, line_number, contents, new_values):
        new_contents = []
        for key, value in new_values.items():
            new_contents.append('%s = %s\n' % (key, value))
        contents.insert(line_number + 1, ''.join(new_contents))

    def _matches_section(self, match, section_name):
        parts = section_name.split(' ')
        unquoted_match = match.group(0) == '[%s]' % section_name
        if len(parts) > 1:
            quoted_match = match.group(0) == '[%s "%s"]' % (
                parts[0], ' '.join(parts[1:]))
            return unquoted_match or quoted_match
        return unquoted_match


############################################################

#
# Main method.
#

def main():
    options = parse_args()
    if options.region is None:
        fail("Missing required arguments. Please run with --help for details.\n", INVALID_PARAMETERS)

    if options.non_interactive is True:
        if options.config_file is None:
            fail("Missing required arguments. Please run with --help for details.\n", INVALID_PARAMETERS)

    if os.geteuid() != 0:
        fail("Please run this script under root privileges.\n", NOT_ROOT)

    if "WINDOWS" in platform.system().upper():
        fail("This script doesn't support Microsoft Windows", PLATFORM_NOT_SUPPORTED)

    python_version = sys.version_info
    if python_version < (2,6) or python_version >= (3,6):
        fail("This script only supports python version 2.6 - 3.5", PLATFORM_NOT_SUPPORTED)

    if options.only_generate_config and options.non_interactive:
        fail("Config generation option only works in interactive mode.", INVALID_PARAMETERS)

    if options.non_interactive is False:
        message("Launching interactive setup of CloudWatch Logs agent ... \n")

    # Write the version of the install and close the file so this gets written first.
    with open(AGENT_SETUP_LOG_FILE, "a") as log_file:
        log_file.write("\nVersion %s Install Starting\n" % AWSLOGS_VERSION)

    # CloudWatchLogsAgentSetup will write stderr and stdout of commands it executes the the log file handle
    with open(AGENT_SETUP_LOG_FILE, "a") as log_file:
        setup = CloudWatchLogsAgentSetup(log_file,
                                         options)
        setup.setup_artifacts()


if __name__ == '__main__':
    main()
