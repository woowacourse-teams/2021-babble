package gg.babble.babble.config.datasource;

import com.zaxxer.hikari.HikariDataSource;
import gg.babble.babble.config.datasource.ReplicationDataSourceProperties.Slave;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Profile({"prod", "dev"})
@EnableConfigurationProperties(ReplicationDataSourceProperties.class)
@Configuration
public class ReplicationDataSourceConfig {

    private final ReplicationDataSourceProperties dataSourceProperties;
    private final JpaProperties jpaProperties;

    public ReplicationDataSourceConfig(final ReplicationDataSourceProperties dataSourceProperties,
                                       final JpaProperties jpaProperties) {
        this.dataSourceProperties = dataSourceProperties;
        this.jpaProperties = jpaProperties;
    }

    @Bean
    public DataSource routingDataSource() {
        DataSource masterDataSource = createDataSource(
            dataSourceProperties.getDriverClassName(),
            dataSourceProperties.getUrl(),
            dataSourceProperties.getUsername(),
            dataSourceProperties.getPassword()
        );

        Map<Object, Object> dataSources = new HashMap<>();
        dataSources.put("master", masterDataSource);

        for (Slave slave : dataSourceProperties.getSlaves().values()) {
            DataSource slaveDatSource = createDataSource(
                slave.getDriverClassName(),
                slave.getUrl(),
                slave.getUsername(),
                slave.getPassword()
            );

            dataSources.put(slave.getName(), slaveDatSource);
        }

        ReplicationRoutingDataSource replicationRoutingDataSource = new ReplicationRoutingDataSource();
        replicationRoutingDataSource.setDefaultTargetDataSource(masterDataSource);
        replicationRoutingDataSource.setTargetDataSources(dataSources);

        return replicationRoutingDataSource;
    }

    private DataSource createDataSource(final String driverClassName, final String url,
                                       final String username, final String password) {
        return DataSourceBuilder.create()
            .type(HikariDataSource.class)
            .url(url)
            .driverClassName(driverClassName)
            .username(username)
            .password(password)
            .build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        EntityManagerFactoryBuilder entityManagerFactoryBuilder = createEntityManagerFactoryBuilder(jpaProperties);
        return entityManagerFactoryBuilder.dataSource(dataSource())
            .packages("gg.babble.babble")
            .build();
    }

    private EntityManagerFactoryBuilder createEntityManagerFactoryBuilder(final JpaProperties jpaProperties) {
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        return new EntityManagerFactoryBuilder(vendorAdapter, jpaProperties.getProperties(), null);
    }

    @Bean
    public DataSource dataSource() {
        return new LazyConnectionDataSourceProxy(routingDataSource());
    }

    @Bean
    public PlatformTransactionManager transactionManager(final EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager tm = new JpaTransactionManager();
        tm.setEntityManagerFactory(entityManagerFactory);
        return tm;
    }
}
