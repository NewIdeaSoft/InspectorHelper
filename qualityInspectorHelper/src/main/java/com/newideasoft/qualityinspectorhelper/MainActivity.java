package com.newideasoft.qualityinspectorhelper;

import com.google.gson.Gson;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author NewIdeaSoft 用户登陆后显示用户信息，和用户可以查看的产品，以及岗位 核对用户信息，选择产品和岗位进入资源查看页面
 */
public class MainActivity extends BaseActivity implements OnClickListener {
	private Button checked;
	private EditText name;
	// 用于显示产品信息，供用户选择输入
	private Spinner product;
	// 用于显示岗位（工序）信息，供用户选择输入
	private Spinner station;
	private BaseAdapter productAdapter;
	private BaseAdapter stationAdapter;
	private UserInfo userInfo;
	private int productItemId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		checked = (Button) findViewById(R.id.checked);
		name = (EditText) findViewById(R.id.name);
		product = (Spinner) findViewById(R.id.product);
		station = (Spinner) findViewById(R.id.station);
		Intent intent = getIntent();
		String json = intent.getStringExtra("userInfo");
		Gson gson = new Gson();
		userInfo = gson.fromJson(json, UserInfo.class);
		if (userInfo != null) {
			name.setText(userInfo.getName());
			productAdapter = new ProductAdapter();
			product.setAdapter(productAdapter);
		} else {
			Toast.makeText(MainActivity.this, "获取用户信息失败，请重新登陆", Toast.LENGTH_SHORT).show();
			backToLogin();
		}
		checked.setOnClickListener(this);
		OnItemSelectedListener listener = new SlectedListener();
		product.setOnItemSelectedListener(listener);
		station.setOnItemSelectedListener(listener);

	}

	// 网路请求的URL
	// private static final String HTTP_URL =
	// "http://192.168.31.189:8080/QualityInspectorHelper/CustomLoginServlet";
	private void backToLogin() {
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	public void onClick(View v) {
		String productName = (String) product.getSelectedItem();
		String stationName = (String) station.getSelectedItem();
		if (name.getText().toString().equals("")) {
			backToLogin();
		} else if (productName.equals("")) {
			Toast.makeText(MainActivity.this, "产品选项不能为空", Toast.LENGTH_SHORT).show();
		} else if (stationName.equals("")) {
			Toast.makeText(MainActivity.this, "岗位选项不能为空", Toast.LENGTH_SHORT).show();
		} else {
			Intent intent = new Intent(this, InspectStandardActivity.class);
			intent.putExtra("product", productName);
			intent.putExtra("station", stationName);
			startActivity(intent);
		}
	}

	/**
	 * 列表项被选择的回调方法
	 * 
	 * @author Administrator
	 *
	 */
	class SlectedListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			// 获得所选项目的信息，在spinner中显示
			switch (parent.getId()) {
			case R.id.product:
				// 选定产品，重新匹配工序
				productItemId = position;
				String productName = (String) product.getSelectedItem();
				if (!productName.equals("")) {
					stationAdapter = new StationAdapter(productName);
					station.setAdapter(stationAdapter);
				}
				station.setAdapter(stationAdapter);
				break;
			case R.id.station:
				if (station.getSelectedItem() == null) {
					Toast.makeText(MainActivity.this, "请先选择产品", Toast.LENGTH_SHORT).show();
				}
				break;
			default:
				break;
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {

		}

	}

	/**
	 * 加载产品下拉菜单的适配器
	 * 
	 * @author Administrator
	 *
	 */
	class ProductAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return userInfo.getProducts().size();
		}

		@Override
		public Object getItem(int position) {
			return userInfo.getProducts().get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView=View.inflate(MainActivity.this, R.layout.spinner_item, null);
			}
			TextView productTextView = (TextView) convertView.findViewById(R.id.spinnerItem);
			productTextView.setText((CharSequence) getItem(position));
			return convertView;
		}
	}

	/**
	 * 加载岗位下拉菜单数据的适配器
	 * 
	 * @author Administrator
	 *
	 */
	class StationAdapter extends BaseAdapter {
		private String stationKey;

		public StationAdapter(String productName) {
			super();
			stationKey = productName;
		}

		@Override
		public int getCount() {
			if (productItemId >= 0) {
				return userInfo.getStation().get(productItemId).get(stationKey).size();
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
			if (productItemId >= 0) {
				return userInfo.getStation().get(productItemId).get(stationKey).get(position);
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView=View.inflate(MainActivity.this, R.layout.spinner_item, null);
			}
			TextView stationTextView = (TextView) convertView.findViewById(R.id.spinnerItem);
			CharSequence item = (CharSequence) getItem(position);
			if (item != null) {
				stationTextView.setText(item);
			} else {
				stationTextView.setText("");
			}
			return convertView;
		}
	}
}
