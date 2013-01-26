package com.flipbox.myevent;

import java.util.Date;

import android.graphics.Bitmap;

public class Event {

	private int id;
	private int latitude;
	private int longitude;
	private Date start_date;
	private Date end_date;
	private Date date_created;
	private Date date_published;
	private String name;
	private String desc;
	private String location;
	private String category;
	private String img_url;
	private String author;
	private String contact;
	private String ticket_price;

	public Event() {

	}

	public Event(int id, String name, String desc, Date start_date,
			Date end_date, String loc, String category, String img_url,
			String contact, Date date_created, Date date_published,
			String author, String ticket_price, int longtd, int latd) {
		this.id = id;
		this.name = name;
		this.desc = desc;
		this.start_date = start_date;
		this.end_date = end_date;
		this.location = loc;
		this.category = category;
		this.img_url = img_url;
		this.contact = contact;
		this.date_created = date_created;
		this.date_published = date_published;
		this.author = author;
		this.ticket_price = ticket_price;
		this.latitude = latd;
		this.longitude = longtd;
		
	}

	@Override
	public String toString() {
		return title + " " + start_date.toString();
		// return title + " " + start_date.toString() + " to " +
		// end_date.toString()
		// + ". " + image.toString() + " " + token;
	}

	// Accessors and Mutators
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
		return desc;
	}

	public void setDesc_uri(String desc_uri) {
		this.desc = desc_uri;
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
