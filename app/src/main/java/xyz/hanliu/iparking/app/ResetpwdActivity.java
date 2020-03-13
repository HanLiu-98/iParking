package xyz.hanliu.iparking.app;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.hanliu.iparking.R;
import xyz.hanliu.iparking.configuration.Config;

public class ResetpwdActivity extends AppCompatActivity {

    @BindView(R.id.input_mobile_resetpwd)
    EditText inputMobile;
    @BindView(R.id.input_password_resetpwd)
    EditText inputPassword;
    @BindView(R.id.input_code_resetpwd)
    EditText inputCode;
    @BindView(R.id.input_fullname_resetpwd)
    EditText inputFullname;
    @BindView(R.id.btn_resetpwd)
    Button btnResetpwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpwd);
        ButterKnife.bind(this);
    }


    /**
     * 点击重置密码按钮的响应事件
     */
    public void start_resetpwd(View button) {
        //如果内容不合法，则直接返回。
        if (!isValid()) {
            onResetpwdFail("信息填完整才能进行密码重置操作！");
            return;
        }
        //输入合法，将注册按钮置为不可用，显示环形进度对话框
        btnResetpwd.setEnabled(false);
        final ProgressDialog progressDialog = new ProgressDialog(ResetpwdActivity.this);
        //进度条采用不明确显示进度的模糊模式
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("正在进行密码重置...");
        progressDialog.show();
        //获取用户输入放在Map中作为请求参数
        String mobile = inputMobile.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();
        String code = inputCode.getText().toString().trim();
        String fullname = inputFullname.getText().toString().trim();
        HashMap<String, String> map = new HashMap<>();
        map.put("mobile", mobile);
        map.put("password", password);
        map.put("fullname", fullname);
//        map.put("nickname",null);

        //进行网络请求
        OkGo.<String>post(Config.URL_RESETPWD)
                .params(map)
                .execute(new StringCallback() {//派生子线程执行耗时的网络操作，回调接口
                    @Override   //网络请求成功的回调函数
                    public void onSuccess(Response<String> response) {
                        //获取服务器响应的字符串
                        String str = response.body();
                        if (str.equals("success")) {
                            //注册成功
                            progressDialog.dismiss();
                            onResetpwdSuccess();
                        } else {
                            progressDialog.dismiss();
                            onResetpwdFail("该手机号码已经注册过了！");
                        }
                    }

                    @Override   //网络请求失败的回调函数
                    public void onError(Response<String> response) {
                        super.onError(response);
                        String message = response.getException().getMessage();
                        //利用Toast显示错误信息
                        Toast.makeText(ResetpwdActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                    }
                });

    }

    /**
     * 判断注册时输入的信息是否合法
     */
    public boolean isValid() {
        boolean valid = true;
        String mobile = inputMobile.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();
        String code = inputCode.getText().toString().trim();
        String fullname = inputFullname.getText().toString().trim();

        if (mobile.isEmpty()) {
            inputMobile.setError("请输入你的手机号");
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
        if (code.isEmpty()) {
            inputCode.setError("请输入验证码");
            valid = false;
        } else {
            inputCode.setError(null);
        }
        if (fullname.isEmpty()) {
            inputFullname.setError("请输入你真实姓名");
            valid = false;
        } else {
            inputFullname.setError(null);
        }
        return valid;
    }

    /**
     * 注册失败
     */
    public void onResetpwdFail(String failmsg) {
        Toast.makeText(ResetpwdActivity.this, failmsg, Toast.LENGTH_LONG).show();
        btnResetpwd.setEnabled(true);
    }

    /**
     * 注册成功
     */
    public void onResetpwdSuccess() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this).
                setTitle("提示").
                setIcon(R.drawable.ic_sentiment_very_satisfied_black_24dp).
                setMessage("恭喜你，密码重置成功！");
        builder.setPositiveButton("去登录", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.show();
    }
}
