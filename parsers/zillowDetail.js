module.exports = function(fs, request, cheerio, task, factual, obj) {



   // console.log(task.url);
    request(task.url, function(error, response, html) {
        if (!error) {
            var $ = cheerio.load(html);

            var title, release, rating;
            var js = {};
            js.images = [];
            //console.log($('#decluttered-search-results').children().first()['0']);
            $('ol.photos img[href]').each(function(i, elem) {
                js.images.push($(this).attr('href'));
            });

            var latitude = $("meta[itemprop=latitude]")

            if (latitude && latitude.attr("content") != undefined)
                js.latitude = (latitude.attr("content"))

            var longitude = $("meta[itemprop=longitude]")
            js.longitude = (longitude.attr("content"))

            var addressTitle = $('h1').text()
            var cityStateZip = $('h1 span').text()
            addressTitle = addressTitle.replace(cityStateZip, "").trim()
            js.address = addressTitle
            var stateInfo = cityStateZip.split(",")
            if (stateInfo.length > 1) {
                js.city = stateInfo[0];
                var stateZipArray = stateInfo[1].trim().split(" ")
                if (stateZipArray.length > 1) {
                    js.state = stateZipArray[0]
                    js.zip = stateZipArray[1]
                }
            }

            $(".addr_bbs").each(function(i, elem) {
                if (i == 0)
                    js.beds = Number(($(this).text().split(" ")[0]))
                if (i == 1)
                    js.baths = Number(($(this).text().split(" ")[0]))
                if (i == 2)
                    js.area = Number(($(this).text().split(" ")[0]).replace(",", ""))
            });
            $(".main-row.home-summary-row span").each(function(i, elem) {
                if ($(this).children("span").length > 0) {
                    js.price = Number($(this).text().replace($(this).children("span").text(), "").replace("$", "").replace(",", ""))
                }
            })
            js.schools = []
            $(".nearby-schools-info").each(function(i, elem) {
                if (i != 0) {
                    var school = {}
                    school.name = ($(this).children(".nearby-schools-name").text().replace("\n", "").trim());
                    school.grades = ($(this).children(".nearby-schools-grades").text());
                    school.distance = ($(this).children(".nearby-schools-distance").text().replace("mi", "").trim());
                    js.schools.push(school);
                }
            })
            $(".fact-group-container li").each(function(i, elem) {


                var fact = $(this).text()
                if (fact === undefined) {} else
                if (fact.split(":").length > 1) {
                    if (fact.split(":")[1].trim().split(",").length > 1) {
                        js[fact.split(":")[0].trim()] = fact.split(":")[1].trim().split(",")
                    } else
                        js[fact.split(":")[0].trim()] = fact.split(":")[1].trim()
                } else {
                    if (fact.split("\\").length > 1) {

                        if (fact.split("\\")[1].trim().split(",").length > 1)
                            js[fact.split("\\")[0].trim()] = fact.split("\\")[1].trim().split(",")

                        else

                            js[fact.split("\\")[0].trim()] = fact.split("\\")[1].trim()
                    } else
                        js[fact] = true
                }
            });

            js.type = ($(".status-icon-row").text().trim())
            var units = false;
            $("tr[data-bedroom]").each(function(i, elem) {
                units = true;
                $(this).find("a").each(function(i, elem) {
                    var t = {}
                    t.url = $(this).attr("href");
                    t.parser = "zillowDetail"
                    t.task = "zillowDetail"
                    t.data = task.data
                    console.log(t)
                });

            });

            if (units !== true) {

                var MongoClient = require('mongodb').MongoClient,
                    format = require('util').format;

                //connect away
                MongoClient.connect('mongodb://52.2.127.199:27017/partb', function(err, db) {
                    if (err) throw console.log(err);


                    //simple json record
                    //	console.log(JSON.stringify(js));
                    //insert record
                    db.collection('house').insert(js, function(err, records) {


                        db.close();
process.exit(code=0)
                    });
                });




            }
         

        }



    })
}
