package com.store.impls;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.store.dao.Store;
import com.store.dao.interfaces.IStoreDAO;
import com.store.exceptions.InvalidRequestException;
import com.store.exceptions.InvalidSQLException;
import com.store.exceptions.StoreException;
import com.store.exceptions.StoreNotFoundException;
import com.store.interfaces.IStoreService;

/**
 * This class has functions to apply business logic.
 * @author parth_pithadiya
 *
 */
@Service
public class StoreService implements IStoreService {

	@Autowired
	IStoreDAO storeDAO;
	/***** BASE URL for GOOGLE's Distance Matrix API *****/
	private static final String baseURL = "https://maps.googleapis.com/maps/api/distancematrix/json?";
	/*****
	 * API_KEY is required to use GOOGLE's Distance Matrix API. This key must be
	 * generated to find the distance between source and destination.
	 * As of now, it is kept as blank. How to generate the key is provided in README.md file.
	 *****/
	private static final String API_KEY = "";
	
	private static final Logger logger = Logger.getLogger(StoreService.class);
	
	/***** To get the store details *****/
	public Store getStore(int storeId) throws InvalidSQLException,
			StoreNotFoundException {
		logger.info("Inside Get Store Service");
		Store store;
		try {
			store = storeDAO.getStoreById(storeId);
			store.setStoreId(storeId);
			if (store.getStoreName() == null) {
				throw new StoreNotFoundException(
						"The given store is not present.");
			}
		} catch (SQLException e) {
			throw new InvalidSQLException(e.getMessage());
		} catch (StoreNotFoundException e) {
			logger.error("ERROR: " + e);
			throw e;
		}
		return store;
	}

	/***** To save the store details *****/
	public int createStore(Store store) throws InvalidRequestException,
			InvalidSQLException, StoreException {
		logger.info("Inside Create Store Service");
		int storeId = 0;
		double storeDistance = 0l;

		try {
			validate(store);
			storeDistance = calculateDistance(store);
			store.setStoreDistance(storeDistance);
			storeId = storeDAO.save(store);
		} catch (InvalidSQLException e) {
			throw e;
		} catch (InvalidRequestException e) {
			throw e;
		} catch (SQLException e) {
			throw new InvalidSQLException(e.getMessage());
		} catch (StoreException e) {
			throw e;
		}
		return storeId;
	}

	/***** To update the store details *****/
	public void updateStore(Store store) throws InvalidSQLException,
			StoreNotFoundException {
		logger.info("Inside Update Store Service");
		try {
			storeDAO.update(store);
		} catch (SQLException e) {
			throw new InvalidSQLException(e.getMessage());
		} catch (StoreNotFoundException e) {
			throw e;
		}
	}

	/***** To delete the store details *****/
	public void deleteStore(int storeId) throws InvalidSQLException {
		logger.info("Inside Delete Store Service");
		try {
			storeDAO.delete(storeId);
		} catch (SQLException e) {
			throw new InvalidSQLException(e.getMessage());
		}
	}
	
	/***** To find the store details *****/
	public List<Store> findStore(String storeName, double xMiles, int zipcode)
			throws InvalidSQLException, StoreNotFoundException {
		List<Store> stores = null;
		try {
			stores = storeDAO.getStoreWithinXMiles(storeName, xMiles, zipcode);
			if (stores.get(0).getStoreName() == null) {
				throw new StoreNotFoundException(
						"The given store is not present.");
			}
		} catch (SQLException e) {
			throw new InvalidSQLException(e.getMessage());
		}
		return stores;
	}

	/***** Utility function to validate the store details *****/
	private boolean validate(Store store) throws InvalidRequestException {
		boolean valid = false;
		if ((store.getStoreName() != null)
				&& (store.getContact().getAddressLine1() != null)
				&& (store.getContact().getCity() != null)
				&& (store.getContact().getState() != null)
				&& (store.getContact().getZipcode() > 0)) {
			valid = true;
		}
		if (!valid) {
			throw new InvalidRequestException("Missing Mandatory Parameters");
		}
		return valid;
	}
	
	/*****
	 * Utility function to calculate the store distance from a post office of
	 * specified USA zipcode
	 *****/
	private double calculateDistance(Store store) throws InvalidSQLException,
			StoreException {
		String origin = new String(store.getContact().getAddressLine1() + ","
				+ store.getContact().getCity() + ","
				+ store.getContact().getState());
		origin = origin.replace(' ', '+');
		String destination = null;
		HttpURLConnection conn = null;
		URL url = null;
		String line = null, outputString = "";
		BufferedReader reader = null;
		JSONObject jsonObject = null, rowObject = null, elementObject = null;
		JSONArray rows = null, elements = null;
		double calculatedDistance = 0.0;
		try {
			// get the post office address of a given USA zipcode.
			destination = storeDAO.getPostAddress(store.getContact()
					.getZipcode());

			destination = destination.replace(' ', '+');
			StringBuilder finalURL = new StringBuilder();
			// generate the final URL to call the GOOGLE's Distance Matrix API.
			finalURL.append(baseURL);
			finalURL.append("origins=" + origin);
			finalURL.append("&destinations=" + destination);
			finalURL.append("&rows=1");
			finalURL.append("&units=imperial");
			finalURL.append("&key=" + API_KEY);
			logger.info(finalURL.toString());
			url = new URL(finalURL.toString());
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			while ((line = reader.readLine()) != null) {
				outputString += line;
			}
			//provides the jSON object
			jsonObject = new JSONObject(outputString);
			rows = jsonObject.getJSONArray("rows");
			rowObject = null;
			for (int i = 0; i < rows.length(); i++) {
				rowObject = rows.getJSONObject(i);

			}
			elements = rowObject.getJSONArray("elements");
			for (int i = 0; i < elements.length(); i++) {
				elementObject = elements.getJSONObject(i);
			}
			// get the distance of a store from a given zipcode in meters and
			// then convert it into miles.
			calculatedDistance = (elementObject.getJSONObject("distance")
					.getDouble("value") * 0.000621371);
		} catch (SQLException e) {
			logger.error("ERROR: " + e);
			throw new InvalidSQLException(e.getMessage());
		} catch (IOException e) {
			logger.error("ERROR: " + e);
			throw new StoreException(e.getMessage());
		} catch (Exception e) {
			logger.error("ERROR: " + e);
			throw new StoreException(e.getMessage());
		}
		return calculatedDistance;
	}

}
