
package com.ytxiang.server.main;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.catalina.Context;

@SpringBootApplication(scanBasePackages = {"com.ytxiang"})
public class FilePotApplication {

	public static void main(String[] args) {
		SpringApplication.run(FilePotApplication.class, args);
	}

    @Bean
    public ServletWebServerFactory servletContainer() {
	TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
	    @Override
	    protected void postProcessContext(Context context) {
		SecurityConstraint securityConstraint = new SecurityConstraint();
		securityConstraint.setUserConstraint("CONFIDENTIAL");
		SecurityCollection collection = new SecurityCollection();
		collection.addPattern("/*");
		securityConstraint.addCollection(collection);
		context.addConstraint(securityConstraint);
	    }
	};
	tomcat.addAdditionalTomcatConnectors(initiateHttpConnector());
	return tomcat;
    }

    private Connector initiateHttpConnector() {
	Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
	connector.setScheme("http");
	connector.setPort(80);
	connector.setSecure(false);
	connector.setRedirectPort(443);
	return connector;
    }

/*
    public TomcatServletWebServerFactory containerFactory() {
	return new TomcatServletWebServerFactory() {
	    protected void customizeConnector(Connector connector) {
		int maxSize = 10485760; //10MB
		super.customizeConnector(connector);
		connector.setMaxPostSize(maxSize);
		connector.setMaxSavePostSize(maxSize);
		if (connector.getProtocolHandler() instanceof AbstractHttp11Protocol) {
		    ((AbstractHttp11Protocol <?>) connector.getProtocolHandler()).setMaxSwallowSize(maxSize);
		}
	    }
	};
    }
*/

}
