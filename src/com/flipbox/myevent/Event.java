package com.flipbox.myevent;

import java.util.Date;

import android.graphics.Bitmap;


public class Event {
	
	private int id;
	private String title;
	private Date start_date;
	private Date end_date;
	private String desc_uri;
	private Bitmap image;
	private String token;
	
	public Event() {
		
	}
	
	public Event(int id, String title, Date start_date, Date end_date,
			String desc_uri, Bitmap image, String token) {
		this.id = id;
		this.title = title;
		this.start_date = start_date;
		this.end_date = end_date;
		this.desc_uri = desc_uri;
		this.image = image;
		this.token = token;
	}
	
	@Override
	public String toString() {
		return title + " " + start_date.toString();
		//return title + " " + start_date.toString() + " to " + end_date.toString()
				//+ ". " + image.toString() + " " + token; 
	}
	
	//Accessors and Mutators
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getStart_date() {
		return start_date;
	}

	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}

	public Date getEnd_date() {
		return end_date;
	}

	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}

	public String getDesc_uri() {
		return desc_uri;
	}

	public void setDesc_uri(String desc_uri) {
		this.desc_uri = desc_uri;
	}

	public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	
}
