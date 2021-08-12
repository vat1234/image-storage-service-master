package com.mycomp.image.storage.service.entity;

import java.io.InputStream;
import java.util.List;

import com.mycomp.image.storage.service.event.State;

/**
 * 
 * @author vat
 *
 */
public interface StorageEntity {

	State createAlbum(String name, String user);

	void deteAlbum(String userName, String albumName);

	void uploadImage(String user, String albumName, String imageName, InputStream inputStream);

	ImageTO getImageByName(String user, String albumName, String imageName);

	List<ImageTO> getImages(String user, String albumName);

	void deleteImage(String user, String albumName, String imageName);

}
