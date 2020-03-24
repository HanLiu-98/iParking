package xyz.hanliu.iparking.configuration;

public class Config {
    /*家里的IP*/
    public static final String IP = "192.168.1.104:8080";
    /*店里的IP*/
//    public static final String IP = "192.168.3.12:8080";




    /*服务器上的项目地址*/
    public static final String HOST = "http://" + IP + "/iParking-Server/";
    /*请求登录的地址*/
    public static final String URL_LOGIN = HOST + "LoginServlet";
    public static final String URL_REGISTER = HOST + "RegisterServlet";
    public static final String URL_RESETPWD = HOST + "ResetpwdServlet";
    public static final String URL_UPLOAD = HOST + "UploadSpaceServlet";
    public static final String URL_GETDETAIL = HOST + "GetSpaceDetailServlet";
    public static final String URL_GETSPACELIST = HOST + "GetSpaceListServlet";
    public static final String URL_GETUSERDATA = HOST + "GetUserDataServlet";

    /*提交订单 2020-03-18 */
    public static final String URL_SUBMITORDER = HOST + "SubmitOrderServlet";

    /*支付操作 2020-03-19 */
    public static final String URL_PAYORDER = HOST + "PayOrderServlet";

    /*获取待支付订单列表 2020-03-20*/
    public static final String URL_GETORDERS = HOST + "GetOrdersServlet";

    /*获取我发布的车位列表 2020-03-21*/
    public static final String URL_GETMYRELEASE=HOST+"GetMyReleaseServlet";

    /*删除车位 2020-03-21*/
    public static final String URL_DELETESPACE=HOST+"DeleteSpaceServlet";

    /*修改价格 2020-03-21*/
    public static final String URL_MODIFYPRICE=HOST+"ModifyPriceServlet";



    /*图片服务器地址*/
    public static final String IMAGE_HOST = "http://" + IP + "/image/";


}
