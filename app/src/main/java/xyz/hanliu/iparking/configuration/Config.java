package xyz.hanliu.iparking.configuration;

public class Config {
    public static final String IP = "192.168.1.104:8080";

    /*服务器上的项目地址*/
    public static final String HOST = "http://" + IP + "/iParking-Server/";
    /*请求登录的地址*/
    public static final String URL_LOGIN = HOST + "LoginServlet";
    public static final String URL_REGISTER = HOST + "RegisterServlet";
    public static final String URL_RESETPWD = HOST + "ResetpwdServlet";
    public static final String URL_UPLOAD = HOST + "UploadSpaceServlet";
    public static final String URL_GETDETAIL = HOST + "GetSpaceDetailServlet";
    public static final String URL_GETSPACELIST = HOST + "GetSpaceListServlet";

    /*图片服务器地址*/
    public static final String IMAGE_HOST = "http://" + IP + "/image/";


}
