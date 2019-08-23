package com.leroymerlin.lmmax;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class FileManager extends Activity {

	private String[] FilePathStrings;
	private String[] FileNameStrings;
	private File[] listFile;
	File file;

	ArrayList<String> pathHistory;
	String lastDirectory;
	int count = 0;

	ListView lvInternalStorage;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_file_manager);

		lvInternalStorage = (ListView) findViewById(R.id.lvInternalStorage);
		findViewById(R.id.btnUpDirectory).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (count != 0) {
					pathHistory.remove(count);
					count--;
					checkInternalStorage();
				}
			}
		});
		findViewById(R.id.select_folder).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (count != 0) {
					pathHistory.remove(count);
					count--;
					checkInternalStorage();
				}
			}
		});
		findViewById(R.id.btnViewSDCard).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				count = 0;
				pathHistory = new ArrayList<String>();
				pathHistory.add(count, System.getenv("EXTERNAL_STORAGE"));
				checkInternalStorage();
			}
		});

		findViewById(R.id.home_btn).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
				startActivity(new Intent(getBaseContext(), Function.class));
			}
		});
		findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});

		//need to check the permissions
		checkFilePermissions();

		lvInternalStorage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				if (new File(FilePathStrings[i]).isFile()) {
					setResult(RESULT_OK, new Intent().putExtra("name", FilePathStrings[i]));
					finish();
				} else {
					count++;
					pathHistory.add(count, FilePathStrings[i]);
					checkInternalStorage();
				}
			}
		});

		count = 0;
		pathHistory = new ArrayList<String>();
		pathHistory.add(count, System.getenv("EXTERNAL_STORAGE"));
		checkInternalStorage();
	}

	private void checkInternalStorage() {
		try {
			if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
				toastMessage("No SD card found.");
			else
				file = new File(pathHistory.get(count));

			listFile = file.listFiles();
			int m = 0;

			for (int i = 0; i < listFile.length; i++)
				if (listFile[i].isFile()) {
					for (int l = 0; l < Memory.formats.split(" ").length; l++)
						if (listFile[i].getName().contains(Memory.formats.split(" ")[l])) {
							m++;
							break;
						}
				} else m++;


			FilePathStrings = new String[m];
			FileNameStrings = new String[m];

			m = 0;

			for (int i = 0; i < listFile.length; i++)
				if (listFile[i].isFile()) {
					for (int l = 0; l < Memory.formats.split(" ").length; l++)
						if (listFile[i].getName().contains(Memory.formats.split(" ")[l])) {
							FilePathStrings[m] = listFile[i].getAbsolutePath();
							FileNameStrings[m] = listFile[i].getName();
							m++;
							break;
						}
				} else {
					FilePathStrings[m] = listFile[i].getAbsolutePath();
					FileNameStrings[m] = listFile[i].getName();
					m++;
				}

			if (FilePathStrings.length > 0)
				((TextView) findViewById(R.id.select_folder)).setText(FilePathStrings[0].split("/")[FilePathStrings[0].split("/").length - 2]);
			else
				((TextView) findViewById(R.id.select_folder)).setText(pathHistory.get(count).split("/")[pathHistory.get(count).split("/").length - 1]);

			lvInternalStorage.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, FileNameStrings));

		} catch (NullPointerException e) {
		}
	}

	private void checkFilePermissions() {
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
			int permissionCheck = this.checkSelfPermission("Manifest.permission.READ_EXTERNAL_STORAGE");
			permissionCheck += this.checkSelfPermission("Manifest.permission.WRITE_EXTERNAL_STORAGE");
			if (permissionCheck != 0) {
				this.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1001); //Any number
			}
		}
	}

	@Override
	public void onBackPressed() {
		if (count != 0) {
			pathHistory.remove(count);
			count--;
			checkInternalStorage();
		} else {
			setResult(RESULT_CANCELED);
			finish();
		}
	}

	private void toastMessage(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}
}




























