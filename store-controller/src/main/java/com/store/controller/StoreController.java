package com.store.controller;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriUtils;

import sun.awt.CharsetString;

import com.store.dao.Store;
import com.store.exceptions.StoreException;
import com.store.interfaces.IStoreService;

@Controller
@RequestMapping("/store")
public class StoreController {

	private static final Logger logger = Logger.getLogger(StoreController.class);
	@Autowired
	IStoreService storeService;
	@RequestMapping(value="/getStore", method=RequestMethod.GET)
	public @ResponseBody Store getStore(@RequestParam("id") int storeId) throws StoreException{
		logger.info("Inside Get Store Controller");
		return storeService.getStore(storeId);
	}
	
	@RequestMapping(value="/createStore", method=RequestMethod.PUT)
	public @ResponseBody int createStore(@RequestBody Store store){
		logger.info("Inside Create Store Controller");
		return storeService.createStore(store);
	}
	
	@RequestMapping(value="/updateStore", method=RequestMethod.POST)
	public @ResponseBody void  updateStore(){
		logger.info("Inside Update Store Controller");
	}
	
	@RequestMapping(value="/deleteStore", method=RequestMethod.DELETE)
	public void  deleteStore(){
		logger.info("Inside Delete Store Controller");
	}
	
	@RequestMapping(value = "/findStores", method = RequestMethod.GET)
	public @ResponseBody List<Store> findStores(
			@RequestParam("storeName") String storeName,
			@RequestParam("zipcode") int zipcode,
			@RequestParam("miles") double xMiles) {
		logger.info("Inside find Stores Controller");
		List<Store> stores = storeService.findStore(storeName, xMiles, zipcode);
		return stores;
	}
}
