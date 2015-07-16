package com.cmpe295b.spark

import org.apache.spark.SparkConf
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.Seconds
import org.apache.spark.SparkContext
import org.apache.spark.streaming.kafka.KafkaUtils
import com.cmpe295b.Execution
import kafka.serializer.StringDecoder
import org.json.JSONObject

object ReadingFromKafka{
  def main (args: Array[String]) {

  val sparkConf=new SparkConf();
  sparkConf.setAppName("Kafka Streaming")
  sparkConf.setMaster("local[2]")
  val sc=new SparkContext(sparkConf)
  val ssc= new StreamingContext(sc,Seconds(10))
  val kafkaConf=Map("metadata.broker.list"->"52.4.219.61:9092,54.164.200.26:9092,54.152.210.81:9092",
      "zookeeper.connect"->"54.174.139.237:2181",
      "group.id"->"kafka-streaming",
      "auto.offset.reset"->"smallest",
      "zookeeper.connection.timeout.ms"->"1000"
  )
  val topics="zipcodetasks"
  val topicMap = topics.split(",").toSet
  val execution=new Execution
  val lines = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc,kafkaConf, topicMap).map(_._2)
  lines.foreachRDD(rdd=>{
    rdd.foreach { task =>
      
      println(task)
      //execution.executeCommand(task) 
      
      try
      {
        val ab:JSONObject=new JSONObject(task)
        println(task)
     
        execution.executeCommand(task)
      
      }
      catch{
        case e: Exception => println("exception caught: " + e);
      }
      }
  })
  ssc.start()
  ssc.awaitTermination()
  }
  
}