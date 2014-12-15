package com.techlooper.config;

import com.couchbase.client.ClusterManager;
import com.couchbase.client.clustermanager.BucketType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.Arrays;

/**
 * Created by khoa-nd on 12/15/14.
 */
public class CouchbaseBucketCreator {

    private static final Logger LOGGER = LoggerFactory.getLogger(CouchbaseBucketCreator.class);

    private String connectionUri;
    private String adminUser;
    private String adminPassword;
    private String bucketName;
    private String bucketPassword;

    public CouchbaseBucketCreator(String connectionUri, String adminUser, String adminPassword, String bucketName, String bucketPassword) {
        this.connectionUri = connectionUri;
        this.adminUser = adminUser;
        this.adminPassword = adminPassword;
        this.bucketName = bucketName;
        this.bucketPassword = bucketPassword;
    }

    @PostConstruct
    public void init() throws Exception {
        //TODO : It always doesn't exist now, check for integration first
        boolean exist = checkBucketExist();

        if (!exist) {
            createBucket();
        }
    }

    private boolean checkBucketExist() {
        return false;
    }

    private void createBucket() throws Exception {
        ClusterManager bucketManager = new ClusterManager(Arrays.asList(URI.create(connectionUri)), adminUser, adminPassword);
        bucketManager.createNamedBucket(BucketType.COUCHBASE, bucketName, 128, 0, bucketPassword, true);
        Thread.sleep(5000);
        bucketManager.shutdown();
    }
}