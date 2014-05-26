package com.example.testapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
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
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ListProduct extends MyActivity {

  public String id;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.list_product);

    GetProducts myTask = new GetProducts();

    myTask.execute();
  }

  class GetProducts extends AsyncTask<Void, Void, JSONArray> {

    @Override
    protected JSONArray doInBackground(Void... params) {
      try {
        String myResponse =
            new DefaultHttpClient().execute(new HttpGet("http://" + ip + ":9000/productListAll"),
                new BasicResponseHandler());

        return new JSONArray(myResponse);

      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }

    @Override
    protected void onPostExecute(JSONArray result) {

      ListView listView = (ListView) findViewById(R.id.list_view);

      listView.setAdapter(new ListProductsAdapter(result));
    }
  }

  class ListProductsAdapter extends BaseAdapter {

    private JSONArray products;

    public ListProductsAdapter(JSONArray products) {
      this.products = products;
    }

    @Override
    public int getCount() {
      return products.length();
    }

    @Override
    public Object getItem(int index) {
      try {
        return products.getJSONObject(index);
      } catch (JSONException e) {
        throw new RuntimeException(e);
      }

    }

    @Override
    public long getItemId(int position) {
      return position;
    }

    @Override
    public View getView(int index, View convertView, ViewGroup parent) {
      View productListItem = getLayoutInflater().inflate(R.layout.list_product_item, parent, false);
      TextView productName = (TextView) productListItem.findViewById(R.id.product_name);
      TextView productDesc = (TextView) productListItem.findViewById(R.id.product_desc);
      TextView productId = (TextView) productListItem.findViewById(R.id.product_id);



      try {
        JSONObject Product = products.getJSONObject(index);
        Button button = (Button) productListItem.findViewById(R.id.add_to_cart);
        productName.setText(Product.getString("name"));
        productDesc.setText("Cat id: " + Product.getString("category_id"));
        productId.setText("Prod id: " + Product.getString("id"));
        button.setTag(Product.getString("id"));

        button.setOnClickListener(onClickListener);

      } catch (JSONException e) {
        throw new RuntimeException(e);
      }
      return productListItem;

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {

      public void onClick(View v) {

        String index = (String) v.getTag();
        
        CreateProductInServer addProduct = new CreateProductInServer(ip);
        Spinner quantity = (Spinner) findViewById(R.id.spin_quantity);
        addProduct.setId(index);
        addProduct.setQuantity(quantity.getSelectedItem().toString());
        Toast.makeText(getApplicationContext(),"Added to order!",
            Toast.LENGTH_SHORT).show();
        addProduct.execute();

      }

    };
  }
  class CreateProductInServer extends AsyncTask<Void, Void, Boolean> {
    private String ip;
    private String id;
    private String quantity;


    public CreateProductInServer(String ip) {
      this.ip = ip;
    }

    public void setId(String id) {
      this.id = id;
    }

    public void setQuantity(String quantity) {
      this.quantity = quantity;
    }


    @Override
    protected Boolean doInBackground(Void... params) {
      try {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://" + ip + ":9000/productShowOne/");
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("product-id", id));
        nameValuePairs.add(new BasicNameValuePair("quantity", quantity));
        post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

        client.execute(post, new BasicResponseHandler());
        return true;
      } catch (Exception e) {
        Log.e("Error adding product to basket!", e.getMessage());
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



}
