package gg.babble.babble.config.datasource;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Map;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class ReplicationRoutingDataSource extends AbstractRoutingDataSource {

    private static final String DATASOURCE_KEY_MASTER = "master";
    private static final String DATASOURCE_KEY_SLAVE = "slave";

    private DataSourceNames<String> slaveNames;

    @Override
    public void setTargetDataSources(final Map<Object, Object> targetDataSources) {
        super.setTargetDataSources(targetDataSources);

        List<String> replicas = targetDataSources.keySet()
            .stream()
            .map(Object::toString)
            .filter(string -> string.contains(DATASOURCE_KEY_SLAVE))
            .collect(toList());

        this.slaveNames = new DataSourceNames<>(replicas);
    }


    @Override
    protected Object determineCurrentLookupKey() {
        boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();

        if (isReadOnly) {
            logger.info("Connection Slave");
            return slaveNames.getNext();
        }

        logger.info("Connection Master");
        return DATASOURCE_KEY_MASTER;
    }

    public static class DataSourceNames<T> {

        private final List<T> values;
        private int index = 0;

        public DataSourceNames(final List<T> values) {
            this.values = values;
        }

        public T getNext() {
            if (index + 1 >= values.size()) {
                index = -1;
            }
            return values.get(++index);
        }
    }
}
