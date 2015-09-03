package com.techlooper.config;

import com.techlooper.model.JobResponse;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
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
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by phuonghqh on 6/25/15.
 */
@Profile("prod")
@Configuration
@EnableJpaAuditing
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.techlooper.repository.vnw")
public class VnwDbConfiguration {

  @Value("${vietnamworks.db.driverClass}")
  private String driverClass;

  @Value("${vietnamworks.db.connectionUrl}")
  private String jdbcUrl;

  @Value("${vietnamworks.db.username}")
  private String user;

  @Value("${vietnamworks.db.password}")
  private String password;

  @Value("${vietnamworks.db.pool.size}")
  private Integer size;


  @Value("classpath:topPriorityJobId.csv")
  private org.springframework.core.io.Resource topPriorityJobIdResource;

  @Bean
  public DataSource dataSource() throws Exception {

    HikariDataSource dataSource = new HikariDataSource();
    dataSource.setJdbcUrl(jdbcUrl);
    dataSource.setUsername(user);
    dataSource.setPassword(password);
    dataSource.setMaximumPoolSize(size);
    dataSource.setMinimumIdle(size);
    dataSource.setDriverClassName(driverClass);
    dataSource.setAllowPoolSuspension(true);
    return dataSource;
  }

  //TODO refactor top job priority
  @Bean
  public Set<String> topPriorityJobIds() throws IOException {
    Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(new FileReader(topPriorityJobIdResource.getFile()));
    Set<String> ids = new HashSet<>();
    for (CSVRecord record : records) {
      ids.add(record.get(0));
    }
    return ids;
  }

  @Bean
  public JpaTransactionManager transactionManager(EntityManagerFactory emf) {
    return new JpaTransactionManager(emf);
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws Exception {
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