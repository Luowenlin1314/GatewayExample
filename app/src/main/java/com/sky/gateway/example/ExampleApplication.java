package com.sky.gateway.example;

import android.app.Application;

import com.sky.lib.gateway.GatewayInterface;
import com.sky.lib.gateway.exception.ConfigWifiException;


/**
 * 作者：Sky on 2017/12/19
 * 用途：//TODO
 */

public class ExampleApplication extends Application {

    @Override
    public void onCreate() {
        try {
            GatewayInterface.gatewayInit(getApplicationContext());
        } catch (ConfigWifiException e) {
            e.printStackTrace();
        }
    }

}
