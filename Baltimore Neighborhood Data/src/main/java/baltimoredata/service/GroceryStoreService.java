package baltimoredata.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import baltimoredata.model.GroceryStore;

public interface GroceryStoreService {
	public Long getGroceryStoreCount();
	public Page<GroceryStore> getGroceryStores(Pageable pageReq);
	public GroceryStore getGroceryStoreById(Integer id);
	public GroceryStore getGroceryStoreByAddressId(Integer addressId);
	public GroceryStore getGroceryStoreByNeighborhoodId(Integer neighborhoodId, String streetAddress);
	public GroceryStore getGroceryStoreByNeighborhoodName(String neighborhoodName, String streetAddress);
	public GroceryStore addGroceryStore(GroceryStore g);
	public void modifyGroceryStoreById(Integer id, GroceryStore modified);
	public void modifyGroceryStoreByAddressId(Integer addressId, GroceryStore modified);
	public void modifyGroceryStoreByNeighborhoodId(Integer neighborhoodId, String streetAddress, GroceryStore modified);
	public void modifyGroceryStoreByNeighborhoodName(String neighborhoodName, String streetAddress, GroceryStore modified);
	public Long deleteGroceryStoreById(Integer id);
	public Long deleteGroceryStoreByAddressId(Integer addressId);
	public Long deleteGroceryStoreByNeighborhoodId(Integer neighborhoodId, String streetAddress);
	public Long deleteGroceryStoreByNeighborhoodName(String neighborhoodName, String streetAddress);
}
