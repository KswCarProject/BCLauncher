package com.touchus.publicutils.bean;

public class Person {
    private int flag;
    private String id;
    private String name;
    private String phone;
    private String remark;

    public Person() {
    }

    public Person(String id2, String name2, String phone2, int flag2, String remark2) {
        this.id = id2;
        this.name = name2;
        this.phone = phone2;
        this.flag = flag2;
        this.remark = remark2;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id2) {
        this.id = id2;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone2) {
        this.phone = phone2;
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
        return "Person [id=" + this.id + ", name=" + this.name + ", phone=" + this.phone + ", flag=" + this.flag + ", remark=" + this.remark + "]";
    }
}
