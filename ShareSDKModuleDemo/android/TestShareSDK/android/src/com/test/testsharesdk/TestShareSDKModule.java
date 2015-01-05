/**
 * This file was auto-generated by the Titanium Module SDK helper for Android
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2010 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 *
 */
package com.test.testsharesdk;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import org.appcelerator.kroll.KrollModule;
import org.appcelerator.kroll.annotations.Kroll;

import org.appcelerator.titanium.TiApplication;
import org.appcelerator.titanium.util.TiRHelper;
import org.appcelerator.titanium.util.TiRHelper.ResourceNotFoundException;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.kroll.common.TiConfig;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.utils.UIHandler;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.os.Message;
import android.widget.Toast;



@Kroll.module(name="TestShareSDK", id="com.test.testsharesdk")
public class TestShareSDKModule extends KrollModule
{

  // Standard Debugging variables
  private static final String LCAT = "TestShareSDKModule";
  private static final boolean DBG = TiConfig.LOGD;

  // You can define constants with @Kroll.constant, for example:
  // @Kroll.constant public static final String EXTERNAL_NAME = value;

  private static final String SHAREICONNAME = "shareicon";
  private static int SHAREICON;
  private static String SHAREICONPATH;

  public TestShareSDKModule()
  {
    super();
  }

  public static String copyFileIntoSDCard(TiApplication app, String fileName){

    String path = null;
    try {
      if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
          && Environment.getExternalStorageDirectory().exists()) {
        path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + fileName;
      }
      else {
        path = TiApplication.getInstance().getFilesDir().getAbsolutePath() + "/"+ fileName;
      }
      Log.d(LCAT, path);
      InputStream in = null;
      OutputStream out = null;

      try{
            in = app.getAssets().open(fileName);
            File outFile = new File(path);
            if (!outFile.exists()) {
              out = new FileOutputStream(outFile);
              Bitmap bitmap = BitmapFactory.decodeStream(in);
              bitmap.compress(CompressFormat.PNG, 100, out);
          }
          }
          catch (IOException e) {
          Log.e(LCAT, e.getMessage());
        }
      finally{
        in.close();
          in = null;
          out.close();
          out = null;
      }

      Log.d(LCAT, "Copy file into sd card:" + path);
    } catch(Throwable t) {
      Log.e(LCAT, t.getMessage());
      Log.e(LCAT, "Copy file into sd card fail");
    }
    return path;
  }

  @Kroll.onAppCreate
  public static void onAppCreate(TiApplication app)
  {
    Log.d(LCAT, "inside onAppCreate");
    ShareSDK.initSDK(app);

    SHAREICON = getApplicationResource(SHAREICONNAME);
    SHAREICONPATH = copyFileIntoSDCard(app, SHAREICONNAME+".jpg");
    // put module init code that needs to run when the application is created
  }

  @Kroll.method
  public void share(final HashMap args){
    Log.d(LCAT, "share");
    Log.d(LCAT, getActivity().toString());

    Activity activity = TiApplication.getInstance().getCurrentActivity();
    Log.d(LCAT, activity.toString());
    ShareSDK.initSDK(activity);

    String title = "" , content = "", url ="", type= "", image="";
    if (args.containsKey("title")) {
      Object otitle = args.get("title");
      if (otitle instanceof String) {
        title = (String)otitle;
      }
    }
    if (args.containsKey("content")) {
      Object ocontent = args.get("content");
      if (ocontent instanceof String) {
        content = (String)ocontent;
      }
    }
    if (args.containsKey("url")) {
      Object ourl = args.get("url");
      if (ourl instanceof String) {
        url = (String)ourl;
      }
    }
    if (args.containsKey("type")) {
      Object otype = args.get("type");
      if (otype instanceof String) {
        type = (String)otype;
      }
    }
    if (args.containsKey("image")) {
      Object oimage = args.get("image");
      if (oimage instanceof String) {
        image = (String)oimage;
      }
    }

    Log.d(LCAT, title);
    Log.d(LCAT, content);
    Log.d(LCAT, url);
    Log.d(LCAT, type);
    Log.d(LCAT, image);

    final OnekeyShare oks = new OnekeyShare();

    int appicon = 0x7f020000;

    try {
      appicon = TiRHelper.getApplicationResource("drawable.appicon");
      Log.d(LCAT, "getApplicationResource drawable.appicon");
    } catch (ResourceNotFoundException e) {
    }

    oks.setNotification(appicon, "测试ShareSDK");

    oks.setAddress("000000");
    oks.setTitle(title);
    oks.setText(content);
    Log.d(LCAT, resolveUrl(null,""));
//      oks.setImagePath(resolveUrl(null,""));
//      oks.setImageUrl("http://img.appgo.cn/imgs/sharesdk/content/2013/07/25/1374723172663.jpg");
    if (url != ""){
      oks.setUrl(url);
    }

    if(!image.isEmpty()){
      if ( image.indexOf("http") > -1 ){
        oks.setImageUrl(image);
      }
      else{
        // 必须在resource下
        String timage = copyFileIntoSDCard(TiApplication.getInstance(), image);
        Log.d(LCAT, "setImagePath:" + timage);
        oks.setImagePath(timage);
      }
    }
    else{
      Log.d(LCAT, "setImagePath:" + SHAREICONPATH);
      oks.setImagePath(SHAREICONPATH);
    }
//      oks.setFilePath(resolveUrl(null,""));
//      oks.setComment(menu.getContext().getString(R.string.share));

//      oks.setSite(menu.getContext().getString(R.string.app_name));
//      oks.setSiteUrl("http://sharesdk.cn");

//      oks.setVenueName("ShareSDK");
//      oks.setVenueDescription("This is a beautiful place!");
//      oks.setLatitude(23.056081f);
//      oks.setLongitude(113.385708f);
    oks.setSilent(false);

//      oks.setShareContentCustomizeCallback(new ShareContentCustomizeDemo());
    Log.d(LCAT, activity.toString());
    oks.show(activity.getBaseContext());
    Log.d(LCAT, "show finish");
  }

  public static int getApplicationResource(String fileName){
    int result = -1;
    try {
      fileName = "drawable." + fileName;
      result = TiRHelper.getApplicationResource(fileName);
      Log.d(LCAT, "getApplicationResource success:"+ fileName);
    } catch (ResourceNotFoundException e) {
      Log.e(LCAT, e.getMessage());
      Log.e(LCAT, "getApplicationResource fail:"+ fileName);
    }
    return result;
  }

  @Kroll.method
  public void stop(){
    ShareSDK.stopSDK(TiApplication.getInstance().getCurrentActivity());
  }

  // Methods
  @Kroll.method
  public String example()
  {
    Log.d(LCAT, "example called");
    return "hello world";
  }

  // Properties
  @Kroll.getProperty
  public String getExampleProp()
  {
    Log.d(LCAT, "get example property");
    return "hello world";
  }


  @Kroll.setProperty
  public void setExampleProp(String value) {
    Log.d(LCAT, "set example property: " + value);
  }

}

