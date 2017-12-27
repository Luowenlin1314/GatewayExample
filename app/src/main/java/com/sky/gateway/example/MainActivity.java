package com.sky.gateway.example;

import android.content.Intent;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sky.lib.gateway.GatewayInterface;
import com.sky.lib.gateway.bean.VO.ConfigWifiVO;
import com.sky.lib.gateway.exception.ConfigWifiException;
import com.sky.lib.gateway.listener.ProListener;

public class MainActivity extends AppCompatActivity {

    private TextView txtGateway;
    private TextView txtWifi;
    private EditText edtWifiPassword;
    private TextView txtResult;

    private LinearLayout llBefore;
    private RelativeLayout rlLoading;

    private ScanResult gatewayScanResult;
    private ScanResult wifiScanResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtGateway = (TextView) findViewById(R.id.txt_gateway);
        txtWifi = (TextView) findViewById(R.id.txt_wifi);
        edtWifiPassword = (EditText) findViewById(R.id.edt_password);
        txtResult = (TextView) findViewById(R.id.txt_result);
        llBefore = (LinearLayout) findViewById(R.id.ll_before);
        rlLoading = (RelativeLayout) findViewById(R.id.rl_loading);

        llBefore.setVisibility(View.VISIBLE);
        rlLoading.setVisibility(View.GONE);

        txtGatewayClick();
        txtWifi();
    }


    private void txtGatewayClick(){
        txtGateway.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toSelect = new Intent(MainActivity.this,SelectWifiActivity.class);
                toSelect.putExtra("type",1);
                startActivityForResult(toSelect,1);
            }
        });
    }

    private void txtWifi(){
        txtWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toSelect = new Intent(MainActivity.this,SelectWifiActivity.class);
                toSelect.putExtra("type",2);
                startActivityForResult(toSelect,2);
            }
        });
    }

    public void config(View v){
        llBefore.setVisibility(View.GONE);
        rlLoading.setVisibility(View.VISIBLE);

        if(gatewayScanResult == null ||
                wifiScanResult == null ||
                TextUtils.isEmpty(edtWifiPassword.getText().toString())){
            Toast.makeText(this,"不能为空",Toast.LENGTH_LONG).show();
        }
        try {
            ConfigWifiVO configWifiVO = new ConfigWifiVO();
            configWifiVO.setGatewayScanResult(gatewayScanResult);
            configWifiVO.setConfigWifiScanResult(wifiScanResult);
            configWifiVO.setConfigWifiPassword(edtWifiPassword.getText().toString());
            configWifiVO.setYunHost("218.17.29.6"); //测试环境
            GatewayInterface.configGatewayWifi(configWifiVO, new ProListener() {
                @Override
                public void onSuccess(Object o) {
                    Log.e("ZM","configGatewayWifi onSuccess : " + o.toString());
                    llBefore.setVisibility(View.VISIBLE);
                    rlLoading.setVisibility(View.GONE);

                    txtResult.setText(o.toString());
                    txtResult.setTextColor(Color.BLACK);
                }

                @Override
                public void onFail(String s) {
                    Log.e("ZM","configGatewayWifi onFail : " + s);
                    llBefore.setVisibility(View.VISIBLE);
                    rlLoading.setVisibility(View.GONE);
                    txtResult.setText(s);
                    txtResult.setTextColor(Color.RED);
                }
            });
        } catch (ConfigWifiException e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null == data) {
            return;
        }
        Bundle bundle = data.getExtras();
        if (bundle == null) {
            return;
        }
        //网关热点
        if (resultCode == 1) {
            gatewayScanResult = bundle.getParcelable("scanResult");
            txtGateway.setText(gatewayScanResult.SSID);
            //网关要连接的wifi
        } else if(resultCode == 2){
            wifiScanResult = bundle.getParcelable("scanResult");
            txtWifi.setText(wifiScanResult.SSID);
        }
    }
}
