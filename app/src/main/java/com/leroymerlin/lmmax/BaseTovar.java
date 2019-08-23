package com.leroymerlin.lmmax;

import java.io.Serializable;

public class BaseTovar implements Serializable {
    public String barcode, lmcode, name_tovar;

    public BaseTovar(String name, String lmcode, String barcode)
    {
        name_tovar = name;
        this.lmcode = lmcode;
        this.barcode = barcode;
    }
}
