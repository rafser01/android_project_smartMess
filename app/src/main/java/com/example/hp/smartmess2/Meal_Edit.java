package com.example.hp.smartmess2;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.hp.smartmess2.restModel.*;
import com.example.hp.smartmess2.restModel.Meal;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Meal_Edit extends Fragment {
    Spinner spn_breakFast;
    Spinner spn_lunch;
    Spinner spn_dinner;
    EditText input_breakFast;
    EditText input_lunch;
    EditText input_dinner;
    Button reset;
    Button submit;
    Button back;
    MealUpdate mu=new MealUpdate();
    String user;

    final String mealUpdate = "http://10.0.2.2:8080/AndroidPrac/updateMeal/";
    com.example.hp.smartmess2.restModel.Meal meal=new Meal();

    public Meal getMeal() {
        return meal;
    }

    public void setMeal(Meal meal) {
        this.meal = meal;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        final View rootView=inflater.inflate(R.layout.fragment_meal__edit,container,false);
        spn_breakFast= (Spinner) rootView.findViewById(R.id.meal_edit_spin_breakFast);
        spn_lunch= (Spinner) rootView.findViewById(R.id.meal_edit_spin_lunch);
        spn_dinner= (Spinner) rootView.findViewById(R.id.meal_edit_spin_dinner);
        input_breakFast= (EditText) rootView.findViewById(R.id.meal_edit_input_breakfast);
        input_lunch= (EditText) rootView.findViewById(R.id.meal_edit_input_lunch);
        input_dinner= (EditText) rootView.findViewById(R.id.meal_edit_input_dinner);
        reset= (Button) rootView.findViewById(R.id.meal_edit_btn_reset);
        submit= (Button) rootView.findViewById(R.id.meal_edit_btn_submit);
        back= (Button) rootView.findViewById(R.id.meal_edit_btn_back);

        input_breakFast.setText(getMeal().getBreakFast()+"");
        input_lunch.setText(getMeal().getLunch()+"");
        input_dinner.setText(getMeal().getDinner()+"");
        final String[] meals={"0.0","0.5","1.0","1.5","2.0","2.5","3.0","3.5","4.0","4.5","5.0"};
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,meals );
        spn_breakFast.setAdapter(adapter);
        spn_lunch.setAdapter(adapter);
        spn_dinner.setAdapter(adapter);






            spn_breakFast.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                int check=0;

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    check=check+1;
                    if(check>1) {


                        input_breakFast.setText(meals[position]);
                    }


                }


                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            spn_dinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                int check=0;
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    check=check+1;
                    if(check>1) {
                        input_dinner.setText(parent.getSelectedItem() + "");
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            spn_lunch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                 int check=0;

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    check=check+1;
                    if(check>1) {
                        input_lunch.setText(parent.getSelectedItem() + "");
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        if(getMeal().getDate()!=null){
            Calendar c=Calendar.getInstance();
            SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
            c.setTime(getMeal().getDate());
            c.add(Calendar.DATE,1);
            String date=df.format(c.getTime());
            getActivity().setTitle(date);

            input_breakFast.setText(getMeal().getBreakFast()+"");
            input_lunch.setText(getMeal().getLunch()+"");
            input_dinner.setText(getMeal().getDinner()+"");


            reset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    input_breakFast.setText(getMeal().getBreakFast()+"");
                    input_lunch.setText(getMeal().getLunch()+"");
                    input_dinner.setText(getMeal().getDinner()+"");
                }
            });
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    double breakfast=Double.parseDouble(input_breakFast.getText().toString().trim());
                    double lunch=Double.parseDouble(input_lunch.getText().toString().trim());
                    double dinner=Double.parseDouble(input_dinner.getText().toString().trim());
                    if(breakfast!=getMeal().getBreakFast() || lunch!=getMeal().getLunch() || dinner !=getMeal().getDinner()){

                        MealUpdate mealUpdate=new MealUpdate();
                        mealUpdate.setId(getMeal().getId());
                        mealUpdate.setBreakFast(breakfast);
                        mealUpdate.setLunch(lunch);
                        mealUpdate.setDinner(dinner);

                        System.out.println(getMeal().getId());
                        setMu(mealUpdate);
                        MealUpdateTask mealUpdateTask=new MealUpdateTask();
                        mealUpdateTask.execute();
                        getMeal().setBreakFast(breakfast);
                        getMeal().setLunch(lunch);
                        getMeal().setDinner(dinner);

                    }

                }
            });
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gotoMealFragmet();
                }
            });

        }








        return rootView;
    }

    public void gotoMealFragmet(){
        com.example.hp.smartmess2.Meal fragment=new com.example.hp.smartmess2.Meal();
        fragment.setUser(getUser());
        FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent,fragment).commit();
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public MealUpdate getMu() {
        return mu;
    }

    public void setMu(MealUpdate mu) {
        this.mu = mu;
    }

    class MealUpdateTask extends AsyncTask<Void, Void, Boolean>{

        @Override
        protected Boolean doInBackground(Void... params) {
            Boolean result=true;
            try {
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                result = restTemplate.postForObject(mealUpdate, getMu(), Boolean.class);

            }catch (Exception e){
                result=false;
            }

                return result;
        }
        protected void onPostExecute(Boolean result){

            if(result==true) {
                Toast.makeText(getContext(),"SuccessFully Saved",Toast.LENGTH_LONG).show();
                   }
            else if (result==false){
                Toast.makeText(getContext(),"Not Saved",Toast.LENGTH_LONG).show();

            }
        }
    }




}
