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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.imageloader.ImageLoader;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.vvendemia.gridviewchallenge.models.Data;
import com.vvendemia.gridviewchallenge.models.ImagesResponse;
import com.vvendemia.gridviewchallenge.transactions.AbsHttpTask;

public class MainActivity extends Activity implements ObservableScrollView.Callbacks {

	private static final String URL_ONE = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=puppies&callback=processResults&rsz=8";
	private static final String URL_TWO = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=kittens&callback=processResults&rsz=8";


	private static final int STATE_ONSCREEN = 0;
	private static final int STATE_OFFSCREEN = 1;
	private static final int STATE_RETURNING = 2;

	private TextView mQuickReturnView;
	private Context mContext;
	private View mPlaceholderView;
	private LinearLayout mainContent;
	private ObservableScrollView mObservableScrollView;
	private ScrollSettleHandler mScrollSettleHandler = new ScrollSettleHandler();
	private int mMinRawY = 0;
	private int mState = STATE_ONSCREEN;
	private int mQuickReturnHeight;
	private int mMaxScrollY;
	private LayoutInflater mInflater;



	// Roman Nuriks Quick Return pattern, can be found here 
	// git clone https://code.google.com/p/romannurik-code/


	@Override
	public void onCreate(Bundle savedInstanceState) {
		//        ViewGroup rootView = (ViewGroup) inflater
		//                .inflate(R.layout.fragment_content, container, false);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mContext = this;
		mainContent = (LinearLayout) findViewById(R.id.mainContent);
		mInflater = this.getLayoutInflater();
		
		mObservableScrollView = (ObservableScrollView) findViewById(R.id.scroll_view);
		mObservableScrollView.setCallbacks(this);

		mQuickReturnView = (TextView) findViewById(R.id.sticky);
		mQuickReturnView.setText("Custom Grid View");
		mPlaceholderView = findViewById(R.id.placeholder);

		mObservableScrollView.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						onScrollChanged(mObservableScrollView.getScrollY());
						mMaxScrollY = mObservableScrollView.computeVerticalScrollRange()
								- mObservableScrollView.getHeight();
						mQuickReturnHeight = mQuickReturnView.getHeight();
					}
				});

		fetchImageData();
		fetchImageDataAgain();

	}

	private void addRegularGrid(){

		int testNum = 39;
		int itemNum = 0;
		LinearLayout gridLayout = null;
		Drawable d = getResources().getDrawable(R.drawable.ic_launcher);

		for(int i = 0 ; i < testNum ; i++) {
			if(itemNum == 0 ) {
				gridLayout = (LinearLayout) mInflater.inflate(R.layout.grid_view_item, null);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT , 300);
				gridLayout.setLayoutParams(params);
				ImageView imageOne = (ImageView) gridLayout.findViewById(R.id.gridItemOne);
				imageOne.setImageDrawable(d);
				imageOne.setVisibility(View.VISIBLE);
				mainContent.addView(gridLayout);
				itemNum++;
			} else {
				ImageView imageTwo = (ImageView) gridLayout.findViewById(R.id.gridItemTwo);
				imageTwo.setImageDrawable(d);
				imageTwo.setVisibility(View.VISIBLE);
				itemNum = 0;
			}
		}
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
	
	@SuppressWarnings("unchecked")
	private void onImagesFound(Data response) {
		if(response.getResults().size() > 0) {
			new LoadImagesTask().execute(response.getUrlStringList()); 
		}else {
			addRegularGrid();
		}

	}

	private class LoadImagesTask extends AsyncTask<List<String>, Void, Void> {

		List<LinearLayout> layoutList;
		
		@Override
		protected Void doInBackground(List<String>... params) {
			layoutList = new ArrayList<LinearLayout>();
			addCustomGrid(params[0]);
			return null;
		}
		
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			addLayoutToMainContent(layoutList);
		}

		private void addCustomGrid(List<String> urls){

			int itemNum = 0;
			LinearLayout gridLayout = null;
			ImageLoader imageLoader = new ImageLoader();

			
			for(int i = 0 ; i < urls.size() ; i++) {
				if(itemNum == 0 ) {
					gridLayout = (LinearLayout) mInflater.inflate(R.layout.grid_view_item, null);
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT , 300);
					gridLayout.setLayoutParams(params);
					ImageView imageOne = (ImageView) gridLayout.findViewById(R.id.gridItemOne);
					Picasso.with(mContext).load(urls.get(i)).resize(550, 350).into(imageOne);
					imageOne.setVisibility(View.VISIBLE);
					layoutList.add(gridLayout);
//					mainContent.addView(gridLayout);
					itemNum++;
				} else {
					ImageView imageTwo = (ImageView) gridLayout.findViewById(R.id.gridItemTwo);
					Picasso.with(mContext).load(urls.get(i)).resize(550, 350).into(imageTwo);
					imageTwo.setVisibility(View.VISIBLE);
					itemNum = 0;
				}
			}
		}

	}
	
	private void addLayoutToMainContent(List<LinearLayout> layoutList) {
		for(int i = 0; i < layoutList.size(); i++) {
			mainContent.addView(layoutList.get(i));
		}
	}

	

	@Override
	public void onScrollChanged(int scrollY) {
		scrollY = Math.min(mMaxScrollY, scrollY);

		mScrollSettleHandler.onScroll(scrollY);

		int rawY = mPlaceholderView.getTop() - scrollY;
		int translationY = 0;

		switch (mState) {
		case STATE_OFFSCREEN:
			if (rawY <= mMinRawY) {
				mMinRawY = rawY;
			} else {
				mState = STATE_RETURNING;
			}
			translationY = rawY;
			break;

		case STATE_ONSCREEN:
			if (rawY < -mQuickReturnHeight) {
				mState = STATE_OFFSCREEN;
				mMinRawY = rawY;
			}
			translationY = rawY;
			break;

		case STATE_RETURNING:
			translationY = (rawY - mMinRawY) - mQuickReturnHeight;
			if (translationY > 0) {
				translationY = 0;
				mMinRawY = rawY - mQuickReturnHeight;
			}

			if (rawY > 0) {
				mState = STATE_ONSCREEN;
				translationY = rawY;
			}

			if (translationY < -mQuickReturnHeight) {
				mState = STATE_OFFSCREEN;
				mMinRawY = rawY;
			}
			break;
		}
		mQuickReturnView.animate().cancel();
		mQuickReturnView.setTranslationY(translationY + scrollY);
	}

	@Override
	public void onDownMotionEvent() {
		mScrollSettleHandler.setSettleEnabled(false);
	}

	@Override
	public void onUpOrCancelMotionEvent() {
		mScrollSettleHandler.setSettleEnabled(true);
		mScrollSettleHandler.onScroll(mObservableScrollView.getScrollY());
	}

	private class ScrollSettleHandler extends Handler {
		private static final int SETTLE_DELAY_MILLIS = 100;

		private int mSettledScrollY = Integer.MIN_VALUE;
		private boolean mSettleEnabled;

		public void onScroll(int scrollY) {
			if (mSettledScrollY != scrollY) {
				// Clear any pending messages and post delayed
				removeMessages(0);
				sendEmptyMessageDelayed(0, SETTLE_DELAY_MILLIS);
				mSettledScrollY = scrollY;
			}
		}

		public void setSettleEnabled(boolean settleEnabled) {
			mSettleEnabled = settleEnabled;
		}

		@Override
		public void handleMessage(Message msg) {
			// Handle the scroll settling.
			if (STATE_RETURNING == mState && mSettleEnabled) {
				int mDestTranslationY;
				if (mSettledScrollY - mQuickReturnView.getTranslationY() > mQuickReturnHeight / 2) {
					mState = STATE_OFFSCREEN;
					mDestTranslationY = Math.max(
							mSettledScrollY - mQuickReturnHeight,
							mPlaceholderView.getTop());
				} else {
					mDestTranslationY = mSettledScrollY;
				}

				mMinRawY = mPlaceholderView.getTop() - mQuickReturnHeight - mDestTranslationY;
				mQuickReturnView.animate().translationY(mDestTranslationY);
			}
			mSettledScrollY = Integer.MIN_VALUE; // reset
		}
	}
}
