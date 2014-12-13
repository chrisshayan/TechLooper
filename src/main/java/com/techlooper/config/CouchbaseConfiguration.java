package com.techlooper.config;

import com.couchbase.client.CouchbaseClient;
import com.couchbase.client.protocol.views.DesignDocument;
import com.couchbase.client.protocol.views.ViewDesign;
import org.springframework.context.annotation.Bean;
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

    @Bean
    public CouchbaseClient couchbaseClient() throws Exception {
        CouchbaseClient client = super.couchbaseClient();
        DesignDocument designDoc = new DesignDocument("userEntity");
        String viewName = "byKey";
        String mapFunction = //TODO extract to backing-view js
          "function (doc, meta) {\n" +
            "  if(doc._class == \"com.techlooper.entity.UserEntity\" ) {" +
            "    emit(doc.key, null);\n" +
            "  }\n" +
            "}";
        ViewDesign viewDesign = new ViewDesign(viewName, mapFunction);
        designDoc.getViews().add(viewDesign);
        client.createDesignDoc(designDoc);

        return client;
    }

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
