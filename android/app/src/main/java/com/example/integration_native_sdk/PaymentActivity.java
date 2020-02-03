package com.example.integration_native_sdk;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.flutter.app.FlutterActivity;
import vn.momo.momo_partner.AppMoMoLib;

public class PaymentActivity extends FlutterActivity {
    private String amount = "10000";
    private String fee = "0";
    int environment = 0;//developer default
    private String merchantName = "CGV Cinemas";
    private String merchantCode = "CGV19072017";
    private String merchantNameLabel = "Nhà cung cấp";
    private String description = "Fast & Furious 8";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle data = getIntent().getExtras();
        if(data != null){
            environment = data.getInt(MoMoConstants.KEY_ENVIRONMENT);
        }
        if(environment == 0){
            AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.DEBUG);
        }else if(environment == 1){
            AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.DEVELOPMENT);
        }else if(environment == 2){
            AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.PRODUCTION);
        }

        requestPayment();
    }

    //example payment
    private void requestPayment() {
        AppMoMoLib.getInstance().setAction(AppMoMoLib.ACTION.PAYMENT);
        AppMoMoLib.getInstance().setActionType(AppMoMoLib.ACTION_TYPE.GET_TOKEN);

        Map<String, Object> eventValue = new HashMap<>();
        //client Required
        eventValue.put("merchantname", merchantName); //Tên đối tác. được đăng ký tại https://business.momo.vn. VD: Google, Apple, Tiki , CGV Cinemas
        eventValue.put("merchantcode", merchantCode); //Mã đối tác, được cung cấp bởi MoMo tại https://business.momo.vn
        eventValue.put("amount", amount); //Kiểu integer
        eventValue.put("orderId", "orderId123456789"); //uniqueue id cho Bill order, giá trị duy nhất cho mỗi đơn hàng
        eventValue.put("orderLabel", "Mã đơn hàng"); //gán nhãn

        //client Optional - bill info
        eventValue.put("merchantnamelabel", "Dịch vụ");//gán nhãn
        eventValue.put("fee", fee); //Kiểu integer
        eventValue.put("description", description); //mô tả đơn hàng - short description

        //client extra data
        eventValue.put("requestId",  merchantCode+"merchant_billId_"+System.currentTimeMillis());
        eventValue.put("partnerCode", merchantCode);
        //Example extra data
        JSONObject objExtraData = new JSONObject();
        try {
            objExtraData.put("site_code", "008");
            objExtraData.put("site_name", "CGV Cresent Mall");
            objExtraData.put("screen_code", 0);
            objExtraData.put("screen_name", "Special");
            objExtraData.put("movie_name", "Kẻ Trộm Mặt Trăng 3");
            objExtraData.put("movie_format", "2D");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        eventValue.put("extraData", objExtraData.toString());

        eventValue.put("extra", "");
        AppMoMoLib.getInstance().requestMoMoCallBack(this, eventValue);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == AppMoMoLib.getInstance().REQUEST_CODE_MOMO && resultCode == -1) {
            if(data != null) {
                if(data.getIntExtra("status", -1) == 0) {
                    if(data.getStringExtra("data") != null && !data.getStringExtra("data").equals("")) {
                        Log.d("debug", "success");
                        String token = data.getStringExtra("data"); //Token response
                        String phoneNumber = data.getStringExtra("phonenumber");
                        String env = data.getStringExtra("env");
                        if(env == null){
                            env = "app";
                        }

                        if(token != null && !token.equals("")) {
                            // TODO: send phoneNumber & token to your server side to process payment with MoMo server
                            // IF Momo topup success, continue to process your order
                        } else {
                            Log.d("debug", "message: " + this.getString(R.string.not_receive_info));
                        }
                    } else {
                        Log.d("debug", this.getString(R.string.not_receive_info));
                    }
                } else if(data.getIntExtra("status", -1) == 1) {
                    String message = data.getStringExtra("message") != null?data.getStringExtra("message"):"Thất bại";
                    Log.d("debug", "message: " + message);
                } else if(data.getIntExtra("status", -1) == 2) {
                    Log.d("debug", "message: " + this.getString(R.string.not_receive_info));
                } else {
                    Log.d("debug", "message: " + this.getString(R.string.not_receive_info));
                }
            } else {
                Log.d("debug", "message: " + this.getString(R.string.not_receive_info));
            }
        } else {
            Log.d("debug", "message: " + this.getString(R.string.not_receive_info_err));
        }
    }
}