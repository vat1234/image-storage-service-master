package com.mycomp.image.storage.service.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mycomp.image.storage.service.AlbumService;
import com.mycomp.image.storage.service.ImageService;
import com.mycomp.image.storage.service.payload.Album;
import com.mycomp.image.storage.service.payload.AlbumResponse;
import com.mycomp.image.storage.service.payload.ImageResponse;
import com.mycomp.image.storage.service.payload.UploadImageResponse;

/**
 * 
 * @author vat
 *
 */
@RestController
public class StorageServiceController {
	@Resource(name = "albumService")
	private AlbumService albumService;
	@Resource(name = "imageService")
	private ImageService imageService;

	@RequestMapping(value = "/albums", method = RequestMethod.POST)
	public ResponseEntity<AlbumResponse> createAlbumns(@RequestHeader("user") final String user,
			@RequestBody final Album album) {
		this.albumService.createAlbum(user, album);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new AlbumResponse("Created album " + album.getName() + " for user " + user));

	}

	@RequestMapping(value = "/albums/{name}/images", method = RequestMethod.POST)
	public ResponseEntity<UploadImageResponse> uploadImage(@RequestHeader("user") final String user,
			@RequestParam("image") final MultipartFile file, @PathVariable("name") final String albumName) {
		final UploadImageResponse response = this.imageService.uploadImage(user, albumName, file);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);

	}

	@RequestMapping(value = "/albums/{albumname}/images/{imagename}", method = RequestMethod.GET)
	public ResponseEntity<ImageResponse> getImage(@RequestHeader("user") final String user,
			@PathVariable("albumname") final String albumName, @PathVariable("imagename") final String imageName) {
		final ImageResponse imageResponse = this.imageService.getImageByName(user, albumName, imageName);
		return ResponseEntity.status(HttpStatus.OK).body(imageResponse);

	}

	@RequestMapping(value = "/albums/{albumname}/images", method = RequestMethod.GET)
	public ResponseEntity<List<ImageResponse>> getImages(@RequestHeader("user") final String user,
			@PathVariable("albumname") final String albumName) {
		final List<ImageResponse> imageResponses = this.imageService.getImages(user, albumName);
		return ResponseEntity.status(HttpStatus.OK).body(imageResponses);

	}

	@RequestMapping(value = "/albums/{albumname}/images/{imagename}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteImage(@RequestHeader("user") final String user,
			@PathVariable("albumname") final String albumName, @PathVariable("imagename") final String imageName) {
		this.imageService.deleteImage(user, albumName, imageName);
		return ResponseEntity.status(HttpStatus.OK)
				.body("Deleted " + imageName + " image successfully from album " + albumName);

	}

	@RequestMapping(value = "/albums/{albumname}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteAlbum(@RequestHeader("user") final String user,
			@PathVariable("albumname") final String albumName) {
		this.albumService.deleteAlbum(user, albumName);
		return ResponseEntity.status(HttpStatus.OK)
				.body("Deleted album " + albumName + " succesfully from user " + user);

	}

}
