package com.mycomp.image.storage.service.entity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import com.mycomp.image.storage.service.constants.Constants;
import com.mycomp.image.storage.service.event.State;
import com.mycomp.image.storage.service.exception.StorageServiceException;
import com.mycomp.image.storage.service.properties.StorageServiceProperties;

import io.minio.MinioClient;
import io.minio.Result;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidArgumentException;
import io.minio.errors.InvalidBucketNameException;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidExpiresRangeException;
import io.minio.errors.InvalidPortException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.NoResponseException;
import io.minio.errors.RegionConflictException;
import io.minio.messages.Item;

/**
 * 
 * @author vat
 *
 */
public class StorageServiceEntity implements StorageEntity {
	private StorageServiceProperties storageServiceProperties;
	private MinioClient minioClient;

	public StorageServiceEntity(StorageServiceProperties storageServiceProperties) {

		this.storageServiceProperties = storageServiceProperties;
		initialize();
	}

	private void initialize() {
		try {
			minioClient = new MinioClient(storageServiceProperties.getMinioServer(),
					storageServiceProperties.getMinioAccessKey(), storageServiceProperties.getMiniosecretKey());
		} catch (InvalidEndpointException | InvalidPortException e) {
			throw new StorageServiceException(
					"Unable connect to client to storage server " + storageServiceProperties.getMinioServer());
		}

	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mycomp.image.storage.service.entity.StorageEntity#createAlbum(java.lang.
	 * String, java.lang.String)
	 */

	@Override
	public State createAlbum(final String user, final String name) {
		boolean isExist;
		try {
			isExist = minioClient.bucketExists(user);
			if (!isExist) {
				minioClient.makeBucket(user);
			}
			if (!isAlbumPresent(user, name)) {
				final String dummyString = "dummy";
				final InputStream is = new ByteArrayInputStream(dummyString.getBytes());
				minioClient.putObject(user, name + Constants.FILE_SEPARATOR + Constants.DUMMY_FILE_NAME, is,
						"application/octet-stream");
				System.out.println("Bucket created successfully");
				return State.NEW;
			} else {
				return State.EXISTS;
			}
		} catch (InvalidKeyException | InvalidBucketNameException | NoSuchAlgorithmException | InsufficientDataException
				| NoResponseException | ErrorResponseException | InternalException | InvalidResponseException
				| IOException | XmlPullParserException | RegionConflictException | InvalidArgumentException e) {
			throw new StorageServiceException("Album " + name + "creation failed", e);
		}

	}

	private boolean isAlbumPresent(final String user, final String name)
			throws XmlPullParserException, InvalidKeyException, InvalidBucketNameException, NoSuchAlgorithmException,
			InsufficientDataException, NoResponseException, ErrorResponseException, InternalException, IOException {
		final Iterable<Result<Item>> myObjects = minioClient.listObjects(user);
		for (final Result<Item> result : myObjects) {
			final Item item = result.get();
			System.out.println(item.lastModified() + ", " + item.size() + ", " + item.objectName());
			final String objectName = item.objectName();
			final String[] values = objectName.split(Constants.FILE_SEPARATOR);
			if (values != null) {
				if (values[0].equals(name)) {
					return true;
				}
			}
		}
		return false;

	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mycomp.image.storage.service.entity.StorageEntity#uploadImage(java.lang.
	 * String, java.lang.String, java.lang.String, java.io.InputStream)
	 */

	@Override
	public void uploadImage(final String user, final String albumName, final String imageName,
			final InputStream inputStream) {

		try {
			minioClient.putObject(user, albumName + Constants.FILE_SEPARATOR + imageName, inputStream,
					"application/octet-stream");

		} catch (InvalidKeyException | InvalidBucketNameException | NoSuchAlgorithmException | InsufficientDataException
				| NoResponseException | ErrorResponseException | InternalException | InvalidResponseException
				| IOException | XmlPullParserException | InvalidArgumentException e) {
			throw new StorageServiceException("Unable to upload image " + imageName + "in album " + albumName, e);
		}

	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mycomp.image.storage.service.entity.StorageEntity#getImageByName(java.lang
	 * .String, java.lang.String, java.lang.String)
	 */

