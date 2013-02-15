package com.flipbox.arsenalindonesia;

import android.graphics.Bitmap;

public class Event {

	private String id;
	private String latitude;
	private String longitude;
	private String startDate;
	private String endDate;
	private String dateCreated;
	private String datePublished;
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

	public Event(String id, String name, String desc, String startDate,
			String endDate, String loc, String category, String imgUrl,
			String contact, String dateCreated, String datePublished,
			String author, String ticketPrice, String lat, String longtd) {
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
		this.latitude = lat;
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
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
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

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getDatePublished() {
		return datePublished;
	}

	public void setDatePublished(String datePublished) {
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
