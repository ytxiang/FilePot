package com.ytxiang.service;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Properties;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.xml.bind.ValidationException;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.S3ClientOptions;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.util.StringUtils;
import com.ytxiang.model.S3File;
import com.ytxiang.model.User;
import com.ytxiang.bean.UserBean;
import com.ytxiang.dao.S3FileDAO;
import com.ytxiang.dao.UserDAO;
import com.ytxiang.dto.S3FileDTO;
import com.ytxiang.dto.S3FilePotDTO;
import com.ytxiang.dto.UserDTO;

@Service
@SuppressWarnings("deprecation")
public class FilepotServiceImpl implements FilepotService {

	@Autowired
	UserDAO userDao;

	@Autowired
	S3FileDAO s3FileDao;

	@Value("${filepot.s3.bucket}")
	private String S3_BUCKET;

	@Value("${filepot.s3.namespace}")
	private String S3_NAMESPACE;

	@Value("${filepot.s3.baseurl}")
	private String S3_BASEURL;

	@Value("${filepot.s3.access.id}")
	private String S3_ACCESS;

	@Value("${filepot.s3.access.secret}")
	private String S3_SECRET;

	private AmazonS3Client s3Client;

	private void initS3Client() {
		// fetch access keys from property file
		AWSCredentials cred = new BasicAWSCredentials(S3_ACCESS, S3_SECRET);

		// instantiate S3 client
		s3Client = new AmazonS3Client(cred);

		// Set S3 region
		s3Client.setRegion(Region.getRegion(Regions.US_WEST_1));

		// Enable Transfer Acceleration Mode
		s3Client.setS3ClientOptions(S3ClientOptions.builder().setAccelerateModeEnabled(true).build());
	}

	@PostConstruct
	private void postInit() {
		initS3Client();
	}

	@Override
	public UserDTO getUser(String userName) {
		User user = userDao.getUserByUserName(userName);

		if (user == null) {
			return null;
		}
		UserDTO userDTO = new UserDTO(user.getId(), user.getUsername(),
				user.getFullName(), user.getNick());
		return userDTO;
	}

	@Override
	public void signUpUser(UserBean userbean) throws ValidationException {

		if (userbean.getUsername() == null || userbean.getUsername().equals("")
				|| userbean.getPassword() == null || userbean.getPassword().equals("")
				|| userbean.getConfirm() == null || userbean.getConfirm().equals("")
				|| userbean.getFullname() == null || userbean.getFullname().equals(""))
			throw new ValidationException("Must provide all required information for registration");

		if (userDao.getUserByUserName(userbean.getUsername()) != null)
			throw new ValidationException("User " + userbean.getUsername() +
					" already exists!");

		if (!userbean.getPassword().equals(userbean.getConfirm()))
			throw new ValidationException("Unmatched passwords, please re-enter");

		User user = new User();
		user.setUsername(userbean.getUsername());
		user.setPassword(userbean.getPassword());
		user.setFullName(userbean.getFullname());
		user.setNick(userbean.getNick());
		userDao.createUser(user);
	}

	@Override
	public S3FileDTO upload(MultipartFile mfile, String notes, String userName)
			throws IllegalStateException, IOException, ValidationException {

		String fileName = mfile.getOriginalFilename();

		return upload(mfile, fileName, userName, notes);
	}

	@Override
	public S3FileDTO upload(MultipartFile mfile, Integer fid, String userName, String notes)
			throws IllegalStateException, IOException, ValidationException {

		String fileName = s3FileDao.getFileName(fid);

		return upload(mfile, fileName, userName, notes);
	}

