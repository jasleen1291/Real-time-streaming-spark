var fs = require('fs');
var request = require('request');
var cheerio = require('cheerio');
//var parser='imdb';
//require('./parsers/'+parser)(fs,request,cheerio
process.argv.shift();
process.argv.shift();

var obj;
if(process.argv.length>0)
{
	var Factual = require('factual-api');
var factual = new Factual(process.argv[1], process.argv[2]);
try
{
  task=JSON.parse(process.argv[0]);


// Read the file and send to the callback
fs.readFile('mapped.json', function(err, data){
	obj = JSON.parse(data)
	require('./parsers/'+task.task)(fs,request,cheerio,task,factual,obj);
})
  
}catch(e)
{
fs.readFile(process.argv[0]+'.json', 'utf8', function (err,data) {
  if (err) {
    return console.log(err);
  }
  var task=(JSON.parse(data));
fs.readFile('mapped.json', function(err, data){
	
	obj = JSON.parse(data)
	require('./parsers/'+task.parser)(fs,request,cheerio,task,factual,obj);
})
});
}

}

