package com.cmpe295b.spark

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.streaming.Seconds
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.kafka.KafkaUtils
import com.mongodb.casbah.commons.conversions.scala._
import com.mongodb.casbah.Imports._
import com.cmpe295b.Execution
import kafka.serializer.StringDecoder
import org.json.JSONObject
object ReadingFromKafka{
  def main (args: Array[String]) {

  val sparkConf=new SparkConf();
  sparkConf.setAppName("Kafka Streaming")
  sparkConf.setMaster("local[2]")
  
 // sparkConf.set("es.index.auto.create", "true")
 // sparkConf.set("es.nodes", "52.2.120.221")
  sparkConf.set("es.resource", "zip")
  val sc=new SparkContext(sparkConf)
  val ssc= new StreamingContext(sc,Seconds(10))
  val kafkaConf=Map("metadata.broker.list"->"52.4.219.61:9092,54.164.200.26:9092,54.152.210.81:9092",
      "zookeeper.connect"->"54.174.139.237:2181",
      "group.id"->"kafka-streaming",
      "auto.offset.reset"->"smallest",
      "zookeeper.connection.timeout.ms"->"1000"
  )
  val mongoClient = MongoClient("52.2.127.199", 27017)
val db = mongoClient("partb")
 val coll = db("zip")
 
 
 
 
 
  val topics="zipcodetasksCA"
  val topicMap = topics.split(",").toSet
  val execution=new Execution
  val lines = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc,kafkaConf, topicMap).map(_._2)
 lines.foreachRDD(rdd=>{
   if(rdd.count()>0)
   {
   var l= rdd.foreach { x => println(x)
      
      val ab=new JSONObject(x)
      val map=scala.collection.mutable.Map.empty[String,Any]
      val keys=ab.keys()
      while(keys.hasNext())
      {
        val key=keys.next().toString()
        map.put(key, ab.get(key))
      }
    val doc=new MongoDBObject(map)
    coll.insert(doc)
   try
    {
     
    }catch{
      case e:Exception=>{
      
    }}
      map
    }
   }
 })
//  println(rdd.print(10))
 
  ssc.start()
  ssc.awaitTermination()
  }
  
}
