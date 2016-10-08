package com.example.hp.smartmess2.service;

import com.example.hp.smartmess2.restModel.ListMoney;
import com.example.hp.smartmess2.restModel.Money;

/**
 * Created by HP on 7/31/2016.
 */
public class ServiceClass {

    public String getHouseRent(ListMoney listMoney){
        double houseRent=0.0;
        for(Money m:listMoney){
            houseRent+=m.getHouseRent();
        }
        String houseR=houseRent+"";
        return  houseR;
    }
}
