package com.store.impls;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.store.dao.Store;
import com.store.dao.interfaces.IStoreDAO;
import com.store.interfaces.IStoreService;

@Service
public class StoreService implements IStoreService {

	@Autowired
	IStoreDAO storeDAO;
	
	private static final Logger logger = Logger.getLogger(StoreService.class);
	public Store getStore(int storeId) {
		logger.info("Inside Get Store Service");
		Store store = storeDAO.getStoreById(storeId);
		return store;
	}

	public int createStore(Store store) {
		logger.info("Inside Create Store Service");
		int storeId = 0;
		if(validate(store)){
			try {
				storeId = storeDAO.save(store);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
		return storeId;
	}

	public void updateStore() {

	}

	public void deleteStore() {

	}

	public List<Store> findStores() {
		return null;
	}

	private boolean validate(Store store){
		boolean valid = false;
		if ((store.getStoreName() != null)
				&& (store.getContact().getAddressLine1() != null)
				&& (store.getContact().getCity() != null)
				&& (store.getContact().getState() != null)
				&& (store.getContact().getCountry() != null)
				&& (store.getContact().getZipcode() > 0)) {
			valid = true;
		}
		return valid;
	}
}
