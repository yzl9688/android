/**
 * 
 */
package com.mad.trafficclient.fragment;

import com.mad.trafficclient.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class Fragment_4 extends Fragment
{
	private Button btnVideo, btnPicture;
	private View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		view = inflater.inflate(R.layout.fragment_layout04, container, false);

		initView();
		initListener();
		getFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new Fragment4Video()).commit();

		return view;
	}

	private void initListener() {
		btnVideo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				btnPicture.setBackgroundResource(R.drawable.button_unselected);
				btnVideo.setBackgroundResource(R.drawable.button_selected);
				getFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new Fragment4Video()).commit();
			}
		});

		btnPicture.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				btnVideo.setBackgroundResource(R.drawable.button_unselected);
				btnPicture.setBackgroundResource(R.drawable.button_selected);
				getFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new Fragment4Picture()).commit();
			}
		});
	}

	private void initView() {
		btnVideo = (Button) view.findViewById(R.id.btnVideo);
		btnPicture = (Button) view.findViewById(R.id.btnPicture);
	}

}
