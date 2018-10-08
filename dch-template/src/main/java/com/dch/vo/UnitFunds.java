package com.dch.vo;

/**
 * Created by sunkqa on 2018/5/22.
 */
public class UnitFunds {
    private String unit;
    private Double funds;

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Double getFunds() {
        return funds;
    }

    public void setFunds(Double funds) {
        this.funds = funds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UnitFunds unitFunds = (UnitFunds) o;

        if (!unit.equals(unitFunds.unit)) return false;
        return funds.equals(unitFunds.funds);
    }

    @Override
    public int hashCode() {
        int result = unit.hashCode();
        result = 31 * result + funds.hashCode();
        return result;
    }
}
