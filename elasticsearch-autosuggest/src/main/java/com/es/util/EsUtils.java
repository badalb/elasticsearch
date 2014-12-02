package com.es.util;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicBoolean;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequestBuilder;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.node.Node;
import org.junit.Assert;

public class EsUtils {

	public static void applyMapping(String index, String type, String location)
			throws Exception {

		String source = readJsonDefn(location);
		Node node = nodeBuilder().node();
		Client client = node.client();

		if (source != null) {
			client.admin().indices().prepareDelete(index).execute().actionGet();

			CreateIndexRequest indexRequest = new CreateIndexRequest(index);
			indexRequest.settings(ESSettings.getSettings());
			indexRequest.mapping(type, source);
			CreateIndexResponse response = client.admin().indices()
					.create(indexRequest).actionGet();
			Assert.assertNotNull(response);

		} else {
			System.out.println("mapping error");
		}

		client.close();
	}

	public static String readJsonDefn(String url) throws Exception {

		StringBuffer bufferJSON = new StringBuffer();

		FileInputStream input = new FileInputStream(
				new File(url).getAbsolutePath());
		DataInputStream inputStream = new DataInputStream(input);
		BufferedReader br = new BufferedReader(new InputStreamReader(
				inputStream));

		String line;

		while ((line = br.readLine()) != null) {
			bufferJSON.append(line);
		}
		br.close();
		return bufferJSON.toString();
	}

	static class MappingListener implements Runnable {
		PutMappingRequestBuilder requestBuilder;
		public AtomicBoolean processComplete;
		PutMappingActionListener actionListener;

		public void run() {
			try {
				requestBuilder.execute(actionListener);
			} catch (Exception e) {
				e.printStackTrace();
				this.processComplete.set(true);
			}
		}

		public MappingListener(PutMappingRequestBuilder requestBuilder) {
			this.processComplete = new AtomicBoolean(false);
			actionListener = new PutMappingActionListener(processComplete);
			this.requestBuilder = requestBuilder;
		}

		static class PutMappingActionListener implements
				ActionListener<PutMappingResponse> {
			public AtomicBoolean processComplete;

			public PutMappingActionListener(AtomicBoolean processComplete) {
				this.processComplete = processComplete;
			}

			public void onResponse(PutMappingResponse response) {
				if (response.isAcknowledged()) {
					System.out.println("template successfully applied");
				}
				processComplete.set(true);
			}

			public void onFailure(Throwable throwable) {
				System.out.println("error applying mapping : " + throwable);
				throwable.printStackTrace();
				processComplete.set(true);
			}
		}
	}

}
