package xyz.hanliu.iparking.app.bean;

import java.io.Serializable;

public class User implements Serializable {
    private String mobile;
    private String nickname;
    private String fullname;
    private String password;

    //后来增加的余额字段
    private float balance;

    /*
     *getter方法
     */
    public String getMobile() {
        return mobile;
    }

    public String getNickname() {
        return nickname;
    }

    public String getFullname() {
        return fullname;
    }

    public String getPassword() {
        return password;
    }

    public float getBalance() {
        return balance;
    }

    /*
     *setter方法
     */

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "User{" +
                "mobile='" + mobile + '\'' +
                ", nickname='" + nickname + '\'' +
                ", fullname='" + fullname + '\'' +
                ", password='" + password + '\'' +
                ", balance=" + balance +
                '}';
    }
}
