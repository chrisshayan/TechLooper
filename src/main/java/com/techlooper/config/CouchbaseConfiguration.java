package com.techlooper.config;

import com.couchbase.client.CouchbaseClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;

import javax.annotation.Resource;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

/**
 * Created by khoa-nd on 12/10/14.
 */
@Configuration
@EnableCouchbaseRepositories(basePackages = "com.techlooper.repository.couchbase")
public class CouchbaseConfiguration extends AbstractCouchbaseConfiguration {

    @Resource
    private Environment env;

    @Bean
    public CouchbaseBucketCreator couchbaseBucketCreator() throws Exception {
        return new CouchbaseBucketCreator(getCouchbaseConnectionUri(), getCouchbaseAdminUser(), getCouchbaseAdminPassword(),
                getBucketName(), getBucketPassword());
    }

    @Bean
    @Override
    @DependsOn("couchbaseBucketCreator")
    public CouchbaseClient couchbaseClient() throws Exception {
        return super.couchbaseClient();
    }

    @Bean
    @DependsOn("couchbaseClient")
    public CouchbaseViewCreator couchbaseViewCreator() throws Exception {
        return new CouchbaseViewCreator();
    }

    @Override
    protected List<String> bootstrapHosts() {
        String host = URI.create(getCouchbaseConnectionUri()).getHost();
        return Arrays.asList(host);
    }

    public String getCouchbaseConnectionUri() {
        return env.getProperty("couchbase.connectionUri", "http://127.0.0.1:8091");
    }

    public String getCouchbaseAdminUser() {
        return env.getProperty("couchbase.adminUser", "Administrator");
    }

    public String getCouchbaseAdminPassword() {
        return env.getProperty("couchbase.adminPassword", "password");
    }

    @Override
    protected String getBucketName() {
        return env.getProperty("couchbase.bucketName");
    }

    @Override
    protected String getBucketPassword() {
        return env.getProperty("couchbase.bucketPassword", "");
    }
}
