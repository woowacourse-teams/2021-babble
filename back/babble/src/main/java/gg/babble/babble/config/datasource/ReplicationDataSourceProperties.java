package gg.babble.babble.config.datasource;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "spring.datasource")
public class ReplicationDataSourceProperties {

    private String driverClassName;
    private String url;
    private String username;
    private String password;
    private final Map<String, Slave> slaves = new HashMap<>();

    public Collection<Slave> getSlaves() {
        return slaves.values();
    }

    @Setter
    @Getter
    public static class Slave {

        private String name;
        private String driverClassName;
        private String url;
        private String username;
        private String password;
    }
}
