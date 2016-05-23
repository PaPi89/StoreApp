package com.store.dao.interfaces;

import java.sql.SQLException;

import com.store.dao.Store;

public interface IStoreDAO {

	public int save(Store store) throws SQLException;
	
	public Store getStoreById(int storeId);
	
	public void update();
	
	public void delete();
}
