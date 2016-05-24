package com.store.dao.impls;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.store.dao.Contact;
import com.store.dao.Store;
import com.store.dao.ZipCodesUSA;
import com.store.dao.interfaces.IStoreDAO;

@Repository
public class StoreDAOImpl implements IStoreDAO {
	
	private static final Logger logger = Logger.getLogger(StoreDAOImpl.class);
	private static final String INSERT_INTO_STORE_ONLY_MANDATORY = "INSERT into store (storeName, storeDistance) values (?,?)";
	private static final String INSERT_INTO_STORE_FULL = "INSERT into store (storeName, storeOwner, storeDistance) values (?,?,?)";
	private static final String INSERT_INTO_CONTACT_ONLY_MANDATORY = "INSERT into contact (addressLine1, city, state, zipcode, storeId) values (?,?,?,?,?)";
	private static final String INSERT_INTO_CONTACT_FULL = "INSERT into contact (addressLine1, addressLine2, city, state, zipcode, storeId) values (?, ?,?,?,?,?)";
	
	private static final String SELECT_FROM_ZIPCODES = "SELECT city,state FROM usa_zipcode WHERE zipcode=?";
	private static final String SELECT_FROM_STORE = "SELECT storeName, StoreOwner FROM store s, contact c WHERE s.storeId = c.storeId AND s.storeName=? AND c.zipcode=? AND s.storeDistance<?";
	private DataSource dataSource;
	 
	@Autowired
    public void setDataSource(DataSource dataSource) throws SQLException{
        this.dataSource = dataSource;
    }
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
				stmt = conn.prepareStatement(INSERT_INTO_CONTACT_ONLY_MANDATORY);
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
			e.printStackTrace();
			logger.error("ERROR: " + e.getMessage());
		} finally {
			stmt.close();
			conn.close();
		}
		return storeId;
	}

	public Store getStoreById(int storeId) {
		logger.info("Inside GET BY ID DAO function");
		return null;
	}

	public void update() {
		// TODO Auto-generated method stub
		
	}

	public void delete() {
		// TODO Auto-generated method stub
		
	}
	public String getPostAddress(int zipcode) throws SQLException {
		logger.info("Inside GET POSTAL ADDRESS function");
		Connection conn = null;
		PreparedStatement stmt = null;
		ZipCodesUSA zipCodesUSA = new ZipCodesUSA();
		try{
			conn = dataSource.getConnection();
			stmt = conn.prepareStatement(SELECT_FROM_ZIPCODES);
			stmt.setInt(1, zipcode);
			
			ResultSet resultSet = stmt.executeQuery();
			while(resultSet.next()){
				zipCodesUSA.setCity(resultSet.getString("city"));
				zipCodesUSA.setState(resultSet.getString("state"));
			}
			resultSet.close();
			zipCodesUSA.setZipcode(zipcode);
		} catch(SQLException e){
			e.printStackTrace();
			logger.error("ERROR: " + e.getMessage());
		} finally {
			stmt.close();
			conn.close();
		}
		return "" + zipCodesUSA.getZipcode() + "," + zipCodesUSA.getCity()
				+ "," + zipCodesUSA.getState();
	}
	
	public List<Store> getStoreWithinXMiles(String storeName, double xMiles, int zipcode)
			throws SQLException {
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
			while(resultSet.next()){
				Store store = new Store();
				store.setStoreName(resultSet.getString("storeName"));
				store.setStoreOwner(resultSet.getString("storeOwner"));
				stores.add(store);
			}
		} catch(SQLException e){
			e.printStackTrace();
			logger.error("ERROR: " + e.getMessage());
		} finally {
			stmt.close();
			conn.close();
		}
		return stores;
	}

}
