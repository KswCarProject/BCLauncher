package com.touchus.publicutils.utils;

import java.util.ArrayList;
import java.util.List;

public class VcardUnit {
    private int flag;
    private String name;
    private List<String> numbers = new ArrayList();
    private String remark;

    public void setName(String name2) {
        if (name2 != null) {
            this.name = name2;
        }
    }

    public void appendNumber(String number) {
        if (number != null) {
            this.numbers.add(number);
        }
    }

    public boolean isvalid() {
        if (this.name == null || this.numbers.size() <= 0) {
            return false;
        }
        return true;
    }

    public List<String> getNumbers() {
        return this.numbers;
    }

    public void setNumbers(List<String> numbers2) {
        this.numbers = numbers2;
    }

    public String getName() {
        return this.name;
    }

    public int getFlag() {
        return this.flag;
    }

    public void setFlag(int flag2) {
        this.flag = flag2;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark2) {
        this.remark = remark2;
    }

    public String toString() {
        return "VcardUnit [name=" + this.name + ", flag=" + this.flag + ", remark=" + this.remark + ", numbers=" + this.numbers + "]";
    }
}
