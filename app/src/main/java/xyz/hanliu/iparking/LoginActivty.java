package xyz.hanliu.iparking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LoginActivty extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void start_register(View button)
    {
        Intent intent=new Intent(this,RegisterActivity.class);

        startActivity(intent);

    }

    public void start_resetpwd(View button)
    {
        Intent intent=new Intent(this,ResetpwdActivity.class);

        startActivity(intent);

    }
}
