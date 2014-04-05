package com.example.todolist.view;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.todolist.R;
import com.example.todolist.model.TodoItem;

public class MainActivity extends Activity implements View.OnKeyListener {

	private TodolistAdapter listAdapter;
	private List<TodoItem> ListTodo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		listAdapter = new TodolistAdapter(this, R.layout.lisrow, R.id.listView);
		ListView listView = (ListView) findViewById(R.id.listView);
		listView.setAdapter(listAdapter);

		EditText edit = (EditText) findViewById(R.id.new_todo);
		edit.setOnKeyListener(this);

	}
	
	protected void onResume(){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		String listName = prefs.getString("pref_listname", "");
		setTitle(listName);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public boolean onOptionItemSlected(MenuItem item){
		switch(item.getItemId()){
		//case R.id.action_settings == 0;
		}
	}

	private class TodolistAdapter extends ArrayAdapter<TodoItem> implements
			View.OnClickListener {

		public TodolistAdapter(Context context, int textViewResourceId,
				int listview) {
			super(context, textViewResourceId, listview);

		}

		@Override
		public void onClick(View v) {

			TodoItem item = (TodoItem) v.getTag();
			if (item != null) {
				if (v.getId() == R.id.done) {
					if (item.getDone() == 0) {
						item.setDone(1);
					} else {
						item.setDone(0);
					}

				} else if (v.getId() == R.id.delete) {
					ListTodo.remove(item);
				}

				listAdapter.notifyDataSetChanged();
			}

		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.lisrow, null);
			}
			TodoItem todoItem = getItem(position);

			CheckBox done = (CheckBox) convertView.findViewById(R.id.done);
			done.setOnClickListener(this);
			done.setTag(todoItem);

			TextView text = (TextView) convertView.findViewById(R.id.text);
			text.setOnClickListener(this);
			text.setTag(todoItem);

			ImageView delete = (ImageView) convertView
					.findViewById(R.id.delete);
			delete.setOnClickListener(this);
			delete.setTag(todoItem);

			done.setChecked(todoItem.getDone() == 1);
			if (todoItem.getDone() == 1) {
				SpannableString span = new SpannableString(todoItem.getText());
				span.setSpan(new StrikethroughSpan(), 0, todoItem.getText().length(), 0);
				text.setText(todoItem.getText());
			} else {
				text.setText(todoItem.getText());
			}

			return convertView;
		}
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {

		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER
					|| keyCode == KeyEvent.KEYCODE_ENTER) {
				EditText edit = (EditText) findViewById(R.id.new_todo);

				if (edit.getText().length() > 0) {
					TodoItem item = new TodoItem();
					item.setText(edit.getText().toString());
					ListTodo.add(item);
					listAdapter.notifyDataSetChanged();
					edit.setText("");

				}
				return true;
			}
		}
		return false;
	}
	
	
}
