package com.cmpe295b;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.json.JSONObject;




public class WorkingLinks {

public void getWorkingLinks()
{
	try {
		List<String> lines=Files.readAllLines(Paths.get("zip_codes_states.csv"),  Charset.defaultCharset());
		for(String line:lines)
		{
			String data[]=line.split(",");
			String zip=data[0];
			if(zip.length()==4)
			{
				zip="0"+zip;
			}
			String city=data[3].replaceAll(" ", "-").toLowerCase();
			String state=data[4].toLowerCase();
			if(!city.equalsIgnoreCase("city"))
		{HttpURLConnection connection = null;
		try{         
		    URL myurl = new URL("http://www.movoto.com/"+city+"-"+state+"/"+zip+"/demographics/");        
		    connection = (HttpURLConnection) myurl.openConnection(); 
		    connection.setInstanceFollowRedirects( false );
		    //Set request to header to reduce load as Subirkumarsao said.       
		    connection.setRequestMethod("HEAD");         
		    int code = connection.getResponseCode();   
		   Execution e=new Execution();
		    
		    if(code==200)
		    {
		    	JSONObject ob=new JSONObject();
		    	ob.put("task", "DetailPage");
		    	ob.put("parser", "DetailPage");
		    	ob.put("zip", zip);
		    	ob.put("city", city);
		    	ob.put("state", state);
		    	e.pushToKafka("zipcodetasks", ob);
		    }
		    
		} catch(Exception e) {
		//Handle invalid URL
			System.out.println(e);
		}
		}}
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
public static void main(String args[])
{
new WorkingLinks().getWorkingLinks();	
}
}
