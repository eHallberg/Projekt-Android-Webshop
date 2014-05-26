package com.example.testapp;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ListCategory extends MyActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.list_category);

		GetCategories myTask = new GetCategories();
		myTask.execute();
	}

	class GetCategories extends AsyncTask<Void, Void, JSONArray> {

		@Override
		protected JSONArray doInBackground(Void... params) {
			try {
				String myResponse = new DefaultHttpClient().execute(new HttpGet("http://" + ip + ":9000/categoryListAll"), new BasicResponseHandler());

				return new JSONArray(myResponse);

			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		protected void onPostExecute(JSONArray result) {

			ListView listView = (ListView) findViewById(R.id.list_view);

			listView.setAdapter(new CategoriesAdapter(result));
		}
	}

	class CategoriesAdapter extends BaseAdapter {

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
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int index, View convertView, ViewGroup parent) {
			View categoryListItem = getLayoutInflater().inflate(R.layout.list_category_item, parent, false);
			TextView categoryName = (TextView) categoryListItem.findViewById(R.id.category_name);

			try {
				JSONObject Category = categories.getJSONObject(index);

				categoryName.setText(Category.getString("name"));
			} catch (JSONException e) {
				throw new RuntimeException(e);
			}
			return categoryListItem;

		}

	}

}