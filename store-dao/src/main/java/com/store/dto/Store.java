package com.store.dto;

/* Store Object to store the data in DB.
 * 
 */

public class Store {

	private int storeId;
	private String storeName, storeOwner;
	private long storeDistance;
	public int getStoreId() {
		return storeId;
	}
	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getStoreOwner() {
		return storeOwner;
	}
	public void setStoreOwner(String storeOwner) {
		this.storeOwner = storeOwner;
	}
	public long getStoreDistance() {
		return storeDistance;
	}
	public void setStoreDistance(long storeDistance) {
		this.storeDistance = storeDistance;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ (int) (storeDistance ^ (storeDistance >>> 32));
		result = prime * result + storeId;
		result = prime * result
				+ ((storeName == null) ? 0 : storeName.hashCode());
		result = prime * result
				+ ((storeOwner == null) ? 0 : storeOwner.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Store other = (Store) obj;
		if (storeDistance != other.storeDistance)
			return false;
		if (storeId != other.storeId)
			return false;
		if (storeName == null) {
			if (other.storeName != null)
				return false;
		} else if (!storeName.equals(other.storeName))
			return false;
		if (storeOwner == null) {
			if (other.storeOwner != null)
				return false;
		} else if (!storeOwner.equals(other.storeOwner))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Store [storeId=" + storeId + ", storeName=" + storeName
				+ ", storeOwner=" + storeOwner + ", storeDistance="
				+ storeDistance + "]";
	}
	
}
