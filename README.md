# toolargetool

A tool for debugging `TransactionTooLargeException` on Android.

> "Most underrated solution." - [Kedar Paranjape, Jun 7 '18 at 14:26](https://stackoverflow.com/questions/11451393/what-to-do-on-transactiontoolargeexception/43193425#comment88495126_50162810)

## Usage

1. Include `toolargetool` as a dependency (you can remove it again once you've debugged your crash):

    - `toolargetool` is available from `mavenCentral()`

    - Add `implementation 'com.gu.android:toolargetool:0.3.0'` in your module's `build.gradle`:
    
          dependencies {
              ...
              implementation 'com.gu.android:toolargetool:0.3.0'
          }
          
2. Import The package

       import com.gu.toolargetool.TooLargeTool;

3. Add code to start logging during app start, for example in your `Application.onCreate` method:

       TooLargeTool.startLogging(this);

4. Monitor logcat output to see which components are writing substantial data to the transaction
   buffer and when:

       $ adb logcat -s TooLargeTool

   Example logcat output (TODO: improve this example):

       D/TooLargeTool: MainActivity.onSaveInstanceState wrote: Bundle@200090398 contains 1 keys and measures 0.6 KB when serialized as a Parcel
                                                                               * android:viewHierarchyState = 0.6 KB

## Release process

_Note: these instructions will only work if you have the required credentials for publishing to the `com.gu` Sonatype repository._

1. Increase all the version number in `toolargetool/build.gradle`
2. Make a commit and tag it with `git tag -a v<version number> -m "<message>"`.
3. Run `./gradlew publishReleasePublicationToSnapshotRepository`.
