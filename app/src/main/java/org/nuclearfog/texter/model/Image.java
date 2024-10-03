package org.nuclearfog.texter.model;


public class Image {

	public static final int NO_ID = -1;

	private int id;
	private String uri;
	private int x, y, w, h;
	private int rank;


	public Image(String uri) {
		this(uri, NO_ID, 0, 0, 0, 0, 0);
	}


	public Image(String uri, int id, int x, int y, int w, int h, int rank) {
		this.uri = uri;
		this.rank = rank;
		this.id = id;
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


	public int getId() {
		return id;
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