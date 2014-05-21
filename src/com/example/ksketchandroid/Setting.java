package com.example.ksketchandroid;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

public class Setting extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		final RadioButton blackButton = (RadioButton) findViewById(R.id.radio0);
		blackButton.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {
	        	MainActivity.model.bc=Color.BLACK;
	        	MainActivity.model.updateAllViews();
	        }
	    });
		final RadioButton redButton = (RadioButton) findViewById(R.id.radio1);
		redButton.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {
	        	MainActivity.model.bc=Color.RED;
	        	MainActivity.model.updateAllViews();
	        }
	    });
		final RadioButton yellowButton = (RadioButton) findViewById(R.id.radio2);
		yellowButton.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {
	        	MainActivity.model.bc=Color.YELLOW;
	        	MainActivity.model.updateAllViews();
	        }
	    });
		final RadioButton blueButton = (RadioButton) findViewById(R.id.radio3);
		blueButton.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {
	        	MainActivity.model.bc=Color.BLUE;
	        	MainActivity.model.updateAllViews();
	        }
	    });
		final RadioButton whiteButton = (RadioButton) findViewById(R.id.radio4);
		whiteButton.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {
	        	MainActivity.model.bc=Color.WHITE;
	        	MainActivity.model.updateAllViews();
	        }
	    });
		
		
		final EditText fpsInput = (EditText) findViewById(R.id.editText1);
		fpsInput.setOnClickListener(new View.OnClickListener()
		{
		    public void onClick(View view)
		    {
		    	try{
		    		MainActivity.model.FPS=(Integer.parseInt(fpsInput.getText().toString()));
		    	}catch(Exception e){}
		    }
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.setting, menu);
		return true;
	}
	
	

}
