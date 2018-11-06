package com.ytxiang.security;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

import com.ytxiang.dao.UserDAOImpl;
import com.ytxiang.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.stereotype.Service;

@Service
@Configurable
public class FacebookConnectionSignup implements ConnectionSignUp {

    UserDAOImpl userDao;

    public FacebookConnectionSignup(UserDAOImpl userDao) {
        this.userDao = userDao;
    }

    @Override
    public String execute(Connection<?> connection) {
	String username = connection.getKey().toString();
        if (userDao.getUserByUserName(username) == null) {
		final User user = new User();
		user.setUsername(username);
		user.setPassword("facebooklogin");
		user.setFullName(connection.getDisplayName());
		userDao.createUser(user);
	}
        return username;
    }

}
