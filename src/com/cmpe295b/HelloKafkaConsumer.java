package com.cmpe295b;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONObject;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.javaapi.message.ByteBufferMessageSet;
import kafka.message.MessageAndOffset;

/**
 * Created by user on 8/4/14.
 */
public class HelloKafkaConsumer extends Thread {
	final static String clientId = "SimpleConsumerDemoClient43";
	final static String TOPIC = "zillowSearchTaskWithData233";
	ConsumerConnector consumerConnector;

	public static void main(String[] argv) throws UnsupportedEncodingException {
		HelloKafkaConsumer helloKafkaConsumer = new HelloKafkaConsumer();
		helloKafkaConsumer.start();
	}

	// val
	// kafkaConf=Map("metadata.broker.list"->"52.4.219.61:9092,54.164.200.26:9092,54.152.210.81:9092",
	// "zookeeper.connect"->"54.174.139.237:2181",
	// "group.id"->"kafka-streaming",
	// "auto.offset.reset"->"smallest",
	// "zookeeper.connection.timeout.ms"->"1000"
	// )
	//
	public HelloKafkaConsumer() {
		Properties properties = new Properties();
		properties.put("zookeeper.connect", "54.174.139.237:2181");
		properties.put("group.id", "test-group33333333333");
		properties.put("auto.offset.reset", "smallest");
		ConsumerConfig consumerConfig = new ConsumerConfig(properties);
		consumerConnector = Consumer
				.createJavaConsumerConnector(consumerConfig);
	}

	@Override
	public void run() {
		Execution ex = new Execution();
		Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
		topicCountMap.put(TOPIC, new Integer(1));
		Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumerConnector
				.createMessageStreams(topicCountMap);
		KafkaStream<byte[], byte[]> stream = consumerMap.get(TOPIC).get(0);
		     
		ConsumerIterator<byte[], byte[]> it = stream.iterator();
		while (it.hasNext()) {
			String command = (new String(it.next().message()));
			System.out.println(command);
			try {
				Object ob = ex.executeCommand(command);
				if (ob.getClass() == JSONArray.class) {
					JSONArray ar = (JSONArray) ob;
					for (int i = 0; i < ar.length(); i++) {
						if (ar.get(i).getClass() == JSONObject.class) {
							System.out.println("sending");
							JSONObject o = (JSONObject) ar.get(i);
							if (o.getString("task") == "zillowSearch")
								ex.pushToKafka("zillowSearchTaskWithData2", o);
							else
								ex.pushToKafka("zillowDetailTaskWithData", o);

						} else {
							System.out.println(ar.get(i).getClass());
						}
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private static void printMessages(ByteBufferMessageSet messageSet)
			throws UnsupportedEncodingException {
		for (MessageAndOffset messageAndOffset : messageSet) {
			ByteBuffer payload = messageAndOffset.message().payload();
			byte[] bytes = new byte[payload.limit()];
			payload.get(bytes);
			System.out.println(new String(bytes, "UTF-8"));
		}
	}
}
