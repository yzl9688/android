/**
 * 
 */
package com.mad.trafficclient.fragment;

import com.mad.trafficclient.R;
import com.mad.trafficclient.util.LoadingDialog;
import com.mad.trafficclient.util.UrlBean;
import com.mad.trafficclient.util.Util;
import com.mad.trafficclient.utilhttp.RequestNetwork;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class Fragment_2 extends Fragment {
	private Spinner sortSpinner;
	private Button btnQuery;
	private ListView listView;
	private UrlBean urlBean;
	private String username;
	private List<HashMap<String, String>> trafficDatas;
	private SimpleAdapter simpleAdapter;
	private int sortRule = 0;

	private View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		view = inflater.inflate(R.layout.fragment_layout02, container, false);
		initView();
		initListener();

		try {
			initData();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return view;
	}

	private void initData() throws JSONException {
		String url = "http://" + urlBean.getUrl() + ":" + urlBean.getPort() + "/api/v2/get_trafficlight_config";
		Log.d("Test", url);
		trafficDatas = new ArrayList<>();
		LoadingDialog.showDialog(getActivity());
		for (int i = 1; i <= 5; i++) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("TrafficLightId", i);
			jsonObject.put("UserName", username);
			final int finalI = i;
			RequestNetwork.request(getActivity(), url, jsonObject, new RequestNetwork.Callback() {
				@Override
				public void invoke(JSONObject jsonObject) {
					if (jsonObject.optString("RESULT").equals("S")) {
						HashMap<String, String> map = new HashMap<>();
						map.put("crossing", String.valueOf(finalI));
						map.put("redDuration", jsonObject.optString("RedTime"));
						map.put("greenDuration", jsonObject.optString("GreenTime"));
						map.put("yellowDuration", jsonObject.optString("YellowTime"));
						trafficDatas.add(map);
						if (trafficDatas.size() == 5) {
							LoadingDialog.disDialog();
							Collections.sort(trafficDatas, new Comparator<HashMap<String, String>>() {
								@Override
								public int compare(HashMap<String, String> t1, HashMap<String, String> t2) {
									return Integer.parseInt(t1.get("crossing")) - Integer.parseInt(t2.get("crossing"));
								}
							});
							simpleAdapter.notifyDataSetChanged();
						}
					}
				}
			});
//
//			HashMap<String, String> map = new HashMap<>();
//						Random random = new Random();
//						map.put("crossing", String.valueOf(i));
//						map.put("redDuration", String.valueOf(random.nextInt(30)));
//						map.put("greenDuration", String.valueOf(random.nextInt(30)));
//						map.put("yellowDuration", String.valueOf(random.nextInt(30)));
//						trafficDatas.add(map);
		}
		simpleAdapter = new SimpleAdapter(getActivity(), trafficDatas, R.layout.list_table_fragment2_item,
				new String[]{"crossing", "redDuration", "greenDuration", "yellowDuration"},
				new int[]{R.id.crossing, R.id.redDuration, R.id.greenDuration, R.id.yellowDuration});

		listView.setAdapter(simpleAdapter);

	}

	private void initView() {
		sortSpinner = (Spinner) view.findViewById(R.id.sortSpinner);
		btnQuery = (Button) view.findViewById(R.id.btnQuery);
		listView = (ListView) view.findViewById(R.id.listView);
		urlBean = Util.loadSetting(getActivity());
		username = urlBean.getUsername();

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
						Collections.sort(trafficDatas, new Comparator<HashMap<String, String>>() {
							@Override
							public int compare(HashMap<String, String> t1, HashMap<String, String> t2) {
								return Integer.parseInt(t1.get("crossing")) - Integer.parseInt(t2.get("crossing"));
							}
						});
						simpleAdapter.notifyDataSetChanged();
						break;
					case 1:
						Collections.sort(trafficDatas, new Comparator<HashMap<String, String>>() {
							@Override
							public int compare(HashMap<String, String> t1, HashMap<String, String> t2) {
								return Integer.parseInt(t2.get("crossing")) - Integer.parseInt(t1.get("crossing"));
							}
						});
						simpleAdapter.notifyDataSetChanged();
						break;
					case 2:
						Collections.sort(trafficDatas, new Comparator<HashMap<String, String>>() {
							@Override
							public int compare(HashMap<String, String> t1, HashMap<String, String> t2) {
								return Integer.parseInt(t1.get("redDuration")) - Integer.parseInt(t2.get("redDuration"));
							}
						});
						simpleAdapter.notifyDataSetChanged();
						break;
					case 3:
						Collections.sort(trafficDatas, new Comparator<HashMap<String, String>>() {
							@Override
							public int compare(HashMap<String, String> t1, HashMap<String, String> t2) {
								return Integer.parseInt(t2.get("redDuration")) - Integer.parseInt(t1.get("redDuration"));
							}
						});
						simpleAdapter.notifyDataSetChanged();
						break;
					case 4:
						Collections.sort(trafficDatas, new Comparator<HashMap<String, String>>() {
							@Override
							public int compare(HashMap<String, String> t1, HashMap<String, String> t2) {
								return Integer.parseInt(t1.get("greenDuration")) - Integer.parseInt(t2.get("greenDuration"));
							}
						});
						simpleAdapter.notifyDataSetChanged();
						break;
					case 5:
						Collections.sort(trafficDatas, new Comparator<HashMap<String, String>>() {
							@Override
							public int compare(HashMap<String, String> t1, HashMap<String, String> t2) {
								return Integer.parseInt(t2.get("greenDuration")) - Integer.parseInt(t1.get("greenDuration"));
							}
						});
						simpleAdapter.notifyDataSetChanged();
						break;
					case 6:
						Collections.sort(trafficDatas, new Comparator<HashMap<String, String>>() {
							@Override
							public int compare(HashMap<String, String> t1, HashMap<String, String> t2) {
								return Integer.parseInt(t1.get("yellowDuration")) - Integer.parseInt(t2.get("yellowDuration"));
							}
						});
						simpleAdapter.notifyDataSetChanged();
						break;
					case 7:
						Collections.sort(trafficDatas, new Comparator<HashMap<String, String>>() {
							@Override
							public int compare(HashMap<String, String> t1, HashMap<String, String> t2) {
								return Integer.parseInt(t2.get("yellowDuration")) - Integer.parseInt(t1.get("yellowDuration"));
							}
						});
						simpleAdapter.notifyDataSetChanged();
						break;
				}
			}
		});

	}

}
