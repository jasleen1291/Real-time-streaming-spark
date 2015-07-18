module.exports = function(fs,request,cheerio,task)
{
var countI=0,countJ=1000;
request(task.url, function(error, response, html){
    if(!error){
        var $ = cheerio.load(html);

    var title, release, rating;
    var json = { houses:[]};

$('.property-info').each(function(i, elem) {
  //console.log ($(this).ht;
    
    if($(this).find('.property-address a').attr("href"))
   { countI++

    var property={}
    property.link="http://www.zillow.com"+$(this).find('.property-address a').attr("href");
    property.task="zillowDetail";
    property.type=$(this).find('.listing-type').text();
   if(property.type!=="FORECLOSED"||property.type!=="COMING SOON"||property.link.indexOf("Undisclosed")>0||property.link.indexOf("AuthRequired")>0||property.type!=="PRE-FORECLOSURE (AUCTION)"||property.type.indexOf("MAKE ME M")>0)
     console.log(JSON.stringify(property));
  }else
  {
   countI++
 var property={}
    property.link="http://www.zillow.com"+$(this).find('a').attr("href");
    //console.log($(this).find('.property-address ').html());
    property.task="zillowDetail";
    property.type=$(this).find('.listing-type').text();
    property.data=task.data
   // console.log(countI+"   "+JSON.stringify(property));
     console.log(JSON.stringify(property));



  }
    //
  
});

var next=$(".zsg-pagination_active").next().children("a").attr("href");
if(next)
{
var paginate={};
paginate.task="zillowSearch";
paginate.url="http://www.zillow.com"+next;
console.log(JSON.stringify(paginate));
}


}})}
