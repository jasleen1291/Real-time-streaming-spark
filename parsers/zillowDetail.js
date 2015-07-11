module.exports = function(fs,request,cheerio,task)
{

request(task.url, function(error, response, html){
    if(!error){
        var $ = cheerio.load(html);
	console.log($("li.shallow span").text());

}
})
}
