module.exports = function(fs, request, cheerio, task, out) {



        //console.log(task.url);
        request(task.url, function(error, response, html) {
                if (!error) {
                    var $ = cheerio.load(html);


                    var json = {};
                    var basicdata = ($('.basicData table tr td'));

                    basicdata.each(function(i, elem) {
                        var key = ($(this).find("span").text())

                        json[key] = $(this).text().replace(key, "");
                    });

                    var soldTitle = ($('span.soldTitle'));
                    soldTitle.each(function(i, elem) {
                        var value = ($(this).next().text())

                        json[$(this).text()] = value
                    });

                    var tables = ($('.tableInfoValue table'));
                    tables.each(function(i, elem) {
                        var key = ($(this).find("tr th"))

                        if (key.length == 1) {
                            json[key.text()] = {}
                            var rows = key.parent().nextAll()
                            rows.each(function(i, elem) {
                                var tds = $(this).find("td")
                                var currentKey;
                                tds.each(function(i, elem) {

                                    if (i === 0) {
                                        currentKey = $(this).text().replace("\r\n", "").trim()
                                        json[key.text()][currentKey] = {}
                                    } else if (i === 1) {
                                        json[key.text()][currentKey]['number'] = $(this).text().replace("\r\n", "").trim()

                                    } else {
                                        json[key.text()][currentKey]['percentage'] = $(this).text().replace("\r\n", "").replace("(", "").replace(")", "").replace("%", "").trim()

                                    }
                                })


                            });


                        }


                    });

                    //json.info=basicdata;
                    var tables = ($('.tableInfoValue').text().split(","));
                    //console.log(tables);
                    // json.val=tables;
                   
                    request("http://www.city-data.com/zips/99501.html", function(error, response, html) {
                            if (!error) {
                                var $ = cheerio.load(html);
                                var stores = {}
                                $("#businesses tbody tr td:not([style])").each(function(i, elem) {
                                        if (i % 2 == 0) {
                                            stores[$(this).text()] = Number($(this).next().text())
                                        }
                                    })

                                
                                json.stores=(stores)

console.log(json)
                            }


                        
                    });

               

        }})}