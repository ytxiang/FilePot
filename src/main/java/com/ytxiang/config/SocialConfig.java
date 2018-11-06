package com.ytxiang.config;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.mem.InMemoryUsersConnectionRepository;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.UserIdSource;

import com.ytxiang.dao.UserDAOImpl;
import com.ytxiang.security.FacebookSignInAdapter;
import com.ytxiang.security.FacebookConnectionSignup;
import com.ytxiang.security.UserDetailService;

@Configuration
public class SocialConfig {

    @Value("${facebook.client.clientId}")
    private String FB_CLIENT_ID;

    @Value("${facebook.client.clientSecret}")
    private String FB_CLIENT_SECRET;

    @Autowired
    private UserDAOImpl userDao;

    @Autowired
    private UserDetailService userDetails;

    @Bean
    @Scope(value="request", proxyMode=ScopedProxyMode.INTERFACES)
    public ConnectionFactoryLocator connectionFactoryLocator() {
    	ConnectionFactoryRegistry registry = new ConnectionFactoryRegistry();
    	registry.addConnectionFactory(new FacebookConnectionFactory(FB_CLIENT_ID, FB_CLIENT_SECRET));
    	return registry;
    }

    @Bean
    public ProviderSignInController providerSignInController(ConnectionFactoryLocator connectionFactoryLocator,
        UsersConnectionRepository usersConnectionRepository) {
        ProviderSignInController providerSignInController =  new ProviderSignInController(connectionFactoryLocator,
            usersConnectionRepository(), new FacebookSignInAdapter(userDetails));
        providerSignInController.setPostSignInUrl("/");
        return providerSignInController;
    }

    @Bean
    @Scope(value="request", proxyMode=ScopedProxyMode.INTERFACES)
    public UsersConnectionRepository usersConnectionRepository() {
        final InMemoryUsersConnectionRepository inMemoryUsersConnectionRepository =
                new InMemoryUsersConnectionRepository(connectionFactoryLocator());
        inMemoryUsersConnectionRepository.setConnectionSignUp(new FacebookConnectionSignup(userDao));
        return inMemoryUsersConnectionRepository;
    }
}
