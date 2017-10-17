package baltimoredata.repository;

import java.io.Serializable;

import org.springframework.data.repository.PagingAndSortingRepository;

import baltimoredata.model.Location;

public interface LocationRepository extends PagingAndSortingRepository<Location, Integer> {
	Location findByAddress_Id(Integer addressId);
	Location findByAddress_Neighborhood_IdAndAddress_StreetAddress(Integer neighborhoodId, String streetAddress);
	Location findByAddress_Neighborhood_NameAndAddress_StreetAddress(String neighborhoodName, String streetAddress);
	Location findByCoords(Serializable coords);
	Location findByBlockNumberAndLotNumber(String blockNumber, String lotNumber);
	Long removeById(Integer id);
	Long removeByAddress_Id(Integer addressId);
	Long removeByAddress_Neighborhood_IdAndAddress_StreetAddress(Integer neighborhoodId, String streetAddress);
	Long removeByAddress_Neighborhood_NameAndAddress_StreetAddress(String neighborhoodName, String streetAddress);
	Long removeByCoords(Serializable coords);
	Long removeByBlockNumberAndLotNumber(String blockNumber, String lotNumber);
}