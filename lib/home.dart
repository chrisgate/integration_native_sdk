import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class HomePage extends StatelessWidget {
  static const surveyMonkeyPlatform = const MethodChannel(
      'com.example.integration_native_sdk_demo/surveyMonkey');
  static String sessionSurveyMonkeyHash = 'Z9PZRB7';

  static const momoPlatform =
      const MethodChannel('com.example.integration_native_sdk_demo/momo');

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
              onPressed: _loadSurveyMonkey,
              child: Text("Load SurveyMonkey"),
            ),
          ),
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
      final result = await momoPlatform.invokeMethod('momo');
      print('result: $result');
    } on PlatformException catch (e) {
      print(e.message);
    }
  }

  Future _loadSurveyMonkey() async {
    try {
      final result = await surveyMonkeyPlatform.invokeMethod(
        'surveyMonkey',
        {"sessionSurveyMonkeyHash": sessionSurveyMonkeyHash},
      );
      print('_loadSurveyMonkey result: $result');
    } on PlatformException catch (e) {
      print(e.runtimeType);
    }
  }
}
