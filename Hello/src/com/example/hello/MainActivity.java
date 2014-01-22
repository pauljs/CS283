package com.example.hello;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;



public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("MainActivity", "In onCreate Method");
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i("MainActivity", "In onDestroy Method");
	}


	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.i("MainActivity", "In onPause Method");
	}


	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Log.i("MainActivity", "In onRestart Method");
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i("MainActivity", "In onResume Method");
	}


	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.i("MainActivity", "In onStart Method");
	}


	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.i("MainActivity", "In onStop Method");
	}    
}
