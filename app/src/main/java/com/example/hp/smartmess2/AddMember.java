package com.example.hp.smartmess2;

import android.content.Intent;
import android.os.AsyncTask;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;


import com.example.hp.smartmess2.restModel.LoginValue;
import com.example.hp.smartmess2.restModel.Member;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;


import java.text.ParseException;
import java.text.SimpleDateFormat;


public class AddMember extends AppCompatActivity {
    EditText user;
    EditText email;

    RadioButton male;
    RadioButton female;

    String userName;
    String messName;
    Member member=new Member();


    final String add_member_url="http://10.0.2.2:8080/AndroidPrac/insertUser/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        Toolbar toolbar= (Toolbar) findViewById(R.id.add_member_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Admin_Menu.class);
                intent.putExtra("messName",getMessName());
                intent.putExtra("user",getUserName());
                startActivity(intent);
            }
        });


        setTitle("Add Member");
        Bundle bundle=getIntent().getExtras();
        setMessName(bundle.getString("messName").toString().trim());
        setUserName(bundle.getString("user").toString().trim());


    }

    public void resetOnAddMember(View view){

    }

    public void  submitOnAddMember(View view){
        user=(EditText)findViewById(R.id.add_member_user);
        email= (EditText) findViewById(R.id.add_member_input_email);
        male= (RadioButton) findViewById(R.id.add_member_radio_male);
        female= (RadioButton) findViewById(R.id.add_member_radio_female);
        getMember().setUser(user.getText().toString().trim());
        getMember().setEmail(email.getText().toString().trim());
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");

        try {
            getMember().setDob(format.parse("0000-00-00"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        getMember().setPassword(user.getText().toString());
        String sexValue=null;
        if(male.isChecked()){
            sexValue="Male";

        }else if (female.isChecked()){
            sexValue="Female";
        }
        getMember().setGender(sexValue);
        getMember().setType("Member");
        getMember().setFirstName("First Name");
        getMember().setLastName("Last Name");
        Bundle extras=getIntent().getExtras();
        String messName=extras.getString("messName");
        getMember().setMessName(messName);
        getMember().setMessName(getMessName());
        AddMemberAsyncTask addMemberAsyncTask=new AddMemberAsyncTask();
        addMemberAsyncTask.execute();
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String getMessName() {
        return messName;
    }

    public void setMessName(String messName) {
        this.messName = messName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    class AddMemberAsyncTask extends AsyncTask<Void, Void, LoginValue>{

        @Override
        protected LoginValue doInBackground(Void... params) {

            try {

                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                LoginValue result = restTemplate.postForObject(add_member_url, getMember(), LoginValue.class);
            return result;
            }catch (Exception e){
                Log.e("Add Member Activity ",e.getMessage(),e);
            }

                return null;
        }

        protected void onPostExecute(LoginValue result){

            if(result.isResult()==true) {
                Toast.makeText(getApplicationContext(), "Successfully saved", Toast.LENGTH_LONG).show();
                  }
            else if (result.isEmailCheck()==true){
                Toast.makeText(getApplicationContext(),"Fail to saved! Email ALready Exist",Toast.LENGTH_LONG).show();
                result.setEmailCheck(false);

            }
            else if(result.isUserCheck()==true){
                Toast.makeText(getApplicationContext(),"Fail to saved! User ALready Exist",Toast.LENGTH_LONG).show();
                result.setUserCheck(false);

            }
        }



    }
}
