package com.store.interfaces;

import java.util.List;

import com.store.dao.Store;
import com.store.exceptions.InvalidRequestException;
import com.store.exceptions.InvalidSQLException;
import com.store.exceptions.StoreException;
import com.store.exceptions.StoreNotFoundException;

public interface IStoreService {

	public Store getStore(int storeId) throws InvalidSQLException,
			StoreNotFoundException;

	public int createStore(Store store) throws InvalidRequestException,
			InvalidSQLException, StoreException;

	public void updateStore(Store store) throws InvalidSQLException, StoreNotFoundException;

	public void deleteStore(int storeId) throws InvalidSQLException;

	public List<Store> findStore(String storeName, double xMiles, int zipcode)
			throws InvalidSQLException, StoreNotFoundException;
}
