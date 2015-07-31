package com.cmpe295b.spark

import scala.util.Random
import java.io.FileWriter

/**
 * @author hduser
 */
object GenerateRatings {
  def main(args: Array[String]) {
    import scala.io.Source

    val filename = "testHouses.csv"
    var lines = Source.fromFile(filename).getLines().toList.map { house =>
      val ar = house.split(",");
      //println(ar(0),ar.drop(2))
      (ar(0), ar.drop(2))
    }
    lines=lines.drop(1)
    var users = Source.fromFile("users.csv").getLines().toList.map { house =>
      val ar = house.split(",");
      //println(ar(0),ar.drop(2))
      (ar(0), ar.drop(1))
    }
    users=users.drop(1)
    val fw = new FileWriter("ratings.csv", true)
    val rand = new Random
    users.foreach(user => {
      val ratings = scala.collection.mutable.Map.empty[String, Float]
      while (ratings.keySet.size < 500) 
      
      {
        val index = rand.nextInt(lines.size)
        if (ratings.get(index + "").isDefined) {

        } else {
          val housefeatures = lines(index)._2
          var rating = 1.0f;
          val features = user._2
          //checking bathroom
          if (features(0).toInt<= housefeatures(0).toInt) {
            rating = rating + 1

          }
          //checking bedroom
          if (features(1).toInt <= housefeatures(1).toInt) {
            rating = rating + 1

          }
          //checking buildyear
          if (features(2).toInt >= housefeatures(2).toInt) {
            rating = rating + 1

          }
          //city---HOA

          val numList = List(3,4,5,6,7,8,9,10,13,14,17,18,20,24,25,26,28,29)
          for (a <- numList) {
            if (features(a).equalsIgnoreCase(housefeatures(a))) {
              rating = rating + 1

            }
          }
          //last remodelled year
          if (features(11).toInt <= housefeatures(11).toInt) {
            rating = rating + 1

          }
          //lot_size
          if (features(15).toInt <= housefeatures(15).toInt) {
            rating = rating + 1

          }
          //parking
          if (features(16).toInt <= housefeatures(16).toInt) {
            rating = rating + 1

          }
          //price
          if (features(19).toInt >= housefeatures(19).toInt) {
            rating = rating + 1

          }
          if (features(21).toInt >= housefeatures(21).toInt) {
            rating = rating + 1

          }
          if (features(22).toInt >= housefeatures(22).toInt) {
            rating = rating + 1

          }
          if (features(23).toInt >= housefeatures(23).toInt) {
            rating = rating + 1

          }
          if (features(27).toInt >= housefeatures(27).toInt) {
            rating = rating + 1

          }
          ratings.put(lines(index)._1, rating)
          fw.write(user._1+","+lines(index)._1+","+rating/3.toInt)
          fw.write("\n")
        }
      }
    })
    fw.close()
  }
}