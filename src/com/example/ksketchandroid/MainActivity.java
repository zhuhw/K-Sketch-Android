package com.example.ksketchandroid;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;

import android.widget.Button;

public class MainActivity extends Activity {

	public static SketchModel model;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		 model = new SketchModel();
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		final Button playButton = (Button) findViewById(R.id.button1);
		playButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	model.setPlaying(!model.getPlaying());
            }
        });
		
		final Button configButton = (Button) findViewById(R.id.button2);
		configButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	startActivity(new Intent(MainActivity.this, Setting.class));
            }
        });
		
		final DrawView dv = (DrawView) findViewById(R.id.draw_view);
		dv.setModel(model);
		
		//model.loadGame();
		
		dv.postInvalidate();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void changeBackground(int color){

		View root = getWindow().getDecorView().findViewById(android.R.id.content);

		// Set the color
		if(color == 1)
			root.setBackgroundColor(Color.BLACK);
		if(color == 2)
			root.setBackgroundColor(Color.RED);
		if(color == 3)
			root.setBackgroundColor(Color.YELLOW);
		if(color == 4)
			root.setBackgroundColor(Color.BLUE);
	}
	
	public void OnStart(){super.onStart();}
	public void OnPause(){super.onPause();}
	public void OnResume(){super.onResume();}
	public void OnStop(){super.onStop();}

}
