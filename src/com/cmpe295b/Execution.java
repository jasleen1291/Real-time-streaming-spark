package com.cmpe295b;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Execution implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Execution obj = new Execution();
		BufferedReader bufferedReader = new BufferedReader(new FileReader(
				"task2.json"));

		StringBuffer stringBuffer = new StringBuffer();
		String line = null;

		while ((line = bufferedReader.readLine()) != null) {

			stringBuffer.append(line);
		}
		bufferedReader.close();
		System.out.println(obj.executeCommand(stringBuffer.toString()));
		// System.out.println("http://www.zillow.com/webservice/GetDeepSearchResults.htm?zws-id=X1-ZWz1a6xks6qf4b_8xo7s&address="+URLEncoder.encode("4300 Verdigris Cir",
		// "utf-8")+"&citystatezip="+URLEncoder.encode("San Jose, CA",
		// "utf-8"));
		// System.out.println(output2);
	}

	public Object executeCommand(String command) {
		// System.out.println(command);
		ProcessBuilder pb = new ProcessBuilder("node", "server.js", command);
		System.out.println("Command:" + command);
		Process p;
		try {
			p = pb.start();

			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					p.getInputStream()));

			String line1 = "";
			while ((line1 = reader.readLine()) != null) {
				// System.out.println(line1);
				try {
					JSONObject o = new JSONObject(line1);
					System.out
							.println("sending" + o.getString("task") + "" + o);
					if (o.getString("task") == "zillowSearch")
						pushToKafka("search", o);
					else
						pushToKafka("data", o);
				} catch (Exception e) {
					System.out.println("Cannot send" + line1);
				}
			}
		}

		catch (Exception ee) {
			System.out.println("Exceptio " + command);
			System.out.println(ee);
		}

		return null;
	}

	public void pushToKafka(String topic, JSONObject line1)
			throws UnsupportedEncodingException, JSONException {
		// System.out.println("Here");
		Properties props = new Properties();

		props.put("metadata.broker.list",
				"52.4.219.61:9092,54.164.200.26:9092,54.152.210.81:9092");
		props.put("serializer.class", "kafka.serializer.StringEncoder");
		props.put("auto.create.topics.enable", "true");
		props.put("zookeeper.connect", "172.31.38.38:2181");
		ProducerConfig config = new ProducerConfig(props);

		Producer<String, String> producer = new Producer<String, String>(config);

		// sending...

		KeyedMessage<String, String> keyedMessage = new KeyedMessage<String, String>(
				topic, line1.toString());
		producer.send(keyedMessage);
		System.out.println("Sent");
	}
}
