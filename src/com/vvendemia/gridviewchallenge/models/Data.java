package com.vvendemia.gridviewchallenge.models;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Data {
	
	
	@SerializedName("results")
	private List<Result> results;
	
	public Data(List<Result> results) {
		super();
		this.results = results;
	}
	
	
	public  ArrayList<String> getUrlStringList() {
		ArrayList<String> urls = new ArrayList<String>();
		
		for(int i = 0; i < results.size(); i++) {
			urls.add(results.get(i).getUrl());
		}
		
		return urls;
	}

	public List<Result> getResults() {
		return results;
	}

	public void setResults(List<Result> results) {
		this.results = results;
	}

	
}
