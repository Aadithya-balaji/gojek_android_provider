**Gox Installation Document**

Gox is a multi module project, need to configure two gradle files app gradle and base gradle

**Step 1:**
In app.gradle
`applicationId "com.gox.app"`

**Step2:**
Configure Project in Google Developer Console and Facebook Developer console
https://console.developers.google.com
https://developers.facebook.com

**Step3:**
In base.gradle

```groovy
resValue("string", "app_name", "GoX")
resValue("string", "facebook_app_id", "394261008038577")
resValue("string", "facebook_accountkit_id", "86a614c0d50f8e010c15b3b92a26dfb4")
resValue("string", "fb_login_protocol_scheme", "fb394261008038577")
resValue("string", "google_map_key", "AIzaSyBAgs4bPWgrbGgE3nrX_kjNAn0vDI_MEOY")
resValue("string", "google_signin_server_client_id", "708455232400-n8p5omqpqoi0f2j4ncbfhmk21vntl8ct.apps.googleusercontent.com")
resValue "string", "default_notification_channel_id", "fcm_default_channel"

buildConfigField "boolean", "ENABLE_TIMER_CALL", "true"
buildConfigField "String", "BASE_URL", '"https://api.gox.services/"'
buildConfigField("String", "SALT_KEY", '"MQ=="')

```
Here **ENABLE_TIMER_CALL** is used to turn off socket and works only based on check request call


**Step 4:**
Change launcher and app_logo(must be background transparent) icon in base module

**Step5:**
Rename package in project structure


**Debugging Notes:**

To debug databinding issues setup
 **--stacktrace** in studio settings


To Run Debugging, Select CompileDebugResources from Gradle tree
app -> build -> compileDebugResources
You can see the issue status and warning in Run terminal


 