	@Override
	public ImageTO getImageByName(final String user, final String albumName, final String imageName) {
		try {
			verifyIfUserExists(user);
			final String objectName = albumName + Constants.FILE_SEPARATOR + imageName;
			final Item item = getItem(user, objectName);
			final String lastModifiedTime = item.lastModified().toString();

			final String imageUrl = minioClient.presignedGetObject(user, objectName, 60 * 60 * 24);

			final ImageTO imageTO = new ImageTO(imageName, imageUrl, lastModifiedTime,
					String.valueOf(item.objectSize()));
			return imageTO;

		} catch (InvalidKeyException | InvalidBucketNameException | NoSuchAlgorithmException | InsufficientDataException
				| NoResponseException | ErrorResponseException | InternalException | InvalidResponseException
				| IOException | XmlPullParserException | InvalidExpiresRangeException e) {
			throw new StorageServiceException(
					"Unable to get image " + imageName + " from album " + albumName + " for user " + user, e);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mycomp.image.storage.service.entity.StorageEntity#getImages(java.lang.
	 * String, java.lang.String)
	 */
	@Override
	public List<ImageTO> getImages(final String user, final String albumName) {
		final List<ImageTO> imageTOs = new ArrayList<>();

		boolean isAlbumExists = false;
		try {
			verifyIfUserExists(user);
			final Iterable<Result<Item>> myObjects = minioClient.listObjects(user);
			for (final Result<Item> result : myObjects) {
				final Item item = result.get();
				final String objectName = item.objectName();
				final String[] values = objectName.split(Constants.FILE_SEPARATOR);
				if (values != null) {
					if (values[0].equals(albumName)) {
						isAlbumExists = true;
						if (!values[1].equals(Constants.DUMMY_FILE_NAME)) {
							updateImageTO(user, imageTOs, item, objectName, values);
						}
					}
				}
			}
			if (!isAlbumExists) {
				throw new StorageServiceException("Album " + albumName + " doesn't exist");
			}

		} catch (InvalidKeyException | InvalidBucketNameException | NoSuchAlgorithmException | InsufficientDataException
				| NoResponseException | ErrorResponseException | InternalException | InvalidResponseException
				| IOException | XmlPullParserException | InvalidExpiresRangeException e) {
			throw new StorageServiceException("Unable to get images  from album " + albumName + " for user " + user, e);
		}

		return imageTOs;
	}

	private void verifyIfUserExists(final String user) throws InvalidBucketNameException, NoSuchAlgorithmException,
			InsufficientDataException, IOException, InvalidKeyException, NoResponseException, XmlPullParserException,
			ErrorResponseException, InternalException, InvalidResponseException {
		final boolean isExists = minioClient.bucketExists(user);
		if (!isExists) {
			throw new StorageServiceException("User " + user + " doesn't exist");
		}
	}

	private void updateImageTO(final String user, final List<ImageTO> imageTOs, final Item item,
			final String objectName, final String[] values) throws InvalidBucketNameException, NoSuchAlgorithmException,
			InsufficientDataException, IOException, InvalidKeyException, NoResponseException, XmlPullParserException,
			ErrorResponseException, InternalException, InvalidExpiresRangeException, InvalidResponseException {
		final String imageUrl = minioClient.presignedGetObject(user, objectName, 60 * 60 * 24);
		final ImageTO imageTO = new ImageTO(values[1], imageUrl, item.lastModified().toString(),
				String.valueOf(item.objectSize()));
		imageTOs.add(imageTO);
	}

	private Item getItem(final String user, final String objectName)
			throws XmlPullParserException, InvalidKeyException, InvalidBucketNameException, NoSuchAlgorithmException,
			InsufficientDataException, NoResponseException, ErrorResponseException, InternalException, IOException {
		final Iterable<Result<Item>> myObjects = minioClient.listObjects(user);
		for (final Result<Item> result : myObjects) {
			final Item item = result.get();
			if (item.objectName().equals(objectName)) {
				return item;
			}

		}
		throw new StorageServiceException("image not available " + objectName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mycomp.image.storage.service.entity.StorageEntity#deleteImage(java.lang.
	 * String, java.lang.String, java.lang.String)
	 */
	@Override
	public void deleteImage(final String user, final String albumName, final String imageName) {
		try {

			final Iterable<Result<Item>> myObjects = minioClient.listObjects(user);
			verifyIfUserExists(user);

			if (!verifyIfAlbumExists(myObjects, albumName)) {
				throw new StorageServiceException("Album doesn't exist " + albumName);
			}
			if (!verifyIfImageExists(myObjects, imageName)) {
				throw new StorageServiceException("Image " + imageName + " doesn't exist in album " + albumName);
			}
			minioClient.removeObject(user, albumName + Constants.FILE_SEPARATOR + imageName);
		} catch (InvalidKeyException | InvalidBucketNameException | NoSuchAlgorithmException | InsufficientDataException
				| NoResponseException | ErrorResponseException | InternalException | InvalidResponseException
				| IOException | XmlPullParserException | InvalidArgumentException e) {
			throw new StorageServiceException("Unable to delte image " + imageName + " from album " + albumName);
		}

	}

	private boolean verifyIfImageExists(final Iterable<Result<Item>> myObjects, final String imageName)
			throws XmlPullParserException, InvalidKeyException, InvalidBucketNameException, NoSuchAlgorithmException,
			InsufficientDataException, NoResponseException, ErrorResponseException, InternalException, IOException {

		for (final Result<Item> result : myObjects) {
			final Item item = result.get();
			final String objectName = item.objectName();
			final String[] values = objectName.split(Constants.FILE_SEPARATOR);
			if (values != null) {
				if (values[1].equals(imageName)) {
					return true;
				}
			}

		}
		return false;

	}

	private boolean verifyIfAlbumExists(final Iterable<Result<Item>> myObjects, final String albumName)
			throws XmlPullParserException, InvalidKeyException, InvalidBucketNameException, NoSuchAlgorithmException,
			InsufficientDataException, NoResponseException, ErrorResponseException, InternalException, IOException {
		for (final Result<Item> result : myObjects) {
			final Item item = result.get();
			final String objectName = item.objectName();
			final String[] values = objectName.split(Constants.FILE_SEPARATOR);
			if (values != null) {
				if (values[0].equals(albumName)) {
					return true;
				}
			}

		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mycomp.image.storage.service.entity.StorageEntity#deteAlbum(java.lang.
	 * String, java.lang.String)
	 */
	@Override
	public void deteAlbum(final String userName, final String albumName) {
		try {

			verifyIfUserExists(userName);
			final Iterable<Result<Item>> myObjects = minioClient.listObjects(userName);
			if (!verifyIfAlbumExists(myObjects, albumName)) {
				throw new StorageServiceException("Album doesn't exist " + albumName);
			}
			for (final Result<Item> result : myObjects) {
				final Item item = result.get();
				final String objectName = item.objectName();

				final String[] values = objectName.split(Constants.FILE_SEPARATOR);
				if (values != null) {
					if (values[0].equals(albumName)) {

						minioClient.removeObject(userName, objectName);

					}
				}

			}
			minioClient.removeObject(userName, Constants.FILE_SEPARATOR + albumName);

		} catch (InvalidKeyException | InvalidBucketNameException | NoSuchAlgorithmException | InsufficientDataException
				| NoResponseException | ErrorResponseException | InternalException | InvalidResponseException
				| IOException | XmlPullParserException | InvalidArgumentException e) {
			throw new StorageServiceException("Unable to delete album " + albumName, e);
		}

	}

}
