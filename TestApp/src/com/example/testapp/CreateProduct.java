package com.example.testapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class CreateProduct extends MyActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.create_product);
    Button button = (Button) findViewById(R.id.create_prod);
    new GetCategoriesFromServer().execute();
    button.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        CreateProductInServer createProductInServer = new CreateProductInServer(ip);
        Spinner category = (Spinner) findViewById(R.id.category_menu);
        Toast.makeText(getApplicationContext(), "Category: " + category.getSelectedItemId(),
            Toast.LENGTH_LONG).show();
        EditText name = (EditText) findViewById(R.id.prod_name);
        EditText desc = (EditText) findViewById(R.id.prod_desc);
        EditText price = (EditText) findViewById(R.id.prod_price);
        createProductInServer.setProductName(name.getText().toString());
        createProductInServer.setProductCategory(category.getSelectedItemId());
        createProductInServer.setProductDesc(desc.getText().toString());
        createProductInServer.setProductPrice(price.getText().toString());
        createProductInServer.execute();
      }
    });
  }

  class CreateProductInServer extends AsyncTask<Void, Void, Boolean> {

    private String ip;

    private String name;

    private String description;

    private String price;

    private String cat;

    public CreateProductInServer(String ip) {
      this.ip = ip;
    }

    public void setProductName(String name) {
      this.name = name;
    }

    public void setProductDesc(String description) {
      this.description = description;
    }

    public void setProductPrice(String price) {
      this.price = price;
    }

    public void setProductCategory(long cat) {
      String s = String.valueOf(cat);
      this.cat = s;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
      try {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://" + ip + ":9000/productEdit");
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("name", name));
        nameValuePairs.add(new BasicNameValuePair("category-id", cat));
        nameValuePairs.add(new BasicNameValuePair("description", description));
        nameValuePairs.add(new BasicNameValuePair("price", price));
        post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        if (cookies != null) {
          for (Cookie cookie : cookies) {
            client.getCookieStore().addCookie(cookie);
          }
        }
        client.execute(post, new BasicResponseHandler());
        return true;
      } catch (Exception e) {
        Log.e("Error creating category", e.getMessage());
        e.printStackTrace();
        return false;
      }
    }

    @Override
    protected void onPostExecute(Boolean success) {
      if (success == true) {
        Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_LONG).show();;
      } else {
        Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_LONG).show();
      }
    }
  }

  class GetCategoriesFromServer extends AsyncTask<Void, Void, JSONArray> {

    @Override
    protected JSONArray doInBackground(Void... params) {
      try {
        String response =
            new DefaultHttpClient().execute(new HttpGet("http://" + ip + ":9000/categoryListAll"),
                new BasicResponseHandler());
        return new JSONArray(response);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }

    @Override
    protected void onPostExecute(JSONArray result) {
      Spinner spinner = (Spinner) findViewById(R.id.category_menu);
      spinner.setAdapter(new CategoriesAdapter(result));
    }
  }

  class CategoriesAdapter extends BaseAdapter implements SpinnerAdapter {

    private JSONArray categories;

    public CategoriesAdapter(JSONArray categories) {
      this.categories = categories;
    }

    @Override
    public int getCount() {
      return categories.length();
    }

    @Override
    public Object getItem(int index) {
      try {
        return categories.getJSONObject(index);
      } catch (JSONException e) {
        throw new RuntimeException(e);
      }
    }

    @Override
    public long getItemId(int index) {
      try {
        return categories.getJSONObject(index).getInt("id");
      } catch (JSONException e) {
        throw new RuntimeException(e);
      }
    }

    @Override
    public View getView(int index, View convertView, ViewGroup parent) {
      TextView textView = new TextView(getApplicationContext());
      try {
        JSONObject grade = categories.getJSONObject(index);
        textView.setText(grade.getString("name"));
      } catch (JSONException e) {
        throw new RuntimeException(e);
      }
      textView.setTextSize(18);
      return textView;
    }
  }
}
