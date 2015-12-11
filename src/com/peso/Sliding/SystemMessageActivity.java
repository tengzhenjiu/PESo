package com.peso.Sliding;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.peso.R;

public class SystemMessageActivity extends Activity {
	private ImageView Sm_return;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.systemmessage);
		Sm_return = (ImageView) findViewById(R.id.sm_return);
		Sm_return.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}
}