	private S3FileDTO upload(MultipartFile mfile, String fileName, String userName, String notes)
			throws IllegalStateException, IOException, ValidationException {

		final int maxFileSize = 10485760;

		if (mfile.getSize() > maxFileSize)
			throw new ValidationException("File size should not be over 10MB");

		/* Assemble to a single file */
		File file = File.createTempFile(fileName, "");
		mfile.transferTo(file);
		String key = new String();
		if (!S3_NAMESPACE.trim().equals("")) {
			 key = S3_NAMESPACE.endsWith("/") ? S3_NAMESPACE : S3_NAMESPACE + "/";
		}
		key += userName + "/" + fileName;

		/* Upload to S3 */
		@SuppressWarnings("unused")
		PutObjectResult poResult = s3Client.putObject(S3_BUCKET, key, file);

		/* Create DB entry if new file or else update */
		S3FileDTO s3FileDTO = createOrUpdateS3File(fileName, notes, key, userName,
				mfile.getSize());
		return s3FileDTO;
	}

	private S3FileDTO createOrUpdateS3File(String fileName, String notes, String path,
			String userName, Long size) {

		User user = userDao.getUserByUserName(userName);

		S3File userFile = s3FileDao.getExistingFile(user.getId(), fileName);

		if (null == userFile) {
			userFile = new S3File();
			userFile.setFileName(fileName);
			userFile.setCreatedTime(new Timestamp(System.currentTimeMillis()));
			userFile.setModifiedTime(new Timestamp(System.currentTimeMillis()));
			userFile.setNotes(notes);
			userFile.setSize(FileUtils.byteCountToDisplaySize(size));
			userFile.setPath(S3_BASEURL + "/" + path);
			userFile.setUser(user);
		} else {
			userFile.setModifiedTime(new Timestamp(System.currentTimeMillis()));
			userFile.setSize(FileUtils.byteCountToDisplaySize(size));
			if (!StringUtils.isNullOrEmpty(notes)) {
				userFile.setNotes(notes);
			}
		}

		s3FileDao.createOrUpdate(userFile);
		S3File returnUserFile = s3FileDao.getExistingFile(user.getId(), fileName);
		S3FileDTO s3FileDTO = new S3FileDTO(returnUserFile.getId(),
				returnUserFile.getFileName(), returnUserFile.getNotes(), returnUserFile.getSize(),
				returnUserFile.getPath(), returnUserFile.getCreatedTime(), returnUserFile.getModifiedTime(),
				returnUserFile.getUser().getFullName());

		return s3FileDTO;
	}

	private S3FilePotDTO getAllFiles(List<S3File> s3Files) {
		S3FilePotDTO filesDTO = new S3FilePotDTO();
		ArrayList<S3FileDTO> fileList = new ArrayList<S3FileDTO>();

		if (s3Files!= null && !CollectionUtils.isEmpty(s3Files)) {
			for (S3File f : s3Files) {
				S3FileDTO s3FileDTO = new S3FileDTO(f.getId(), f.getFileName(),
						f.getNotes(), f.getSize(), f.getPath(),
						f.getCreatedTime(), f.getModifiedTime(),
						f.getUser().getFullName());
				fileList.add(s3FileDTO);
			}
		}

		filesDTO.setFileList(fileList);
		return filesDTO;
	}

	@Override
	public S3FilePotDTO getFileList() {
		List<S3File> s3Files = s3FileDao.getAllS3File();

		return getAllFiles(s3Files);
	}

	@Override
	public S3FilePotDTO getFileList(String userName) {
		User user = userDao.getUserByUserName(userName);

		List<S3File> s3Files = s3FileDao.getAllS3File(user.getId());
		return getAllFiles(s3Files);
	}

	@Override
	public void deleteFile(Integer fileId) {
		String username = s3FileDao.getFileOwner(fileId);

		if (username != null)
			deleteFile(fileId, username);
	}

	@Override
	public void deleteFile(Integer fileId, String userName) {
		// Fetch bucket name and key
		String fileName = s3FileDao.getFileName(fileId);
		String key = new String();
		if (!S3_NAMESPACE.trim().equals("")) {
			 key = S3_NAMESPACE.endsWith("/") ? S3_NAMESPACE : S3_NAMESPACE + "/";
		}
		key += userName + "/" + fileName;

		// delete file from S3
		s3Client.deleteObject(S3_BUCKET, key);

		// delete entry from RDS table
		s3FileDao.deleteFile(fileId);
	}
}
