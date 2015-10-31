package org.springside.examples.quickstart.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_advert")
public class Advert extends IdEntity{
	private String purl;
	public String getPurl() {
		return purl;
	}

	public void setPurl(String purl) {
		this.purl = purl;
	}
	private String url;
	private String title;
	private String type;
	
	public Advert() {
	}

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
