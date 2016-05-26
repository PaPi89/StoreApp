package com.store.dao.impls;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.store.dao.Contact;
import com.store.dao.Store;
import com.store.dao.ZipCodesUSA;
import com.store.dao.interfaces.IStoreDAO;
import com.store.exceptions.StoreNotFoundException;

/**
 * This class has function which does actual database operations.
 * @author parth_pithadiya
 *
 */
@Repository
public class StoreDAOImpl implements IStoreDAO {
	
	private static final Logger logger = Logger.getLogger(StoreDAOImpl.class);
	
	/***** INSERT Queries for StoreApp *****/
	private static final String INSERT_INTO_STORE_ONLY_MANDATORY = "INSERT into store (storeName, storeDistance) values (?,?)";
	private static final String INSERT_INTO_STORE_FULL = "INSERT into store (storeName, storeOwner, storeDistance) values (?,?,?)";
	private static final String INSERT_INTO_CONTACT_ONLY_MANDATORY = "INSERT into contact (addressLine1, city, state, zipcode, storeId) values (?,?,?,?,?)";
	private static final String INSERT_INTO_CONTACT_FULL = "INSERT into contact (addressLine1, addressLine2, city, state, zipcode, storeId) values (?, ?,?,?,?,?)";
	
	/***** UPDATE Queries for StoreApp *****/
	private static final String UPDATE_STORE_INFO = "UPDATE store SET";
	private static final String UPDATE_STORE_CONTACT_INFO = "UPDATE contact SET";
	
	/***** DELETE Queries for StoreApp *****/
	private static final String DELETE_FROM_STORE = "DELETE FROM store where storeId=?";
	private static final String DELETE_FROM_CONTACT = "DELETE FROM contact where storeId=?";
	
	/***** SELECT Queries for StoreApp *****/
	private static final String SELECT_FROM_STORE_BY_ID = "Select s.storeName, s.storeOwner, c.addressLine1, c.city, c.state, c.zipcode FROM store s, contact c WHERE s.storeId = ? AND c.storeId = ?";
	private static final String SELECT_FROM_ZIPCODES = "SELECT city,state FROM usa_zipcode WHERE zipcode=?";
	private static final String SELECT_FROM_STORE = "SELECT storeName, StoreOwner FROM store s, contact c WHERE s.storeId = c.storeId AND s.storeName=? AND c.zipcode=? AND s.storeDistance<?";
	private DataSource dataSource;
	 
	@Autowired
    public void setDataSource(DataSource dataSource) throws SQLException{
        this.dataSource = dataSource;
    }
	
