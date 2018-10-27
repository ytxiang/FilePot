package com.ytxiang.dao;

import java.util.List;
import com.ytxiang.model.S3File;

public interface S3FileDAO {

	/**
	 * Delete a File
	 *
	 * @param file ID
	 */
	void deleteFile(Integer fid);

	/**
	 * Get File metadata from user ID and file name
	 *
	 * @param user ID
	 * @param fileName
	 * @return
	 */
	S3File getExistingFile(Integer uid, String fileName);

	/**
	 * Update file metadata in database
	 *
	 * @param fileName
	 */
	void createOrUpdate(S3File file);

	/**
	 * Retrieve all files for a particular user
	 * @param user ID
	 * @return
	 */
	List<S3File> getAllS3File(Integer uid);

	/**
	 * Get file name from file ID
	 * @param file ID
	 * @return file name
	 */
	public String getFileName(Integer fid);
}
