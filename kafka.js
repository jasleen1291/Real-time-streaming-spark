var kafka = require('kafka-node');
var HighLevelConsumer = kafka.HighLevelConsumer;
var Offset = kafka.Offset;
var Client = kafka.Client;

var topic =  'zillowSearchTaskWithData2';
var client = new Client('54.174.139.237:2181"','consumer'+process.pid);
var payloads = [ { topic: topic }];
var options = {
    groupId: 'kafka-node-group',

    fromBeginning: true
};
var consumer = new HighLevelConsumer(client, payloads, options);
var offset = new Offset(client);

consumer.on('message', function (message) {
    console.log(this.id, message);
});
consumer.on('error', function (err) {
    console.log('error', err);
});
consumer.on('offsetOutOfRange', function (topic) {
    console.log("------------- offsetOutOfRange ------------");
    topic.maxNum = 2;
    offset.fetch([topic], function (err, offsets) {
        var min = Math.min.apply(null, offsets[topic.topic][topic.partition]);
        consumer.setOffset(topic.topic, topic.partition, min);
    });
});/**
 * 
 */