package com.mycomp.image.storage.service.payload;

public class AlbumResponse {

	private String name;

	public AlbumResponse(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
