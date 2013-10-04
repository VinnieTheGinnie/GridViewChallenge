package com.vvendemia.gridviewchallenge;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.imageloader.ImageLoader;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.vvendemia.gridviewchallenge.models.Data;
import com.vvendemia.gridviewchallenge.models.ImagesResponse;
import com.vvendemia.gridviewchallenge.transactions.AbsHttpTask;

public class MainActivity extends Activity  {

	private static final String URL_ONE = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=puppies&callback=processResults&rsz=8";
	private static final String URL_TWO = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=kittens&callback=processResults&rsz=8";
	
	
	private LayoutInflater mInflater;
	private GridView mGrid;
	GridViewAdapter mAdapter;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mInflater = this.getLayoutInflater();
		
		mGrid = (GridView) findViewById(R.id.myGrid);
		mAdapter = new GridViewAdapter(this , 0,  new ArrayList<String>());
		mGrid.setAdapter(mAdapter);
		
		fetchImageData();
		fetchImageDataAgain();

	}




	public void fetchImageData() {
		Log.d("TAG", "EFetching Image Data");
		new FetchImagesTask("GET", URL_ONE).execute();
	}

	public void fetchImageDataAgain() {
		Log.d("TAG", "EFetching Image Data");
		new FetchImagesTask("GET", URL_TWO).execute();
	}

	private class FetchImagesTask extends AbsHttpTask {

		public FetchImagesTask(String verb, String url) {
			super(verb, url);
		}

		@Override
		protected void onError(String error) {
			// TODO Show dialog/message
			Log.d("TAG", "Error occured");

		}

		@Override
		protected void onSuccess(String response) {

			String newResp = response.substring(15 , response.length() - 1);

			ImagesResponse imageResponse = new Gson().fromJson(
					newResp, ImagesResponse.class);


			Data results = imageResponse.getData();

			onImagesFound(results);

		}

	}
	
	private void onImagesFound(Data response) {
		mAdapter.addAll(response.getUrlStringList());
		mAdapter.notifyDataSetChanged();
	}
}
