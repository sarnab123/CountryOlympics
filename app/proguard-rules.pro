# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/global/Downloads/adt-bundle-mac-x86_64-20140702/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:
-dontwarn org.simpleframework.**
-dontwarn com.bea.**


-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Application classes that will be serialized/deserialized over Gson
-keep class com.olympics.olympicsandroid.model.** { *; }

-keep class sun.misc.Unsafe { *; }

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

keep all classes that might be used in XML layouts
-keep public class * extends android.view.View
-keep public class * extends android.app.Fragment
-keep public class * extends android.support.v4.Fragment

-keepattributes *Annotation*, EnclosingMethod

-keep class com.winwin.** { *; }
-keepnames class org.codehaus.jackson.** { *; }


-dontoptimize
-dontshrink

keep all public and protected methods that could be used by java reflection
-keepclassmembernames class * {
 public protected <methods>;
}

-keepclasseswithmembernames class * {
native <methods>;
}

-keepclasseswithmembernames class * {
 public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembernames class * {
  public <init>(android.content.Context, android.util.AttributeSet, int);
}


-keepclassmembers enum * {
  public static **[] values();
  public static ** valueOf(java.lang.String);
 }

-keep class * implements android.os.Parcelable {
public static final android.os.Parcelable$Creator *;
}

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
