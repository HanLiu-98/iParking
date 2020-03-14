package xyz.hanliu.iparking.data;

import java.util.ArrayList;
import java.util.List;

import xyz.hanliu.iparking.app.bean.User;
import xyz.hanliu.iparking.utils.JsonBean;

public class GlobalData {
    /*已经登录的用户数据*/
    public static User user;


    /*省市区三级联动需要的数据*/
    public static List<JsonBean> options1Items = new ArrayList<>();
    public static ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    public static ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();


}
