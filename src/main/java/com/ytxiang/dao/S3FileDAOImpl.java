package com.ytxiang.dao;

import java.util.List;

import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ytxiang.model.S3File;
import com.ytxiang.model.User;

/**
 * DAO Implementation class
 */
@Repository
@Transactional
@SuppressWarnings("unchecked")
public class S3FileDAOImpl implements S3FileDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void deleteFile(Integer fid) {
		Session session = sessionFactory.getCurrentSession();
		Object persistentInstance = session.load(S3File.class, fid);
		if (persistentInstance != null) {
			session.delete(persistentInstance);
		}
	}

	@Override
	public S3File getExistingFile(Integer userId, String fileName) {
		Query<S3File> query = sessionFactory.getCurrentSession()
										  .getNamedQuery("findExistingFile");
		query.setParameter("userId", userId);
		query.setParameter("fileName", fileName);
		List<S3File> list = query.getResultList();

		return (list.size() > 0) ? list.get(0) : null;
	}

	@Override
	public void createOrUpdate(S3File file) {
		sessionFactory.getCurrentSession().saveOrUpdate(file);
	}

	@Override
	public List<S3File> getAllS3File() {
		List<S3File> files = null;
		Query<S3File> query = sessionFactory.getCurrentSession()
										  .getNamedQuery("findAllFiles");
		files = query.list();

		return files;
	}

	@Override
	public List<S3File> getAllS3File(Integer uid) {
		List<S3File> files = null;
		Query<S3File> query = sessionFactory.getCurrentSession()
										  .getNamedQuery("findUserFiles");
		query.setParameter("userId", uid);
		files = query.list();

		return files;
	}

	@Override
	public String getFileName(Integer fid) {
		Query<S3File> query = sessionFactory.getCurrentSession()
										  .getNamedQuery("getFileByID");
		query.setParameter("fileId", fid);
		List<S3File> list = query.getResultList();

		return (list.size() > 0) ? list.get(0).getFileName() : null;
	}

	@Override
	public String getFileOwner(Integer fid) {
		Query<S3File> query = sessionFactory.getCurrentSession()
										  .getNamedQuery("getFileByID");
		query.setParameter("fileId", fid);
		List<S3File> list = query.getResultList();

		return (list.size() > 0) ? list.get(0).getUser().getUsername() : null;
	}
}
