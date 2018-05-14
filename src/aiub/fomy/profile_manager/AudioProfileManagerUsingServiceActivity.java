package aiub.fomy.profile_manager;


import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;

public class AudioProfileManagerUsingServiceActivity extends Activity {
	
	Intent s;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
        
        s=new Intent(this,profileManagerService.class);
        startService(s);
        
        
    }
}