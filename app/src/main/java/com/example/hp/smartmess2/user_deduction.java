package com.example.hp.smartmess2;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.hp.smartmess2.restModel.ListMoney;
import com.example.hp.smartmess2.restModel.Member;
import com.example.hp.smartmess2.restModel.Money;
import com.example.hp.smartmess2.restModel.User;
import com.example.hp.smartmess2.service.ServiceClass;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class user_deduction extends Fragment {
    Spinner chooseDeduction;
    EditText inputAmount;
    Button submit;
    TextView maintenanceCost;
    TextView serventBill;
    TextView utility;
    TextView houseRent;

    String selectedDeduction;
    Money money=new Money();
    final String insertMoney = "http://10.0.2.2:8080/AndroidPrac/insertMoney/";
    final String deduction_url = "http://10.0.2.2:8080/AndroidPrac/getDepostOfMemberList/";
    ListMoney listMoney=new ListMoney();
    User user;
    Member member=new Member();
    double houseR;
    double maintenanceR;
    double utilityR;
    double serventR;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_user_deduction, container, false);
        chooseDeduction= (Spinner) rootView.findViewById(R.id.deduction_spinner);
        inputAmount= (EditText) rootView.findViewById(R.id.deduction_input_amount);
        submit= (Button) rootView.findViewById(R.id.deduction_btn_add);
        maintenanceCost= (TextView) rootView.findViewById(R.id.deduction_maintenance_cost);
        serventBill= (TextView) rootView.findViewById(R.id.deduction_servent_bill);
        utility= (TextView) rootView.findViewById(R.id.deduction_utility);
        houseRent= (TextView) rootView.findViewById(R.id.deduction_house_rent);
        final String[] deductions={"Choose","House Rent","Utility","Maintenance","Servant" };
        int hidingIndex=0;
        CustomAdapter adapter=new CustomAdapter(getContext(),android.R.layout.simple_spinner_item,deductions,hidingIndex);

        chooseDeduction.setAdapter(adapter);

        chooseDeduction.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                setSelectedDeduction(parent.getSelectedItem().toString().trim());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        double houseRents=0;
        double serventCost=0;
        double utilityCost=0;
        double maintenanceCostAmount=0;
        for(Money m:getListMoney()){
            if(m.getHouseRent()!=null)
                houseRents+=m.getHouseRent();
            if(m.getUtilities()!=null)
                utilityCost+=m.getUtilities();
            if(m.getServentBill()!=null)
                serventCost+=m.getServentBill();
            if(m.getMaintenance()!=null)
                maintenanceCostAmount+=m.getMaintenance();
        }

        setHouseR(houseRents);
        setMaintenanceR(maintenanceCostAmount);
        setUtilityR(utilityCost);
        setServentR(serventCost);


        serventBill.setText(serventCost+"");
        houseRent.setText(houseRents+"");
        utility.setText(utilityCost+"");
        maintenanceCost.setText(maintenanceCostAmount+"");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputAmount.getText().length() != 0 && inputAmount.getText() != null && getSelectedDeduction().length() != 0 && getSelectedDeduction()!=null && !getSelectedDeduction().equalsIgnoreCase("choose")) {
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.custom_dialog_deduction_confirm);
                    TextView confirm_type = (TextView) dialog.findViewById(R.id.deduction_confirm_type);
                    final EditText confirm_date = (EditText) dialog.findViewById(R.id.deduction_confirm_date);
                    final TextView confirm_amount = (TextView) dialog.findViewById(R.id.deduction_confirm_amount);
                    Button confirm_cancel = (Button) dialog.findViewById(R.id.deduction_confirm_cancel);
                    Button confirm_submit = (Button) dialog.findViewById(R.id.deduction_confirm_submit);

                    dialog.setTitle("Confirm Deductions");
                    dialog.show();

                    //set date text watcher///////

                    TextWatcher tw = new TextWatcher() {
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
                                }
                                ;
                                //Fix for pressing delete next to a forward slash
                                if (clean.equals(cleanC)) sel--;

                                if (clean.length() < 8) {
                                    clean = clean + ddmmyyyy.substring(clean.length());
                                } else {
                                    //This part makes sure that when we finish entering numbers
                                    //the date is correct, fixing it otherwise
                                    int day = Integer.parseInt(clean.substring(0, 2));
                                    int mon = Integer.parseInt(clean.substring(2, 4));
                                    int year = Integer.parseInt(clean.substring(4, 8));

                                    if (mon > 12) mon = 12;
                                    cal.set(Calendar.MONTH, mon - 1);
                                    year = (year < 1960) ? 1960 : (year > 2100) ? 2100 : year;
                                    cal.set(Calendar.YEAR, year);
                                    // ^ first set year for the line below to work correctly
                                    //with leap years - otherwise, date e.g. 29/02/2012
                                    //would be automatically corrected to 28/02/2012

                                    day = (day > cal.getActualMaximum(Calendar.DATE)) ? cal.getActualMaximum(Calendar.DATE) : day;
                                    clean = String.format("%02d%02d%02d", day, mon, year);
                                }
                                clean = String.format("%s/%s/%s", clean.substring(0, 2),
                                        clean.substring(2, 4),
                                        clean.substring(4, 8));

                                sel = sel < 0 ? 0 : sel;
                                current = clean;
                                confirm_date.setText(current);
                                confirm_date.setSelection(sel < current.length() ? sel : current.length());


                            }

                        }

                        ;

                        //We also implement the other two functions because we have to
                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    };
                    //------------------------------
                    confirm_date.addTextChangedListener(tw);
                    confirm_type.setText(getSelectedDeduction());
                    confirm_amount.setText(inputAmount.getText().toString().trim());
                    //Submit button acction
                    confirm_submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            System.out.println(getUser().getName());
                            getMember().setUser(getUser().getName().toString());
                            getMoney().setUser(getMember());
                            double amount=Double.parseDouble(confirm_amount.getText().toString().trim());
                            if(getSelectedDeduction().equalsIgnoreCase("House Rent")){
                                getMoney().setHouseRent(amount);
                            }else if (getSelectedDeduction().equalsIgnoreCase("Utility")){
                                getMoney().setUtilities(amount);
                            }else if (getSelectedDeduction().equalsIgnoreCase("Maintenance")){
                                getMoney().setMaintenance(amount);
                            }else if (getSelectedDeduction().equalsIgnoreCase("Servant")){
                                getMoney().setServentBill(amount);
                            }
                            String date=confirm_date.getText().toString();
                            DateFormat format=new SimpleDateFormat("dd/MM/yyyy");
                            if(date!=null && date.length()!=0){
                                try {
                                    Date dateParse = format.parse(date);
                                    format = new SimpleDateFormat("yyyy-MM-dd");
                                    String dateConverted = format.format(dateParse);
                                    dateParse = format.parse(dateConverted);

                                    getMoney().setDate(dateParse);

                                    //getListmoney te new Money add kore dilam
//                                    Calendar calendar=Calendar.getInstance();
//                                    calendar.setTime(dateParse);
//                                    calendar.add(Calendar.DATE,-1);
//                                    Date dateToBeAdded=calendar.getTime();
//                                    getListMoney().add(new Money(getMember(),getHouseR(),getUtilityR(),getServentR(),getMaintenanceR(),dateToBeAdded));
//                                    //------------------------

                                    User_Deduction_Task user_deduction_task=new User_Deduction_Task();
                                    user_deduction_task.execute();
                                    dialog.dismiss();

                                }catch (ParseException e){
                                    Snackbar.make(rootView,"Date not formatted",Snackbar.LENGTH_LONG).show();
                                }
                            }else {
                                Snackbar.make(rootView,"Date field empty",Snackbar.LENGTH_LONG).show();
                            }



                        }
                    });

                    confirm_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }else {
                    Snackbar.make(rootView,"Fill All Fields",Snackbar.LENGTH_LONG).show();
                }
            }
        });


        return rootView;

    }

    public double getHouseR() {
        return houseR;
    }

    public void setHouseR(double houseR) {
        this.houseR = houseR;
    }

    public double getMaintenanceR() {
        return maintenanceR;
    }

    public void setMaintenanceR(double maintenanceR) {
        this.maintenanceR = maintenanceR;
    }

    public double getUtilityR() {
        return utilityR;
    }

    public void setUtilityR(double utilityR) {
        this.utilityR = utilityR;
    }

    public double getServentR() {
        return serventR;
    }

    public void setServentR(double serventR) {
        this.serventR = serventR;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Money getMoney() {
        return money;
    }

    public void setMoney(Money money) {
        this.money = money;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ListMoney getListMoney() {
        return listMoney;
    }

    public void setListMoney(ListMoney listMoney) {
        this.listMoney = listMoney;
    }

    public String getSelectedDeduction() {
        return selectedDeduction;
    }

    public void setSelectedDeduction(String selectedDeduction) {
        this.selectedDeduction = selectedDeduction;
    }

    class CustomAdapter extends ArrayAdapter<String>{

        int hindingItemIndex;

        public CustomAdapter(Context context, int textViewResourceId, String[] objects, int hidingItemIndex) {

            super(context,textViewResourceId,objects);
            this.hindingItemIndex=hidingItemIndex;

        }




        public View getDropDownView(int position, View contentview, ViewGroup paren){
            View v=null;
            if(position==hindingItemIndex){
                TextView tv=new TextView(getContext());
                tv.setVisibility(View.GONE);
                v=tv;
            }else {
                v=super.getDropDownView(position,null,paren);
            }
            return v;
        }





    }

    class User_Deduction_Task extends AsyncTask<Void, Void, Boolean> {
        private ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Saving Deductions");
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


            user_deduction user_deduction=new user_deduction();
            user_deduction.setListMoney(getListMoney());
            user_deduction.setUser(getUser());
            getFragmentManager().beginTransaction().replace(R.id.flContent,user_deduction).commit();
            if(dialog.isShowing())
                dialog.dismiss();
        }


    }
}
