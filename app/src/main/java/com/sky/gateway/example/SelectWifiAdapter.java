package com.sky.gateway.example;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.View;
import android.widget.TextView;

import java.util.List;

/**
 * 作者：Sky on 2017/12/19
 * 用途：//TODO
 */

public class SelectWifiAdapter extends BaseRecyclerAdapter<ScanResult>{


    public SelectWifiAdapter(Context context, int layoutId, List<ScanResult> data) {
        super(context, layoutId, data);
    }

    @Override
    public void convert(BaseRecyclerViewHolder holder, final ScanResult scanResult, final int position) {
        TextView textView = holder.getTextView(R.id.txt_wifi_ssid);
        textView.setText(scanResult.SSID);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClickListener != null){
                    itemClickListener.onClick(scanResult);
                }
            }
        });
    }

    private ItemClickListener itemClickListener;
    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }


    interface ItemClickListener{
        void onClick(ScanResult scanResult);
    }

}
