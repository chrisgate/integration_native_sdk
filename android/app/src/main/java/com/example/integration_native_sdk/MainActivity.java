package com.example.integration_native_sdk;

import android.content.Intent;

import androidx.annotation.NonNull;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugins.GeneratedPluginRegistrant;

public class MainActivity extends FlutterActivity {

    private static final String SURVEY_MONKEY_CHANNEL = "com.example.integration_native_sdk_demo/surveyMonkey";
    private MethodChannel.Result surveyMonkeyResult;
    private static final int REQUEST_CODE = 120;

    private static final String MOMO_CHANNEL = "com.example.integration_native_sdk_demo/momo";
    private MethodChannel.Result momoResult;

    @Override
    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        GeneratedPluginRegistrant.registerWith(flutterEngine);

        new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), MOMO_CHANNEL)
                .setMethodCallHandler(
                        (call, result) -> {
                            if (call.method.equals("momo")) {
                                startMomoActivity(result);
                            }
                        }
                );
    }

    private void startMomoActivity(MethodChannel.Result result) {
        Intent intent = new Intent(this, PaymentActivity.class);
        this.momoResult = result;
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            momoResult.success("success");
        }
    }
}
