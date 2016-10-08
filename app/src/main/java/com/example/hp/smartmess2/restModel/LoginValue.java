package com.example.hp.smartmess2.restModel;

/**
 * Created by HP on 7/14/2016.
 */
public class LoginValue {

    boolean userCheck;
    boolean emailCheck;
    boolean result;

    public LoginValue() {
    }

    public LoginValue(boolean userCheck, boolean emailCheck, boolean result) {
        this.userCheck = userCheck;
        this.emailCheck = emailCheck;
        this.result = result;
    }

    public boolean isUserCheck() {
        return userCheck;
    }

    public void setUserCheck(boolean userCheck) {
        this.userCheck = userCheck;
    }

    public boolean isEmailCheck() {
        return emailCheck;
    }

    public void setEmailCheck(boolean emailCheck) {
        this.emailCheck = emailCheck;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
