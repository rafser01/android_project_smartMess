package com.example.hp.smartmess2.restModel;

/**
 * Created by HP on 7/20/2016.
 */
public class MealUpdate {
    int id;
    double breakFast;
    double lunch;
    double dinner;

    public MealUpdate(int id, double breakFast, double lunch, double dinner) {
        this.id = id;
        this.breakFast = breakFast;
        this.lunch = lunch;
        this.dinner = dinner;
    }

    public MealUpdate() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
