package com.dch.service;

import com.sun.jersey.spi.spring.container.servlet.SpringServlet;
import com.sun.jersey.test.framework.AppDescriptor;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import org.junit.Before;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;



public abstract class BaseTest extends JerseyTest {

    @Override
    protected AppDescriptor configure() {
        WebAppDescriptor webAppDescriptor = new WebAppDescriptor.Builder("com.dch")
                .contextParam("contextConfigLocation", "classpath*:/spring*.xml")
                .servletClass(SpringServlet.class)
                .initParam("com.sun.jersey.api.json.POJOMappingFeature", "true")
                .servletPath("/api/")
                .contextListenerClass(ContextLoaderListener.class)
                .requestListenerClass(RequestContextListener.class)
                .build();
        return webAppDescriptor;
    }

    @Before
    public void beforeTest(){
    }
}
