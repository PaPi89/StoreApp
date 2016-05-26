package com.store.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.store.dao.Store;
import com.store.exceptions.InvalidRequestException;
import com.store.exceptions.InvalidSQLException;
import com.store.exceptions.StoreException;
import com.store.exceptions.StoreNotFoundException;
import com.store.interfaces.IStoreService;

/**
 * This is REST controller class.
 * It accepts all requests for StoreApp.
 * @author parth_pithadiya
 *
 */
@Controller
@RequestMapping("/store")
public class StoreController {

	private static final Logger logger = Logger.getLogger(StoreController.class);
	@Autowired
	IStoreService storeService;
	
	/**
	 * @PathVariable id - storeId which is stored in backend.
	 * @return Store - if success, returns the store object which will be
	 *         returned as jSON object.
	 */
	/***** REST API to get the store details. *****/
	@RequestMapping(value = "/getStore/{id}", method = RequestMethod.GET)
	public @ResponseBody Store getStore(@PathVariable("id") int storeId)
			throws InvalidSQLException, StoreNotFoundException {
		logger.info("Inside Get Store Controller");
		try {
			return storeService.getStore(storeId);
		} catch (InvalidSQLException e) {
			throw e;
		} catch (StoreNotFoundException e) {
			throw e;
		}
	}
	
	/**
	 * @RequestBody Store - provide a jSON object which will be stored as
	 *              'Store' object.
	 * @return int storeId - if success, returns the storeId of newly created
	 *         object.
	 */
	/***** REST API to create the store. *****/
	@RequestMapping(value = "/createStore", method = RequestMethod.PUT)
	public @ResponseBody int createStore(@RequestBody Store store)
			throws InvalidRequestException, InvalidSQLException, StoreException {
		logger.info("Inside Create Store Controller");
		int storeId = 0;
		try {
			storeId = storeService.createStore(store);
		} catch (InvalidRequestException e) {
			throw e;
		} catch (InvalidSQLException e) {
			throw e;
		} catch (StoreException e) {
			throw e;
		}
		return storeId;
	}
	
	/**
	 * @RequestBody Store - provide a jSON object which will be udapted as
	 *              'Store' object.
	 * @return void
	 */
	/***** REST API to update the store details. *****/
	@RequestMapping(value = "/updateStore", method = RequestMethod.POST)
	public @ResponseBody void updateStore(@RequestBody Store store)
			throws InvalidSQLException, StoreNotFoundException {
		logger.info("Inside Update Store Controller");
		try {
			storeService.updateStore(store);
		} catch (InvalidSQLException e) {
			throw e;
		} catch (StoreNotFoundException e) {
			throw e;
		}
	}
	
	/**
	 * @RPathVariable StoreId - provide an integer.
	 * @return void
	 */
	/***** REST API to delete the store. *****/	
	@RequestMapping(value="/deleteStore/{storeId}", method=RequestMethod.DELETE)
	public @ResponseBody void  deleteStore(@PathVariable("storeId") int storeId) throws InvalidSQLException{
		logger.info("Inside Delete Store Controller");
		try {
			storeService.deleteStore(storeId);
		} catch (InvalidSQLException e) {
			throw e;
		}
	}
	
	/**
	 * @RequestParam String StoreName - provide a string object of store to be searched.
	 * @RequestParam int zipcode - provide a valid zipcode to be searched for the given store.
	 * @RequestParam double miles - provide miles to search store within.
	 * @return Store store - if success, returns the store details
	 */
	/***** REST API to find the store details. *****/
	@RequestMapping(value = "/findStores", method = RequestMethod.GET)
	public @ResponseBody List<Store> findStores(
			@RequestParam("storeName") String storeName,
			@RequestParam("zipcode") int zipcode,
			@RequestParam("miles") double xMiles) throws InvalidSQLException, StoreNotFoundException {
		logger.info("Inside find Stores Controller");
		List<Store> stores = null;
		try {
			stores = storeService.findStore(storeName, xMiles, zipcode);
		} catch (InvalidSQLException e) {
			throw e;
		} catch (StoreNotFoundException e) {
			throw e;
		}
		return stores;
	}
}
