package com.mycomp.image.storage.service.payload;

/**
 * 
 * @author vat
 *
 */
public class ImageResponse {
	private String name;
	private String downloadUri;
	private String lastModifiedTime;
	private String size;

	public ImageResponse(String name, String downloadUri, String lastModifiedTime, String size) {
		super();
		this.name = name;
		this.downloadUri = downloadUri;
		this.lastModifiedTime = lastModifiedTime;
		this.size = size;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDownloadUri() {
		return downloadUri;
	}

	public void setDownloadUri(String downloadUri) {
		this.downloadUri = downloadUri;
	}

	public String getLastModifiedTime() {
		return lastModifiedTime;
	}

	public void setLastModifiedTime(String lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

}
