package com.example.dije;
import java.util.Calendar;
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

//1 - 10.10.64.28 
//2 - 10.10.65.83
public class ClientActivity extends Activity{
	Context context;
	OscP5 oscP5;
	NetAddress myRemoteLocation;
	String IP = "10.10.64.28";
	MediaPlayer mediaPlayer;
	int SERVER_PORT = 12000;
	int CLIENT_PORT = 12001;
    boolean playing = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.client);
        context = this;
		oscP5 = new OscP5(this, SERVER_PORT);
		myRemoteLocation = new NetAddress(IP, CLIENT_PORT);
		
		playing = false; 
		//mediaPlayer = MediaPlayer.create(context, R.raw.track2);
		//mediaPlayer.start(); // no need to call prepare(); create() does that for you
		
		
		oscP5.addListener(new OscEventListener() {

			@Override
			public void oscStatus(OscStatus arg0) {
			}

			@Override
			public void oscEvent(final OscMessage arg0) {
				if (arg0.checkAddrPattern("/play") == true) {
					Log.d("mytag", "recieved from Server play");
					int milsec = arg0.get(0).intValue();
					int second = arg0.get(1).intValue();
					int minute = arg0.get(2).intValue();
	                
					Calendar c = Calendar.getInstance();
					int mymil = c.get(Calendar.MILLISECOND);
					int mysec = c.get(Calendar.SECOND);
					int mymin = c.get(Calendar.MINUTE);
					
					int fastforward = (mysec*1000 + mymil) - (second*1000 +milsec);
 					Log.d("mytag", fastforward+ "");
					if(playing ==  false){
					mediaPlayer = MediaPlayer.create(context, R.raw.track2);
					mediaPlayer.start();
					mediaPlayer.seekTo(fastforward);
					
					 // no need to call prepare(); create() does that for you
					playing = true;
					}
					
				}
			}
		});
		
		
		Button connect = (Button) findViewById(R.id.connectToServer);
        connect.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				//Log.d("mytag", "whatever you want");
				//Intent myIntent = new Intent(context, );
				Log.d("mytag", "connecting to Server from Client");
     
				sendMessage(1);
				
				
				
			}
		});
        
        


        
   
		
	}
 	public void sendMessage(final float val) {

		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				OscMessage myMessage = new OscMessage("/connect");
				myMessage.add(val); /* add an int to the osc message */
	
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
