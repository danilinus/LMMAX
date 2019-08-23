package com.leroymerlin.lmmax;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.drive.DriveContents;

import java.util.ArrayList;
import java.util.List;

public class TovarList extends Activity {

	List<DriveContents> Tovars = new ArrayList<>();

	List<Address> list_tovars = new ArrayList<>();
	List<ListView> list_of_list = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tovar_list);
		findViewById(R.id.home_btn).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
				startActivity(new Intent(getBaseContext(), Function.class));
			}
		});
		findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});

		((ProgressBar) findViewById(R.id.progress_loading_tovars)).setMax(Memory.g.length - 1);

		for (int i = 0; i < Memory.g.length; i++)
			CKR(i);
	}

	public void CKR(int i) {
		Memory.getMetadataBuffer(Memory.g[i]).continueWith(task1 -> {
			if (task1.getResult().getCount() == 0) {
				((ProgressBar) findViewById(R.id.progress_loading_tovars)).setProgress(i);
				//Memory.RemAtT(Memory.g[i]);
			}
			for (int n = 0; n < task1.getResult().getCount(); n++) {
				Memory.openReadFile(task1.getResult(), n).continueWith(task ->
				{
					Tovars.add(task.getResult());
					return Memory.getTextFromFile(task).continueWith(task2 ->
					{
						((ProgressBar) findViewById(R.id.progress_loading_tovars)).setProgress(i);
                        /*if (Memory.CheckAddress(task2.getResult().split("\n")[4]))
                            addToAddress(task2.getResult().split("\n")[4], task2.getResult().split("\n")[2], task2.getResult(), task.getResult(), Memory.g[i]);
                        else
                            Memory.RemoveTovar(Memory.g[i], task.getResult());*/
						return null;
					});
				});
			}
			return null;
		});
	}

	public int kl = 0;

	public void addToAddress(String address, String name, String content, DriveContents dc, String nf) {
		int i;
		for (i = 0; i < list_tovars.toArray().length; i++)
			if (list_tovars.get(i).getName(address))
				break;

		if (i == list_tovars.toArray().length)
			addAddress(address);
		list_tovars.get(i).NewFile(name, content, dc, nf);
		list_of_list.get(i).setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_tovars.get(i).nameFiles));
		setListViewHeightBasedOnChildren(list_of_list.get(i));
		kl++;
	}

	public void addAddress(String name) {
		TextView t = new TextView(this);
		t.setText("—————— " + name + " ——————");
		t.setTextSize(20);
		t.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
		((LinearLayout) findViewById(R.id.content_tovar_list)).addView(t);
		ListView l = new ListView(this);
		l.setLayoutParams(new LinearLayout.LayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)));
		l.setVerticalScrollBarEnabled(false);
		l.setHorizontalScrollBarEnabled(false);
		l.setTag(list_of_list.toArray().length);
		l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {

				Memory.tovarData = list_tovars.get((int) l.getTag()).dc.get(i);
				Memory.newFile = list_tovars.get((int) l.getTag()).newFile.get(i);
				finish();
				startActivity(new Intent(getBaseContext(), ContentData.class));
			}
		});
		list_of_list.add(l);
		list_tovars.add(new Address(name));
		((LinearLayout) findViewById(R.id.content_tovar_list)).addView(list_of_list.get(list_of_list.toArray().length - 1));
	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null)
			return;

		int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
		int totalHeight = 0;
		View view = null;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			view = listAdapter.getView(i, view, listView);
			if (i == 0)
				view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, LinearLayout.LayoutParams.WRAP_CONTENT));

			view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
			totalHeight += view.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}
}