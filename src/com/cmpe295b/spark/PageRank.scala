package com.cmpe295b.spark
import org.apache.spark.mllib.recommendation.ALS
import org.apache.spark.SparkContext
import org.apache.spark.SparkConf

/**
 * @author hduser
 */
object PageRank {
  def main(args: Array[String]) {
    val ratingsFile = "ratings.csv"
    val sparkConf = new SparkConf();
    sparkConf.setAppName("Kafka Streaming")
    sparkConf.setMaster("local[2]") .set("spark.executor.memory", "1g") 

    val sc = new SparkContext(sparkConf)
    val ratings=sc.textFile(ratingsFile)
      .map { line => 
      val fields=line.split(",")
     // println("fields(2)"+fields(2))
      (fields(0),fields(1),fields(2).toDouble)
    }
    
  /*  val numOfMoviesRatedByEachUser=ratings.groupBy(tup=>tup._1).map(tup=>(tup._1,tup._2.size))
    //numOfMoviesRatedByEachUser.saveAsTextFile("sdjk")
    val numRatersPerMovie=ratings
      .groupBy(tup=>tup._2)
      .map(grouped=>(grouped._1,grouped._2.size))
    val ratingsWithSize=ratings
    .groupBy(tup=>tup._2)
    .join(numRatersPerMovie)
    .flatMap(joined=>{
      joined._2._1.map(f=>(f._1,f._2,f._3,joined._2._2))
    })
    
    val ratings2=ratingsWithSize.keyBy(tup=>tup._1)
    val ratingPairs=ratingsWithSize
    .keyBy(tup=>tup._1)
    .join(ratings2)
    .filter(f=>f._2._1._2<f._2._2._2)
    
    val vectorCals=
      ratingPairs
      .map(data=>{
        val key=(data._2._1._2,data._2._2._2)
        val stats=
          (data._2._1._3 * data._2._2._3,
          data._2._1._3,
          data._2._2._3,
          math.pow(data._2._1._3, 2),
          math.pow(data._2._2._3, 2),
          data._2._1._4,
          data._2._2._4
          )
          (key,stats)
      })
      .groupByKey()
      .map(data=>{
        val key=data._1
        val vals=data._2
        val size=vals.size
        val dotProduct=vals.map(f=>f._1).sum
        val ratingSum=vals.map(f=>f._2).sum
        val rating2Sum=vals.map(f=>f._3).sum
        val ratingSq=vals.map(f=>f._4).sum
        val rating2Sq=vals.map(f=>f._5).sum
        val numRaters=vals.map(f=>f._6).max
        val numRaters2=vals.map(f=>f._7).max
        (key,(size,dotProduct,ratingSum,rating2Sum,ratingSq,rating2Sq,numRaters,numRaters2))
      })
      val similiarities=vectorCals.map(fields=>{
        val key=fields._1
        val (size,dotProduct,ratingSum,rating2Sum,ratingSq,rating2Sq,numRaters,numRaters2)=fields._2
        val corr={
          val numerator=size*dotProduct -ratingSum*rating2Sum
          val denominator=math.sqrt(size * ratingSq - ratingSum * ratingSum) * math.sqrt(size * rating2Sq - rating2Sum * rating2Sum)
          
         // println(numerator+"\t"+denominator)
          numerator/denominator
        }
        (key._1,key._2,corr)
      })
    //  similiarities.saveAsTextFile("fdd")
     val s= similiarities.sortBy(tup=>tup._1)
     s.map(tuple=>(tuple._1,(tuple._2,tuple._3)))
     .reduceByKey((tup1,tup2)=>("",tup1._2+tup2._2)).foreach(tup=>println(tup._1+"\t"+tup._2._2))
     
     // numOfMoviesRatedByEachUser.foreach(println)*/
  }
  def getSimiliarity()
  {
    
  }
}