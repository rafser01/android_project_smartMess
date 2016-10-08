package com.example.hp.smartmess2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.hp.smartmess2.R;
import com.example.hp.smartmess2.restModel.ListMoney;
import com.example.hp.smartmess2.restModel.Money;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by HP on 7/26/2016.
 */
public class User_Deposit_BaseAdapter extends BaseAdapter {
    LayoutInflater inflater;
    Context context;
    TextView date;
    TextView deposit;

    ListMoney listMoney=new ListMoney();

    public User_Deposit_BaseAdapter(Context context, ListMoney listMoney) {
        this.context = context;
        this.listMoney = listMoney;
        this.inflater=LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return listMoney.size();
    }

    @Override
    public Money getItem(int position) {
        return listMoney.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=inflater.inflate(R.layout.list_user_deposit,parent,false);
        }
        date= (TextView) convertView.findViewById(R.id.list_user_deposit_date);
        deposit= (TextView) convertView.findViewById(R.id.list_user_deposit_deposit);
        Money money=listMoney.get(position);
        Date dateUtil=money.getDate();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(dateUtil);
        calendar.add(Calendar.DATE,1);

        String dateString=format.format(calendar.getTime());
        date.setText(dateString);
        deposit.setText(money.getDeposit()+"");


        return convertView;
    }
}
