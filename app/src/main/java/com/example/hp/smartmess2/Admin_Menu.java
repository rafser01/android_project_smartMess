package com.example.hp.smartmess2;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import android.widget.Toast;


import com.example.hp.smartmess2.restModel.Member;


import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Admin_Menu extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    Map<String,List<com.example.hp.smartmess2.restModel.Meal>> member_meal_list=new HashMap<String,List<com.example.hp.smartmess2.restModel.Meal> >();
    List<String> meal_submenu=new ArrayList<String>();

    Meal meal;
    private FragmentManager fragmentManager=getSupportFragmentManager();

    //-------------------
    ExpandableListAdapter mMenuAdapter;
    ExpandableListView expandableList;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    String messName;
    final String meal_member_url="http://10.0.2.2:8080/AndroidPrac/getMemberWithMeal/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__menu);


        //--------------------------
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        expandableList= (ExpandableListView) findViewById(R.id.navigationmenu);
        final NavigationView navigationView = (NavigationView)    findViewById(R.id.nvView);

        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }


        final Member_Meal_Task member_meal_task=new Member_Meal_Task();
        member_meal_task.execute();



        //Call Member_Meal_Task before prepareListData()
        prepareListData();
        mMenuAdapter = new com.example.hp.smartmess2.adapter.ExpandableListAdapter(this, listDataHeader,   listDataChild, expandableList);

        while(member_meal_list.keySet().size()==0){
            try{
                Thread.sleep(100);
            }catch (Exception e){

            }
        }
        Bundle bundle=getIntent().getExtras();
        setUser(bundle.getString("user").toString().trim());
        if(meal_submenu.size()>=2) {
            meal = new Meal();
            meal.setUser(getUser());
            fragmentManager.beginTransaction().replace(R.id.flContent, meal).commit();
        }else {
            //add add member fragment
            Intent intent=new Intent(this,AddMember.class);
            intent.putExtra("messName",getMessName());
            intent.putExtra("user",getUser());
            startActivity(intent);
        }

        expandableList.setAdapter(mMenuAdapter);
        expandableList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

//                if(listDataChild.get(listDataHeader.get(groupPosition))==null){
//                    Toast.makeText(getApplicationContext(),"item id "+groupPosition,Toast.LENGTH_LONG).show();
//                    setTitle(listDataHeader.get(groupPosition).toUpperCase());
//
//
//                    drawerLayout.closeDrawers();
//                }
                switch (groupPosition){
                    case 0:
                        Intent intent=new Intent(getApplicationContext(),AddMember.class);
                        drawerLayout.closeDrawers();
                        intent.putExtra("messName",getMessName());
                        System.out.println("user "+getUser());
                        intent.putExtra("user",getUser());
                        startActivity(intent);

                        break;
                }

                return false;
            }
        });
        expandableList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {


                 switch (groupPosition){
                     case 1:

                         meal=new Meal();
                         meal.setUser(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).toString().trim());
                         fragmentManager.beginTransaction().replace(R.id.flContent,meal).commit();
                         break;
                     case 2:
                         if(childPosition==0){
                            Deposit deposit=new Deposit();
                             deposit.setMessName(getMessName());
                             setTitle("Deposit");
                             fragmentManager.beginTransaction().replace(R.id.flContent,deposit).commit();
                         }else if (childPosition==1){
                             Deduction deduction=new Deduction();
                             deduction.setMessName(getMessName());
                             setTitle("Debit/Deductions");
                             fragmentManager.beginTransaction().replace(R.id.flContent,deduction).commit();
                         }

                         break;

                 }



                drawerLayout.closeDrawers();
                return false;
            }
        });

    }

    public String getMessName() {
        return messName;
    }

    public void setMessName(String messName) {
        this.messName = messName;
    }

    public Map<String, List<com.example.hp.smartmess2.restModel.Meal>> getMember_meal_list() {
        return member_meal_list;
    }

    public void setMember_meal_list(Map<String, List<com.example.hp.smartmess2.restModel.Meal>> member_meal_list) {
        this.member_meal_list = member_meal_list;
    }

    String user;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    //---------new method starts here ------
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        Toast.makeText(getApplicationContext(),"id "+menuItem.getItemId(),Toast.LENGTH_LONG).show();
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding data header
        listDataHeader.add("Add Member");
        listDataHeader.add("Meal");
        listDataHeader.add("Transaction");
        listDataHeader.add("Schedule");
        listDataHeader.add("To-Let");
        listDataHeader.add("Notice");
        listDataHeader.add("Balance Transfer");


        // Adding child data
        List<String> subMenuofTransaction= new ArrayList<String>();
        subMenuofTransaction.add("Deposit");
        subMenuofTransaction.add("Deduction");
        List<String> subMenuofSchedule= new ArrayList<String>();
        subMenuofSchedule.add("Bazar Schedule");
        subMenuofSchedule.add("Cleaning Schedule");






        listDataChild.put(listDataHeader.get(1),meal_submenu);

        listDataChild.put(listDataHeader.get(2), subMenuofTransaction);// Header, Child data
        listDataChild.put(listDataHeader.get(3), subMenuofSchedule);


    }
    public  void setMeals(){

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class Member_Meal_Task extends AsyncTask<Void, Void, Map<String,List<com.example.hp.smartmess2.restModel.Meal>>> {



        @Override
        protected Map<String,List<com.example.hp.smartmess2.restModel.Meal>> doInBackground(Void... params) {
            try {

                Bundle bundle=getIntent().getExtras();
                String messName=bundle.getString("messName");
                setMessName(messName);

                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());


                Log.e("Mess Name from menu ",messName);
                member_meal_list = restTemplate.getForObject(meal_member_url+messName+"/",member_meal_list.getClass());

                Iterator iterator=member_meal_list.keySet().iterator();
                while (iterator.hasNext()){
                    meal_submenu.add(iterator.next().toString());
                }

                setMember_meal_list(member_meal_list);
                Log.e("meal submenu 1 ",meal_submenu.size()+"");


                return member_meal_list;

            }catch (Exception e){
                Log.e("Meal Member exception  ",e.getMessage());
            }




            return null;
        }


    }

}



