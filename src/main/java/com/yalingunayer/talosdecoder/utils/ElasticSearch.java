package com.yalingunayer.talosdecoder.utils;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElasticSearch {
    private Client client;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public ElasticSearch(String hostname, int port) {
	TransportClient tclient = new TransportClient();
	tclient.addTransportAddress(new InetSocketTransportAddress(hostname, port));
	client = tclient;
	Runtime.getRuntime().addShutdownHook(new Thread(() -> {
	    logger.info("Shutting down ElasticSearch client...");
	    if (client != null)
		client.close();
	}));
    }

    public Client client() {
	return client;
    }
}
