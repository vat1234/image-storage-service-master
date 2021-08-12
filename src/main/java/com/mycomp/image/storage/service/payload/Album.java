package com.mycomp.image.storage.service.payload;

public class Album {

	private String name;

	public Album(final String name) {
		super();

		this.name = name;
	}

	public Album() {
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "AlbumTO [name=" + name + "]";
	}

}
