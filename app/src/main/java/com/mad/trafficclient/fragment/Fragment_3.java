/**
 * 
 */
package com.mad.trafficclient.fragment;

import com.mad.trafficclient.R;
import com.mad.trafficclient.database.Fragment1DatabaseHelper;
import com.mad.trafficclient.util.LoadingDialog;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;


public class Fragment_3 extends Fragment
{
	private Spinner sortSpinner;
	private Button btnQuery;
	private ListView listView;
	private TextView noneHistory;
	private Fragment1DatabaseHelper dbHelper;
	private List<HashMap<String, String>> rechargeDatas;
	private SimpleAdapter simpleAdapter;
	private int sortRule = 0;
	private View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		view = inflater
				.inflate(R.layout.fragment_layout03, container, false);

		initView();
		initData();
		initListener();
		sort();  // 默认降序显示

		return view;
	}

	private void sort() {
		sortSpinner.setSelection(1);  // 设置spinner的默认值
		Collections.sort(rechargeDatas, new Comparator<HashMap<String, String>>() {
			@Override
			public int compare(HashMap<String, String> t1, HashMap<String, String> t2) {
				return -(t1.get("rechargeTime").compareTo(t2.get("rechargeTime")));
			}
		});
		simpleAdapter.notifyDataSetChanged();
	}

	private void initListener() {

		sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
				sortRule = i;
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

			}
		});

		btnQuery.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				switch (sortRule) {
					case 0:
						Collections.sort(rechargeDatas, new Comparator<HashMap<String, String>>() {
							@Override
							public int compare(HashMap<String, String> t1, HashMap<String, String> t2) {
								return t1.get("rechargeTime").compareTo(t2.get("rechargeTime"));
							}
						});
						simpleAdapter.notifyDataSetChanged();
						break;
					case 1:
						Collections.sort(rechargeDatas, new Comparator<HashMap<String, String>>() {
							@Override
							public int compare(HashMap<String, String> t1, HashMap<String, String> t2) {
								return -(t1.get("rechargeTime").compareTo(t2.get("rechargeTime")));
							}
						});
						simpleAdapter.notifyDataSetChanged();
						break;
				}
			}
		});
	}

	private void initData() {
		LoadingDialog.showDialog(getActivity());
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query("recharge_log", null, null, null, null, null, null);
		rechargeDatas = new ArrayList<>();
		simpleAdapter = new SimpleAdapter(getActivity(), rechargeDatas, R.layout.list_table_fragment3_item,
				new String[]{"serialNumber", "carNumber", "rechargeMoney", "operator", "rechargeTime"},
				new int[] {R.id.serialNumber, R.id.carNumber, R.id.rechargeMoney, R.id.operator, R.id.rechargeTime});
		if (cursor.moveToFirst()) {
			noneHistory.setText("");
			do {
				HashMap<String, String> map = new HashMap<>();
				Log.d("Test", cursor.getString(cursor.getColumnIndex("_id")));
				Log.d("Test", cursor.getString(cursor.getColumnIndex("car_number")));
				Log.d("Test", cursor.getString(cursor.getColumnIndex("recharge_money")));
				Log.d("Test", cursor.getString(cursor.getColumnIndex("name")));
				Log.d("Test", cursor.getString(cursor.getColumnIndex("time")));
				map.put("serialNumber", cursor.getString(cursor.getColumnIndex("_id")));
				map.put("carNumber", cursor.getString(cursor.getColumnIndex("car_number")));
				map.put("rechargeMoney", cursor.getString(cursor.getColumnIndex("recharge_money")));
				map.put("operator", cursor.getString(cursor.getColumnIndex("name")));
				map.put("rechargeTime", cursor.getString(cursor.getColumnIndex("time")));
				rechargeDatas.add(map);
			} while (cursor.moveToNext());
		} else {
			noneHistory.setText("暂无历史记录");
		}
		listView.setAdapter(simpleAdapter);
		LoadingDialog.disDialog();
	}

	private void initView() {
		sortSpinner = (Spinner) view.findViewById(R.id.sortSpinner);
		btnQuery = (Button) view.findViewById(R.id.btnQuery);
		listView = (ListView) view.findViewById(R.id.listView);
		dbHelper = new Fragment1DatabaseHelper(getActivity());
		noneHistory = (TextView) view.findViewById(R.id.noneHistory);
	}

}
