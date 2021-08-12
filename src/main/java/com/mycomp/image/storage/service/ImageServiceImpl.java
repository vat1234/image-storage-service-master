package com.mycomp.image.storage.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.mycomp.image.storage.service.constants.Constants;
import com.mycomp.image.storage.service.entity.ImageTO;
import com.mycomp.image.storage.service.entity.StorageEntity;
import com.mycomp.image.storage.service.entity.StorageServiceEntity;
import com.mycomp.image.storage.service.event.Event;
import com.mycomp.image.storage.service.exception.StorageServiceException;
import com.mycomp.image.storage.service.payload.ImageResponse;
import com.mycomp.image.storage.service.payload.SupportedType;
import com.mycomp.image.storage.service.payload.UploadImageResponse;
import com.mycomp.image.storage.service.producer.Producer;
import com.mycomp.image.storage.service.properties.StorageServiceProperties;
/*
 * 
 */

@Service("imageService")
public class ImageServiceImpl implements ImageService {
	@Autowired
	private StorageServiceProperties storageServiceProperties;

	@Autowired
	public ImageServiceImpl() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mycomp.image.storage.service.ImageService#uploadImage(java.lang.String,
	 * java.lang.String, org.springframework.web.multipart.MultipartFile)
	 */
	@Override
	public UploadImageResponse uploadImage(final String user, final String albumName, final MultipartFile file) {
		final String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		// Check if the file's name contains invalid characters
		validateImageName(fileName);
		if (!validateImageFormat(file.getContentType())) {
			throw new StorageServiceException("Not supported image format " + file.getContentType());
		}
		try {
			final StorageEntity storageEntity = new StorageServiceEntity(storageServiceProperties);
			storageEntity.uploadImage(user, albumName, fileName, file.getInputStream());
			final Producer producer = new Producer(storageServiceProperties);
			producer.sendMessage(Event.CREATE,
					"Image " + fileName + " uploaded in album " + albumName + " for user " + user);
			return new UploadImageResponse(fileName, file.getContentType(), file.getSize());

		} catch (final IOException e) {
			throw new StorageServiceException("Sorry! Unable get the stream" + fileName);
		}

	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mycomp.image.storage.service.ImageService#deleteImage(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */

	private void validateImageName(final String fileName) {
		if (fileName.contains("..")) {
			throw new StorageServiceException("Sorry! Filename contains invalid path sequence " + fileName);
		}
	}

	private boolean validateImageFormat(final String contentType) {
		for (final SupportedType supportedType : SupportedType.values()) {
			final String[] value = contentType.split(Constants.FILE_SEPARATOR);
			if (supportedType.name().equals(value[1])) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void deleteImage(final String user, final String albumName, final String imageName)
			throws StorageServiceException {
		final StorageEntity storageEntity = new StorageServiceEntity(storageServiceProperties);
		storageEntity.deleteImage(user, albumName, imageName);
		final Producer producer = new Producer(storageServiceProperties);
		producer.sendMessage(Event.DELETE, "Image " + imageName + " delted from album " + albumName);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mycomp.image.storage.service.ImageService#getImageByName(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public ImageResponse getImageByName(final String user, final String albumName, final String imageName) {
		final StorageEntity storageEntity = new StorageServiceEntity(storageServiceProperties);
		final ImageTO imageTO = storageEntity.getImageByName(user, albumName, imageName);
		if (null != imageTO) {
			return new ImageResponse(imageTO.getName(), imageTO.getDownloadUrl(), imageTO.getLastModifiedTime(),
					imageTO.getSize());
		}
		throw new StorageServiceException("Image " + imageName + " not found in album " + albumName + " for user");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mycomp.image.storage.service.ImageService#getImages(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public List<ImageResponse> getImages(final String user, final String albumName) {
		final StorageEntity storageEntity = new StorageServiceEntity(storageServiceProperties);
		final List<ImageTO> imageTOs = storageEntity.getImages(user, albumName);
		if (!imageTOs.isEmpty()) {

			return convert(imageTOs);
		}
		throw new StorageServiceException("Unable to load images from album " + albumName);
	}

	private List<ImageResponse> convert(final List<ImageTO> imageTOs) {
		final List<ImageResponse> imageResponses = new ArrayList<>();
		for (final ImageTO imageTO : imageTOs) {
			final ImageResponse imageResponse = new ImageResponse(imageTO.getName(), imageTO.getDownloadUrl(),
					imageTO.getLastModifiedTime(), imageTO.getSize());
			imageResponses.add(imageResponse);
		}
		return imageResponses;
	}

}
