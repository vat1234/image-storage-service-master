package com.mycomp.image.storage.service.payload;

/**
 * 
 * @author vat
 *
 */
public class UploadImageResponse {
	private String name;
	private String type;
	private long size;

	public UploadImageResponse(final String name, final String type, final long size) {
		this.name = name;
		this.type = type;
		this.size = size;
	}

	public String getName() {
		return name;
	}

	public void setName(final String fileName) {
		this.name = fileName;
	}

	public String getType() {
		return type;
	}

	public void setType(final String fileType) {
		this.type = fileType;
	}

	public long getSize() {
		return size;
	}

	public void setSize(final long size) {
		this.size = size;
	}

}