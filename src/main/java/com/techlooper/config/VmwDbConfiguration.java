package com.techlooper.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.beans.PropertyVetoException;

/**
 * Created by phuonghqh on 6/25/15.
 */
@Configuration
@EnableJpaAuditing
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.techlooper.repository.vnw")
public class VmwDbConfiguration {

  @Value("${vietnamworks.db.driverClass}")
  private String driverClass;

  @Value("${vietnamworks.db.connectionUrl}")
  private String jdbcUrl;

  @Value("${vietnamworks.db.username}")
  private String user;

  @Value("${vietnamworks.db.password}")
  private String password;

  @Value("${vietnamworks.db.pool.maxSize}")
  private Integer maxPoolSize;

  @Bean(destroyMethod = "close")
  public DataSource dataSource() throws PropertyVetoException {
    HikariDataSource dataSource = new HikariDataSource();
    dataSource.setJdbcUrl(jdbcUrl);
    dataSource.setUsername(user);
    dataSource.setPassword(password);
    dataSource.setMaximumPoolSize(maxPoolSize);
    dataSource.setDriverClassName(driverClass);
    return dataSource;
  }

  @Bean
  public JpaTransactionManager transactionManager(EntityManagerFactory emf) {
    return new JpaTransactionManager(emf);
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws PropertyVetoException {
    LocalContainerEntityManagerFactoryBean lemfb = new LocalContainerEntityManagerFactoryBean();
    lemfb.setDataSource(dataSource());

    HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
    jpaVendorAdapter.setDatabase(Database.MYSQL);
    jpaVendorAdapter.setGenerateDdl(false);
    jpaVendorAdapter.setShowSql(true);
    lemfb.setJpaVendorAdapter(jpaVendorAdapter);

    lemfb.setPackagesToScan("com.techlooper.entity.vnw");
    return lemfb;
  }

  @Bean
  public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
    return new PersistenceExceptionTranslationPostProcessor();
  }
}