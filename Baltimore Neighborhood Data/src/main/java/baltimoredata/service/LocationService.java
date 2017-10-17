package baltimoredata.service;

import java.io.Serializable;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import baltimoredata.model.Location;

public interface LocationService {
	public Long getLocationCount();
	public Page<Location> getLocations(Pageable pageReq);
	public Location getLocationById(Integer id);
	public Location getLocationByAddressId(Integer addressId);
	public Location getLocationByNeighborhoodId(Integer neighborhoodId, String streetAddress);
	public Location getLocationByNeighborhoodName(String neighborhoodName, String streetAddress);
	public Location getLocationByCoords(Serializable coords);
	public Location getLocationByBlockNumberAndLotNumber(String blockNumber, String lotNumber);
	public Location addLocation(Location l);
	public void modifyLocationById(Integer id, Location modified);
	public void modifyLocationByAddressId(Integer addressId, Location modified);
	public void modifyLocationByNeighborhoodId(Integer neighborhoodId, String streetAddress, Location modified);
	public void modifyLocationByNeighborhoodName(String neighborhoodName, String streetAddress, Location modified);
	public void modifyLocationByCoords(Serializable coords, Location modified);
	public void modifyLocationByBlockNumberAndLotNumber(String blockNumber, String lotNumber, Location modified);
	public Long deleteLocationById(Integer id);
	public Long deleteLocationByAddressId(Integer addressId);
	public Long deleteLocationByNeighborhoodId(Integer neighborhoodId, String streetAddress);
	public Long deleteLocationByNeighborhoodName(String neighborhoodName, String streetAddress);
	public Long deleteLocationByCoords(Serializable coords);
	public Long deleteLocationByBlockNumberAndLotNumber(String blockNumber, String lotNumber);
}
