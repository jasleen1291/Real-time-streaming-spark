package com.cmpe295b.spark

import org.apache.spark.SparkConf
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.Seconds
import org.apache.spark.SparkContext
import org.apache.spark.streaming.kafka.KafkaUtils
import com.cmpe295b.Execution

object ReadingFromKafka{
  def main (args: Array[String]) {

  val sparkConf=new SparkConf();
  sparkConf.setAppName("Kafka Streaming")
  sparkConf.setMaster("local[2]")
  val sc=new SparkContext(sparkConf)
  val ssc= new StreamingContext(sc,Seconds(10))
 /* val kafkaConf=Map("metadata.broker.list"->"52.4.219.61:9092,54.164.200.26:9092,54.152.210.81:9092",
      "zookeeper.connect"->"172.31.38.38:2181",
      "group.id"->"kafka-streaming",
      "auto.offset.reset"->"smallest",
      "zookeeper.connection.timeout.ms"->"1000"
  )*/
  val topics="zillowSearch,zillowDetail"
  val topicMap = topics.split(",").map((_, 1)).toMap
  val execution=new Execution
  val lines = KafkaUtils.createStream(ssc, "172.31.38.38:2181", "kafka-streaming", topicMap).map(_._2)
  lines.foreachRDD(rdd=>{
    rdd.foreach { task => execution.executeCommand(task) }
  })
  }
}