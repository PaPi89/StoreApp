package com.store.interfaces;

import java.util.List;

import com.store.dao.Store;

public interface IStoreService {

	public Store getStore(int storeId);
	
	public int createStore(Store store);
	
	public void updateStore();
	
	public void deleteStore();
	
	public List<Store> findStores();
}
