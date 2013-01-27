package com.flipbox.myevent;

import java.util.Date;

import android.graphics.Bitmap;

public class Event {

	private int id;
	private int latitude;
	private int longitude;
	private Date startDate;
	private Date endDate;
	private Date dateCreated;
	private Date datePublished;
	private String name;
	private String desc;
	private String location;
	private String category;
	private String imgUrl;
	private String author;
	private String contact;
	private String ticketPrice;
	private Bitmap image;

	public Event() {

	}

	public Event(int id, String name, String desc, Date startDate,
			Date endDate, String loc, String category, String imgUrl,
			String contact, Date dateCreated, Date datePublished,
			String author, String ticketPrice, int longtd, int latd) {
		this.id = id;
		this.name = name;
		this.desc = desc;
		this.startDate = startDate;
		this.endDate = endDate;
		this.location = loc;
		this.category = category;
		this.imgUrl = imgUrl;
		this.contact = contact;
		this.dateCreated = dateCreated;
		this.datePublished = datePublished;
		this.author = author;
		this.ticketPrice = ticketPrice;
		this.latitude = latd;
		this.longitude = longtd;
		
	}

	@Override
	public String toString() {
		return name + " " + startDate.toString();
		// return title + " " + startDate.toString() + " to " +
		// endDate.toString()
		// + ". " + image.toString() + " " + token;
	}

	// Accessors and Mutators
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getImageUrl() {
		return imgUrl;
	}

	public void setImageUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Bitmap getImage() {
		return image;
	}

	public int getLatitude() {
		return latitude;
	}

	public void setLatitude(int latitude) {
		this.latitude = latitude;
	}

	public int getLongitude() {
		return longitude;
	}

	public void setLongitude(int longitude) {
		this.longitude = longitude;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getDatePublished() {
		return datePublished;
	}

	public void setDatePublished(Date datePublished) {
		this.datePublished = datePublished;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getTicketPrice() {
		return ticketPrice;
	}

	public void setTicketPrice(String ticketPrice) {
		this.ticketPrice = ticketPrice;
	}
}
