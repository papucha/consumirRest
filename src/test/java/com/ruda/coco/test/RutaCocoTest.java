package com.ruda.coco.test;

import java.util.Properties;

import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ruda.coco.rest.dto.RequestDTO;

/**
 * 
 * @author Assert Solutions S.A.S <info@assertsolutions.com>
 *         <br/>
 *         Date: 9/04/2018 9:17:11 a.m.
 *
 */
public class RutaCocoTest extends CamelSpringTestSupport {

    private static final String PROPERTIES_FILE_DIR = "src/test/resources/";
    private static Properties testProperties = new Properties();
    
    @Test
    public void testRoute() throws Exception {
	final String fromRoute = "direct:fromRoute";

	context.getRouteDefinition("restServerRoute").adviceWith(context, new AdviceWithRouteBuilder() {
	    @Override
	    public void configure() throws Exception {
		replaceFromWith(fromRoute);
		weaveAddLast().log("Finishing the unit test of the route ").to("mock://endroute");
	    }
	});
	context.start();
	// Agregamos un mock endpoint
	MockEndpoint mockEndpoint = getMockEndpoint("mock://endroute");
	mockEndpoint.expectedMinimumMessageCount(1);
	
	RequestDTO request = new RequestDTO();
	request.setNum1(8f);
	request.setNum2(2.5f);
	request.setOperador('+');
	template.sendBody(fromRoute, request);

	mockEndpoint.assertIsSatisfied(2000L);
	
    }

    /**
     * Carga del archivo de propiedades para los Test Unitarios
     * 
     * @throws Exception
     */
    @BeforeClass
    public static void init() throws Exception {
	testProperties.load(RutaCocoTest.class.getResourceAsStream("/ruta.coco.properties"));
    }
    
    @BeforeClass
    public static void setUpProperties() throws Exception {
	System.setProperty("karaf.home", PROPERTIES_FILE_DIR);
	System.setProperty("project.artifactId", "Test-Maven-Artifact");
    }

    @Override
    protected AbstractApplicationContext createApplicationContext() {
	return new ClassPathXmlApplicationContext("META-INF/spring/camel-context.xml",
		"META-INF/spring/properties-beans.xml");
    }

}
