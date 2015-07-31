package com.cmpe295b.spark

import org.apache.spark.SparkConf
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.SQLContext._
import org.elasticsearch.spark.sql._
import org.apache.spark.SparkContext


import org.joda.time.Seconds
object ReadingFromKafka{
  def main (args: Array[String]) {

  val sparkConf=new SparkConf();
  sparkConf.setAppName("Kafka Streaming")
  sparkConf.setMaster("local[2]")
  
  sparkConf.set("es.index.auto.create", "true")
 // sparkConf.set("es.nodes", "52.2.120.221")
  sparkConf.set("es.resource", "zip")
  val sc=new SparkContext(sparkConf)
 // val ssc= new StreamingContext(sc,Seconds(10))
 
 val sqlContext = new org.apache.spark.sql.SQLContext(sc)
val df = sqlContext.read.format("com.databricks.spark.csv").option("header", "true").load("test.csv")
df.printSchema()
//df.collect()
df.saveToEs("partb/houses")

 
 
 /*
 
  val topics="zillowDetail2"
  val topicMap = topics.split(",").toSet
  val execution=new Execution
  val lines = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc,kafkaConf, topicMap).map(_._2)
    lines.foreachRDD { x => x.foreach { x => 
    println(x);
    //  execution.executeCommand(x) 
    } }
  */
  
  
  /* lines.foreachRDD(rdd=>{
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
 })*/
//  //println(rdd.print(10))
 
 // ssc.start()
 // ssc.awaitTermination()
  }
  
}
