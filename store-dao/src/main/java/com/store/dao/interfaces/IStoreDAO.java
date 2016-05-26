package com.store.dao.interfaces;

import java.sql.SQLException;
import java.util.List;

import com.store.dao.Store;
import com.store.exceptions.StoreNotFoundException;

/**
 * Interface for StoreDAO implementation.
 * @author parth_pithadiya
 *
 */
public interface IStoreDAO {

	public int save(Store store) throws SQLException;

	public Store getStoreById(int storeId) throws SQLException;

	public void update(Store store) throws SQLException, StoreNotFoundException;

	public void delete(int storeId) throws SQLException;

	public List<Store> getStoreWithinXMiles(String storeName, double xMiles,
			int zipcode) throws SQLException;

	public String getPostAddress(int zipcode) throws SQLException;
}
