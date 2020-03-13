package xyz.hanliu.iparking.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.hanliu.iparking.R;
import xyz.hanliu.iparking.configuration.Config;
import xyz.hanliu.iparking.data.GlobalData;
import xyz.hanliu.iparking.app.bean.User;

public class LoginActivty extends AppCompatActivity {

    @BindView(R.id.input_mobile)
    EditText inputMobile;
    @BindView(R.id.input_password)
    EditText inputPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.remember_pwd)
    CheckBox cb_rememberpwd;

    private SharedPreferences pref;                 //  取值对象
    private SharedPreferences.Editor editor;        //  存值对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载布局
        setContentView(R.layout.activity_login);
        //绑定控件
        ButterKnife.bind(this);
        rememberPwd();


    }

    /**
     * 利用SharedPreferences存储，实现记住账号密码功能
     */
    void rememberPwd() {
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        //进入是否记录密码键的结果中查询，勾选的话会返回true,否则返回false
        boolean isRemember = pref.getBoolean("remember_password", false);
        //如果用户上次登录点击了记住账号密码按钮
        if (isRemember) {
            //将账号和密码都设置到文本框中
            String mobile = pref.getString("mobile", "");
            String password = pref.getString("password", "");
            inputMobile.setText(mobile);
            inputPassword.setText(password);
            cb_rememberpwd.setChecked(true);  // 设置勾选
        }
    }


    /*
     *“注册用户”文本框的单击相应事件
     */
    public void clickview_register(View button) {
        Intent intent = new Intent(LoginActivty.this, RegisterActivity.class);
        startActivity(intent);
    }

    /**
     * “忘记密码”文本框的单击相应事件
     */
    public void clickview_resetpwd(View button) {
        Intent intent = new Intent(LoginActivty.this, ResetpwdActivity.class);
        startActivity(intent);
    }

    /**
     * 登录按钮的响应事件
     */
    public void start_login(View button) {
        //如果内容不合法，则直接返回。
        if (!isValid()) {
            onLoginFailed("输入手机号和密码后才能进行登录操作");
            return;
        }
        //输入合法，将登录按钮置为不可用，显示环形进度对话框
        btnLogin.setEnabled(false);
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivty.this);
        //进度条采用不明确显示进度的模糊模式
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("登录中...");
        progressDialog.show();
        //获取用户输入放在Map中作为请求参数
        String mobile = inputMobile.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();
        HashMap<String, String> map = new HashMap<>();
        map.put("mobile", mobile);
        map.put("password", password);
        //进行网络请求
        OkGo.<String>post(Config.URL_LOGIN)
                .params(map)
                .execute(new StringCallback() {//派生子线程执行耗时的网络操作，回调接口
                    @Override   //网络请求成功的回调函数
                    public void onSuccess(Response<String> response) {
                        //获取服务器响应的JSON字符串
                        String json = response.body();
                        //利用GSON库,将字符串转换成User对象
                        Gson gson = new Gson();
                        User user = gson.fromJson(json, User.class);
                        if (user != null) {
                            //在服务器上查询到用户信息
                            editor = pref.edit();
                            if (cb_rememberpwd.isChecked()) {
                                //复选框如果被选中，存储手机号和密码信息
                                editor.putBoolean("remember_password", true);
                                editor.putString("mobile", mobile);
                                editor.putString("password", password);
                            } else {
                                editor.clear();
                            }
                            editor.apply();
                            //跳转到APP主页面
                            onLoginSuccess(user);
                            Toast.makeText(LoginActivty.this, "欢迎你，" + user.getNickname() + "！", Toast.LENGTH_SHORT).show();

                        } else {
                            progressDialog.dismiss();
                            onLoginFailed("用户名或密码错误！");
                        }
                    }

                    @Override   //网络请求成功的回调函数
                    public void onError(Response<String> response) {
                        super.onError(response);
                        String message = response.getException().getMessage();
                        //利用Toast显示错误信息
                        Toast.makeText(LoginActivty.this, "" + message, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 判断输入的用户名和密码是否合法
     */
    public boolean isValid() {
        boolean valid = true;
        String mobile = inputMobile.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();
        if (mobile.isEmpty()) {
            inputMobile.setError("请输入你的账号");
            valid = false;
        } else {
            inputMobile.setError(null);
        }
        if (password.isEmpty()) {
            inputPassword.setError("请输入你的密码");
            valid = false;
        } else {
            inputPassword.setError(null);
        }
        return valid;
    }

    /**
     * 登录成功
     */
    public void onLoginSuccess(User user) {
        btnLogin.setEnabled(true);
        GlobalData.user = user;
        Intent intent = new Intent(LoginActivty.this, MainActivity.class);
        intent.putExtra("USER", user);
        startActivity(intent);
        finish();
    }

    /**
     * 登录失败
     */
    public void onLoginFailed(String failmsg) {
        Toast.makeText(LoginActivty.this, failmsg, Toast.LENGTH_LONG).show();
        btnLogin.setEnabled(true);
    }
}
