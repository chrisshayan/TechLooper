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
import java.util.List;

/**
 * Created by khoa-nd on 12/15/14.
 */
public class CouchbaseViewCreator {

  private static final Logger LOGGER = LoggerFactory.getLogger(CouchbaseViewCreator.class);

  public static final String COUCHBASE_VIEW_BASE_DIR = "couchbase_view/";

  @Resource
  private CouchbaseClient couchbaseClient;

  private ClassLoader classLoader;

  public CouchbaseViewCreator() {
    this.classLoader = this.getClass().getClassLoader();
  }

  @PostConstruct
  public void init() throws Exception {
    DesignDocument designDoc;
    try {
      designDoc = couchbaseClient.getDesignDoc("userregistration");
    }
    catch (Throwable throwable) {
      LOGGER.debug(throwable.getMessage(), throwable);
      designDoc = new DesignDocument("userregistration");
    }

    List<String> viewDefinitionFiles = IOUtils.readLines(classLoader.getResourceAsStream(COUCHBASE_VIEW_BASE_DIR));
    for (String viewDefinitionFileName : viewDefinitionFiles) {
      String viewDefinitionFunction = IOUtils.toString(
        classLoader.getResourceAsStream(COUCHBASE_VIEW_BASE_DIR + viewDefinitionFileName));
      ViewDesign viewDesign = new ViewDesign(FilenameUtils.getBaseName(viewDefinitionFileName), viewDefinitionFunction, "_count");
      designDoc.getViews().add(viewDesign);
    }

    couchbaseClient.createDesignDoc(designDoc);
  }

}