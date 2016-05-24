package com.store.dao.interfaces;

import java.sql.SQLException;
import java.util.List;

import com.store.dao.Store;

public interface IStoreDAO {

	public int save(Store store) throws SQLException;
	
	public Store getStoreById(int storeId);
	
	public void update();
	
	public void delete();
	
	public List<Store> getStoreWithinXMiles(String storeName, double xMiles, int zipcode) throws SQLException;
	
	public String getPostAddress(int zipcode) throws SQLException;
}
