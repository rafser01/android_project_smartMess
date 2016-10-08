package com.example.hp.smartmess2;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.hp.smartmess2.adapter.User_Deduction_Base_Adapter;
import com.example.hp.smartmess2.restModel.ListMoney;
import com.example.hp.smartmess2.restModel.MapMoney;
import com.example.hp.smartmess2.restModel.User;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class Deduction extends Fragment {
    ListView deduction_listView;
    Map<String, ListMoney> userMap=new HashMap<String, ListMoney>();
    List<User> userList=new ArrayList<User>();
    String messName;
    private ProgressDialog dialog;
    final String deduction_url = "http://10.0.2.2:8080/AndroidPrac/getDepostOfMemberList/";

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_deduction, container, false);
        deduction_listView= (ListView) rootView.findViewById(R.id.list_view_user_deduction);

        Deduction_Task deduction_task=new Deduction_Task();
        deduction_task.execute();

        while (userMap.keySet().size()==0){
            try {

                Thread.sleep(10);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        deduction_listView.setAdapter(new User_Deduction_Base_Adapter(getContext(),userList));
        deduction_listView.setClickable(true);
        deduction_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                user_deduction user_deduction=new user_deduction();
                user_deduction.setUser(userList.get(position));
                user_deduction.setListMoney(userMap.get(userList.get(position).getName().trim()));
                getFragmentManager().beginTransaction().replace(R.id.flContent,user_deduction).commit();
            }
        });


        return rootView;

    }

    public String getMessName() {
        return messName;
    }

    public void setMessName(String messName) {
        this.messName = messName;
    }

    class  Deduction_Task extends AsyncTask<Void,Void,MapMoney> {

        protected void onPreExecute() {
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Loading User");
            dialog.show();

        }

        @Override
        protected MapMoney doInBackground(Void... params) {
            try {

                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                userMap = restTemplate.getForObject(deduction_url + getMessName() + "/", MapMoney.class);

                Iterator iterator=userMap.keySet().iterator();
                while (iterator.hasNext()){
                    userList.add(new User(iterator.next().toString().trim()));
                }

            }catch (Exception e){
                Log.e("Deduction asy exception", e.getMessage());
            }

            return null;
        }

        protected void onPostExecute(MapMoney result) {

            if(dialog.isShowing())
                dialog.dismiss();

        }
    }
}
