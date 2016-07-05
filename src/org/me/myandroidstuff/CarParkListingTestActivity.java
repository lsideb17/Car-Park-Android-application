package org.me.myandroidstuff;
/*By Lewis Sidebotham - S1318445*/
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import android.widget.ViewFlipper;


public class CarParkListingTestActivity extends Activity implements OnClickListener
{
	private TextView response;
	private TextView errorText;
	private String result;
	private int occupancytotal;
	private int totalspaces;
	private int capacitytotal;
	private Button ExitButton;
	private Button change_orientation;
	private Button Portrait_view2;
	private Button Landscape_view2;
	private Button additional_data;
	private Button Next_page;
	private Button refresh_page;
	private View mainView;
	private View textview;
	private TextView textresult;
	XMLParser parser = new XMLParser();
	private String sourceListingURL = "http://open.glasgow.gov.uk/api/live/parking.php?type=xml";
	int orientation;
	carpark carPark;
	Intent carparktotals;
	List<carpark> carparks; 
	private ListView listView;
	

	//summaryButton.setOnClickListener(this); 
	/** Called when the activity is first created. */

	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
		
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
       
        change_orientation = (Button) findViewById(R.id.chorientation);
        additional_data = (Button) findViewById(R.id.btn_ad);
        Next_page = (Button) findViewById(R.id.nextpage);
        refresh_page = (Button) findViewById(R.id.refresh);
        mainView = (View) findViewById(R.id.mainview);
        textresult = (TextView) findViewById(R.id.CarParks);
        carparktotals = new Intent(CarParkListingTestActivity.this, CarParkTotals.class);
        listView = (ListView) findViewById(R.id.list);
        TextView responses;
       
        // Get the TextView object on which to display the results
        
