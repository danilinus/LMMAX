package com.leroymerlin.lmmax;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BaseOtdel implements Serializable {
    public List<BaseTovar> tovars = new ArrayList<>();

    public void add(BaseTovar bt)
    {
        tovars.add(bt);
    }

    public void clear()
    {
        tovars.clear();
    }

    public void remove(int i)
    {
        tovars.remove(i);
    }

    public void add(int i, BaseTovar bt)
    {
        tovars.add(i, bt);
    }

    public int findAtLmcode(String lmcode)
    {
        for (int i = 0; i < tovars.size(); i++)
            if (tovars.get(i).lmcode.replace(" ", "").replace("\n", "").equals(lmcode.replace(" ", "").replace("\n", "")))
                return i;
        return -1;
    }

    public int findAtBarcode(String barcode)
    {
        for (int i = 0; i < tovars.size(); i++)
            if (tovars.get(i).barcode.replace(" ", "").replace("\n", "").equals(barcode.replace(" ", "").replace("\n", "")))
            return i;
        return -1;
    }
}
