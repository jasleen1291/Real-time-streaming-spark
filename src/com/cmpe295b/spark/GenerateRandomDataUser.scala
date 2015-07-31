package com.cmpe295b.spark

import java.util.HashMap
import java.util.HashSet
import scala.util.Random
import java.io.FileWriter

/**
 * @author hduser
 */
object GenerateRandomDataUser {
  var bathroom: Integer = _;
  var bedrooms: Integer = _;
  var built_year: Integer = _;
  var city: String = _;
  var cooling: Boolean = _;
  var dishwasher: Boolean = _;
  var dryer: Boolean = _;
  var flooring: String = _;
  var flooringType = Array("Stone", "Carpet", "Vinyl", "Ceramic Tile", "Cork", "Linoleum")
  var garbage_displosure: Boolean = _;
  var heating: Boolean = _;
  var HOA: Boolean = _;
  var last_remodeled_year: Integer = _;
  var last_sold: Integer = _;
  var laundary: Boolean = _;

  var listing_type: String = _;
  var lot_size: Integer = _;
  var parking: Integer = _;
  var pets: Boolean = _;
  var pool: Boolean = _;
  var price: Integer = _;
  var referigrator: Boolean = _;
  var school_elementary: Integer = _;
  var school_middle: Integer = _;
  var school_high: Integer = _;
  var single_family: Boolean = _;
  var spa: Boolean = _;
  var storage: Boolean = _;
  var stories: Integer = _;
  var house_type: String = _;
  var house_type_List = Array("condo", "town house", "single family", "society")
  var rentList = Array(600, 750, 800, 850, 950, 1050, 1100, 1220, 1300, 1800)
  var rentPrice = Array(1000, 1150, 1400, 1475, 1525, 1695, 1880, 2200, 2700, 3500)
  var buyList = Array(600, 850, 1050, 1800, 2200, 2800, 3250, 3400, 3800, 5000)
  var buyPrice = Array(200000, 250000, 350000, 450000, 575000, 690000, 770000, 870000, 925000, 1100000)
  var zip: String = _;
  val city_zip = new HashMap[String, String]()
  val city_neigh = new HashMap[String, HashSet[String]]()
  val city_zip_pruned = new HashMap[String, String]()
  def generateData() {
    import scala.io.Source

    val filename = "a.csv"
    for (line <- Source.fromFile(filename).getLines()) {
      val ab = line.split(",")
      if (ab.length == 2) {
        city_zip.put(ab(1), ab(0))
      }
    }

    val filename2 = "city_neigh.csv"
    for (line <- Source.fromFile(filename2).getLines()) {
      val ab = line.split(",")
      if (ab.length == 2) {

        if (city_neigh.get(ab(0)) == null) {
          val set = new HashSet[String]()
          city_neigh.put(ab(0), set)
        }

        val a = city_neigh.get(ab(0))
        a.add(ab(1))
        city_neigh.put(ab(0), a)

      }
    }
    val it = city_neigh.keySet().iterator()
    while (it.hasNext()) {
      val city = it.next()
      city_zip_pruned.put(city, city_zip.get(city))
    }

  }
  def fixedLength(n: Int) = {

    val rand = new Random()
    val first = rand.nextInt(9) + 1
    val randVect = first +: (1 until n).map { x => rand.nextInt(10) }
    BigInt(randVect.mkString)
  }
  def main(args: Array[String]) {
    generateData();
    val rand = new Random()
    val fw = new FileWriter("users.csv", true)
    try {
      val total = "id" + "," + "bathroom" + "," + "bedrooms" + "," + "built_year" + "," + "city" + "," + "cooling" + "," + "dishwasher" + "," + "dryer" + "," + "flooring" + "," + "garbage_displosure" + "," + "heating" + "," + "HOA" + "," + "last_remodeled_year" + "," + "last_sold" + "," + "laundary" + "," + "listing_type" + "," + "lot_size" + "," + "parking" + "," + "pets" + "," + "pool" + "," + "price" + "," + "referigrator" + "," + "school_elementary" + "," + "school_middle" + "," + "school_high" + "," + "single_family" + "," + "spa" + "," + "storage" + "," + "stories" + "," + "house_type" + "," + "zip"
      //println(total)
      fw.write(total + "\n")
    } finally {}
    //     val mongo = new MongoClient( "52.2.40.219" , 27017 );
    //     val  db = mongo.getDB("partb");
    //     val collection=db.getCollection("houses");
    for (a <- 1 to 200) {
      // val document = new BasicDBObject();
      val cityArray = city_zip_pruned.keySet().toArray()
      val cityS = cityArray(rand.nextInt(cityArray.length)).toString()
      city = cityS
      val neighborArray = city_neigh.get(cityS).toArray()
      val neighbor = neighborArray(rand.nextInt(neighborArray.length))
      // address=fixedLength(4)+" "+neighbor
      //document.append("address", address)
      zip = city_zip_pruned.get(city)
      //document.append("zip", zip)
      bedrooms = (rand.nextInt(4)) + 1
      //document.append("bedrooms", bedrooms)
      if (bedrooms > 1) {
        bathroom = rand.nextInt(bedrooms - 1) + 1;
        //document.append("bathroom", bathroom)
      } else {
        bathroom = 1
        //document.append("bathroom", bathroom)
      }
      
      //document.append("city", city)
      built_year = rand.nextInt(2015 - 1970) + 1970;
      //document.append("built_year", built_year)
      cooling = rand.nextBoolean()
      //document.append("cooling", cooling)
      dishwasher = rand.nextBoolean()
      //document.append("dishwasher", dishwasher)
      dryer = rand.nextBoolean()
      //document.append("dryer", dryer)
      flooring = flooringType.apply(rand.nextInt(flooringType.length))
      //document.append("flooring", flooring)
      garbage_displosure = rand.nextBoolean()
      //document.append("garbage_displosure",  garbage_displosure)
      heating = rand.nextBoolean()
      //document.append("heating",  heating)
      last_remodeled_year = rand.nextInt(2015 - built_year) + built_year
      //document.append("last_remodeled_year",  last_remodeled_year)
      HOA = rand.nextBoolean()
      //document.append("HOA",  HOA)
      last_sold = rand.nextInt(2015 - built_year) + built_year
      //document.append("last_sold", last_sold)
      laundary = rand.nextBoolean()
      //document.append("laundary", laundary)
      if (rand.nextBoolean()) {
        listing_type = "Sale"
        //document.append("listing_type", listing_type)
      } else {
        listing_type = "Rent"
        //document.append("listing_type", listing_type)
      }
      if (listing_type.equalsIgnoreCase("Sale")) {
        val index = rand.nextInt(buyList.length)
        lot_size = buyList(index) + ((rand.nextInt(5) + 1) / buyList(index)) * 100
        //document.append("lot_size", lot_size)
        price = buyPrice(index) + ((rand.nextInt(5) + 1) / buyList(index)) * 100
        //document.append("price", price)
      } else {
        val index = rand.nextInt(rentList.length)
        lot_size = rentList(index) + ((rand.nextInt(5) + 1) / rentList(index)) * 100
        //document.append("lot_size", lot_size)
        price = rentPrice(index) + ((rand.nextInt(5) + 1) / rentList(index)) * 100
        //document.append("price", price)
      }
      parking = rand.nextInt(2) + 1
      //document.append("parking", parking)
      pets = rand.nextBoolean()
      //document.append("pets", pets)
      pool = rand.nextBoolean()
      //document.append("pool", pool)
      referigrator = rand.nextBoolean()
      //document.append("referigrator",referigrator)
      school_elementary = rand.nextInt(11)
      //document.append("school_elementary",school_elementary)
      school_middle = rand.nextInt(11)
      // document.append("school_middle",school_middle)
      school_high = rand.nextInt(11)
      //document.append("school_high",school_high)
      // single_family=rand.nextBoolean()
      spa = rand.nextBoolean()
      ///document.append("spa",spa)
      storage = rand.nextBoolean()
      //document.append("storage",storage)
      stories = rand.nextInt(2) + 1
      //document.append("stories",stories)
      house_type = house_type_List(rand.nextInt(house_type_List.length))
      //document.append("house_type",house_type)
      // single_family=rand.nextBoolean()
      //   collection.insert(document);

      val total = "u" + a + "," + bathroom + "," + bedrooms + "," + built_year + "," + city + "," + cooling + "," + dishwasher + "," + dryer + "," + flooring + "," + garbage_displosure + "," + heating + "," + HOA + "," + last_remodeled_year + "," + last_sold + "," + laundary + "," + listing_type + "," + lot_size + "," + parking + "," + pets + "," + pool + "," + price + "," + referigrator + "," + school_elementary + "," + school_middle + "," + school_high + "," + single_family + "," + spa + "," + storage + "," + stories + "," + house_type + "," + zip
      fw.write(total + "\n")

    }
    fw.close()
  }
}