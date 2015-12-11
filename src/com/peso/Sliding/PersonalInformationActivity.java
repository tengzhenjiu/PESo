package com.peso.Sliding;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.peso.MaininterfaceActivity;
import com.peso.R;

/**
 * 个人信息页面
 * 
 * @author Anne daxiong
 * 
 */
public class PersonalInformationActivity extends Activity implements
		OnDateSetListener {
	protected static final OnDateSetListener DatePickerListener = null;
	private ImageView P_return;
	private TextView nickname;
	String name;
	private TextView signature;
	private TextView birthday;
	private TextView interest;
	private TextView sex;
	private TextView email;
	private TextView school;
	private TextView major;
	private TextView signature_text;
	private TextView interest_text;
	private TextView sex_text;
	private TextView email_text;
	private TextView school_text;
	private TextView major_text;
	private Context mContext;
	private Date myDate;
	private DatePickerDialog dlg;
	private Button logout_button;
	SharedPreferences sharedPreferences;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personalinformation);
		mContext = this;
		
		P_return = (ImageView) findViewById(R.id.p_return);
		
		nickname = (TextView) findViewById(R.id.nickname);
		signature = (TextView) findViewById(R.id.signature);
		birthday = (TextView) findViewById(R.id.birthday);
		interest= (TextView) findViewById(R.id.interest);
		sex=(TextView) findViewById(R.id.sex);
		email=(TextView) findViewById(R.id.email);
		school=(TextView) findViewById(R.id.school);
		major=(TextView) findViewById(R.id.major);
		signature_text = (TextView) findViewById(R.id.signature_text);

		interest_text= (TextView) findViewById(R.id.interest_text);
		sex_text=(TextView) findViewById(R.id.sex_text);
		email_text=(TextView) findViewById(R.id.email_text);
		school_text=(TextView) findViewById(R.id.school_text);
		major_text=(TextView) findViewById(R.id.major_text);
		
		logout_button= (Button) findViewById(R.id.logout_button);
		// 点击我的昵称
		sharedPreferences = getSharedPreferences("UserInfo",
				MODE_PRIVATE);
		name = sharedPreferences.getString("username", "Username");
		nickname.setText(name);
		
		logout_button.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				sharedPreferences = getSharedPreferences(
						"UserInfo", MODE_PRIVATE);
				Editor editor = sharedPreferences
						.edit();
				editor.remove("username");
				editor.remove("password");
				editor.commit();// 提交修改
				
				Intent intent = new Intent(mContext,
						MaininterfaceActivity.class);
				startActivity(intent);		
			}
		});

		nickname.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext,
						ChangenicknameActivity.class);
				startActivityForResult(intent, 1);
			}
		});
		// 点击返回键
		P_return.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		// 点击我的个签
		signature_text.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(
						mContext,
						PersonalitysignatureActivity.class);
				startActivity(intent);
			}
		});
		birthday.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Calendar d = Calendar.getInstance(Locale.CHINA);
				// 创建一个日历引用d，通过静态方法getInstance() 从指定时区
				// Locale.CHINA 获得一个日期实例
				// 创建一个Date实例
				d.setTime(new Date());
				// 设置日历的时间，把一个新建Date实例myDate传入
				int year = d.get(Calendar.YEAR);
				int month = d.get(Calendar.MONTH);
				int day = d.get(Calendar.DAY_OF_MONTH);
				// 获得日历中的 year month day
				DatePickerDialog dlg = new DatePickerDialog(
						PersonalInformationActivity.this,
						DatePickerListener, year,
						month, day);
				// 新建一个DatePickerDialog 构造方法中
				// （设备上下文，OnDateSetListener时间设置监听器，默认年，默认月，默认日）
				dlg.show();
				// 让DatePickerDialog显示出来
			}
		});
	}

	/*
	 * public void onClick(View v){ switch (v.getId()){ case R.id.p_return:
	 * finish(); break; case R.id.nickname: Intent intent=new
	 * Intent(this,ChangenicknameActivity.class);
	 * startActivityForResult(intent,1);
	 */
	// case R.id.signature :
	// Intent intent1=new Intent(this,PersonalitysignatureActivity.class);
	// startActivityForResult(intent1,3);
	// }
	// }
	// 从跳转的界面传入返回值
	protected void onActivityResult(int requestCode, int resultCode,
			Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == 2) {
			String content = data.getStringExtra("data");
			nickname.setText(content);
		}
		if (requestCode == 3 && resultCode == 4) {
			String content1 = data.getStringExtra("data");
			signature_text.setText(content1);
		}
	}

	//
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		// DatePickerDialog 中按钮Set按下时自动调用

		// 通过id获得TextView对象
		birthday.setText(Integer.toString(year) + "-"
				+ Integer.toString(monthOfYear) + "-"
				+ Integer.toString(dayOfMonth));
		// 设置text
	}
}
