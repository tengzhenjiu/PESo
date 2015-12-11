package com.peso.Sliding;

/**
 *点击个性签名后跳转的界面
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.peso.R;

public class PersonalitysignatureActivity extends Activity {
	private ImageView P_return;
	private Button save_btn;// 保存按钮
	private EditText changeSignature;// 输入昵称的文本框

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personality_signature);
		changeSignature = (EditText) findViewById(R.id.signature1);
		P_return = (ImageView) findViewById(R.id.sign_arrow1);
		save_btn = (Button) findViewById(R.id.save_button1);
		// 点击事件
		save_btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent data = new Intent();
				data.putExtra("data", changeSignature.getText()
						.toString());
				// 将输入edittext的内容作为data数据
				setResult(4, data);
				finish();
			}
		});
		P_return.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});

	}
}
