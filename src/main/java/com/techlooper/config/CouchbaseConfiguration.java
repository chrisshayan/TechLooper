package com.techlooper.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * Created by NguyenDangKhoa on 12/10/14.
 */
@Configuration
@EnableCouchbaseRepositories(basePackages = "com.techlooper.repository.couchbase")
public class CouchbaseConfiguration extends AbstractCouchbaseConfiguration {

    @Resource
    private Environment env;

    @Override
    protected List<String> bootstrapHosts() {
        return Arrays.asList(env.getProperty("couchbase.host", "127.0.0.1"));
    }

    @Override
    protected String getBucketName() {
        return env.getProperty("couchbase.bucket", "default");
    }

    @Override
    protected String getBucketPassword() {
        return env.getProperty("couchbase.password", "");
    }
}
