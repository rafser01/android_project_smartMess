package com.example.hp.smartmess2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.hp.smartmess2.adapter.DatePicker;
import com.example.hp.smartmess2.restModel.Fragment_Login;
import com.example.hp.smartmess2.restModel.LoginValue;
import com.example.hp.smartmess2.restModel.Member;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Registration extends Fragment {

    EditText user;
    EditText email;
    EditText password;
    RadioButton male;
    RadioButton female;
    EditText dob;
    Button date_picker;
    EditText firstName;
    EditText lastName;
    Button clearFields;
    Button submit;
    RadioGroup sex;
    EditText messName;
    final static String type="Admin";
    final String registration_url="http://10.0.2.2:8080/AndroidPrac/insertUser/";



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        final View rootView=inflater.inflate(R.layout.fragment_registration,container,false);


        user= (EditText) rootView.findViewById(R.id.reg_input_user);
        email= (EditText) rootView.findViewById(R.id.reg_input_email);
        password= (EditText) rootView.findViewById(R.id.reg_input_pass);
        male= (RadioButton) rootView.findViewById(R.id.reg_radio_male);
        female= (RadioButton) rootView.findViewById(R.id.reg_radio_female);
        dob= (EditText) rootView.findViewById(R.id.reg_input_dob);
        date_picker= (Button) rootView.findViewById(R.id.reg_date_picker);
        firstName= (EditText) rootView.findViewById(R.id.reg_input_firstName);
        lastName= (EditText) rootView.findViewById(R.id.reg_input_lastName);
        clearFields= (Button) rootView.findViewById(R.id.reg_btn_clear);
        submit= (Button) rootView.findViewById(R.id.reg_btn_submit);
        sex= (RadioGroup) rootView.findViewById(R.id.reg_radioGroup_sex);
        messName= (EditText) rootView.findViewById(R.id.reg_input_messName);

        Toolbar toolbar= (Toolbar) rootView.findViewById(R.id.registration_toolbar);
        toolbar.setTitle("Registration");
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment_Login fragment_login=new Fragment_Login();
                getFragmentManager().beginTransaction().replace(R.id.fl_login_registration,fragment_login).commit();
            }
        });





        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sexValue=null;
                if(male.isChecked()){
                    sexValue="Male";
                }else if (female.isChecked()){
                    sexValue="Female";
                }

                List<String> list=new ArrayList<String>();
                list.add(user.getText().toString());
                list.add(email.getText().toString());
                list.add(password.getText().toString());
                list.add(dob.getText().toString());
                if(sexValue!=null){
                    list.add(sexValue.toString());
                }

                list.add(firstName.getText().toString());
                list.add(lastName.getText().toString());
                list.add(messName.getText().toString());

                boolean cheak=true;
                for(int i=0;i<list.size();i++){
                    if(list.get(i).isEmpty() ){

                        cheak=false;
                    }
                }


                if(cheak==true) {

                    DateFormat format=new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date date=format.parse(dob.getText().toString());
                        Member member = new Member(user.getText().toString().trim(),

                                password.getText().toString().trim(),
                                email.getText().toString().trim(),
                                sexValue,
                                date,
                                firstName.getText().toString(),
                                lastName.getText().toString(),
                                messName.getText().toString(),
                                type


                        );

                        RegistrationTask task = new RegistrationTask();
                        task.setMember(member);
                        task.execute();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                }else {

                    Snackbar snackbar=Snackbar.make(rootView,"Fill All Fields",Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });

        clearFields.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFields(user,email,password,sex,dob,firstName,lastName,messName);
            }
        });

        date_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment=new DatePicker();
                dialogFragment.show(getFragmentManager(),"Date Picker");
            }
        });





        return rootView;
    }


    @Override
    public void onAttach(Activity activity) {
        activity=(FragmentActivity) activity;
        super.onAttach(activity);
    }



    public void clearFields(EditText user, EditText email, EditText pass, RadioGroup sex, EditText dob, EditText firstName, EditText lastName, EditText messName){
        user.setText("");
        email.setText("");
        dob.setText("");
        firstName.setText("");
        lastName.setText("");
        pass.setText("");
        sex.clearCheck();
        messName.setText("");
    }

    public  void setDate(FragmentActivity activity, StringBuffer stringBuffer){
        dob= (EditText) activity.findViewById(R.id.reg_input_dob);
        dob.setText(stringBuffer.toString());
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getFragmentManager().popBackStack();
                System.out.println("in  ..");
                getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }





    private class RegistrationTask extends AsyncTask<Void, Void, LoginValue>{

        Member member=new Member();

        public Member getMember() {
            return member;
        }

        public void setMember(Member member) {
            this.member = member;
        }

        @Override
        protected LoginValue doInBackground(Void... params) {
            try {
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                LoginValue result = restTemplate.postForObject(registration_url, getMember(), LoginValue.class);
                return result;

            }catch (Exception e){
                Log.e("Registration_Activy",e.getMessage(),e);
            }




            return null;
        }

        protected void onPostExecute(LoginValue result){

            if(result.isResult()==true) {
                Toast.makeText(getContext(), "Successfully saved", Toast.LENGTH_LONG).show();
            }
            else if (result.isEmailCheck()==true){
                Toast.makeText(getContext(),"Fail to saved! Email ALready Exist",Toast.LENGTH_LONG).show();
                result.setEmailCheck(false);

            }
            else if(result.isUserCheck()==true){
                Toast.makeText(getContext(),"Fail to saved! User ALready Exist",Toast.LENGTH_LONG).show();
                result.setUserCheck(false);

            }
        }
    }



}
