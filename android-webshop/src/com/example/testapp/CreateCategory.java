package com.example.testapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateCategory extends MyActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.create_category);

		Button button = (Button) findViewById(R.id.create_cat);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CreateCategoryOnServer createCategoryOnServer = new CreateCategoryOnServer(ip);

				EditText name = (EditText) findViewById(R.id.create_cat_text);

				createCategoryOnServer.setCategoryName(name.getText().toString());

				createCategoryOnServer.execute();

			}
		});
	}

	class CreateCategoryOnServer extends AsyncTask<Void, Void, Boolean> {

		private String ip;

		private String name;

		public CreateCategoryOnServer(String ip) {
			this.ip = ip;
		}

		public void setCategoryName(String name) {
			this.name = name;
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				DefaultHttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost("http://" + ip + ":9000/categoryEdit");

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("name", name));

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
				Toast.makeText(getApplicationContext(), "Category Added!", Toast.LENGTH_LONG).show();
				;
			} else {
				Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_LONG).show();
			}
		}
	}
}
