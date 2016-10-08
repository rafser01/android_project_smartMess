package com.example.hp.smartmess2.restModel;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by HP on 7/25/2016.
 */
public class Money implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer idAuto;
    private Double deposit;
    private Double houseRent;
    private Double utilities;
    private Double serventBill;
    private Double foodCost;
    private Double maintenance;
    private Date date;
    private Double bazarCost;
    private String remark1;
    private String remark2;
    private Member user;
    public Money() {
    }

    public Money(Integer idAuto) {
        this.idAuto = idAuto;
    }

    public Money(Member user, Date date, Double deposit) {
        this.user = user;
        this.date = date;
        this.deposit = deposit;
    }

    public Money(Member user, Double houseRent, Double utilities, Double serventBill, Double maintenance, Date date) {
        this.user = user;
        this.houseRent = houseRent;
        this.utilities = utilities;
        this.serventBill = serventBill;
        this.maintenance = maintenance;
        this.date=date;
    }

    public Money(Integer idAuto, Date date) {
        this.idAuto = idAuto;
        this.date = date;
    }

    public Integer getIdAuto() {
        return idAuto;
    }

    public void setIdAuto(Integer idAuto) {
        this.idAuto = idAuto;
    }

    public Double getDeposit() {
        return deposit;
    }

    public void setDeposit(Double deposit) {
        this.deposit = deposit;
    }

    public Double getHouseRent() {
        return houseRent;
    }

    public void setHouseRent(Double houseRent) {
        this.houseRent = houseRent;
    }

    public Double getUtilities() {
        return utilities;
    }

    public void setUtilities(Double utilities) {
        this.utilities = utilities;
    }

    public Double getServentBill() {
        return serventBill;
    }

    public void setServentBill(Double serventBill) {
        this.serventBill = serventBill;
    }

    public Double getFoodCost() {
        return foodCost;
    }

    public void setFoodCost(Double foodCost) {
        this.foodCost = foodCost;
    }

    public Double getMaintenance() {
        return maintenance;
    }

    public void setMaintenance(Double maintenance) {
        this.maintenance = maintenance;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getBazarCost() {
        return bazarCost;
    }

    public void setBazarCost(Double bazarCost) {
        this.bazarCost = bazarCost;
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

    public Member getUser() {
        return user;
    }

    public void setUser(Member user) {
        this.user = user;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAuto != null ? idAuto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Money)) {
            return false    ;
        }
        Money other = (Money) object;
        if ((this.idAuto == null && other.idAuto != null) || (this.idAuto != null && !this.idAuto.equals(other.idAuto))) {
            return false;
        }
        return true;
    }

}
