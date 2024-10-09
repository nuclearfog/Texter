package org.nuclearfog.texter.model;


public class Image {

	private String uri;
	private int x, y, w, h;
	private int rank;


	public Image(String uri) {
		this(uri, 0, 0, 0, 0, 0);
	}


	public Image(String uri, int x, int y, int w, int h, int rank) {
		this.uri = uri;
		this.rank = rank;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}


	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}


	public void setMeasurements(int w, int h) {
		this.w = w;
		this.h = h;
	}


	public void setRank(int rank) {
		this.rank = rank;
	}


	public int getX() {
		return x;
	}


	public int getY() {
		return y;
	}


	public int getWidth() {
		return w;
	}


	public int getHeight() {
		return h;
	}


	public String getPath() {
		return uri;
	}


	public int getRank() {
		return rank;
	}
}