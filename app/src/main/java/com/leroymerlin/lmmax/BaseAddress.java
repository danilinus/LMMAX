package com.leroymerlin.lmmax;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BaseAddress implements Serializable {
	public List<AddressCast> addressList = new ArrayList<>();

	public BaseAddress() {
		addressList = new ArrayList<>();
	}

	public void add(AddressCast addressCast) {
		addressList.add(addressCast);
	}

	public void add(String name) {
		addressList.add(new AddressCast(name));
	}

	public AddressCast get(int i) {
		return addressList.get(i);
	}

	public void remove(int i) {
		addressList.remove(i);
	}

	public int find(String name) {
		for (int i = 0; i < addressList.size(); i++)
			if (addressList.equals(name))
				return i;
		return -1;
	}

	public String get() {
		String result = "";
		for (int i = 0; i < addressList.size() - 1; i++)
			result += addressList.get(i).name_address + " ";
		if (addressList.size() > 0)
			result += addressList.get(addressList.size() - 1).name_address;
		return result;
	}

	public boolean check(String name) {
		for (int i = 0; i < addressList.size(); i++)
			if (addressList.get(i).name_address.equals(name))
				return true;
		return false;
	}
}