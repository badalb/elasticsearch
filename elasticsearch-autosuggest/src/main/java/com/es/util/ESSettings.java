package com.es.util;

import java.io.IOException;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

public class ESSettings {

	public static XContentBuilder getSettings() throws IOException {
		
		   XContentBuilder settings = XContentFactory.jsonBuilder()
		            .startObject()
		                 .startObject("index")
		                      .startObject("analysis")
		                           .startObject("analyzer")                 
		                                .startObject("ngram_analyzer")             
		                                      .field("tokenizer", "whitespace")
		                                      .field("type", "custom")
		                                      .field("filter", new String[]{ "lowercase", "asciifolding", "ngram_filter"})
		                                .endObject()
		                                //added more
		                                .startObject("whitespace_analyzer")             
		                                      .field("tokenizer", "whitespace")
		                                      .field("type", "custom")
		                                      .field("filter", new String[]{ "lowercase", "asciifolding"})
		                                .endObject()
		                            .endObject()
		                            .startObject("filter")
		                            .startObject("ngram_filter")
		                                 .field("type", "nGram")
		                                 .field("min_gram", "2")
		                                 .field("max_gram", "20")
		                                 .field("token_chars", new String[]{ "letter", "digit", "punctuation", "symbol"})
		                            .endObject()  
		                            .endObject()
		                      .endObject()
		                 .endObject()
		            .endObject();
		   
		   return settings;
	}

}
