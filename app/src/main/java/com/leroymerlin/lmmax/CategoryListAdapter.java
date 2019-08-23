package com.leroymerlin.lmmax;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class CategoryListAdapter extends ArrayAdapter {

    private List<Model> dataList;
    Context mContext;
    CompoundButton.OnCheckedChangeListener listener;

    private static class ViewHolder {
        TextView categoryName;
        CheckBox selection;
    }

    public CategoryListAdapter(List<Model> dataLst, Context context) {
        super(context, R.layout.listview_item, dataLst);
        this.dataList = dataLst;
        this.mContext = context;
    }

    public CategoryListAdapter(String[] dataLst, Context context) {
        super(context, R.layout.listview_item, dataLst);
        List<Model> models = new ArrayList<>();
        for (int i = 0; i < dataLst.length; i++)
            models.add(new Model(dataLst[i]));
        this.dataList = models;
        this.mContext = context;
    }

    public CategoryListAdapter(String[] dataLst, Context context, CompoundButton.OnCheckedChangeListener listener) {
        super(context, R.layout.listview_item, dataLst);
        List<Model> models = new ArrayList<>();
        for (int i = 0; i < dataLst.length; i++)
            models.add(new Model(dataLst[i]));
        this.dataList = models;
        this.mContext = context;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Model getItem(int position) {
        return dataList.get(position);
    }


    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {

        final int itemPosition = position;
        ViewHolder viewHolder;
        final View viewOut;

        /*if (view != null) {
            viewHolder = (ViewHolder) view.getTag();
            viewOut = view;
        } else {*/
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item, parent, false);

            TextView categoryName = (TextView) view.findViewById(R.id.cat_name);
            CheckBox selection = (CheckBox) view.findViewById(R.id.cat_select);


            selection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    dataList.get(itemPosition).isSelected = isChecked;
                    if (listener != null)
                        listener.onCheckedChanged(buttonView, isChecked);
                }
            });

            viewHolder.categoryName = categoryName;
            viewHolder.selection = selection;

            viewOut = view;
            view.setTag(viewHolder);

        //}

        Model item = getItem(position);

        viewHolder.categoryName.setText(item.name);
        viewHolder.selection.setChecked(item.isSelected);

        return viewOut;
    }

    public boolean isSelected()
    {
        for (int i = 0; i < getCount(); i++)
            if(getItem(i).isSelected)
                return true;
        return false;
    }

    public String getSelected() {
        String s = "";
        for (int i = 0; i < getCount(); i++)
            if (getItem(i).isSelected)
                if (s == "")
                    s += getItem(i).name;
                else
                    s += ", " + getItem(i).name;
        return s;
    }
}

class Model
{
    String name = "";
    boolean isSelected = false;

    public Model(String name)
    {
        this.name = name;
    }

    public Model(String name, boolean isSelected)
    {
        this.name = name; this.isSelected = isSelected;
    }
}
