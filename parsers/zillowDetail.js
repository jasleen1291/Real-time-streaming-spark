module.exports = function(fs, request, cheerio, task, factual, obj) {



//console.log(task.url);
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
            var units=false;
            $("tr[data-bedroom]").each(function(i, elem) {
                units=true;
                $(this).find("a").each(function(i, elem) {
                    var t={}
                    t.url=$(this).attr("href");
                    t.parser="zillowSearch"
                    t.task="zillowSearch"
                    t.data=task.data
                    console.log(t)
                });

            });

if(units!==true)
console.log("hi")
            var json = []
            

/*
            factual.get('/t/places-us', {
                geo: {
                    "$circle": {
                        "$center": [js.latitude, js.longitude],
                        "$meters": 1000
                    }
                },
                filters: {
                    category_ids: {
                        "$includes_any": [23, 24, 44, 51, 53, 230, 312]
                    }
                },
                limit: 50
            }, function(error, res) {

                console.log(res.data.length)
                for (i = 0; i < res.data.length; i++) {
                    var obj = {}
                    var data = res.data[i]
                    var category_labels = data.category_labels
                    var category = []
                    for (j = 0; j < category_labels.length; j++) {

                        var ar = []
                        for (k = 0; k < data.category_labels[j].length; k++) {
                            ar.push(data.category_labels[j][k])
                        }
                        console.log(ar)
                        category.push(JSON.stringify(data.category_labels[j]))

                    }
                    obj.name = data.name
                    obj.distance = data['$distance']
                    obj.category = category
                    json.push(obj)
                }
                console.log(data)
                    //console.log(json)
            });

*/


      




}



})
}