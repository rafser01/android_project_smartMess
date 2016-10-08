package com.example.hp.smartmess2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hp.smartmess2.R;

import com.example.hp.smartmess2.restModel.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HP on 8/4/2016.
 */
public class User_Deduction_Base_Adapter extends BaseAdapter {
    LayoutInflater inflater;
    Context context;
     List<User> userList=new ArrayList<User>();
    ImageView icon;
    TextView name;

    public User_Deduction_Base_Adapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
        this.inflater = LayoutInflater.from(this.context);
    }


    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public User getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=inflater.inflate(R.layout.list_deduction,parent,false);
        }

        convertView.setMinimumHeight(100);
        icon= (ImageView) convertView.findViewById(R.id.deduction_image);
        name= (TextView) convertView.findViewById(R.id.deduction_text_name);
        name.setText(getItem(position).getName());

        return convertView;
    }
}
