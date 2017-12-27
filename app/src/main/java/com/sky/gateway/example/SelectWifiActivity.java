package com.sky.gateway.example;

import android.app.Activity;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.sky.lib.gateway.GatewayInterface;
import com.sky.lib.gateway.exception.ConfigWifiException;

import java.util.ArrayList;

/**
 * 作者：Sky on 2017/12/19.
 * 用途：//TODO
 */

public class SelectWifiActivity extends Activity{

    private TextView txtTitle;
    private RecyclerView recyclerView;
    private SelectWifiAdapter selectWifiAdapter;

    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectwifi);
        txtTitle = (TextView) findViewById(R.id.txt_title);
        recyclerView = (RecyclerView) findViewById(R.id.recycleview);

        type = getIntent().getIntExtra("type",1);
        if(type == 1){
            txtTitle.setText("请选择网关热点");
        }else{
            txtTitle.setText("请选择网关要连接的WIFI");
        }


        intiView();

        initData();
    }

    private void intiView(){
        selectWifiAdapter = new SelectWifiAdapter(this,R.layout.item_wifi_scan,null);
        LinearLayoutManager lm = new LinearLayoutManager(this, 1, false);
        recyclerView.setLayoutManager(lm);
        recyclerView.setAdapter(selectWifiAdapter);
        selectWifiAdapter.setItemClickListener(new SelectWifiAdapter.ItemClickListener() {
            @Override
            public void onClick(ScanResult scanResult) {
                toResult(scanResult);
            }
        });
    }

    private void initData(){
        try {
            ArrayList<ScanResult> scanResults = GatewayInterface.getWifiList(type);
            selectWifiAdapter.updateDatas(scanResults);
        } catch (ConfigWifiException e) {
            e.printStackTrace();
        }
    }

    private void toResult(ScanResult scanResult){
        Intent resultIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable("scanResult", scanResult);
        resultIntent.putExtras(bundle);
        setResult(type, resultIntent);
        finish();
    }

}
