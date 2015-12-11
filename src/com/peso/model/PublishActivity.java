package com.peso.model;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

import com.peso.R;

public class PublishActivity extends Activity {
	private ImageView iv_back_publish;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_publish);
		// setContentView(R.layout.activity_comment);
		/*
		 * iv_back_publish=(ImageView)
		 * findViewById(R.id.return_imageView);
		 * iv_back_publish.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO
		 * Auto-generated method stub finish(); } });
		 */
	}
}
