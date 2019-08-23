package com.leroymerlin.lmmax;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AddressCast implements Serializable {
	public String name_address = "";
	public List<Tovar> tovarList = new ArrayList<>();

	public void add(Tovar tovar) {
		for (int i = 0; i < tovarList.size(); i++)
			if (tovarList.get(i).lmcode.equals(tovar.lmcode))
				tovarList.set(i, new Tovar(tovarList.get(i).name_tovar, tovarList.get(i).lmcode, tovarList.get(i).barcode, tovarList.get(i).sum + tovar.sum));
			else
				tovarList.add(tovar);
	}

	public void remove(int i) {
		tovarList.remove(i);
	}

	public AddressCast(String name) {
		name_address = name;
	}

	public AddressCast(AddressCast ac) {
		name_address = ac.name_address;
		tovarList = ac.tovarList;
	}
}
