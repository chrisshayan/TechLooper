package com.techlooper.config;

import com.couchbase.client.ClusterManager;
import com.couchbase.client.clustermanager.BucketType;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;

import static org.mockito.Mockito.*;

/**
 * Created by khoa-nd on 12/16/14.
 */
public class CouchbaseBucketCreatorTest {

    private CouchbaseBucketCreator couchbaseBucketCreator;
    private ClusterManager bucketManager;
    private String connectionUri;
    private String adminUser;
    private String adminPassword;
    private String bucketName;
    private String bucketPassword;

    @Before
    public void setUp() throws Exception {
        connectionUri = "http://127.0.0.1:8091";
        adminUser = "Administrator";
        adminPassword = "password";
        bucketName = "techlooper_user";
        bucketPassword = "password";
        couchbaseBucketCreator = new CouchbaseBucketCreator(connectionUri, adminUser, adminPassword, bucketName, bucketPassword);
        bucketManager = mock(ClusterManager.class);
        ReflectionTestUtils.setField(couchbaseBucketCreator, "bucketManager", bucketManager);
    }

    @Test
    public void shouldCreateNewBucketIfNotExist() throws Exception {
        when(bucketManager.listBuckets()).thenReturn(Arrays.asList("newBucketName"));

        couchbaseBucketCreator.init();

        verify(bucketManager).createNamedBucket(BucketType.COUCHBASE, bucketName, 128, 0, bucketPassword, true);
        verify(bucketManager).shutdown();
    }

    @Test
    public void shouldNotCreateNewBucketIfExist() throws Exception {
        when(bucketManager.listBuckets()).thenReturn(Arrays.asList(bucketName));

        couchbaseBucketCreator.init();

        verify(bucketManager, never()).createNamedBucket(BucketType.COUCHBASE, bucketName, 128, 0, bucketPassword, true);
    }
}