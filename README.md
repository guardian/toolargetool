# toolargetool

A tool for debugging `TransactionTooLargeException` on Android.

## Usage

1. Include `toolargetool` as a dependency, you can remove it again once you've debugged your crash:

       maven { url 'https://dl.bintray.com/guardian/android' } // in project build.gradle
       compile 'com.gu.android:toolargetool:0.1.5@aar' // in module build.gradle

2. Add code to start logging during app start, for example in your `Application.onCreate` method:

       TooLargeTool.startLogging(this);

3. Monitor logcat output to see which components are writing substantial data to the transaction
   buffer and when:

       $ adb logcat -s TooLargeTool

   Example logcat output (TODO: improve this example):

       D/TooLargeTool: MainActivity.onSaveInstanceState wrote: Bundle@200090398 contains 1 keys and measures 0.6 KB when serialized as a Parcel
                                                                               * android:viewHierarchyState = 0.6 KB

## Release process

_Note: these instructions will only work if you have the relevant Bintray credentials_

1. Open `toolargetool/bintray.gradle`.
2. Increase all the version numbers.
3. Make a commit and tag it with git tag -a v<version number> -m "<message>".
4. Run `./gradlew clean install && ./gradlew bintrayUpload`.
