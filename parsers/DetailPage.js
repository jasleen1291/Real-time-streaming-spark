module.exports = function(fs, request, cheerio, task, out) {
var mongo = require('mongodb');
var monk = require('monk');
var db = monk('52.2.127.199:27017/partb');
var collection = db.get('zip');
var city=task.city.replace("/ /g","-")
var state=task.state
var url="http://www.movoto.com/"+city+"-"+state+"/"+task.zip+"/demographics/"
var url2="http://www.city-data.com/zips/"+task.zip+".html"
      // console.log(url2);
        request(url, function(error, response, html) {
                if (!error) {
                    var $ = cheerio.load(html);


                    var json = {};
                    json.zip=task.zip
                    json.city=task.city
                    var basicdata = ($('.basicData table tr td'));

                    basicdata.each(function(i, elem) {
                        var key = ($(this).find("span").text().trim().replace("/ /g","_").split('.').join('').replace(":","").replace("$/","price_per"))

                        json[key] = $(this).text().replace(key, "");
                    });

                    var soldTitle = ($('span.soldTitle'));
                    soldTitle.each(function(i, elem) {
                        var value = ($(this).next().text())

                        json[$(this).text().split("$").join("").split(".").join("")] = value
                    });

                    var tables = ($('.tableInfoValue table'));
                    tables.each(function(i, elem) {
                        var key = ($(this).find("tr th"))

                        if (key.length == 1) {
                        	keytext=key.text().split(".").join("")
                            json[keytext] = {}
                            var rows = key.parent().nextAll()
                            rows.each(function(i, elem) {
                                var tds = $(this).find("td")
                                var currentKey;
                                tds.each(function(i, elem) {

                                    if (i === 0) {
                                        currentKey = $(this).text().replace("\r\n", "").trim().split("$").join("").split(".").join("")
                                        json[keytext][currentKey] = {}
                                    } else if (i === 1) {
                                        json[keytext][currentKey]['number'] = $(this).text().replace("\r\n", "").trim()

                                    } else {
                                        json[keytext][currentKey]['percentage'] = $(this).text().replace("\r\n", "").replace("(", "").replace(")", "").replace("%", "").trim()

                                    }
                                })


                            });


                        }


                    });

                    //json.info=basicdata;
                    var tables = ($('.tableInfoValue').text().split(","));
                   //console.log(tables);
                    // json.val=tables;
                    var Entities = require('html-entities').XmlEntities;
 
entities = new Entities();
                  // console.log(json)
                    request(url2, function(error, response, html) {
                            if (!error) {
                                var $ = cheerio.load(html);
                                var stores = {}
                                $("#businesses tbody tr td:not([style])").each(function(i, elem) {
                                        if (i % 2 == 0) {
                                        	
                                            stores[entities.decode($(this).html())] = Number($(this).next().text())
                                        }
                                    })

                                
                                json.stores=(stores)

                                console.log(json)
                                process.exit()
                            }else
                            	{
                            	  process.exit()
                            	}


                        
                    });

               

        }})}