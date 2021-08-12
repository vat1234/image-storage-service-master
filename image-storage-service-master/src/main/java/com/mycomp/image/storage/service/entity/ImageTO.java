package com.mycomp.image.storage.service.entity;

/**
 * 
 * @author vat
 *
 */
public class ImageTO {
	private String name;
	private String downloadUrl;
	private String lastModifiedTime;
	private String size;

	public ImageTO(final String name, final String downloadUrl, final String lastModifiedTime, final String size) {
		super();
		this.name = name;
		this.downloadUrl = downloadUrl;
		this.lastModifiedTime = lastModifiedTime;
		this.size = size;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(final String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public String getLastModifiedTime() {
		return lastModifiedTime;
	}

	public void setLastModifiedTime(final String lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}

	public String getSize() {
		return size;
	}

	public void setSize(final String size) {
		this.size = size;
	}

}
