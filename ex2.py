import sys
import json
from Naked.toolshed.shell import execute_js, muterun_js

from kafka import KafkaConsumer

# more advanced consumer -- multiple topics w/ auto commit offset
# management
consumer = KafkaConsumer('zillowSearch2',
                   bootstrap_servers=["52.4.219.61:9092","54.164.200.26:9092","54.152.210.81:9092"],
                   group_id='my_consumer_group',
                   auto_commit_enable=True,
                   auto_commit_interval_ms=30 * 1000,
                   auto_offset_reset='smallest')
from kafka import SimpleProducer, KafkaClient

# To send messages synchronously
kafka = KafkaClient(["52.4.219.61:9092","54.164.200.26:9092","54.152.210.81:9092"])
producer = SimpleProducer(kafka)
# Infinite iteration
for m in consumer:
	name = 'taskss.json'  # Name of text file coerced with +.txt

    	try:
		file = open(name,'w+')   # Trying to create a new file or open one
		file.write(str(m.value))
		file.close()

    	except:
		print('Something went wrong! Can\'t tell what?')
		sys.exit(0) # quit Python
	response=muterun_js('server taskss')
	if response.exitcode==0 and len(response.stdout)>0:
		print("&&"+response.stdout+"&&")			
		li=(json.loads(response.stdout))
		for item in li:
			print item["task"]
			print json.dumps(item)
			producer.send_messages(bytes(item["task"]+"2"), bytes(json.dumps(item)))
