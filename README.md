# toolargetool

A tool for debugging `TransactionTooLargeException` on Android.

## Usage

1. Include `toolargetool` as a dependency, you can remove it again once you've debugged your crash:

       maven { url 'https://dl.bintray.com/guardian/android' } // in project build.gradle
       compile 'com.gu.android:toolargetoollib:0.1.1@aar' // in module build.gradle

2. Add code to start logging during app start, for example in your `Application.onCreate` method:

       TooLargeTool.logEverything(this, "toolargetool", Log.DEBUG);

3. Monitor logcat output to see which components are writing substantial data to the transaction
   buffer and when:

       $ adb logcat -s toolargetool
