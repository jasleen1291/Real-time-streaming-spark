package com.cmpe295b.spark
import org.apache.spark.mllib.recommendation.ALS
import org.apache.spark.SparkContext
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel
import org.apache.spark.SparkConf
import org.apache.spark.mllib.recommendation.Rating
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel
/**
 * @author hduser
 */
object GenerateRecommendation {
  def main(args: Array[String]) {
    val ratingsFile = "ratings.csv"
    val sparkConf = new SparkConf();
    sparkConf.setAppName("Kafka Streaming")
    sparkConf.setMaster("local[2]").set("spark.executor.memory", "1g")

    val sc = new SparkContext(sparkConf)
    val ratings = sc.textFile(ratingsFile)
      .map { line =>
        val fields = line.split(",")
        // println("fields(2)"+fields(2))
        Rating(fields(0).substring(1).toInt, fields(1).substring(1).toInt, fields(2).toDouble)
      }
 /*   val rank = 10
    val numIterations = 20
    val model = ALS.train(ratings, rank, numIterations, 0.01)

    // Evaluate the model on rating data
    val usersProducts = ratings.map {
      case Rating(user, product, rate) =>
        (user, product)
    }
    val predictions =
      model.predict(usersProducts).map {
        case Rating(user, product, rate) =>
          ((user, product), rate)
      }
    val ratesAndPreds = ratings.map {
      case Rating(user, product, rate) =>
        ((user, product), rate)
    }.join(predictions)
    val MSE = ratesAndPreds.map {
      case ((user, product), (r1, r2)) =>
        val err = (r1 - r2)
        err * err
    }.mean()
    println("Mean Squared Error = " + MSE)

    // Save and load model
    model.save(sc, "myModelPath")
    
    val sameModel = MatrixFactorizationModel.load(sc, "myModelPath")
    val pred = sameModel.predict(100, 100)
    println(pred)
    sameModel.recommendProducts(100, 10).foreach { x => println(x) }*/
     val numOfMoviesRatedByEachUser=ratings.groupBy(tup=>tup.user).map(tup=>(tup._1,tup._2.size))
    //numOfMoviesRatedByEachUser.saveAsTextFile("sdjk")
    val numRatersPerMovie=ratings
      .groupBy(tup=>tup.product)
      .map(grouped=>(grouped._1,grouped._2.size))
    val ratingsWithSize=ratings
    .groupBy(tup=>tup.product)
    .join(numRatersPerMovie)
    .flatMap(joined=>{
      joined._2._1.map(f=>(f.user,f.product,f.user,joined._2._2))
    })
    
    val ratings2=ratingsWithSize.keyBy(tup=>tup._1)
    val ratingPairs=ratingsWithSize
    .keyBy(tup=>tup._1)
    .join(ratings2)
    .filter(f=>f._2._1._2<f._2._2._2)
    val PRIOR_COUNT = 10
  val PRIOR_CORRELATION = 0
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
        val movie1=fields._1
         val movie2=fields._2
        val (size,dotProduct,ratingSum,rating2Sum,ratingSq,rating2Sq,numRaters,numRaters2)=fields._2
       val corr = correlation(size, dotProduct, ratingSum, rating2Sum, ratingSq, rating2Sq)
        val regCorr = regularizedCorrelation(size, dotProduct, ratingSum, rating2Sum, ratingSq, rating2Sq, PRIOR_COUNT, PRIOR_CORRELATION)
        val cosSim = cosineSimilarity(dotProduct, scala.math.sqrt(ratingSq), scala.math.sqrt(rating2Sq))
        val jaccard = jaccardSimilarity(size, numRaters, numRaters2)
        
        (movie1,movie2,corr, regCorr, cosSim, jaccard)
        
       
      }).saveAsTextFile("output.txt")
      
    //  
     
     
     // numOfMoviesRatedByEachUser.foreach(println)
  }
   def correlation(size : Double, dotProduct : Double, ratingSum : Double, 
    rating2Sum : Double, ratingNormSq : Double, rating2NormSq : Double) = {
      
    val numerator = size * dotProduct - ratingSum * rating2Sum
    val denominator = scala.math.sqrt(size * ratingNormSq - ratingSum * ratingSum) * scala.math.sqrt(size * rating2NormSq - rating2Sum * rating2Sum)
    
    numerator / denominator
  }
  
  /**
   * Regularize correlation by adding virtual pseudocounts over a prior:
   *   RegularizedCorrelation = w * ActualCorrelation + (1 - w) * PriorCorrelation
   * where w = # actualPairs / (# actualPairs + # virtualPairs).
   */
  def regularizedCorrelation(size : Double, dotProduct : Double, ratingSum : Double, 
    rating2Sum : Double, ratingNormSq : Double, rating2NormSq : Double, 
    virtualCount : Double, priorCorrelation : Double) = {

    val unregularizedCorrelation = correlation(size, dotProduct, ratingSum, rating2Sum, ratingNormSq, rating2NormSq)
    val w = size / (size + virtualCount)

    w * unregularizedCorrelation + (1 - w) * priorCorrelation
  }  

  /**
   * The cosine similarity between two vectors A, B is
   *   dotProduct(A, B) / (norm(A) * norm(B))
   */
  def cosineSimilarity(dotProduct : Double, ratingNorm : Double, rating2Norm : Double) = {
    dotProduct / (ratingNorm * rating2Norm)
  }

  /**
   * The Jaccard Similarity between two sets A, B is
   *   |Intersection(A, B)| / |Union(A, B)|
   */
  def jaccardSimilarity(usersInCommon : Double, totalUsers1 : Double, totalUsers2 : Double) = {
    val union = totalUsers1 + totalUsers2 - usersInCommon
    usersInCommon / union
  }  
  def getSimiliarity() {

  }
}