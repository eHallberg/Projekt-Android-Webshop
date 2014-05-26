package com.example.testapp;

import java.util.List;

import org.apache.http.cookie.Cookie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MyActivity extends Activity {

  static protected final String ip = "192.168.0.103";

  static protected List<Cookie> cookies;

  static protected boolean loggedIn = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main_frame);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main_menu, menu);
    return loggedIn;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.string.create_category) {
      startActivity(new Intent(this, CreateCategory.class));
    }
    if (item.getItemId() == R.string.create_product) {
      startActivity(new Intent(this, CreateProduct.class));
    }
    if (item.getItemId() == R.string.list_category) {
      startActivity(new Intent(this, ListCategory.class));
    }
    if (item.getItemId() == R.string.list_product) {
      startActivity(new Intent(this, ListProduct.class));
    }
    if (item.getItemId() == R.string.logout) {
      Intent intent = new Intent(this, Login.class);
      intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
          | Intent.FLAG_ACTIVITY_NEW_TASK);
      startActivity(intent);
      finish();
      loggedIn = false;
      startActivity(new Intent(this, Login.class));
    }
    return true;
  }
}
