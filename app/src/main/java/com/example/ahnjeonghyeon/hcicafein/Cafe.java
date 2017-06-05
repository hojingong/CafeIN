package com.example.ahnjeonghyeon.hcicafein;

/**
 * Created by AhnJeongHyeon on 2017. 6. 4..
 */

public class Cafe {
    private String name;
    private String address;
    private String phone;

    public String getName(){
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
