package com.example.hp.smartmess2;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.smartmess2.adapter.User_Deposit_BaseAdapter;
import com.example.hp.smartmess2.restModel.ListMeal;
import com.example.hp.smartmess2.restModel.ListMoney;
import com.example.hp.smartmess2.restModel.Member;
import com.example.hp.smartmess2.restModel.Money;
import com.example.hp.smartmess2.restModel.User;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;


public class UserDeposit extends Fragment {
    public ListMoney listMoney=new ListMoney();
    User user;
    ListView listView;
    TextView monthlyDeposit;
    TextView balance;
    EditText add_deposit;
    Button btn_add_deposit;
    EditText dialog_date;
    EditText dialog_deposit;
    Button dialog_cancel;
    Button dialog_submit;
    private Money money=new Money();
    private Member member=new Member();
    final String insertMoney = "http://10.0.2.2:8080/AndroidPrac/insertMoney/";
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_user_deposit, container, false);
        listView= (ListView) rootView.findViewById(R.id.user_deposit_list_view);
        monthlyDeposit= (TextView) rootView.findViewById(R.id.user_deposit_monthly_deposit);
        balance= (TextView) rootView.findViewById(R.id.user_deposit_balance);
        add_deposit= (EditText) rootView.findViewById(R.id.user_deposit_input_deposit);
        btn_add_deposit= (Button) rootView.findViewById(R.id.user_deposit_btn_add_deposit);

        getActivity().setTitle(getUser().getName());
        listView.setAdapter(new User_Deposit_BaseAdapter(getContext(),getListMoney()));


        double monthlyDepositValue=0.0;
        double balanceValue=0.0;
        for(Money m:getListMoney()){
            if(m.getDeposit()!=null)
            monthlyDepositValue+=m.getDeposit();

            if(m.getBazarCost()!=null)
            balanceValue+=m.getBazarCost();
            if(m.getFoodCost()!=null)
            balanceValue+=m.getFoodCost();
            if(m.getHouseRent()!=null)
            balanceValue+=m.getHouseRent();
            if(m.getMaintenance()!=null)
            balanceValue+=m.getMaintenance();
            if(m.getServentBill()!=null)
            balanceValue+=m.getServentBill();
            if(m.getUtilities()!=null)
            balanceValue+=m.getUtilities();
        }
        monthlyDeposit.setText(monthlyDepositValue+"");

        balance.setText(monthlyDepositValue-balanceValue+"");
        if((monthlyDepositValue-balanceValue)>0){
            balance.setTextColor(Color.parseColor("#009877"));
        }else {
            balance.setTextColor(Color.parseColor("#FF4081"));
        }
        btn_add_deposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(add_deposit.getText()!=null && add_deposit.getText().length()!=0) {
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.custom_dialog_deposit_confirm);
                    dialog.setTitle("Confirm Deposit....");
                    dialog_date= (EditText) dialog.findViewById(R.id.custom_dialog_deposit_input_date);
                    dialog_deposit= (EditText) dialog.findViewById(R.id.custom_dialog_deposit_input_deposit);
                    dialog_cancel= (Button) dialog.findViewById(R.id.custom_dialog_cancel_btn);
                    dialog_submit= (Button) dialog.findViewById(R.id.custom_dialog_btn_submit);

                    //-------------------------Text watcher--------------
                    TextWatcher tw=new TextWatcher() {
                        private String current = "";
                        private String ddmmyyyy = "DDMMYYYY";
                        private Calendar cal = Calendar.getInstance();

                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (!s.toString().equals(current)) {
                                String clean = s.toString().replaceAll("[^\\d.]", "");
                                String cleanC = current.replaceAll("[^\\d.]", "");

                                int cl = clean.length();
                                int sel = cl;

                                for (int i = 2; i <= cl && i < 6; i += 2) {
                                    sel++;
                                };
                                //Fix for pressing delete next to a forward slash
                                if (clean.equals(cleanC)) sel--;

                                if (clean.length() < 8){
                                    clean = clean + ddmmyyyy.substring(clean.length());
                                }else{
                                    //This part makes sure that when we finish entering numbers
                                    //the date is correct, fixing it otherwise
                                    int day  = Integer.parseInt(clean.substring(0,2));
                                    int mon  = Integer.parseInt(clean.substring(2,4));
                                    int year = Integer.parseInt(clean.substring(4,8));

                                    if(mon > 12) mon = 12;
                                    cal.set(Calendar.MONTH, mon-1);
                                    year = (year<1960)?1960:(year>2100)?2100:year;
                                    cal.set(Calendar.YEAR, year);
                                    // ^ first set year for the line below to work correctly
                                    //with leap years - otherwise, date e.g. 29/02/2012
                                    //would be automatically corrected to 28/02/2012

                                    day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                                    clean = String.format("%02d%02d%02d",day, mon, year);
                                }
                                clean = String.format("%s/%s/%s", clean.substring(0, 2),
                                        clean.substring(2, 4),
                                        clean.substring(4, 8));

                                sel = sel < 0 ? 0 : sel;
                                current = clean;
                                dialog_date.setText(current);
                                dialog_date.setSelection(sel < current.length() ? sel : current.length());


                            }

                        };
                        //We also implement the other two functions because we have to
                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    };
                    //------------------------------
                    dialog_date.addTextChangedListener(tw);
                    dialog_deposit.setText(add_deposit.getText().toString());



                    dialog.show();


                    dialog_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog_submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                DateFormat format=new SimpleDateFormat("dd/MM/yyyy");

                                String date=dialog_date.getText().toString();
                            if(date!=null && date.length()!=0 && dialog_deposit.getText().length()!=0) {
                                try {
                                    Date dateParse = format.parse(date);
                                    format = new SimpleDateFormat("yyyy-MM-dd");
                                    String dateConverted = format.format(dateParse);
                                    dateParse=format.parse(dateConverted);

                                    getMoney().setDate(dateParse);
                                    getMoney().setDeposit(Double.parseDouble(add_deposit.getText().toString().trim()));
                                    getMember().setUser(getUser().getName().toString());
                                    getMoney().setUser(getMember());
                                    //getListmoney te new Money add kore dilam
                                    Calendar calendar=Calendar.getInstance();
                                    calendar.setTime(dateParse);
                                    calendar.add(Calendar.DATE,-1);
                                    Date dateToBeAdded=calendar.getTime();
                                    getListMoney().add(new Money(getMember(),dateToBeAdded,Double.parseDouble(add_deposit.getText().toString().trim())));
                                    UserDepositTask userDepositTask=new UserDepositTask();
                                    userDepositTask.execute();
                                    dialog.dismiss();


                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }else {
                                Snackbar.make(rootView,"Fill All Fields",Snackbar.LENGTH_LONG).show();
                            }


                        }
                    });


                }else {
                    Snackbar.make(rootView,"Emplty Deposit Field",Snackbar.LENGTH_LONG).show();
                }
            }
        });



        return rootView;

    }

    public Money getMoney() {
        return money;
    }

    public void setMoney(Money money) {
        this.money = money;
    }

    public ListMoney getListMoney() {
        return listMoney;
    }

    public void setListMoney(ListMoney listMoney) {
        this.listMoney = listMoney;
    }

    public User getUser() {
        return user;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void setUser(User user) {
        this.user = user;
    }

    class UserDepositTask extends AsyncTask<Void, Void, Boolean>{
        private ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Saving Deposit");
            dialog.show();

        }
        Boolean result;
        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                result = restTemplate.postForObject(insertMoney, getMoney(), Boolean.class);

            }catch (Exception e){
                result=false;
            }
                return result;
        }
        protected void onPostExecute(Boolean result) {


            UserDeposit userDeposit=new UserDeposit();
            userDeposit.setListMoney(getListMoney());
            userDeposit.setUser(getUser());
            getFragmentManager().beginTransaction().replace(R.id.flContent,userDeposit).commit();
            if(dialog.isShowing())
                dialog.dismiss();
        }


    }

}
