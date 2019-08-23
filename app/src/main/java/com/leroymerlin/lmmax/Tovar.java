package com.leroymerlin.lmmax;

import java.io.Serializable;

public class Tovar implements Serializable {
    public String barcode = "";
    public String lmcode = "";
    public String name_tovar = "";
    public int sum = 0;

    public Tovar(String name, String lmcode, String barcode, int sum)
    {
        name_tovar = name;
        this.lmcode = lmcode;
        this.barcode = barcode;
        this.sum = sum;
    }

    public void add(int a)
    {
        sum += a;
    }
}