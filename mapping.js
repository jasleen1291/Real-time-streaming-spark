var fs = require('fs'),
    obj

// Read the file and send to the callback
fs.readFile('factual_taxonomy.json', handleFile)

// Write the callback function
function handleFile(err, data) {
    if (err) throw err
    obj = JSON.parse(data)
	var json={}
    for (var key in obj) {
    if (obj.hasOwnProperty(key)) {
        json[key]=obj[key].labels.en
    }

	}
	fs.writeFile("mapped.json", JSON.stringify(json), function(err) {
    if(err) {
        return console.log(err);
    }

    console.log("The file was saved!");
}); 
}