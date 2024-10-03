package org.nuclearfog.texter.model;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Post implements Serializable {

	public static final int ID_SKETCH = 0;

	private int id;
	private long timestamp;
	private String title = "";
	private String html = "";
	private List<Image> images = new ArrayList<>();


	public Post() {
		this(ID_SKETCH);
	}


	public Post(int id) {
		timestamp = System.currentTimeMillis();
		this.id = id;
	}


	public Post(int id, long timestamp, String title, String html) {
		this.id = id;
		this.timestamp = timestamp;
		this.title = title;
		this.html = html;
	}


	public int getId() {
		return id;
	}


	public long getTimestamp() {
		return timestamp;
	}


	public void setText(@NonNull String html) {
		this.html = html;
	}


	@NonNull
	public String getText() {
		return html;
	}


	public void setTitle(@NonNull String title) {
		this.title = title;
	}


	@NonNull
	public String getTitle() {
		return title;
	}


	public void addImage(Image image) {
		image.setRank(images.size());
		images.add(image);
	}


	public void addImages(List<Image> images) {
		this.images.addAll(images);
	}


	public void removeImage(int position) {
		if (position >= 0 && position < images.size()) {
			images.remove(position);
		}
	}


	public Image[] getImages() {
		return images.toArray(new Image[0]);
	}
}