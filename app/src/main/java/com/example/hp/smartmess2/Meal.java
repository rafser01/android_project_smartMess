package com.example.hp.smartmess2;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.hp.smartmess2.adapter.Meal_Base_Adapter;
import com.example.hp.smartmess2.restModel.ListMeal;
import com.example.hp.smartmess2.restModel.MapMeal;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class Meal extends Fragment {
    ListView listView;
    List<com.example.hp.smartmess2.restModel.Meal> meals = new ArrayList<com.example.hp.smartmess2.restModel.Meal>();
    Map<String, ListMeal> member_meal_list = new HashMap<String, ListMeal>();
    final String meal_member_url = "http://10.0.2.2:8080/AndroidPrac/getMemberWithMeal/";

    String user;
    private ProgressDialog dialog;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_meal, container, false);

        listView = (ListView) rootView.findViewById(R.id.meal_list_view);

        Member_Meal_Task member_meal_task = new Member_Meal_Task();

        member_meal_task.execute();
        getActivity().setTitle(getUser());

        int i=0;
        while (getMeals().size() == 0) {
            try {
                i++;
                if(i==20)
                    break;
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(i<20) {


            listView.setAdapter(new Meal_Base_Adapter(getContext(), getMeals()));


            listView.setClickable(true);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Meal_Edit fragment = new Meal_Edit();
                    fragment.setUser(getUser());

                    fragment.setMeal(getMeals().get(position));
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
                }
            });
            i=0;

        }

        return rootView;
    }




    public List<com.example.hp.smartmess2.restModel.Meal> getMeals() {
        return meals;
    }

    public void setMeals(List<com.example.hp.smartmess2.restModel.Meal> meals) {
        this.meals = meals;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    private class Member_Meal_Task extends AsyncTask<Void, Void, Map<String, ListMeal>> {

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Loading Meal");
            dialog.show();

        }

        @Override
        protected Map<String, ListMeal> doInBackground(Void... params) {
            try {



                    String messName = getActivity().getIntent().getExtras().getString("messName");


                    RestTemplate restTemplate = new RestTemplate();
                    restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());


                    Log.e("Mess Name from meal ", messName);
                    member_meal_list = restTemplate.getForObject(meal_member_url + messName + "/", MapMeal.class);


                    String user = getActivity().getIntent().getExtras().getString("user").trim();

//                setUser(user);
                    setMeals(member_meal_list.get(getUser()));
                    System.out.println("d " + getMeals().size());

//                Thread.sleep(500);


                return member_meal_list;

            } catch (Exception e) {
                Log.e("Meal Member exception  ", e.getMessage());
            }


            return null;
        }

        protected void onPostExecute(Map<String, ListMeal> result) {

            if(dialog.isShowing())
            dialog.dismiss();

        }





    }
}


