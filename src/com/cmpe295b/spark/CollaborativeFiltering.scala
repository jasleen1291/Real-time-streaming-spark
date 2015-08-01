package com.cmpe295b.spark
import org.apache.spark.mllib.recommendation.ALS
import org.apache.spark.SparkContext
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel
import org.apache.spark.SparkConf
import org.apache.spark.mllib.recommendation.Rating
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel
/**
 * @author jnkaur
 */
object CollaborativeFiltering {
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
    val rank = 10
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
    
    val sameModel = MatrixFactorizationModel.load(sc, "myModelPat2h34")
    val pred = sameModel.predict(100, 100)
    println(pred)
    sameModel.recommendProducts(100, 10).foreach { x => println(x) }
  }
}