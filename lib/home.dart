import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class HomePage extends StatelessWidget {
  static const platform =
      const MethodChannel('com.example.integration_native_sdk_demo/native');
  static const KEY_MOMO_NATIVE = "requestPaymentMOMO";

  var momoOrderDetail = {
    "merchantname": "CGV Cinema",
    "merchantcode": "CGV19072017",
    "merchantnamelabel": "Nhà cung cấp",
    "partner_code": "CGV19072017",
    "amount": 10000,
    "description": "Thanh toán combo 100",
    "orderId": "orderId123456789",
  };

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(""),
      ),
      body: Column(
        children: <Widget>[
          Center(
            child: RaisedButton(
              onPressed: _loadMomo,
              child: Text("Load Momo"),
            ),
          ),
        ],
      ),
    );
  }

  Future _loadMomo() async {
    try {
      final result = await platform.invokeMethod(
        KEY_MOMO_NATIVE,
        momoOrderDetail,
      );
      print('result: $result');
    } on PlatformException catch (e) {
      print(e.message);
    }
  }
}
