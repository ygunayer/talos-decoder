package com.yalingunayer.talosdecoder.output;

import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.io.IOUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yalingunayer.talosdecoder.dto.FlatOutputTextEntry;
import com.yalingunayer.talosdecoder.dto.InputTextEntry;
import com.yalingunayer.talosdecoder.dto.OutputTextEntry;
import com.yalingunayer.talosdecoder.utils.ElasticSearch;
import com.yalingunayer.talosdecoder.utils.SerializationUtils;

public class ElasticSearchTextSink extends PostProcessingTextSink<FlatOutputTextEntry> {
    private final static String INDEX_NAME = "talos-texts";
    private final static String TYPE_NAME = "text";

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private AtomicBoolean prepared;
    private ElasticSearch elastic;

    public ElasticSearchTextSink(String hostname, int port) throws UnknownHostException {
	prepared = new AtomicBoolean(false);
	elastic = new ElasticSearch(hostname, port);
    }

    private synchronized void prepare() {
	if (prepared.get())
	    return;
	logger.info("Preparing ElasticSearch index and mappings...");

	final ClassLoader cl = getClass().getClassLoader();
	InputStream in = null;
	try {
	    Client client = elastic.client();

	    in = cl.getResourceAsStream("create-index.json");
	    String requestBody = IOUtils.toString(in, Charset.forName("utf-8"));

	    CreateIndexRequest req = new CreateIndexRequest(INDEX_NAME);
	    req.source(requestBody);
	    client.admin().indices().create(req).actionGet();
	    logger.info("Successfully created index {} and set mappings for {}", INDEX_NAME, TYPE_NAME);

	    prepared.set(true);
	} catch (IOException ex) {
	    throw new RuntimeException("Failed to prepare mappings", ex);
	} finally {
	    IOUtils.closeQuietly(in);
	}
    }

    @Override
    protected FlatOutputTextEntry postProcess(OutputTextEntry entry) throws Exception {
	FlatOutputTextEntry e = new FlatOutputTextEntry(entry);
	byte[] bytes = SerializationUtils.mapper().writeValueAsBytes(e);
	String key = entry.getInput().getKey();

	Client client = elastic.client();
	// TODO maybe wrap these in futures and collect them in the
	// process() method instead
	IndexResponse response = client.prepareIndex("talos-texts", "text", entry.getInput().getKey()).setSource(bytes)
		.execute().get();
	logger.debug("Index for {} was {}", key, response.isCreated() ? "created" : "not created");

	return e;
    }

    @Override
    public Collection<OutputTextEntry> process(Collection<InputTextEntry> entries) {
	prepare();
	return super.process(entries);
    }
}
