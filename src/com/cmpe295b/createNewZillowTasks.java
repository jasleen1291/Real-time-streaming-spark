package com.cmpe295b;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;

import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

public class createNewZillowTasks {
	public static void main(String args[]) throws UnknownHostException
	{
		 Mongo mongo = new Mongo("52.2.127.199", 27017);
		  DB db = mongo.getDB("partb");
	 Execution ex=new Execution();
		  // get a single collection
		  DBCollection collection = db.getCollection("zip");
		  DBCursor cursor = collection.find();
		  while (cursor.hasNext()) {
			DBObject object= cursor.next();
			String zip=(String) (object.get("zip"));
			String url="http://www.zillow.com/homes/1_ah/"+zip+"_rb/";
			JSONObject obj=new JSONObject();
			obj.put("url", url);
			obj.put("task","zillowSearch");
			obj.put("parser","zillowSearch");
			obj.put("data",new JSONObject(object.toMap()));
			try {
				ex.pushToKafka("zillowSearchTaskWithData", obj);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  }
	}
}
