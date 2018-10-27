
package com.ytxiang.server.main;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;

@SpringBootApplication(scanBasePackages = {"com.ytxiang"})
public class FilePotApplication {

	public static void main(String[] args) {
		SpringApplication.run(FilePotApplication.class, args);
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
