package com.example.dije;
import java.util.Calendar;
import java.util.LinkedList;
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


public class ServerActivity extends Activity {
	int SERVER_PORT = 12001;
	int CLIENT_PORT = 12000;
	Context context;
	OscP5 oscP5;
	NetAddress myRemoteLocation;
	String IP = "10.10.64.83";
	MediaPlayer mediaPlayer = new MediaPlayer();
    int	 milsectime;
    int	 minute;
    int sectime;
   
    boolean playing = false;    
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.server);
		context = this;
		oscP5 = new OscP5(this, SERVER_PORT);
		myRemoteLocation = new NetAddress(IP, CLIENT_PORT);
		playing = false;
		oscP5.addListener(new OscEventListener() {

			@Override
			public void oscStatus(OscStatus arg0) {
			}

			@Override
			public void oscEvent(final OscMessage arg0) {
				
				if (arg0.checkAddrPattern("/connect") == true) {
					Log.d("mytag", "requesting a connection from a Client");

					sendMessage();

				}
			}
		});
		

		Button playbutton = (Button) findViewById(R.id.play);
		playbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if(playing == false){
				playing = true;
					mediaPlayer = MediaPlayer.create(context, R.raw.track2);
				mediaPlayer.start(); // no need to call prepare(); create() does that for you
				 Calendar c = Calendar.getInstance();
				milsectime = c.get(Calendar.MILLISECOND);
				sectime = c.get(Calendar.SECOND);
				minute = c.get(Calendar.MINUTE);
				sendMessage();
				
				}
				
			}
			
			
		});
		
		
	}
	public void sendMessage() {

		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				Log.d("mytag", "sending Client play");

				OscMessage myMessage = new OscMessage("/play");
				myMessage.add(milsectime) ; //, sectime, minute); /* add an int to the osc message */
		        myMessage.add(sectime); 
		        myMessage.add(minute); 
				/* send the message */
				oscP5.send(myMessage, myRemoteLocation);
			}
		});
		t.start();

	}
	
	@Override
	protected void onPause() {
		super.onPause();
		oscP5.stop();
		mediaPlayer.stop();
	}
}
