package baltimoredata.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import baltimoredata.model.Library;

public interface LibraryRepository extends PagingAndSortingRepository<Library, Integer> {
	Library findByAddress_Id(Integer addressId);
	Library findByAddress_Neighborhood_IdAndAddress_StreetAddress(Integer neighborhoodId, String streetAddress);
	Library findByAddress_Neighborhood_NameAndAddress_StreetAddress(String neighborhoodName, String streetAddress);
	Long removeById(Integer id);
	Long removeByAddress_Id(Integer addressId);
	Long removeByAddress_Neighborhood_IdAndAddress_StreetAddress(Integer neighborhoodId, String streetAddress);
	Long removeByAddress_Neighborhood_NameAndAddress_StreetAddress(String neighborhoodName, String streetAddress);
}