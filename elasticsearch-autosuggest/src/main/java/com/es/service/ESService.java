package com.es.service;

import java.util.List;

import com.es.domian.Address;


public interface ESService {

	public void initialize();

	public List<Address> search(String index, String val);

}
