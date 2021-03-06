package io.jexxa.infrastructure.drivingadapter.rest;

import static io.jexxa.infrastructure.drivingadapter.rest.RESTConstants.APPLICATION_TYPE;
import static io.jexxa.infrastructure.drivingadapter.rest.RESTConstants.CONTENT_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Properties;
import java.util.stream.Stream;

import javax.net.ssl.SSLContext;

import io.jexxa.application.applicationservice.SimpleApplicationService;
import kong.unirest.Unirest;
import kong.unirest.apache.ApacheClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class HttpsRESTfulRPCAdapterIT
{
    private static final String METHOD_GET_SIMPLE_VALUE = "getSimpleValue";

    private static final int DEFAULT_VALUE = 42;
    private final SimpleApplicationService simpleApplicationService = new SimpleApplicationService();

    @SuppressWarnings("unused")
    // Run the tests with Port 0 (random port and port 8080)
    static Stream<Integer> httpsPorts() {
        return Stream.of(0,8081);
    }

    @BeforeEach
    void initTest() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException, CertificateException, IOException
    {
        // NOTE: To run this test we need to create a truststore has described here https://magicmonster.com/kb/prg/java/ssl/pkix_path_building_failed/

        //Arrange
        SSLContext sslContext =  new SSLContextBuilder().loadTrustMaterial(
                HttpsRESTfulRPCAdapterIT.class.getResource("/trustStore.jks"), //path to jks file
                "changeit".toCharArray(), //enters in the truststore password for use
                new TrustSelfSignedStrategy() //will trust own CA and all self-signed certs
        ).build();

        CloseableHttpClient customHttpClient = HttpClients.custom().setSSLContext(sslContext)
                .setSSLHostnameVerifier(new NoopHostnameVerifier()).build();

        Unirest.config().httpClient(ApacheClient.builder(customHttpClient));

        Unirest.config().sslContext(sslContext);
        Unirest.config().hostnameVerifier(new NoopHostnameVerifier());
    }

    @ParameterizedTest
    @MethodSource("httpsPorts")
    void testHTTPSConnectionRandomPort(Integer httpsPort)
    {
        //Arrange
        var properties = new Properties();
        var defaultHost = "0.0.0.0";

        properties.put(RESTfulRPCAdapter.HOST_PROPERTY, defaultHost);
        properties.put(RESTfulRPCAdapter.HTTPS_PORT_PROPERTY, httpsPort.toString());
        properties.put(RESTfulRPCAdapter.KEYSTORE_PASSWORD, "test123");
        properties.put(RESTfulRPCAdapter.KEYSTORE, "keystore.jks");

        var objectUnderTest = RESTfulRPCAdapter.createAdapter(properties);
        objectUnderTest.register(simpleApplicationService);
        objectUnderTest.start();

        String restPath = "https://localhost:" + objectUnderTest.getHTTPSPort() + "/SimpleApplicationService/";

        //Act
        Integer result = Unirest.get(restPath + METHOD_GET_SIMPLE_VALUE)
                .header(CONTENT_TYPE, APPLICATION_TYPE)
                .asObject(Integer.class).getBody();


        //Assert
        assertNotNull(result);
        assertEquals(DEFAULT_VALUE, simpleApplicationService.getSimpleValue());
        assertEquals(simpleApplicationService.getSimpleValue(), result.intValue() );
    }

    @AfterEach
    void tearDown()
    {
        Unirest.shutDown();
    }
}
