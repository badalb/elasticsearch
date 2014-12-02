package com.es.util;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.client.Client;
import org.elasticsearch.node.Node;

public class IndexMappingClient {

	public static void createIndexMapping() {
		Node node = nodeBuilder().node();
		Client client = node.client();
		
		  client.prepareIndex("javaclient",
		  "test").setSource(putJsonDocument("kolkata", "West Bengal",
		  "700001")).execute().actionGet(); client.prepareIndex("javaclient",
		  "test").setSource(putJsonDocument( "madgaon", "Goa",
		  "440001")).execute().actionGet(); client.prepareIndex("javaclient",
		  "test").setSource(putJsonDocument( "chennai", "tamilnadu",
		 "550001")).execute().actionGet(); client.prepareIndex("javaclient",
		  "test").setSource(putJsonDocument("kolhapur", "Maharashtra",
		  "450011")).execute().actionGet(); client.prepareIndex("javaclient",
		  "test").setSource(putJsonDocument( "sholapur", "Maharashtra",
		  "460001")).execute().actionGet(); client.prepareIndex("javaclient",
		  "test").setSource(putJsonDocument( "nagpur", "tamilnadu",
		  "450001")).execute().actionGet();
		 
		client.close();
		node.close();
	}
	

	public static Map<String, Object> putJsonDocument(
			String city, String state, String zip) {

		Map<String, Object> jsonDocument = new HashMap<String, Object>();
		jsonDocument.put("city", city);
		jsonDocument.put("state", state);
		jsonDocument.put("zip", zip);

		return jsonDocument;
	}
}
