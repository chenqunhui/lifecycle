package com.ch.mq.kafka;

import java.util.Properties;
import java.util.concurrent.Future;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;


public class LocalProduce implements InitializingBean{
	
	private static Logger logger = Logger.getLogger(LocalProduce.class);
	
	private static Producer<String, String> producer = null;
	
	private String topic;
	private String keySerializer = "org.apache.kafka.common.serialization.StringSerializer";
	private String valueSerializer = "org.apache.kafka.common.serialization.StringSerializer";
	private String zkConnect;
	private String bootstrapServers;
	
	public Future<RecordMetadata> sendMsg(String message){
		ProducerRecord<String,String> record = new ProducerRecord<String,String> (topic,message);
		return producer.send(record);
	}

	

	@Override
	public void afterPropertiesSet() throws Exception {
		if(null != producer){
			return;
		}
		synchronized(LocalProduce.class){
			if(null != producer){
				return;
			}
			if(StringUtils.isEmpty(topic)){
				logger.error("Topic can not be null,init TrackProducer failed !");
				throw new Error("TrackProduct init error !");
			}
			Properties props = new Properties();
			props.put("zk.connect", this.zkConnect);
			props.put("bootstrap.servers", this.bootstrapServers);
			props.put("acks", "1");
			props.put("retries", 0);
			props.put("batch.size", 16384);
			props.put("metadata.fetch.timeout.ms", 3000l);
			props.put("key.serializer", this.keySerializer);
	        props.put("value.serializer", this.valueSerializer);
	        producer = new KafkaProducer<String, String>(props);
	        if(logger.isDebugEnabled())
	        logger.debug("TrackProducer [topic:"+topic+"] init success!");
		}
	}
	
	public void close(){
		if(null != producer){
			try{
				producer.close();
			}catch(Exception e){
				
			}
		}
	}
	
	
	
	public static void main(String[] args){
		/*props.put("zk.connect", "172.16.9.184:2181");
		props.put("bootstrap.servers", "172.16.9.184:9095");
		//ProducerConfig  config1 = new ProducerConfig(prop);
		props.put("key.serializer", );
        props.put("value.serializer", );
		//Producer<String, String> producer = new KafkaProducer<String, String>(props);
		//KeyedMessage<String, String> msg = new KeyedMessage<String, String>("test-topic", );
		//
		
		//producer.close();
		
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("group.id", "local");
		props.put("partition.assignment.strategy", "roundrobin");
*/
		
	}



	public Producer<String, String> getProducer() {
		return producer;
	}



	public void setProducer(Producer<String, String> producer) {
		this.producer = producer;
	}



	public String getTopic() {
		return topic;
	}



	public void setTopic(String topic) {
		this.topic = topic;
	}



	public String getKeySerializer() {
		return keySerializer;
	}



	public void setKeySerializer(String keySerializer) {
		this.keySerializer = keySerializer;
	}



	public String getValueSerializer() {
		return valueSerializer;
	}



	public void setValueSerializer(String valueSerializer) {
		this.valueSerializer = valueSerializer;
	}



	public String getZkConnect() {
		return zkConnect;
	}



	public void setZkConnect(String zkConnect) {
		this.zkConnect = zkConnect;
	}



	public String getBootstrapServers() {
		return bootstrapServers;
	}



	public void setBootstrapServers(String bootstrapServers) {
		this.bootstrapServers = bootstrapServers;
	}
	

}
