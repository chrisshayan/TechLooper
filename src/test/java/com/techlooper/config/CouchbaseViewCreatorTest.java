package com.techlooper.config;

import com.couchbase.client.CouchbaseClient;
import com.couchbase.client.protocol.views.DesignDocument;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.InputStream;
import java.util.List;

import static org.mockito.Mockito.*;

public class CouchbaseViewCreatorTest {

    private CouchbaseViewCreator couchbaseViewCreator;

    private ClassLoader classLoader;
    private CouchbaseClient couchbaseClient;

    @Before
    public void setUp() throws Exception {
        couchbaseViewCreator = new CouchbaseViewCreator();
        couchbaseClient = mock(CouchbaseClient.class);
        classLoader = mock(ClassLoader.class);

        ReflectionTestUtils.setField(couchbaseViewCreator, "couchbaseClient", couchbaseClient);
        ReflectionTestUtils.setField(couchbaseViewCreator, "classLoader", classLoader);
    }

    @Test
    @Ignore
    public void testInit() throws Exception {
        InputStream couchbaseViewDir =
                this.getClass().getClassLoader().getResourceAsStream(CouchbaseViewCreator.COUCHBASE_VIEW_BASE_DIR);
        when(classLoader.getResourceAsStream(CouchbaseViewCreator.COUCHBASE_VIEW_BASE_DIR)).thenReturn(couchbaseViewDir);

        List<String> viewDefinitionFiles = IOUtils.readLines(couchbaseViewDir);
        for (String viewDefinitionFileName : viewDefinitionFiles) {
            InputStream viewDefinitionStream =
                    classLoader.getResourceAsStream(CouchbaseViewCreator.COUCHBASE_VIEW_BASE_DIR + viewDefinitionFileName);
            when(classLoader.getResourceAsStream(CouchbaseViewCreator.COUCHBASE_VIEW_BASE_DIR + viewDefinitionFileName))
                    .thenReturn(viewDefinitionStream);
        }

        DesignDocument designDocument = new DesignDocument("userEntity");
        when(couchbaseClient.getDesignDoc("userEntity")).thenReturn(designDocument);

        couchbaseViewCreator.init();

        verify(couchbaseClient).createDesignDoc(designDocument);
    }
}