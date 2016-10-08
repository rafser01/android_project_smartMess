package com.example.hp.smartmess2.restModel;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hp.smartmess2.R;
import com.example.hp.smartmess2.Registration;

/**
 * Created by HP on 7/14/2016.
 */
public class Fragment_Login extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View rootView=inflater.inflate(R.layout.login,container,false);
        Toolbar toolbar= (Toolbar) rootView.findViewById(R.id.login_toolbar);
        toolbar.setTitle("Login");




        return rootView;
    }


}
