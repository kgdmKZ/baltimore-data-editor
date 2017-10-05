package baltimoredata.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import baltimoredata.model.GroceryStore;

public interface GroceryStoreRepository extends PagingAndSortingRepository<GroceryStore, Integer> {
	GroceryStore findByAddress_Id(Integer addressId);
	GroceryStore findByAddress_Neighborhood_IdAndAddress_StreetAddress(Integer neighborhoodId, String streetAddress);
	GroceryStore findByAddress_Neighborhood_NameAndAddress_StreetAddress(String neighborhoodName, String streetAddress);
	Long removeById(Integer id);
	Long removeByAddress_Id(Integer addressId);
	Long removeByAddress_Neighborhood_IdAndAddress_StreetAddress(Integer neighborhoodId, String streetAddress);
	Long removeByAddress_Neighborhood_NameAndAddress_StreetAddress(String neighborhoodName, String streetAddress);
}