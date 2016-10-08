package com.example.hp.smartmess2;


import android.content.Intent;

import android.os.AsyncTask;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.example.hp.smartmess2.restModel.Fragment_Login;
import com.example.hp.smartmess2.restModel.Member;


import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class Login extends AppCompatActivity {
    final String login_url="http://10.0.2.2:8080/AndroidPrac/login/";
    EditText email;
    EditText password;
    FragmentManager fragmentManager=getSupportFragmentManager();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        Fragment fragment=new Fragment_Login();
        fragmentManager.beginTransaction().replace(R.id.fl_login_registration,fragment).commit();




    }

    public void login(View view){

        email= (EditText) findViewById(R.id.login_input_email);
        password= (EditText) findViewById(R.id.login_input_password);

        LoginTask loginTask=new LoginTask();
        loginTask.email=email.getText().toString();
        loginTask.pass=password.getText().toString();

        loginTask.execute();


    }
    public void registration(View view){


        Registration fragment=new Registration();
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
//        fragmentManager.beginTransaction().replace(R.id.fl_login_registration,fragment).commit();
        fragmentTransaction.replace(R.id.fl_login_registration,fragment).addToBackStack(null).commit();

    }






    private class LoginTask extends AsyncTask<Void, Void, Member>{
        String email;
        String pass;

        @Override
        protected Member doInBackground(Void... params) {

            try{


                RestTemplate restTemplate=new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                Log.e("email & Pass "+email,pass);

                Member member=restTemplate.getForObject(login_url+email+"/"+pass+"/", Member.class);
                return member;
            }catch (Exception e){

                Log.e("exception ",e.getMessage());
            }

            return null;
        }
        @Override
        protected void onPostExecute(Member member){
            try {
                if (member.getUser() != null && member.getType().equalsIgnoreCase("admin")) {

                    Bundle bundle=new Bundle();
                    bundle.putString("messName",member.getMessName());
                    bundle.putString("user",member.getUser());
                    Log.e("Mess Name from Login ",member.getMessName());
                    Intent intent=new Intent(getApplicationContext(),Admin_Menu.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_LONG).show();
                }
            }catch (NullPointerException e){
                Toast.makeText(getApplicationContext(), "Fill All Fields " , Toast.LENGTH_LONG).show();
            }
        }
    }


}
