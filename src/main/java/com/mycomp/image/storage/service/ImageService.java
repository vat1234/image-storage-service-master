package com.mycomp.image.storage.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.mycomp.image.storage.service.exception.StorageServiceException;
import com.mycomp.image.storage.service.payload.ImageResponse;
import com.mycomp.image.storage.service.payload.UploadImageResponse;

/**
 * 
 * @author vat
 *
 */
public interface ImageService {
	UploadImageResponse uploadImage(String user, String albumName, MultipartFile file);

	ImageResponse getImageByName(String user, String albumName, String imageName);

	List<ImageResponse> getImages(String user, String albumName);

	void deleteImage(String user, String albumName, String imageName) throws StorageServiceException;

}
