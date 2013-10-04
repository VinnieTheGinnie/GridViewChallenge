package com.vvendemia.gridviewchallenge.models;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class ImagesResponse {
	
	@SerializedName("responseData")
	private Data responseData;

	public Data getData() {
		return responseData;
	}

	public void setData(Data data) {
		this.responseData = data;
	}
	
}
