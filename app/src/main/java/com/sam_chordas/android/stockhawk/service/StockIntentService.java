package com.sam_chordas.android.stockhawk.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.gcm.TaskParams;

/**
 * Created by sam_chordas on 10/1/15.
 */
public class StockIntentService extends IntentService {

  public StockIntentService(){
    super(StockIntentService.class.getName());
  }

  public StockIntentService(String name) {
    super(name);
  }

  @Override protected void onHandleIntent(Intent intent) {
    Log.d(StockIntentService.class.getSimpleName(), "Stock Intent Service");
    StockTaskService stockTaskService = new StockTaskService(this);
    Bundle args = new Bundle();
    if (intent.getStringExtra("tag").equals("add")){
      args.putString("symbol", intent.getStringExtra("symbol"));  // putting symbol of new company received from MystocksActivity in the bundle ags to send it further to StockTaskService
    }
    // We can call OnRunTask from the intent service to force it to run immediately instead of
    // scheduling a task.
    // calling onRunTask in StockTaskService class
    stockTaskService.onRunTask(new TaskParams(intent.getStringExtra("tag"), args));
  }
}

  /**
   *  The IntentService does the following:
   *  1. Creates a default worker thread that executes all intents delivered to onStartCommand()
   *     separate from your application's main thread.
   *  2. Creates a work queue that passes one intent at a time to your onHandleIntent() implementation,
   *     so you never have to worry about multi-threading.
   *  3. Stops the service after all start requests have been handled, so you never have to call stopSelf().
   *  4. Provides default implementation of onBind() that returns null.
   *  5. Provides a default implementation of onStartCommand() that sends the intent to the work queue and
   *     then to your onHandleIntent() implementation.
   *
   *  All this adds up to the fact that all you need to do is implement  onHandleIntent() to do the work provided by the client.
   *  (Though, you also need to provide a small constructor for the service.)
   */


