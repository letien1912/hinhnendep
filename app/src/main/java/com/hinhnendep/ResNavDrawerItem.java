package com.hinhnendep;

public class ResNavDrawerItem {

	private String albumId, albumTitle;
	private int img;
	// boolean flag to check for recent album
	private boolean isRecentAlbum = false;

	public ResNavDrawerItem() {
	}

	public ResNavDrawerItem(String albumId, String albumTitle, int img) {
		this.albumId = albumId;
		this.albumTitle = albumTitle;
		this.img = img;
	}

	public ResNavDrawerItem(String albumId, String albumTitle, int img,
			boolean isRecentAlbum) {
		this.img = img;
		this.albumTitle = albumTitle;
		this.isRecentAlbum = isRecentAlbum;
	}


	public int getImg() {
		return img;
	}

	public void setImg(int img) {
		this.img = img;
	}

	public String getAlbumId() {
		return albumId;
	}

	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}

	public String getTitle() {
		return this.albumTitle;
	}

	public void setTitle(String title) {
		this.albumTitle = title;
	}

	public boolean isRecentAlbum() {
		return isRecentAlbum;
	}

	public void setRecentAlbum(boolean isRecentAlbum) {
		this.isRecentAlbum = isRecentAlbum;
	}
}
