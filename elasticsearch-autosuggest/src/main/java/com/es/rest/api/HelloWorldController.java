package com.es.rest.api;

import java.util.List;

import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.es.domian.Address;
import com.es.service.ESService;
import com.google.gson.Gson;

@RestController
@RequestMapping({ "/api/v1", "" })
public class HelloWorldController {

	@Autowired
	private ESService esService;

	@RequestMapping(value = "/es/create", method = RequestMethod.GET)
	public String create() {

		esService.initialize();
		return "Hello World: Index created";
	}

	@RequestMapping(value = "/es/search/{param}", method = RequestMethod.GET)
	public String search(@PathVariable String param) {

		List<Address> resultList = esService.search("javaclient", param);
		Assert.assertNotNull(resultList);
		//return "Hello World: Index search successful";
		return  new Gson().toJson(resultList);
	}
}
