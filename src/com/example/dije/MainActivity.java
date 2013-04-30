package com.example.dije;

import java.io.IOException;


import java.util.Locale;

import netP5.NetAddress;
import oscP5.OscEventListener;
import oscP5.OscMessage;
import oscP5.OscP5;
import oscP5.OscStatus;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
    
	Context context;
	OscP5 oscP5;
	NetAddress myRemoteLocation;
	String IP = "10.75.163.141";
	MediaPlayer mediaPlayer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		context = this;
		
		
		//Log.d("mytag", "whatever you want");
		//String url = Environment.getExternalStorageDirectory() + "/files/music.mp3";
		//Utils.playSound(url, 5);
		//Utils.speak(this, "hello", Locale.ENGLISH);
		
		//mediaPlayer.seekTo(msec)
		
		/*
		String url = "http://thinkplace.berkeley.edu/sites/default/files/Track2.mp3"; // your URL here
		MediaPlayer mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		try {
			mediaPlayer.setDataSource(url);
			mediaPlayer.prepare(); // might take long! (for buffering, etc)
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mediaPlayer.start();
        */
		
        
          
         
		//mymessage 0.1 5 "hello"
		

		Button serverbutton = (Button) findViewById(R.id.server);
		serverbutton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				//Log.d("mytag", "whatever you ant");
				//Intent myIntent = new Intent(context, );
				Intent myIntent = new Intent(context, ServerActivity.class);
				startActivity(myIntent);
				
			}
		});
		
		Button clientbutton = (Button) findViewById(R.id.clientbutton);
		clientbutton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				//Log.d("mytag", "whatever you want");
				
				Intent myIntent = new Intent(context, ClientActivity.class);
				startActivity(myIntent);
			}
		});
		
	}

  
	public void sendMessage(final float val) {

		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				OscMessage myMessage = new OscMessage("/test");
				myMessage.add(val); /* add an int to the osc message */
	
				/* send the message */
				oscP5.send(myMessage, myRemoteLocation);
			}
		});
		t.start();

	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
   
}
