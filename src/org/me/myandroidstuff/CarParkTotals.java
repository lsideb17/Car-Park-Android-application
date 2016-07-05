package org.me.myandroidstuff;
/*By Lewis Sidebotham - S1318445*/
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.res.Configuration;



public class CarParkTotals extends Activity implements OnClickListener{

	TextView Spaces;
    TextView OccSpaces;
    TextView OccRate;
    int orientation;
    private Button back_button;
    
	public void onCreate(Bundle savedInstanceState) 
    {
		
		
        super.onCreate(savedInstanceState);
        setContentView(R.layout.totals);
        back_button = (Button) findViewById(R.id.btnBack);
        Intent carparktotals = getIntent();
        
        int OccupancyTotal = carparktotals.getIntExtra("totaloccupancy", 0);
        int CapacityTotal = carparktotals.getIntExtra("totalcapacity", 0);
        int SpacesTotal = carparktotals.getIntExtra("totalspaces", 0);
        
       
       OccSpaces = (TextView)findViewById(R.id.occspaces);
       OccRate = (TextView)findViewById(R.id.occrate);
       Spaces = (TextView)findViewById(R.id.spaces);
            
       OccSpaces.setText(" Total Capacity : " + CapacityTotal);
       Log.e("Succesful display","Yes" + CapacityTotal);
	   Spaces.setText("Total Spaces : " + SpacesTotal);
	   Log.e("Succesful display","Yes" + SpacesTotal);
	   OccRate.setText(" Total Occupancy : " + OccupancyTotal + "%");
	   Log.e("Succesful display","Yes" + OccupancyTotal);
	   
	   back_button.setOnClickListener(this);
    }
	@Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
 
        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "Landscape", Toast.LENGTH_SHORT).show();
            Log.i("Succesful Change","Yes");
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(this, "Portrait", Toast.LENGTH_SHORT).show();
            Log.i("Succesful Change","Yes");
        }
    }	
	public void onClick(View v) {
		 
		   Button back_button = (Button) findViewById(R.id.btnBack);
	    	  
	       if(v==back_button)
        {
	    	 finish(); 
	    	 Log.i("Succesful return","Yes");
        }
	}
}
