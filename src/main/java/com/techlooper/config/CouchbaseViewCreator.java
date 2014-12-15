package com.techlooper.config;

import com.couchbase.client.CouchbaseClient;
import com.couchbase.client.protocol.views.DesignDocument;
import com.couchbase.client.protocol.views.ViewDesign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * Created by khoa-nd on 12/15/14.
 */
public class CouchbaseViewCreator {

    private static final Logger LOGGER = LoggerFactory.getLogger(CouchbaseViewCreator.class);

    @Resource
    private CouchbaseClient couchbaseClient;

    @PostConstruct
    public void init() throws Exception {
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
        couchbaseClient.createDesignDoc(designDoc);
    }

}