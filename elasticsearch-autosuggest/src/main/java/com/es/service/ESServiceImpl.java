package com.es.service;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.node.Node;
import org.elasticsearch.search.SearchHit;
import org.springframework.stereotype.Service;

import com.es.domian.Address;
import com.es.util.EsUtils;
import com.es.util.IndexMappingClient;


@Service
public class ESServiceImpl implements ESService{

	@Override
	public void initialize() {
		
		try {
			EsUtils.applyMapping("javaclient", "test", "src/main/resources/mapping.json");
		} catch (Exception e) {
			e.printStackTrace();
		}

		IndexMappingClient.createIndexMapping();
		
	}

	@Override
	public List<Address> search(String index, String val) {
		
		Node node = nodeBuilder().node();
		Client client = node.client();
		List<Address> list= searchAutoFillDocument(client, index, val);
		client.close();
		return list;
	}

	private static List<Address> searchAutoFillDocument(Client client, String index, String value) {


		SearchResponse response = client.prepareSearch(index)
				.setSearchType(SearchType.QUERY_AND_FETCH)
				.setQuery(QueryBuilders.queryString(value)).setFrom(0)
				.setSize(60).setExplain(true).execute().actionGet();

		SearchHit[] results = response.getHits().getHits();

		System.out.println("Current results: " + results.length);
		List<Address> addressList = new ArrayList<Address>();
		
		for (SearchHit hit : results) {
			//System.out.println("------------------------------");
			Map<String, Object> result = hit.getSource();
			Address address = new Address();
			address.setCity((String) result.get("city"));
			address.setState((String) result.get("state"));
			address.setZip((String) result.get("zip"));
			//System.out.println(result);
			addressList.add(address);
			
		}
	
		return addressList;
		
	}
	
}
