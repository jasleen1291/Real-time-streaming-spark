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
 
 
 
 
 
  val topics="zipcodetasksCA"
  val topicMap = topics.split(",").toSet
  val execution=new Execution
  val lines = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc,kafkaConf, topicMap).map(_._2)
 lines.foreachRDD(rdd=>{
   if(rdd.count()>0)
   {
     
 
   var l= rdd.foreach { x => println(x)
      
     if(new JSONObject(x).get("zip").toString().toInt>96162)
     {
       println(x)
       
      val mongoClient = MongoClient("52.2.127.199", 27017)
      val db = mongoClient("partb")
       val coll = db("zip")
      val ab=new JSONObject(execution.executeCommand(x))
      val map=scala.collection.mutable.Map.empty[String,Any]
      val levelOneKeys=ab.keys()
      while(levelOneKeys.hasNext())
      {
        val level1key=levelOneKeys.next().toString().replaceAll("\\.", "_")
         try
        {
            val obj=new JSONObject(ab.get(level1key).toString());
            val innerMap=scala.collection.mutable.Map.empty[String,Any]
            val levelTwoKeys=obj.keys()
            while(levelTwoKeys.hasNext())
            {
              val level2key=levelTwoKeys.next().toString().replaceAll("\\.", "_")
             try
              {
               val innerInner=new JSONObject( obj.get(level2key).toString())
               
               val innerInnerMap=scala.collection.mutable.Map.empty[String,Any]
               val levelThreeKeys=innerInner.keys()
               //println(levelThreeKeys)
               while(levelThreeKeys.hasNext())
               {
                 val level3=levelThreeKeys.next().toString().replaceAll("\\.", "_")
                 //println(level3)
                 innerInnerMap.put(level3, innerInner.get(level3).toString())
               }
              // //println(innerInnerMap)
               innerMap.put(level2key, new MongoDBObject(innerInnerMap))
              }
              catch{
                case e:Exception=>
                innerMap.put(level2key.replaceAll("\\.", "_"), obj.get(level2key).toString())
              }
            }
            map.put(level1key.replaceAll("\\.", "_"), new MongoDBObject(innerMap))
        }catch
        {
          case e:Exception=>{
             map.put(level1key,ab.get(level1key).toString())
          }
        }
           
          
      }
    val doc=new MongoDBObject(map)
    coll.insert(doc)
   try
    {
     
    }catch{
      case e:Exception=>{
      
    }}
      map
    }}
   }
 })
//  //println(rdd.print(10))
 
  ssc.start()
  ssc.awaitTermination()
  }
  
}
