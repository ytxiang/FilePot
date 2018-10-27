package com.ytxiang.dao;

import java.util.List;
import org.springframework.stereotype.Repository;

import org.hibernate.query.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.ytxiang.dao.UserDAO;
import com.ytxiang.model.S3File;
import com.ytxiang.model.User;

@Repository("userDao")
@Transactional
@SuppressWarnings("unchecked")
public class UserDAOImpl implements UserDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public List<User> getAllUser() {
		List<User> user = sessionFactory.getCurrentSession()
										.createQuery("from User", User.class)
										.list();
		return user;
	}

	@Override
	public User getUser(String userName, String password) {
		Query<User> query = sessionFactory.getCurrentSession()
										  .getNamedQuery("findUserByCred");
		query.setParameter("userName", userName);
		query.setParameter("password", password);
		List<User> list = query.getResultList();

		return (list.size() > 0) ? list.get(0) : null;

	}

	@Override
	public User getUserByUserName(String userName) {
		Query<User> query = sessionFactory.getCurrentSession()
				.getNamedQuery("findUserByName");
		query.setParameter("userName", userName);
		List<User> list = query.getResultList();

		return (list.size() > 0) ? list.get(0) : null;
	}

	@Override
	public void createUser(User user) {
		sessionFactory.getCurrentSession().save(user);
	}
}