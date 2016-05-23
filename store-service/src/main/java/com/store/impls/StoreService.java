package com.store.impls;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.store.dto.Store;
import com.store.interfaces.IStoreService;

@Service
public class StoreService implements IStoreService {

	private static final Logger logger = Logger.getLogger(StoreService.class);
	public Store getStore(int storeId) {
		logger.info("Inside Get Store Service");
		//dummy store object
		Store store = new Store();
		store.setStoreId(1);
		store.setStoreName("BigBazar");
		store.setStoreOwner("ABC");
		store.setStoreDistance(50l);
		
		//pass the id and get the store information from DB.
		return store;
	}

	public int createStore(Store store) {
		logger.info("Inside Create Store Service");
		logger.info(store.toString());
		//logger.info(store.getContact().toString());
		//pass it to dao layer to store in DB.
		return store.getStoreId();
	}

	public void updateStore() {

	}

	public void deleteStore() {

	}

	public List<Store> findStores() {
		return null;
	}

}
