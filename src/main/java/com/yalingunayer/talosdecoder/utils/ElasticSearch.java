package com.yalingunayer.talosdecoder.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElasticSearch {
    private Client client;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public ElasticSearch(String hostname, int port) throws UnknownHostException {
	TransportClient tclient = TransportClient.builder().build();
	InetAddress address = InetAddress.getByName(hostname);
	tclient.addTransportAddress(new InetSocketTransportAddress(address, port));
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
