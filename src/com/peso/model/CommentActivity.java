package com.peso.model;

import Thread.CommThread;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.peso.R;
import com.peso.view.SwipeBackActivity;
import com.tencent.tauth.Tencent;

import dbmodels.Publication;

@SuppressLint("NewApi")
public class CommentActivity extends SwipeBackActivity implements
		OnClickListener {
	private ImageView content_back;
	private ImageView content_comment;
	private TextView title;
	private TextView date;
	private TextView authors_num;
	private TextView authors;
	private TextView abstracts;
	private ImageView Image;

	private TextView publish_in;
	private TextView page;
	private TextView date_conference;
	private TextView doi;
	private TextView publisher;
	private TextView publish_in_con;
	private TextView page_con;
	private TextView date_conference_con;
	private TextView doi_con;
	private TextView publisher_con;

	View parent;

	private String text;
	private ImageView content_share;
	private ImageView content_copy;
	private ImageView favs;

	private Publication pub = new Publication();

	private Tencent mTencent;

	private String paperId = "";
	private String activeUserName = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comment);
		int size = (int) (Resources.getSystem().getDisplayMetrics().widthPixels / 30);// 16dip
		/*
		 * int sizeh = size * 30 / 24;// 20dip int sizel = size * 30 /
		 * 32;// 15dip
		 */
		int sizeh2 = size * 30 / 8;// 35dip
		mTencent = Tencent.createInstance("1104917717",
				this.getApplicationContext());
		pub = (Publication) getIntent().getSerializableExtra("pub");

		/********************* title ******************/
		title = (TextView) findViewById(R.id.comment_title);
		// title.setMovementMethod(ScrollingMovementMethod.getInstance());可滚动
		if (pub.getTitle() != null && pub.getTitle().length() != 0)
			title.setText(pub.getTitle());
		else
			title.setText("Untitled");
		// title.setTextSize(sizeh);// 设置字体大小
		// 获取项目目录中 fonts下的jl.ttf字体文件
		Typeface typeface = Typeface.createFromAsset(
				CommentActivity.this.getAssets(),
				"fonts/minglan.ttf");
		// 给控件设置字体样式
		title.setTypeface(typeface);
		/********************* title ******************/
		/********************* date ******************/
		date = (TextView) findViewById(R.id.comment_date);
		if (pub.getDate() != null && pub.getDate().length() != 0)
			date.setText(pub.getDate() + "\n");
		else if (pub.getYear() != null
				&& pub.getYear().toString().length() != 0)
			date.setText(pub.getYear() + "\n");
		else
			date.setText("No date\n");
		// date.setTextSize(sizeh);
		date.setTypeface(typeface);

		/********************* date ******************/
		/* authors_num */
		authors_num = (TextView) findViewById(R.id.comment_authors_num);
		if (pub.getAuthors() != null && pub.getAuthors().size() != 0) {
			authors_num.setText(pub.getAuthors().size()
					+ "\nAuthor(s)");
			// authors_num.setTextSize(size);
			authors_num.setTypeface(typeface);
			authors_num.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, sizeh2));
		}

		/********************* author ******************/
		authors = (TextView) findViewById(R.id.comment_authors);
		if (pub.getAuthorStr() != null
				&& pub.getAuthorStr().length() != 0)
			authors.setText(pub.getAuthorStr().replace(";",
					"\t\t\t"));
		else
			authors.setText("No authors" + "\n");
		// authors.setTextSize(size);
		authors.setTypeface(typeface);

		/********************* author ******************/
		/********************* abstract ******************/
		abstracts = (TextView) findViewById(R.id.comment_contents);
		if (pub.get_abstract() != null
				&& pub.get_abstract().length() != 0)
			abstracts.setText("Abstract:\n" + "\t\t"
					+ pub.get_abstract() + "\n");
		else
			abstracts.setText("No abstracts\n\n");
		// abstracts.setTextSize(sizel);
		abstracts.setTypeface(typeface);

		/********************* abstract ******************/

		/********************* download ******************/
		Image = (ImageView) findViewById(R.id.comment_download);
		if (pub.getUrl() != null && pub.getUrl().get(0).length() != 0) {
			Image.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setAction("android.intent.action.VIEW");
					Uri url = Uri.parse(pub.getUrl().get(0));
					intent.setData(url);
					startActivity(intent);
				}
			});
		} else
			Image.setImageResource(R.drawable.package_download_none);

		/********************* download ******************/
		/* 以下是标签内容 */
		publish_in_con = (TextView) findViewById(R.id.comment_publish_in_content);
		if (pub.getVenue().getRaw() != null
				&& pub.getVenue().getRaw().length() != 0)
			publish_in_con.setText(pub.getVenue().getRaw());
		else
			publish_in_con.setText("No data\n");
		// publish_in_con.setTextSize(sizel);
		publish_in_con.setTypeface(typeface);
		/*
		 * View contentView =
		 * getLayoutInflater().inflate(R.layout.activity_comment, null);
		 * 
		 * popupWindow = new PopupWindow(contentView,
		 * ViewGroup.LayoutParams.MATCH_PARENT,
		 * ViewGroup.LayoutParams.WRAP_CONTENT);
		 * popupWindow.setFocusable(true); // 获得焦点
		 * popupWindow.setBackgroundDrawable(new
		 * BitmapDrawable());//点击空白处可以返回
		 */
		/*-------------绑定id---------------*/
		page = (TextView) findViewById(R.id.comment_page);
		date_conference = (TextView) findViewById(R.id.comment_date_conference);
		doi = (TextView) findViewById(R.id.comment_doi);
		publisher = (TextView) findViewById(R.id.comment_publisher);
		page_con = (TextView) findViewById(R.id.comment_page_content);
		date_conference_con = (TextView) findViewById(R.id.comment_date_conference_content);
		doi_con = (TextView) findViewById(R.id.comment_doi_content);
		publisher_con = (TextView) findViewById(R.id.comment_publisher_content);
		/*-------------\绑定id---------------*/
		page.setTypeface(typeface);
		date_conference.setTypeface(typeface);
		doi.setTypeface(typeface);
		publisher.setTypeface(typeface);
		/* page */
		if (pub.getPageStr() != null && pub.getPageStr().length() != 0)
			page_con.setText(pub.getPageStr());
		else
			page_con.setText("No data");
		// page_con.setTextSize(sizel);
		page_con.setTypeface(typeface);
		/* date_conference */
		if (pub.getDate() != null && pub.getDate().length() != 0)
			date_conference_con.setText(pub.getDate());
		else
			date_conference_con.setText("No data");
		// date_conference_con.setTextSize(sizel);
		date_conference_con.setTypeface(typeface);

		/* doi */
		if (pub.getDoi() != null && pub.getDoi().length() != 0)
			doi_con.setText(pub.getDoi());
		else
			doi_con.setText("No data");
		// doi_con.setTextSize(sizel);
		doi_con.setTypeface(typeface);
		/* publisher */
		if (pub.getSrc() != null && pub.getSrc().length() != 0)
			publisher_con.setText(pub.getSrc());
		else
			publisher_con.setText("No data");
		// publisher_con.setTextSize(sizel);
		publisher_con.setTypeface(typeface);
		if (pub.getId() != null && pub.getId().length() != 0) {
			paperId = pub.getId();
		}

		/*-------------绑定顶部ImageViewid并设置点击事件监听---------------*/
		// 返回键
		content_back = (ImageView) findViewById(R.id.comment_return);
		content_back.setOnClickListener(this);
		content_comment = (ImageView) findViewById(R.id.Comment_img);
		content_comment.setOnClickListener(this);
		content_share = (ImageView) findViewById(R.id.comment_share_img);
		content_share.setOnClickListener(this);
		content_copy = (ImageView) findViewById(R.id.comment_copy);
		content_copy.setOnClickListener(this);

		/*
		 * content_textsize = (ImageView)
		 * findViewById(R.id.comment_content_size);
		 * content_textsize.setOnClickListener(this);
		 */

		favs = (ImageView) findViewById(R.id.comment_favs);
		favs.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.comment_return:
				finish();
				overridePendingTransition(R.anim.push_left_inn,
						R.anim.push_left_outt);
				break;
			case R.id.Comment_img:
				Intent intent = new Intent(
						CommentActivity.this,
						PublishActivity.class);
				startActivity(intent);
				break;
			case R.id.comment_share_img:
				Intent intent_share = new Intent(
						Intent.ACTION_SEND);
				intent_share.setType("text/plain"); // "image/*"
				intent_share.putExtra(Intent.EXTRA_SUBJECT,
						"共享软件");
				intent_share.putExtra(
						Intent.EXTRA_TEXT,
						"我在安卓市场发现了个好东东“Peso论文系统”，快来下载吧！！"
								+ "\n"
								+ pub.getUrl()
										.get(0));
				intent_share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(Intent.createChooser(
						intent_share,
						"Select app to share"));
				break;
			case R.id.comment_copy:
				Toast.makeText(CommentActivity.this, "将内容复制好了",
						Toast.LENGTH_SHORT).show();
				ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE); // 打开复制的服务
				ClipData clip = ClipData
						.newPlainText("这里text是复制的viewcontent内容",
								text);
				clipboard.setPrimaryClip(clip);
				break;

			/*
			 * case R.id.comment_content_size:
			 * ChangeFrontSize(TextSizeValue);
			 */

			case R.id.comment_favs:
				SharedPreferences sharedPreferences = getSharedPreferences(
						"UserInfo", MODE_PRIVATE);
				activeUserName = sharedPreferences.getString(
						"username", "");
				if (activeUserName != "") {
					CommThread favs = new CommThread(
							"FAVSID"
									+ paperId
									+ "USERNAME"
									+ activeUserName);
					favs.start();

					try {
						Thread.sleep(1500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch
						// block
						e.printStackTrace();
					}

					switch (favs.recMsg) {
						case "1":
							favs.recMsg = "Increase the favorites to succeed";
							break;
						case "0":
							favs.recMsg = "Already exist";
							break;
						case "2":
							favs.recMsg = "Please login in to use it";
							break;
						case "3":
							favs.recMsg = "3";
							break;
						default:
							favs.recMsg = "Unknown Error";
							break;
					}
					Toast.makeText(CommentActivity.this,
							favs.recMsg,
							Toast.LENGTH_SHORT)
							.show();
				} else {
					Toast.makeText(CommentActivity.this,
							"You need to log in after collection",
							Toast.LENGTH_SHORT)
							.show();
				}
				break;
			default:
				break;
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (mTencent != null) {
			mTencent.onActivityResult(requestCode, resultCode, data);
		}
	}

	/*
	 * public void ChangeFrontSize(int TextSizeValue) { Alltext= (TextView)
	 * findViewById(R.id.comment_main);
	 * 
	 * switch (TextSizeValue) { case 15: Alltext.setTextSize(20);
	 * TextSizeValue = 20; break; case 20: Alltext.setTextSize(15);
	 * TextSizeValue = 15; break; default: break; } }
	 */

}
