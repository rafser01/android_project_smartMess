package com.example.hp.smartmess2;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.hp.smartmess2.adapter.DepositBaseAdapter;
import com.example.hp.smartmess2.restModel.ListMeal;
import com.example.hp.smartmess2.restModel.ListMoney;
import com.example.hp.smartmess2.restModel.ListUser;
import com.example.hp.smartmess2.restModel.MapMoney;
import com.example.hp.smartmess2.restModel.User;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class Deposit extends Fragment {
    ListView deposit_listView;
    Map<String, ListMoney> userMap=new HashMap<String, ListMoney>();
    List<User> userList=new ArrayList<User>();
    String messName;
    private ProgressDialog dialog;
    final String depost_url = "http://10.0.2.2:8080/AndroidPrac/getDepostOfMemberList/";
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_deposit, container, false);
        deposit_listView= (ListView) rootView.findViewById(R.id.deposit_list_view);

        DepositTask depositTask=new DepositTask();
        depositTask.execute();

        while (userMap.keySet().size()==0){
            try {

                Thread.sleep(10);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }




        deposit_listView.setAdapter(new DepositBaseAdapter(getContext(),userList));
        deposit_listView.setClickable(true);
        deposit_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserDeposit userDeposit=new UserDeposit();
                userDeposit.setUser(userList.get(position));
                userDeposit.setListMoney(userMap.get(userList.get(position).getName().trim()));
                getFragmentManager().beginTransaction().replace(R.id.flContent,userDeposit).commit();
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

    class  DepositTask extends AsyncTask<Void,Void,MapMoney>{

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
                    userMap = restTemplate.getForObject(depost_url + getMessName() + "/", MapMoney.class);

                Iterator iterator=userMap.keySet().iterator();
                while (iterator.hasNext()){
                    userList.add(new User(iterator.next().toString().trim()));
                }

                }catch (Exception e){
                Log.e("Deposit asyn exception ", e.getMessage());
            }

                return null;
        }

        protected void onPostExecute(MapMoney result) {

            if(dialog.isShowing())
                dialog.dismiss();

        }
    }
}
