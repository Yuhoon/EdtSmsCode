package com.wcong.edtsmscode;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements EdtSmsCodeLayout.InputFinishListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((EdtSmsCodeLayout) findViewById(R.id.edt)).setInputFinishListener(this);
    }

    @Override
    public void onInputFinish(String code) {
        Toast.makeText(this, code, Toast.LENGTH_SHORT).show();
    }
}
