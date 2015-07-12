var fs = require('fs');
var request = require('request');
var cheerio = require('cheerio');
//var parser='imdb';
//require('./parsers/'+parser)(fs,request,cheerio
process.argv.shift();
process.argv.shift();


if(process.argv.length>0)
{
try
{
  task=JSON.parse(process.argv[0]);

  require('./parsers/'+task.task)(fs,request,cheerio,task);
}catch(e)
{
fs.readFile(process.argv[0]+'.json', 'utf8', function (err,data) {
  if (err) {
    return console.log(err);
  }
  var task=(JSON.parse(data));
require('./parsers/'+task.parser)(fs,request,cheerio,task);
});
}
}