        try
        {
        	
        	result =  sourceListingString(sourceListingURL);
        	
        	XMLParser parser = new XMLParser();
			carparks = parser.parseData(result);

			adapt();
			Log.i("Succesful array Adpater","Yes");
		
        	        	
          	// Do some processing of the data to get the individual parts of the XML stream
        	// At some point put this processing into a separate thread of execution
           	// Display the string in the TextView object just to demonstrate this capability
        	// This will need to be removed at some point
        	//response.setText(result);
        }
        catch(IOException ae)
        {
        	// Handle error
        	//response.setText("Error");
        	// Add error info to log for diagnostics
        	errorText.setText(ae.toString());
        }
        listView.setOnItemClickListener(new OnItemClickListener() {
        	 
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	//TextView tv = (TextView) findViewById(R.id.textview1);
            	carPark=(carpark)carparks.get(position);
            	showcustomDialog("Car Park Identity: " + carPark.getcarParkIdentity()  + "\n" +"Car Park Status: " + carPark.getCarParkStatus() + "\n" + "Car Park Occupancy: " + carPark.getCarParkOccupancy() + "% " + "\n" + "Total Capacity: " + carPark.getTotalCapacity()  + "\n" + "Occupied Spaces: " + carPark.getOccupiedSpaces());
            	Log.i("Succesful OnClick","Yes");
            	
            	//Toast.makeText(getApplicationContext(), "Car Park Identity: " + carPark.getcarParkIdentity()  + "\n" +"Car Park Status: " + carPark.getCarParkStatus() + "\n" + "Car Park Occupancy: " + carPark.getCarParkoOccupancy() + "% " + "\n" + "Total Capacity: " + carPark.getTotalCapacity()  + "\n" + "Occupied Spaces: " + carPark.getOccupiedSpaces(), Toast.LENGTH_SHORT).show();
            
            }
        });
        
        
        
        		final Handler handler = new Handler();
        		handler.postDelayed(new Runnable() {
        			public void run(){
        				ShowDialogAsyncTask background = new ShowDialogAsyncTask();
        				background.execute();
        				handler.postDelayed(this, 200000);
        			}
        			
    			}, 200000);
           Log.i("Succesful Refresh","Yes");
        
        		change_orientation.setOnClickListener(this);
        		//ExitButton.setOnClickListener(this);
        		additional_data.setOnClickListener(this);
        		Next_page.setOnClickListener(this);
        		refresh_page.setOnClickListener(this);
        	
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
        
        
    // End of onCreate
    // Method to handle the reading of the data from the XML stream
    private static String sourceListingString(String urlString)throws IOException
    {
	 	String result = "";
	 	
    	InputStream anInStream = null;
    	int response = -1;
    	URL url = new URL(urlString);
    	URLConnection conn = url.openConnection();
    	
    	// Check that the connection can be opened
    	if (!(conn instanceof HttpURLConnection))
    			throw new IOException("Not an HTTP connection");
    	try
    	{
    		// Open connection
    		HttpURLConnection httpConn = (HttpURLConnection) conn;
    		httpConn.setAllowUserInteraction(false);
    		httpConn.setInstanceFollowRedirects(true);
    		httpConn.setRequestMethod("GET");
    		httpConn.connect();
    		response = httpConn.getResponseCode();
    		// Check that connection is Ok
    		if (response == HttpURLConnection.HTTP_OK)
    		{
    			// Connection is Ok so open a reader 
    			anInStream = httpConn.getInputStream();
    			InputStreamReader in= new InputStreamReader(anInStream);
    			BufferedReader bin= new BufferedReader(in);
    			
    			// Read in the data from the XML stream
    			bin.readLine(); // Throw away the header
    			String line = new String();
    			while (( (line = bin.readLine())) != null)
    			{
    				result = result + "\n" + line;
    				Log.i("Succesful return","Yes");
    				
    			}
    		}
    	}
    	catch (Exception ex)
    	{
    			throw new IOException("Error connecting");
    	}
    	
    	// Return result as a string for further processing
    	return result;
    }
    
    public class XMLParser {
    	
    	List<carpark> carparks;
    	
    	private carpark carParkObject;
    	private String text;
    	
    	
    	public XMLParser() {
    		
    		
    		
    	}
    	public List parseData(String feed)
    	{
    		
    		try
    		{
    			carparks = new ArrayList<carpark>();
    			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
    			factory = XmlPullParserFactory.newInstance();
    			factory.setNamespaceAware(true);
    			XmlPullParser parser = factory.newPullParser();
    			parser.setInput(new StringReader(feed));
    			int eventType = parser.getEventType();
    			while (eventType != XmlPullParser.END_DOCUMENT) 
    			{
    				String tagName = parser.getName();
    				
    				switch(eventType){
    				
    				case XmlPullParser.START_TAG:
    					
    					if(tagName.equalsIgnoreCase("carParkIdentity")){
    						
    						carParkObject = new carpark();
    					}
    					
    					break;
    					
    				case XmlPullParser.TEXT:
    					
    					text = parser.getText();
    					break;
    					
    				case XmlPullParser.END_TAG:
    					
    					if(tagName.equalsIgnoreCase("carParkIdentity")){
    						
    						text = text.substring(0,text.indexOf(":"));
    						carparks.add(carParkObject);
    						carParkObject.setCarParkIdentity(text);
    						Log.e("MyTag","identity is " + text);
    						
    					}else if(tagName.equalsIgnoreCase("carParkOccupancy")){
    						
    						carParkObject.setCarParkOccupancy(Integer.parseInt(text));
    						Log.e("MyTag","occupancy is " + text);
    						
    					    						
    					}else if(tagName.equalsIgnoreCase("carParkStatus")){
    						
    						carParkObject.setCarParkStatus(text);
    						Log.e("MyTag","Status is " + text);
    						
    					}else if(tagName.equalsIgnoreCase("occupiedSpaces")){
    						
    						carParkObject.setOccupiedSpaces(Integer.parseInt(text));
    						Log.e("MyTag","Occupied spaces is " + text);
    						
    					}else if(tagName.equalsIgnoreCase("totalCapacity")){
    						
    						carParkObject.setTotalCapacity(Integer.parseInt(text));
    						Log.e("MyTag","Total capacity is " + text);
    					}
    					
    					break;
    					
    				}
    				eventType = parser.next();
    				Log.e("Parsing works","Yes ");
    			}
    			
    		      
    			Log.i("Successful parse","Yes");
    			   
    		}catch (XmlPullParserException ae1)
    		{
    			Log.e("MyTag","Parsing error" + ae1.toString());
    		}
    		catch (IOException ae1)
    		{
    			Log.e("MyTag","IO error during parsing");
    		}
    		
    		return carparks;
    		
    		
    		//Log.e("MyTag","End document");


    	}
    }
    
    private void adapt(){
    	ArrayAdapter<carpark> adapter = new ArrayAdapter<carpark>(CarParkListingTestActivity.this, R.layout.list_item, carparks);
		listView.setAdapter(adapter);
    }
    
    private void showcustomDialog(String info)
	{
		// Custom dialog setup
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.dialog_info);
		dialog.setTitle("Car Park details");
		Log.i("Dialog Successful", "Yes");
		
		TextView text = (TextView) dialog.findViewById(R.id.infoView);
		text.setText(info);
		Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);			
		dialogButton.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
					dialog.dismiss();//Upon clicking the button, the dialog box disappears
					Log.i("Succesful close","Yes");
					
			}
		});
		 
		dialog.show();
	}


    private class ShowDialogAsyncTask extends AsyncTask<Void, Void, Void>
    {
    	@Override
        protected void onPreExecute(){
        	try {
				result =  sourceListingString(sourceListingURL);
				Log.e("Preexecute","Pre execute");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        }
    	
    	@Override
		     protected Void doInBackground(Void... params) 
		     {
		    	 carparks = null;
		    	 try
		        {
		        	
		        	//XMLParser parser = new XMLParser();
					carparks = parser.parseData(result);
					Log.e("InBackground","Pre execute");
					
		        }
		        catch(Exception ae)
		        {
		        	// Handle error
		        	//response.setText("Error");
		        	// Add error info to log for diagnostics
		        	errorText.setText(ae.toString());
		        }
				return null;
		     }  
    	
    	protected void onPostExecute(Void result) 
		     {
    			adapt();
		    	 Log.e("Execute","Pre execute");
		    	 Toast.makeText(CarParkListingTestActivity.this, "Page refreshed", Toast.LENGTH_LONG).show();
		    	 
		    	 
		    
		     }
    
    }
    
    

    
    
	public void onClick(View v) {
		 
		   Button change_orientation = (Button) findViewById(R.id.chorientation);
	       TextView textresult = (TextView)findViewById(R.id.CarParks); 	  
           if(v==change_orientation)
           {
         	  final String[] orientation ={"Landscape","Portrait"};
					AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setTitle("Select Orientation");
					builder.setItems(orientation,new DialogInterface.OnClickListener(){
						public void onClick(DialogInterface dialog, int whichItem) 
				           {
				        	   Toast.makeText(getApplicationContext(), orientation[whichItem], Toast.LENGTH_SHORT).show();
				        	   switch(whichItem)
				               {
				               		case 0 :
				               			
				               		setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			                        break;
			                        case 1 :
			                        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			                        break;
				               }
				           }
					});
					builder.setCancelable(false);
					
					AlertDialog alert = builder.create();	
					alert.show();
     }
                                     
      Button Next_page = (Button) findViewById(R.id.nextpage);
			if(v==Next_page)
			{
				for (int i=0; i<listView.getChildCount(); i++)
            	{
            		carPark=(carpark)carparks.get(i);
            		Log.d("Colour Change", "Here");
            		 if (carPark.getCarParkStatus().equals("carParkClosed") )
             		{
             			(listView.getChildAt(i)).setBackgroundColor(Color.BLACK);
             			Log.e("MyTag","list is colour,  black ");
             			
             		}
            		 else if(carPark.getCarParkOccupancy() >= 0 && carPark.getCarParkOccupancy() <= 33)
            			
            		{
            			(listView.getChildAt(i)).setBackgroundColor(Color.GREEN);
            			Log.e("MyTag","list is colour green ");
            		}
            		else if(carPark.getCarParkOccupancy() >= 34 && carPark.getCarParkOccupancy() <= 66)
            		{
            			(listView.getChildAt(i)).setBackgroundColor(Color.YELLOW);
            			Log.e("MyTag","list is colour yellow ");
            		}
            		else if(carPark.getCarParkOccupancy() >= 67 && carPark.getCarParkOccupancy() <= 90)
            		{
            			(listView.getChildAt(i)).setBackgroundColor(Color.MAGENTA);
            			Log.e("MyTag","list is colour magenta ");
            		}
            		else if(carPark.getCarParkOccupancy() >= 91 && carPark.getCarParkOccupancy() <= 99)
            		{
            			(listView.getChildAt(i)).setBackgroundColor(Color.RED);
            			Log.e("MyTag","list is colour red ");
            		}
            		else 
            		{
            			(listView.getChildAt(i)).setBackgroundColor(Color.WHITE);
            			Log.e("MyTag","list is colour,  black ");
            			
            		}
            	}
				
			} 
			
			Button Refreshpg = (Button) findViewById(R.id.refresh);
			if(v==Refreshpg)
			{
				ShowDialogAsyncTask update = new ShowDialogAsyncTask();
				 update.execute();
				 Log.i("Refresh", "Succesful");
							 
				
			}
			Button additional_data = (Button) findViewById(R.id.btn_ad);   
					if(v==additional_data)
          
					{
						
          			for(carpark carPark:carparks){	            			
          			totalspaces = totalspaces + carPark.getOccupiedSpaces();
          			Log.e("MyTag","total spots is " + totalspaces);
          			capacitytotal = capacitytotal + carPark.getTotalCapacity();
          			Log.e("MyTag","Total capacity " + capacitytotal);
          			
          			//Total Capacity ; " + capacitytotal + "\n" + "Total Spaces ; " + spacestotal
          			}
          			
          			occupancytotal = (int)(((double)totalspaces/capacitytotal)*100);//here the division is done to get the a figure, it is then set as a double, then it is multiplied by 100, then made an INT as int roundsw it to a full number so the total isnt eg 45.4576753476765 instead its 45.
          			Log.e("MyTag","total occupancy " + occupancytotal);
          			
          			//Toast.makeText(this, " Total Capacity : " + capacitytotal + "\n" + "Total Spaces : " + totalspaces + "\n" +  " Total Occupancy : " + occupancytotal + "%", Toast.LENGTH_SHORT).show();
          			
          			carparktotals.putExtra("totaloccupancy", occupancytotal);
					carparktotals.putExtra("totalcapacity", capacitytotal);
					carparktotals.putExtra("totalspaces", totalspaces);
					CarParkListingTestActivity.this.startActivity(carparktotals);
					Log.e("MyTag","results " + occupancytotal + totalspaces + capacitytotal);
          			Log.i("Succesful display of results","Yes");
					}
					
					
					
							totalspaces= 0;
							capacitytotal=0;
							occupancytotal=0;
				
			}
	
					
					
}
   // End of sourceListingString 

    
