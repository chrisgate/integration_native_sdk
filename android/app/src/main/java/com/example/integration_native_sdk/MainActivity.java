package com.example.integration_native_sdk;

import android.content.Intent;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugins.GeneratedPluginRegistrant;
import vn.momo.momo_partner.AppMoMoLib;
import vn.momo.momo_partner.MoMoParameterNamePayment;

public class MainActivity extends FlutterActivity {

    private static final String CHANNEL = "com.example.integration_native_sdk_demo/native";
    private static final String KEY_MOMO_NATIVE = "requestPaymentMOMO";
    private MethodChannel.Result momoResult;

    @Override
    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        GeneratedPluginRegistrant.registerWith(flutterEngine);

        new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL)
                .setMethodCallHandler(new NativePlugin());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        super.onActivityResult(requestCode, resultCode, result);
        if (requestCode == AppMoMoLib.getInstance().REQUEST_CODE_MOMO && resultCode == -1) {
            if (result != null) {
                Map<String, Object> resultMap = new HashMap<>();
                resultMap.put("data", result.getStringExtra("data"));
                resultMap.put("status", "" + result.getIntExtra("status", -1));
                resultMap.put("message", result.getStringExtra("message"));
                resultMap.put("phonenumber", result.getStringExtra("phonenumber"));
                momoResult.success(resultMap);
            } else {
                momoResult.success(null);
            }
        } else {
            momoResult.success(null);
        }
    }

    public class NativePlugin implements MethodChannel.MethodCallHandler {
        @Override
        public void onMethodCall(MethodCall call, @NonNull MethodChannel.Result result) {
            switch (call.method) {
                case KEY_MOMO_NATIVE:
                    requestMomoPayment(call, result);
                    break;
                case "":
                    break;
                default:
                    result.notImplemented();
            }
        }
    }

    private void requestMomoPayment(MethodCall call, MethodChannel.Result result) {
        AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.DEVELOPMENT);

        String merchantName = call.argument("merchantname");
        String merchantCode = call.argument("merchantcode");
        String merchantNameLabel = call.argument("merchantnamelabel");
        String partnerCode = call.argument("partner_code");
        Integer amount = call.<Integer>argument("amount");
        String description = call.argument("description");
        String orderId = call.argument("orderId");

        this.momoResult = result;

        requestPayment(
                merchantCode, merchantName, merchantNameLabel, partnerCode, description, Objects.requireNonNull(amount), orderId);
    }

    private void requestPayment(String merchantCode, String merchantName, String merchantNameLabel, String partnerCode, String description, @NonNull Integer amount, String orderId) {
        AppMoMoLib.getInstance().setAction(AppMoMoLib.ACTION.PAYMENT);
        AppMoMoLib.getInstance().setActionType(AppMoMoLib.ACTION_TYPE.GET_TOKEN);

        Map<String, Object> eventValue = new HashMap<>();

        //client Required

        eventValue.put(MoMoParameterNamePayment.MERCHANT_NAME, merchantName);
        eventValue.put(MoMoParameterNamePayment.MERCHANT_CODE, merchantCode);
        eventValue.put(MoMoParameterNamePayment.PARTNER_CODE, partnerCode);
        eventValue.put(MoMoParameterNamePayment.AMOUNT, amount.toString());
        eventValue.put(MoMoParameterNamePayment.DESCRIPTION, description);

        //client Optional
        eventValue.put(MoMoParameterNamePayment.MERCHANT_NAME_LABEL, merchantNameLabel);
        eventValue.put(MoMoParameterNamePayment.REQUEST_ID, orderId);
        eventValue.put(MoMoParameterNamePayment.REQUEST_TYPE, "payment");
        eventValue.put(MoMoParameterNamePayment.LANGUAGE, "vi");
        eventValue.put(MoMoParameterNamePayment.EXTRA, "");

        //Request momo app
        AppMoMoLib.getInstance().requestMoMoCallBack(this, eventValue);
    }
}
