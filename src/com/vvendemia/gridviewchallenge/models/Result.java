package com.vvendemia.gridviewchallenge.models;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Result {

	@SerializedName("url")
	private String url;

	public Result(String url) {
		super();
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
}
