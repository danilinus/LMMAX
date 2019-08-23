package com.leroymerlin.lmmax;

import com.google.android.gms.drive.DriveContents;

import java.util.ArrayList;
import java.util.List;

public class Address {
	public String name = "";
	public List<String> nameFiles;
	public List<String> contentFiles;
	public List<DriveContents> dc;
	public List<String> newFile;

	public Address(String name) {
		this.name = name;
		nameFiles = new ArrayList<>();
		contentFiles = new ArrayList<>();
		dc = new ArrayList<>();
		newFile = new ArrayList<>();
	}

	public void NewFile(String name, String content, DriveContents dc, String nf) {
		nameFiles.add(name);
		contentFiles.add(content);
		this.dc.add(dc);
		newFile.add(nf);
	}

	public String getName() {
		return name;
	}

	public boolean getName(String name) {
		return this.name.equals(name);
	}
}
