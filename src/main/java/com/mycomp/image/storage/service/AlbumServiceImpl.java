package com.mycomp.image.storage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycomp.image.storage.service.entity.StorageEntity;
import com.mycomp.image.storage.service.entity.StorageServiceEntity;
import com.mycomp.image.storage.service.event.Event;
import com.mycomp.image.storage.service.event.State;
import com.mycomp.image.storage.service.payload.Album;
import com.mycomp.image.storage.service.producer.Producer;
import com.mycomp.image.storage.service.properties.StorageServiceProperties;

@Service("albumService")
public class AlbumServiceImpl implements AlbumService {
	@Autowired
	private StorageServiceProperties storageServiceProperties;

	@Autowired
	public AlbumServiceImpl() {

	}

	@Override
	public void createAlbum(String user, final Album album) {
		final StorageEntity storageEntity = new StorageServiceEntity(storageServiceProperties);
		State state = storageEntity.createAlbum(user, album.getName());
		if (state == State.NEW) {
			final Producer producer = new Producer(storageServiceProperties);
			producer.sendMessage(Event.CREATE, "Album " + album.getName() + " created for user " + user);
		}

	}

	@Override
	public void deleteAlbum(String user, String albumName) {
		StorageEntity storageEntity = new StorageServiceEntity(storageServiceProperties);
		storageEntity.deteAlbum(user, albumName);
		final Producer producer = new Producer(storageServiceProperties);
		producer.sendMessage(Event.DELETE, "Deleted album " + albumName + " from user "+user);

	}

}
