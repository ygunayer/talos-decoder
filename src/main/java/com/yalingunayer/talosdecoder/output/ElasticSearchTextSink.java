package com.yalingunayer.talosdecoder.output;

import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.io.IOUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
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
    private final static String INDEX_NAME = "talos";
    private final static String TYPE_NAME = "text";

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private AtomicBoolean prepared;
    private ElasticSearch elastic;
    private String indexName;

    public ElasticSearchTextSink(String hostname, int port) throws UnknownHostException {
        this(hostname, port, INDEX_NAME);
    }

    public ElasticSearchTextSink(String hostname, int port, String indexName) throws UnknownHostException {
        this.prepared = new AtomicBoolean(false);
        this.elastic = new ElasticSearch(hostname, port);
        this.indexName = indexName;
    }

    private synchronized boolean indexExists() {
        IndicesExistsRequest req = new IndicesExistsRequest(indexName);
        Client client = elastic.client();
        IndicesExistsResponse response = client.admin().indices().exists(req).actionGet();
        return response.isExists();
    }

    private synchronized void prepare() {
        if (prepared.get())
            return;

        if (this.indexExists()) {
            logger.info("Index already exists, skipping...");
            return;
        }

        logger.info("Preparing ElasticSearch index {}", indexName);

        final ClassLoader cl = getClass().getClassLoader();
        InputStream in = null;
        try {
            Client client = elastic.client();

            in = cl.getResourceAsStream("mapping.json");
            String requestBody = IOUtils.toString(in, Charset.forName("utf-8"));

            CreateIndexRequest req = new CreateIndexRequest(indexName);
            req.source(requestBody);
            client.admin().indices().create(req).actionGet();
            logger.info("Successfully created index {} and set mappings for {}", indexName, TYPE_NAME);

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
        IndexResponse response = client.prepareIndex(indexName, TYPE_NAME, entry.getInput().getKey()).setSource(bytes).execute().get();
        logger.debug("Index for {} was {}", key, response.isCreated() ? "created" : "not created");

        return e;
    }

    @Override
    public Collection<OutputTextEntry> process(Collection<InputTextEntry> entries) {
        prepare();
        return super.process(entries);
    }
}
