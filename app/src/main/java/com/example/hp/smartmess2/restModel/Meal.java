package com.example.hp.smartmess2.restModel;

import java.util.Date;

/**
 * Created by HP on 7/16/2016.
 */
public class Meal {
    private Date date;

    private double breakFast;

    private double lunch;

    private double dinner;

    private String remark1;

    private String remark2;

    private Integer id;



    public Meal() {
    }

    public Meal(Date date, double breakFast, double lunch, double dinner, String remark1, String remark2, Integer id) {
        this.id = id;

        this.date = date;
        this.breakFast = breakFast;
        this.lunch = lunch;
        this.dinner = dinner;
    }



    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getBreakFast() {
        return breakFast;
    }

    public void setBreakFast(double breakFast) {
        this.breakFast = breakFast;
    }

    public double getLunch() {
        return lunch;
    }

    public void setLunch(double lunch) {
        this.lunch = lunch;
    }

    public double getDinner() {
        return dinner;
    }

    public void setDinner(double dinner) {
        this.dinner = dinner;
    }

    public String getRemark1() {
        return remark1;
    }

    public void setRemark1(String remark1) {
        this.remark1 = remark1;
    }

    public String getRemark2() {
        return remark2;
    }

    public void setRemark2(String remark2) {
        this.remark2 = remark2;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


}
