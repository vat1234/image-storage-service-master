package com.mycomp.image.storage.service;

import com.mycomp.image.storage.service.payload.Album;

/**
 * 
 * @author vat
 *
 */
public interface AlbumService {

	void createAlbum(String user, Album album);

	void deleteAlbum(String user, String albumName);

}
