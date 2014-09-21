package com.example.testapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
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

public class Login extends MyActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (loggedIn) {
			setContentView(R.layout.logged_in_true);
		} else {
			setContentView(R.layout.login);

			Button button = (Button) findViewById(R.id.login);
			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					LogInTask logInTask = new LogInTask(ip);

					EditText name = (EditText) findViewById(R.id.login_name);
					EditText pass = (EditText) findViewById(R.id.login_password);

					logInTask.logInName(name.getText().toString());
					logInTask.logInPassword(pass.getText().toString());

					logInTask.execute();
				}

			});
		}
	}

	class LogInTask extends AsyncTask<Void, Void, Boolean> {

		private String ip;

		private String name;

		private String password;

		public LogInTask(String ip) {
			this.ip = ip;
		}

		public void logInName(String name) {
			this.name = name;
		}

		public void logInPassword(String password) {
			this.password = password;
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				HttpPost post = new HttpPost("http://" + ip + ":9000/login");

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

				nameValuePairs.add(new BasicNameValuePair("username", name));
				nameValuePairs.add(new BasicNameValuePair("password", password));

				post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				DefaultHttpClient client = new DefaultHttpClient();

				client.execute(post, new BasicResponseHandler());

				cookies = client.getCookieStore().getCookies();

				return true;
			} catch (Exception e) {
				Log.e("Error loggin in ", e.getMessage());
				e.printStackTrace();
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean success) {
			if (success == true) {
				Toast.makeText(getApplicationContext(), "Logged in!", Toast.LENGTH_LONG).show();
				loggedIn = true;
				invalidateOptionsMenu();
				onCreate(new Bundle());

			} else {
				Toast.makeText(getApplicationContext(), "Wrong login / password!", Toast.LENGTH_LONG).show();
			}
		}
	}
}
