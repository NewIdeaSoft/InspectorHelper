package com.newideasoft.qualityinspectorhelper;

import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends BaseActivity implements OnClickListener {
	private EditText phoneNumberET;
	private EditText password;
	private Button login;
	private Button newCustomer;
	public static final String URLPATH = "http://192.168.31.189:8080/QualityInspectorHelper/CustomLoginServlet";
	private ProgressDialog dialog;
	private CheckBox rememberInfo;
	private CheckBox aotuLogin;
	private OnCheckedChangeListener cListener;
	private SharedPreferences sp;
	private Editor edit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		sp = LoginActivity.this.getSharedPreferences("userinfo", MODE_PRIVATE);
		edit = sp.edit();
		String rememberedNum = sp.getString("phonenumber", "");
		String rememberedPassword = sp.getString("password", "");
		if (sp.getBoolean("aotulogin", false)) {
			// 自动登陆
			login(rememberedNum, rememberedPassword, URLPATH);
		}
		phoneNumberET = (EditText) findViewById(R.id.phoneNumberET);
		password = (EditText) findViewById(R.id.password);
		phoneNumberET.setText(rememberedNum);
		password.setText(rememberedPassword);
		login = (Button) findViewById(R.id.login);
		newCustomer = (Button) findViewById(R.id.newCustomer);
		rememberInfo = (CheckBox) findViewById(R.id.rememberInfo);
		aotuLogin = (CheckBox) findViewById(R.id.aotuLogin);
		cListener = new CheckedListener();
		rememberInfo.setOnCheckedChangeListener(cListener);
		aotuLogin.setOnCheckedChangeListener(cListener);
		login.setOnClickListener(this);
		newCustomer.setOnClickListener(this);

	}

	public static StringBuffer loginInfoToPm(String phoneNumber, String password) throws Exception {
		StringBuffer sb = new StringBuffer();
		Map<String, String> param = new HashMap<String, String>();
		param.put("number", phoneNumber);
		param.put("password", password);
		for (Map.Entry<String, String> entry : param.entrySet()) {
			sb.append(entry.getKey() + "=");
			sb.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
			sb.append("&");
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb;
	}

	public static String canLogin(HttpURLConnection connection) throws Exception {
		String userInfoJson = null;
		ByteArrayOutputStream baos = NetWorkTools.geResponseData(connection);
		if (baos != null) {
			userInfoJson = baos.toString();
		}
		return userInfoJson;
	}

	private void login(String userName, String userPassword, String urlPath) {
		new AsyncTask<String, Void, String>() {
			@Override
			protected void onPostExecute(String result) {
				dialog.dismiss();
				
				if(result!=null){
					if(rememberInfo.isChecked()){
						edit.putString("phonenumber", phoneNumberET.getText().toString());
						edit.putString("password", password.getText().toString());
						if(aotuLogin.isChecked()){
							edit.putBoolean("aotulogin", true);
						}
						edit.commit();
					}
					Intent customerIntent = new Intent(LoginActivity.this, MainActivity.class);
					customerIntent.putExtra("userInfo", result);
					startActivity(customerIntent);
					finish();
				}else {
					Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
					password.setText("");
				}
			}

			@Override
			protected void onPreExecute() {
				dialog = new ProgressDialog(LoginActivity.this);
				dialog.setTitle("正在登陆...");
				dialog.show();
			}

			@Override
			protected String doInBackground(String... params) {
				String canlogin = null;
				try {
					StringBuffer sb = loginInfoToPm(params[0], params[1]);
					byte[] data = sb.toString().getBytes();
					HttpURLConnection connection = NetWorkTools.getConnection(params[2], data);
					canlogin = canLogin(connection);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return canlogin;
			}
		}.execute(userName, userPassword, urlPath);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login:
			// 向服务器发送请求，上传用户名和密码，如果正确启动登陆后的主界面，如果错误发出提示
			String phoneNumber = phoneNumberET.getText().toString();
			String accountPassword = password.getText().toString();
			login(phoneNumber, accountPassword, URLPATH);
			break;
		case R.id.newCustomer:
			Intent intent = new Intent(LoginActivity.this, CreatNewCustomerActivity.class);
			startActivity(intent);
			break;
		}
	}

	class CheckedListener implements OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			switch (buttonView.getId()) {
			case R.id.rememberInfo:
				if (isChecked) {
					edit.putString("phonenumber", phoneNumberET.getText().toString());
					edit.putString("password", password.getText().toString());
				} else {
					aotuLogin.setChecked(false);
					edit.clear();
				}
				edit.commit();
				break;
			case R.id.aotuLogin:
				if (isChecked) {
					rememberInfo.setChecked(true);
					edit.putBoolean("aotulogin", true);
				} else {
					edit.putBoolean("aotulogin", false);
				}
				edit.commit();
				break;
			}
		}
	}
}
