package com.example.hp.smartmess2.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.hp.smartmess2.R;
import com.example.hp.smartmess2.restModel.Meal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by HP on 7/17/2016.
 */
public class Meal_Base_Adapter extends BaseAdapter {
    LayoutInflater inflater;
    Context context;
    List<Meal>  meals=new ArrayList<Meal>();

    TextView date;
    TextView breakFast;
    TextView lunch;
    TextView dinner;


    public Meal_Base_Adapter(Context context, List<Meal> meals) {
        this.context=context;
        this.meals=meals;
        this.inflater=LayoutInflater.from(this.context);
    }

    public Meal getMeal(int position){
        Meal meal=new Meal();
       meal=meals.get(position);



        return meal;
    }

    @Override
    public int getCount() {

        return meals.size();
    }

    @Override
    public Meal getItem(int position) {
        return getMeal(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=inflater.inflate(R.layout.list_meal, parent,false);

        }

        date= (TextView) convertView.findViewById(R.id.meal_date);
        breakFast= (TextView) convertView.findViewById(R.id.meal_break_fast);
        lunch= (TextView) convertView.findViewById(R.id.meal_lunch);
        dinner= (TextView) convertView.findViewById(R.id.meal_dinner);

        convertView.setMinimumHeight(100);
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        String stringDate=format.format(getItem(position).getDate());
        Calendar c=Calendar.getInstance();
        if(stringDate!=null) {
            try {
                c.setTime(format.parse(stringDate));
                c.add(Calendar.DATE, 1);
                stringDate = format.format(c.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        date.setText(stringDate);
        breakFast.setText(getItem(position).getBreakFast()+"");
        breakFast.setTextColor(Color.BLUE);
        lunch.setText(getItem(position).getLunch()+"");
        lunch.setTextColor(Color.BLUE);
        dinner.setText(getItem(position).getDinner()+"");
        dinner.setTextColor(Color.BLUE);


        return convertView;
    }
}
