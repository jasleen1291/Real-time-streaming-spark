module.exports = function(fs,request,cheerio,task)
{
var countI=0,countJ=1000;
request(task.url, function(error, response, html){
    if(!error){
        var $ = cheerio.load(html);

    var title, release, rating;
    var json = { houses:[]};
    var tasks=[]
//console.log(task)
$('.property-info').each(function(i, elem) {
  //console.log ($(this).ht;
    
    if($(this).find('.property-address a').attr("href"))
   { countI++

    var property={}
    property.url="http://www.zillow.com"+$(this).find('.property-address a').attr("href");
    property.task="zillowDetail";
    property.type=$(this).find('.listing-type').text();
    property.data=task.data
   if(property.type!=="FORECLOSED"||property.type!=="COMING SOON"||property.link.indexOf("Undisclosed")==-1||property.link.indexOf("AuthRequired")<0||property.type!=="PRE-FORECLOSURE (AUCTION)"||property.type.indexOf("MAKE ME M")<0)
   {
	 tasks.push(property);
 // console.log(property)
  }else
  {
   countI++
 var property={}
    property.url="http://www.zillow.com"+$(this).find('a').attr("href");
    //console.log($(this).find('.property-address ').html());
    property.task="zillowDetail";
    property.type=$(this).find('.listing-type').text();
    property.data=task.data
   // console.log(countI+"   "+JSON.stringify(property));
  tasks.push(property);



  }}
    //
  
});
 // console.log(JSON.stringify(tasks));
var next=$(".zsg-pagination_active").next().children("a").attr("href");
if(next)
{
var paginate={};
paginate.task="zillowSearch";
paginate.url="http://www.zillow.com"+next;
paginate.data=task.data
tasks.push(paginate);

}
console.log(JSON.stringify(tasks));
}
   
})}
