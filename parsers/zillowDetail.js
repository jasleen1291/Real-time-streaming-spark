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

                    	if(fact.split("\\")[1].trim().split(",").length>1)
                    	js[fact.split("\\")[0].trim()] =fact.split("\\")[1].trim().split(",")

                    		else

                        js[fact.split("\\")[0].trim()] = fact.split("\\")[1].trim()
                    } else
                        js[fact] = true
                }
            });
            $("tr[data-bedroom]").each(function(i, elem) {

console.log($(this).text())
               
            });


            var json = []
            js.type = ($(".status-icon-row").text().trim())
            console.log(js)
                /*   factual.get('/t/places-us', {
                           geo: {
                               "$circle": {
                                   "$center": [js.latitude, js.longitude],
                                   "$meters": 1000
                               }
                           },
                           filters: {
                               category_ids: {
                                   "$includes_any": [415]
                               }
                           }
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
                               /*factual.get('/t/places-us',{geo:{"$circle":{"$center":[js.latitude, js.longitude],"$meters":1000}}, filters:{category_ids:{"$includes_any":[62]}}}, function (error, res) {
                                 console.log(res.data.length)
                                 for(i=0;i<res.data.length;i++)
                                 {
                                 	var obj={}
                                 	var data=res.data[i]
                                 	var category_labels=data.category_labels
                                 	var category=[]
                                 	for(j=0;j<category_labels.length;j++)
                                 	{
                                 		var ar=[]
                                 		for(k=0;k<data.category_labels[j].length;k++)
                                 		{
                                 			ar.push(data.category_labels[j][k])
                                 		}
                                 		console.log(ar)
                                 		category.push(JSON.stringify(data.category_labels[j]))
                                 		
                                 	}
                                 	obj.name=data.name
                                 	obj.distance=data['$distance']
                                 	obj.category=category
                                 	json.push(obj)
                                 }
                               console.log(data)

                               });

                               factual.get('/t/places-us',{geo:{"$circle":{"$center":[js.latitude, js.longitude],"$meters":1000}}, filters:{category_ids:{"$includes_any":[123]}}}, function (error, res) {
                                console.log(res.data.length)
                                 for(i=0;i<res.data.length;i++)
                                 {
                                 	var obj={}
                                 	var data=res.data[i]
                                 	var category_labels=data.category_labels
                                 	var category=[]
                                 	for(j=0;j<category_labels.length;j++)
                                 	{
                                 		var ar=[]
                                 		for(k=0;k<data.category_labels[j].length;k++)
                                 		{
                                 			ar.push(data.category_labels[j][k])
                                 		}
                                 		console.log(ar)
                                 		category.push(JSON.stringify(data.category_labels[j]))
                                 		
                                 	}
                                 	obj.name=data.name
                                 	obj.distance=data['$distance']
                                 	obj.category=category
                                 	json.push(obj)
                                 }


                               console.log(data)


                               });

                               factual.get('/t/places-us',{geo:{"$circle":{"$center":[js.latitude, js.longitude],"$meters":1000}}, filters:{category_ids:{"$includes_any":[23,24,44,51,53,230,312]}},limit:50}, function (error, res) {
                                 
                               console.log(res.data.length)
                                 for(i=0;i<res.data.length;i++)
                                 {
                                 	var obj={}
                                 	var data=res.data[i]
                                 	var category_labels=data.category_labels
                                 	var category=[]
                                 	for(j=0;j<category_labels.length;j++)
                                 	{
                                 		
                               var ar=[]
                                 		for(k=0;k<data.category_labels[j].length;k++)
                                 		{
                                 			ar.push(data.category_labels[j][k])
                                 		}
                                 		console.log(ar)
                                 		category.push(JSON.stringify(data.category_labels[j]))
                                 		
                                 	}
                                 	obj.name=data.name
                                 	obj.distance=data['$distance']
                                 	obj.category=category
                                 	json.push(obj)
                                 }
                                 console.log(data)
                               //console.log(json)
                               });




                               });





                               /*var addres=($('.building-title').text().trim().split(","))
                               console.log(addres);
                               */
                /*
                var addressArray=($('.addr h1').text().trim().split(","));
                //console.log(addressArray[0].trim())
                if(addressArray.length>5){
                json.address=addressArray[0].trim();
                json.city=addressArray[1].trim();
                var stateZipArray=addressArray[2].trim().split(" ");
                json.state=stateZipArray[0].trim();
                json.zip=stateZipArray[1].trim();
                }
                else{
                  var addressArray=($('.building-addr.prop-addr-city.de-emph.notranslate').text().trim().split(","))
                 // console.log(addressArray);
                  json.address=addressArray[0].trim();

                }
                /*

                */
                /*
                var houseSpace=($('.addr h3').text().trim());

                json.houseSpecification=houseSpace.trim();

                var houseisfor=($('.status-icon-row.for-sale-row.home-summary-row').text());
                //console.log(houseisfor);
                json.housefor=houseisfor.trim();

                var amount=($('.main-row.home-summary-row').text());
                //console.log(amount);
                json.price=amount;

                var detail=($('.notranslate').text());
                json.description=detail;


                // Facts Contains Fact and Feature Block Can you check is this right or need to more in this
                var fact=($('.fact-group-container.zsg-content-component.top-facts').text().trim().split());
                if(fact.length>0){
                json.facts=fact;
                }
                else {
                  var fact=($('.building-attrs-group.zsg-content-component').text()).trim().split(",");
                  json.facts=fact;
                }

                var moredetail=($('.fact-group-container.zsg-content-component.z-moreless-content').text().trim().split(","));
                if(moredetail.length>0){
                json.moredetails=moredetail;
                }


                var availableunit=($('.units-list-grouped.zsg-table.building-units-table').text().trim().split(","));
                if(availableunit.length>0){
                json.availableunits=availableunit;
                }
                // Price History not been Added stuck how to add price history
                //document.querySelectorAll(".zsg-table.yui3-toggle-content-minimized tr.null")

                //console.log(priceHistory);
                //School Ratings
                var rating=($(".nearby-schools-list")).text().trim().split("/n");
                json.schoolGrading=rating;


                 //json.housespace=addressArray[3].trim();

                console.log(json);
                */
        }



    })
}