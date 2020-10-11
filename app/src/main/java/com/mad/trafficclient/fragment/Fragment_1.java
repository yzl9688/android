/**
 * 
 */
package com.mad.trafficclient.fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mad.trafficclient.R;
import com.mad.trafficclient.database.Fragment1DatabaseHelper;
import com.mad.trafficclient.util.LoadingDialog;
import com.mad.trafficclient.util.UrlBean;
import com.mad.trafficclient.util.Util;
import com.mad.trafficclient.utilhttp.RequestNetwork;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


public class Fragment_1 extends Fragment {
	private TextView accountBalance;
	private Spinner carSpinner;
	private EditText rechargeMoney;
	private Button btnQuery, btnRecharge;
	private int carNumber = 1;
	private String username = "user1";
	private View view;
	private Fragment1DatabaseHelper dbHelper = null;

	private UrlBean urlBean;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		view = inflater
				.inflate(R.layout.fragment_layout01, container, false);
		initView();
		initListener();
		queryData();
		SQLiteDatabase mDb = dbHelper.getWritableDatabase();
		Cursor cursor = mDb.query("recharge_log", null, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				Log.d("Test", cursor.getString(cursor.getColumnIndex("car_number")));
				Log.d("Test", cursor.getString(cursor.getColumnIndex("recharge_money")));
				Log.d("Test", cursor.getString(cursor.getColumnIndex("name")));
				Log.d("Test", cursor.getString(cursor.getColumnIndex("time")));

			} while(cursor.moveToNext());
		}
		cursor.close();
		return view;
	}

	private void initView() {
		accountBalance = (TextView) view.findViewById(R.id.accountBalance);
		carSpinner = (Spinner) view.findViewById(R.id.carSpinner);
		rechargeMoney = (EditText) view.findViewById(R.id.rechargeMoney);
		btnQuery = (Button) view.findViewById(R.id.btnQuery);
		btnRecharge = (Button) view.findViewById(R.id.btnRecharge);
		urlBean = Util.loadSetting(getActivity());
		username = urlBean.getUsername();
		dbHelper = new Fragment1DatabaseHelper(getActivity());
	}

	private void initListener() {
		carSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
				carNumber = i + 1;
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

			}
		});

		btnQuery.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				queryData();
			}
		});

		btnRecharge.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				int money = Integer.valueOf(String.valueOf(rechargeMoney.getText()));
				if (money < 1 || money > 999) {
					Toast.makeText(getActivity(), "只能输入1-999之间的整数", Toast.LENGTH_SHORT).show();
					return;
				}
				String url = "http://" + urlBean.getUrl() + ":" + urlBean.getPort() + "/api/v2/set_car_account_recharge";
				JSONObject jsonObject = new JSONObject();
				try {
					jsonObject.put("CarId", carNumber);
					jsonObject.put("Money", rechargeMoney.getText());
					jsonObject.put("UserName", username);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				RequestNetwork.request(getActivity(), url, jsonObject, new RequestNetwork.Callback() {
					@Override
					public void invoke(JSONObject jsonObject) {
						if (jsonObject.optString("RESULT").equals("S")) {
							SQLiteDatabase mDb = dbHelper.getWritableDatabase();
							SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							String currentTime = formatter.format(new Date());
							ContentValues values = new ContentValues();
							values.put("car_number", carNumber);
							values.put("recharge_money", String.valueOf(rechargeMoney.getText()));
							values.put("name", username);
							values.put("time", currentTime);
							mDb.insert("recharge_log", null, values);
							Toast.makeText(getActivity(), "充值成功", Toast.LENGTH_SHORT).show();
							queryData();
							mDb.close();

						}
						Log.d("Test", jsonObject.toString());
					}
				});
			}
		});

	}

	private void queryData() {
		LoadingDialog.showDialog(getActivity());
		String url = "http://" + urlBean.getUrl() + ":" + urlBean.getPort() + "/api/v2/get_car_account_balance";
		Log.d("Test", url);
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("CarId", carNumber);
			jsonObject.put("UserName", "user1");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		RequestNetwork.request(getActivity(), url, jsonObject, new RequestNetwork.Callback() {
			@Override
			public void invoke(JSONObject jsonObject) {
				String balance = null;
				if (jsonObject.optString("RESULT").equals("S")) {
					balance = jsonObject.optString("Balance");
				}
				accountBalance.setText(balance + "元");
				LoadingDialog.disDialog();
			}
		});
	}


}
