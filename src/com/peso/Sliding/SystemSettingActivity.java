package com.peso.Sliding;

import Thread.CommThread;
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.peso.R;

public class SystemSettingActivity extends Activity implements OnClickListener {
	private ImageView Systemsetting;
	private LinearLayout sysAboutUs;
	private LinearLayout sysHelp;
	private LinearLayout sysUpdateCheck;
	private LinearLayout sysFeedBack;
	private ImageView sysBack;
	public String version = "0";

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.systemsetting);

		Systemsetting = (ImageView) findViewById(R.id.systemsetting);
		sysAboutUs = (LinearLayout) findViewById(R.id.sys_about_us);
		sysFeedBack = (LinearLayout) findViewById(R.id.sys_feedback);
		sysHelp = (LinearLayout) findViewById(R.id.sys_help);
		sysUpdateCheck = (LinearLayout) findViewById(R.id.sys_checkupdate);
		sysBack = (ImageView) findViewById(R.id.sys_back);
		click();

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.sys_about_us:
				break;
			case R.id.sys_checkupdate:
				try {
					startUC();
				} catch (NameNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case R.id.sys_feedback:
				break;
			case R.id.sys_help:
				break;
			case R.id.sys_back:
				finish();
				overridePendingTransition(R.anim.push_left_inn,
						R.anim.push_left_outt);
				break;

			default:
				break;
		}

	}

	public void click() {
		sysAboutUs.setOnClickListener(this);
		sysFeedBack.setOnClickListener(this);
		sysHelp.setOnClickListener(this);
		sysUpdateCheck.setOnClickListener(this);
		sysBack.setOnClickListener(this);
	}

	public void startUC() throws NameNotFoundException {

		CommThread ct = new CommThread("SYSSETTINGUPDATECHECK");// 实例化comm
									// 并初始化msg
		ct.start();// 开始使用comm进行与服务器通讯
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (ct.recMsg == "") {
			// 连接超时
		}
		if (ct.recMsg != null && ct.recMsg != "") {
			version = ct.recMsg;
			System.out.println("得到的版本号码:" + version);
		}

		int versioncode = 0;
		PackageManager manager = this.getPackageManager();
		PackageInfo info = manager.getPackageInfo(
				this.getPackageName(), 0);
		version = info.versionName;
		versioncode = info.versionCode;

		if (Integer.parseInt(version) > versioncode) {
			Toast.makeText(this, "发现新版本", Toast.LENGTH_SHORT)
					.show();
			UpdateManager UM = new UpdateManager(
					SystemSettingActivity.this);// 实例化UpdateManager类
			UM.checkUpdateInfo();// 开始更新
		} else {
			Toast.makeText(this, "已经是最新版", Toast.LENGTH_SHORT)
					.show();
		}
	}

}