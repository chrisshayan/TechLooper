package com.techlooper.config;

import com.couchbase.client.CouchbaseClient;
import com.couchbase.client.protocol.views.DesignDocument;
import com.couchbase.client.protocol.views.ViewDesign;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by khoa-nd on 12/15/14.
 */
public class CouchbaseViewCreator {

    private static final Logger LOGGER = LoggerFactory.getLogger(CouchbaseViewCreator.class);

    private static final String COUCHBASE_VIEW_BASE_DIR = "couchbase_view/";

    @Resource
    private CouchbaseClient couchbaseClient;

    @PostConstruct
    public void init() throws Exception {
        DesignDocument designDoc = new DesignDocument("userEntity");

        List<String> viewDefinitionFiles = scanCouchbaseViewDefinitionFiles();
        for (String viewDefinitionFileName : viewDefinitionFiles) {
            String viewDefinitionFunction = IOUtils.toString(getResourceAsStream(COUCHBASE_VIEW_BASE_DIR + viewDefinitionFileName));
            ViewDesign viewDesign = new ViewDesign(FilenameUtils.getBaseName(viewDefinitionFileName), viewDefinitionFunction);
            designDoc.getViews().add(viewDesign);
        }

        couchbaseClient.createDesignDoc(designDoc);
    }

    private List<String> scanCouchbaseViewDefinitionFiles() throws IOException {
        return IOUtils.readLines(getResourceAsStream(COUCHBASE_VIEW_BASE_DIR));
    }

    private InputStream getResourceAsStream(String resourceName) {
        return this.getClass().getClassLoader().getResourceAsStream(resourceName);
    }

}