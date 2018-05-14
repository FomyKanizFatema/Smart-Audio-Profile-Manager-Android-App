package aiub.fomy.profile_manager;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.IBinder;
import android.util.Log;
public class profileManagerService  extends Service {
	static SensorManager mProx,mAcc;
	static SensorEventListener proxLis,accLis;
	static AudioManager audioManager;	
	boolean onDesk,upSideCovered;
	double proxVal,accVal;
	String modeName;
	IBinder binder;
	Sensor sensor,sProx,sAcc;
	SensorManager sensorManager;
	SensorEventListener listenAcc,listenProx;
	Listener lp,la;
	double mode,previousProxVal,newProxVal; //Mode 0-Silent,1-Vibrate,2-Normal
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	public void onCreate(){
		proxVal=0;
		accVal=0;
		onDesk=false;
		upSideCovered=false;
		audioManager=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
		mProx=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
		mAcc=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
		sProx=mProx.getDefaultSensor(Sensor.TYPE_PROXIMITY);
		sAcc=mAcc.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		lp=new Listener(1);
		la=new Listener(2);
	}
	public int onStartCommand(Intent intent, int flag, int i){
		//audioManager=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
		//audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
		
		listenProx=new SensorEventListener(){
			
							public void onAccuracyChanged(Sensor arg0, int arg1) {
								// TODO Auto-generated method stub
								
							}
			
							public void onSensorChanged(SensorEvent event) {
								
								proxVal=event.values[0];
								newProxVal=proxVal;
								if(event.values[0]<sProx.getMaximumRange()){
									upSideCovered=true;
									changeRingerMode();
									//display.displayChange(proxVal,accVal,modeName);
								}
								else{
									upSideCovered=false;
									changeRingerMode();
									//display.displayChange(proxVal,accVal,modeName);
								}
								
							}
							
						};
						listenAcc=new SensorEventListener(){
			
							public void onAccuracyChanged(Sensor arg0, int arg1) {
								// TODO Auto-generated method stub
								
							}
			
							public void onSensorChanged(SensorEvent event) {
								
								accVal=event.values[1];
								if(event.values[1]>0.2||event.values[1]<-0.2){
									onDesk=false;
									changeRingerMode();
									//display.displayChange(proxVal,accVal,modeName);
								}
								else{
									onDesk=true;
									changeRingerMode();
									//display.displayChange(proxVal,accVal,modeName);
								}
								
							}};
						//if(previousProxVal!=newProxVal){
							mAcc.registerListener(listenAcc, sAcc,SensorManager.SENSOR_DELAY_NORMAL);
							//previousProxVal=newProxVal;
						//}
						
						mProx.registerListener(listenProx, sProx,SensorManager.SENSOR_DELAY_NORMAL);
								
					 
		
		return START_STICKY;
	}
	public void setCurrentMode(int i){
		mode=i;
	}
	public double getCurrentMode(){
		return mode;
	}
	public void setpreviousProxVal(int i){
		previousProxVal=i;
	}
	public double getpreviousProxVal(){
		return previousProxVal;
	}
	 public void changeRingerMode(){
		 
	    	if(onDesk==true && upSideCovered==true){	    		
				audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
				
				modeName="Mode:Silent | Position:On Desk Upside Down";
	    		
	    		
			}	
			else if(onDesk==true && upSideCovered==false){
				audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
				modeName="Mode:Normal | Position:On Desk Upside Up";
			}
			else if(onDesk==false&&upSideCovered==true){
				audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
				modeName="Mode:Vibrate | Position:In Pocket";
			}
			else if(onDesk==false && upSideCovered==false){
				audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
				modeName="Mode:Normal | Position:In Hand";			
			}
	    	
	    }
	 public class Listener implements SensorEventListener{
		 int typeOfSensor;
		 
		 Listener(int type){
			 typeOfSensor=type;
		 }
		public void onAccuracyChanged(Sensor arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		public void onSensorChanged(SensorEvent event) {
			if(typeOfSensor==2){
				accVal=event.values[1];
				if(event.values[1]>0.2||event.values[1]<-0.2){
					onDesk=false;
					changeRingerMode();
					//display.displayChange(proxVal,accVal,modeName);
				}
				else{
					onDesk=true;
					changeRingerMode();
					//display.displayChange(proxVal,accVal,modeName);
				}
			}
			else if(typeOfSensor==1){
				//mAcc.registerListener(this, sAcc,SensorManager.SENSOR_DELAY_NORMAL);
				proxVal=event.values[0];
				if(event.values[0]<sProx.getMaximumRange()){
					upSideCovered=true;
					changeRingerMode();
					//display.displayChange(proxVal,accVal,modeName);
				}
				else{
					upSideCovered=false;
					changeRingerMode();
					//display.displayChange(proxVal,accVal,modeName);
				}
				
			}
			
		}
		 
	 }

}