	/***** Below function stores the Store object into store table of database *****/
	public int save(Store store) throws SQLException {
		logger.info("Inside SAVE DAO function");
		int storeId = 0;
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = dataSource.getConnection();
			if ((store.getStoreName() != null)
					&& (store.getStoreOwner() == null)) {
				stmt = conn.prepareStatement(INSERT_INTO_STORE_ONLY_MANDATORY,
						Statement.RETURN_GENERATED_KEYS);
				stmt.setString(1, store.getStoreName());
				stmt.setDouble(2, store.getStoreDistance());
				stmt.executeUpdate();
			} else {
				stmt = conn.prepareStatement(INSERT_INTO_STORE_FULL,
						Statement.RETURN_GENERATED_KEYS);
				stmt.setString(1, store.getStoreName());
				stmt.setString(2, store.getStoreOwner());
				stmt.setDouble(3, store.getStoreDistance());
				stmt.executeUpdate();
			}
			ResultSet resultSet = stmt.getGeneratedKeys();
			if (resultSet.next()) {
				storeId = resultSet.getInt(1);
			}
			resultSet.close();
			stmt.close();
			Contact contact = store.getContact();
			contact.setStoreId(storeId);
			if ((contact.getAddressLine1() != null)
					&& (contact.getCity() != null)
					&& (contact.getState() != null)
					&& (contact.getZipcode() > 0) && (contact.getStoreId() > 0)) {
				stmt = conn
						.prepareStatement(INSERT_INTO_CONTACT_ONLY_MANDATORY);
				stmt.setString(1, contact.getAddressLine1());
				stmt.setString(2, contact.getCity());
				stmt.setString(3, contact.getState());
				stmt.setInt(4, contact.getZipcode());
				stmt.setInt(5, contact.getStoreId());
				stmt.executeUpdate();
			} else {
				stmt = conn.prepareStatement(INSERT_INTO_CONTACT_FULL);
				stmt.setString(1, contact.getAddressLine1());
				stmt.setString(2, contact.getAddressLine2());
				stmt.setString(3, contact.getCity());
				stmt.setString(4, contact.getState());
				stmt.setInt(5, contact.getZipcode());
				stmt.setInt(5, contact.getStoreId());
				stmt.executeUpdate();
			}
		} catch (SQLException e) {
			logger.error("ERROR: " + e);
			throw e;
		} finally {
			stmt.close();
			conn.close();
		}
		return storeId;
	}

	/***** The below function gets the store details by using storeId from database *****/
	public Store getStoreById(int storeId) throws SQLException {
		logger.info("Inside GET BY ID DAO function");
		Store store = new Store();
		Contact contact = new Contact();
		store.setContact(contact);
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = dataSource.getConnection();
			stmt = conn.prepareStatement(SELECT_FROM_STORE_BY_ID);
			stmt.setInt(1, storeId);
			stmt.setInt(2, storeId);

			ResultSet resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				store.setStoreName(resultSet.getString("storeName"));
				store.setStoreOwner(resultSet.getString("storeOwner"));
				store.getContact().setAddressLine1(
						resultSet.getString("addressLine1"));
				store.getContact().setCity(resultSet.getString("city"));
				store.getContact().setState(resultSet.getString("state"));
				store.getContact().setZipcode(resultSet.getInt("zipcode"));
			}
		} catch (SQLException e) {
			logger.error("ERROR: " + e);
			throw e;
		} finally {
			stmt.close();
			conn.close();
		}
		return store;
	}

	/***** Below function updates the store details in database *****/
	public void update(Store store) throws SQLException, StoreNotFoundException {
		logger.info("Inside UPDATE STORE function");
		Connection conn = null;
		PreparedStatement stmt = null;
		String query = null;
		try {
			conn = dataSource.getConnection();
			query = buildStoreUpdateQuery(store);
			stmt = conn.prepareStatement(query);

			int result = stmt.executeUpdate();
			query = null;
			if (result == 0) {
				throw new StoreNotFoundException("User Not Exists");
			}
			stmt.close();
			if (store.getContact() != null) {
				store.getContact().setStoreId(store.getStoreId());
				query = buildContactUpdateQuery(store.getContact());
				stmt = conn.prepareStatement(query);

				result = stmt.executeUpdate();
			}
		} catch (SQLException e) {
			logger.error("ERROR: " + e);
			throw e;
		} catch (StoreNotFoundException e) {
			logger.error("ERROR: " + e);
			throw e;
		}
	}

	/***** Below function deletes the store details from database *****/
	public void delete(int storeId) throws SQLException {
		logger.info("Inside DELETE STORE function");
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = dataSource.getConnection();
			stmt = conn.prepareStatement(DELETE_FROM_CONTACT);
			stmt.setInt(1, storeId);

			stmt.executeUpdate();
			stmt.close();

			stmt = conn.prepareStatement(DELETE_FROM_STORE);
			stmt.setInt(1, storeId);

			stmt.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} finally {
			stmt.close();
			conn.close();
		}
	}
	
	/*****
	 * Below function is used to get the destination address for GOOGLE's
	 * distance matrix API
	 *****/
	public String getPostAddress(int zipcode) throws SQLException {
		logger.info("Inside GET POSTAL ADDRESS function");
		Connection conn = null;
		PreparedStatement stmt = null;
		ZipCodesUSA zipCodesUSA = new ZipCodesUSA();
		try {
			conn = dataSource.getConnection();
			stmt = conn.prepareStatement(SELECT_FROM_ZIPCODES);
			stmt.setInt(1, zipcode);

			ResultSet resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				zipCodesUSA.setCity(resultSet.getString("city"));
				zipCodesUSA.setState(resultSet.getString("state"));
			}
			resultSet.close();
			zipCodesUSA.setZipcode(zipcode);
		} catch (SQLException e) {
			logger.error("ERROR: " + e);
			throw e;
		} finally {
			stmt.close();
			conn.close();
		}
		return "" + zipCodesUSA.getZipcode() + "," + zipCodesUSA.getCity()
				+ "," + zipCodesUSA.getState();
	}
	
	/*****
	 * Below function gives the store details from a given zipcode within
	 * certain miles
	 *****/
	public List<Store> getStoreWithinXMiles(String storeName, double xMiles,
			int zipcode) throws SQLException {
		logger.info("Inside GET STORE WITHIN X MILES function");
		Connection conn = null;
		PreparedStatement stmt = null;
		List<Store> stores = null;
		try {
			conn = dataSource.getConnection();
			stmt = conn.prepareStatement(SELECT_FROM_STORE);
			stmt.setString(1, storeName);
			stmt.setInt(2, zipcode);
			stmt.setDouble(3, xMiles);

			ResultSet resultSet = stmt.executeQuery();
			stores = new ArrayList<Store>();
			while (resultSet.next()) {
				Store store = new Store();
				store.setStoreName(resultSet.getString("storeName"));
				store.setStoreOwner(resultSet.getString("storeOwner"));
				stores.add(store);
			}
		} catch (SQLException e) {
			logger.error("ERROR: " + e);
			throw e;
		} finally {
			stmt.close();
			conn.close();
		}
		return stores;
	}

	/*****
	 * Below function is utility function to generate update query dynamically
	 * for store table
	 *****/
	private String buildStoreUpdateQuery(Store store) {
		StringBuilder storeQuery = new StringBuilder(UPDATE_STORE_INFO);
		if ((store.getStoreName() != null) && (store.getStoreOwner() != null)) {
			storeQuery.append(" storeName='" + store.getStoreName()
					+ "', storeOwner='" + store.getStoreOwner() + "'");
		} else if (store.getStoreName() != null) {
			storeQuery.append(" storeName='" + store.getStoreName() + "'");
		} else {
			storeQuery.append(" storeOwner='" + store.getStoreOwner() + "'");
		}
		storeQuery.append(" WHERE storeId=" + store.getStoreId());
		return storeQuery.toString();
	}
	
	/*****
	 * Below function is utility function to generate update query dynamically
	 * for contact table
	 *****/
	private String buildContactUpdateQuery(Contact contact){
		StringBuilder contactQuery = new StringBuilder(UPDATE_STORE_CONTACT_INFO);
		Map<String,String> params = new HashMap<String, String>();
		if(contact.getAddressLine1() != null){
			params.put("addressLine1", contact.getAddressLine1());
		}
		if(contact.getAddressLine2() != null){
			params.put("addressLine2", contact.getAddressLine2());
		}
		if(contact.getCity() != null){
			params.put("city", contact.getCity());
		}
		if(contact.getState() != null){
			params.put("state", contact.getState());
		}
		if(contact.getZipcode() > 0){
			params.put("zipcode", ""+contact.getZipcode());
		}
		if(params.size() > 1){
			for(String key : params.keySet()){
				contactQuery.append(" " + key + "='" + params.get(key) + "',");
			}
			contactQuery.replace(contactQuery.lastIndexOf(","), contactQuery.lastIndexOf(","), "");
		} else {
			for(String key : params.keySet()){
				contactQuery.append(" " + key + "='" + params.get(key) + "'");
			}
		}
		contactQuery.append(" WHERE storeId=" + contact.getStoreId());
		return contactQuery.toString();
	}
}
