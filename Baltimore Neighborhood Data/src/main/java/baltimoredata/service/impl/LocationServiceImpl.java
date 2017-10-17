package baltimoredata.service.impl;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import baltimoredata.exception.ConflictException;
import baltimoredata.exception.ResourceNotFoundException;
import baltimoredata.model.Address;
import baltimoredata.model.Location;
import baltimoredata.repository.LocationRepository;
import baltimoredata.service.LocationService;

@Service
@Transactional
public class LocationServiceImpl implements LocationService {
	@Autowired
    LocationRepository locationRepository;
    
    @Transactional(readOnly=true)
	public Long getLocationCount() {
		return locationRepository.count();
	}
	
	@Transactional(readOnly=true)
	public Page<Location> getLocations(Pageable pageReq) {
		return locationRepository.findAll(pageReq);
	}
	
	@Transactional(readOnly=true)
	public Location getLocationById(Integer id) {
		return locationRepository.findOne(id);
	}
	
	@Transactional(readOnly=true)
	public Location getLocationByAddressId(Integer addressId) {
		return locationRepository.findByAddress_Id(addressId);
	}
	
	@Transactional(readOnly=true)
	public Location getLocationByNeighborhoodId(Integer neighborhoodId, String streetAddress) {
		return locationRepository.findByAddress_Neighborhood_IdAndAddress_StreetAddress(neighborhoodId, streetAddress);
	}
	
	@Transactional(readOnly=true)
	public Location getLocationByNeighborhoodName(String neighborhoodName, String streetAddress) {
		return locationRepository.findByAddress_Neighborhood_NameAndAddress_StreetAddress(neighborhoodName, streetAddress);
	}
	
	@Transactional(readOnly=true)
	public Location getLocationByCoords(Serializable coords) {
		return locationRepository.findByCoords(coords);
	}
	
	@Transactional(readOnly=true)
	public Location getLocationByBlockNumberAndLotNumber(String blockNumber, String lotNumber) {
		return locationRepository.findByBlockNumberAndLotNumber(blockNumber, lotNumber);
	}
	
	public Location addLocation(Location l) {
		Integer addressId = l.getAddress().getId();
		Location sameAddress = locationRepository.findByAddress_Id(addressId);
		if (sameAddress != null) {
			throw new ConflictException("location", "addressId", addressId.toString());
		}
		
		String blockNumber = l.getBlockNumber();
		String lotNumber = l.getLotNumber();
		Location sameBlockLot = locationRepository.findByBlockNumberAndLotNumber(blockNumber, lotNumber);
		if (sameBlockLot != null) {
			throw new ConflictException("location", "blockNumber, lotNumber", blockNumber + ", " + lotNumber);
		}
		
		Serializable coords = l.getCoords();
		Location sameCoords = locationRepository.findByCoords(coords);
		if (sameCoords != null) {
			throw new ConflictException("location", "coords", coords.toString());
		}
		return locationRepository.save(l);
	}
	
	private void modifyLocation(Location modified, Location existing) {
		Address newAddress = modified.getAddress();
		Integer newAddressId = newAddress.getId();
		Location sameAddress = locationRepository.findByAddress_Id(newAddressId);
		Integer existingId = existing.getId();
		Integer conflictId = sameAddress == null ? null : sameAddress.getId();
		
		if (conflictId != null && conflictId != existingId) {
			throw new ConflictException("location", "addressId", newAddressId.toString());
		}
		
		String newBlockNumber = modified.getBlockNumber();
		String newLotNumber = modified.getLotNumber();
		Location sameBlockLot = locationRepository.findByBlockNumberAndLotNumber(newBlockNumber, newLotNumber);
		if (sameBlockLot != null) {
			throw new ConflictException("location", "blockNumber, lotNumber", newBlockNumber + ", " + newLotNumber);
		}
		
		Serializable newCoords = modified.getCoords();
		Location sameCoords = locationRepository.findByCoords(newCoords);
		if (sameCoords != null) {
			throw new ConflictException("location", "coords", newCoords.toString());
		}
		
		existing.setAddress(newAddress);
		existing.setCoords(modified.getCoords());
		existing.setBlockNumber(modified.getBlockNumber());
		existing.setLotNumber(modified.getLotNumber());
		
		locationRepository.save(existing);
	}
	
	public void modifyLocationById(Integer id, Location modified) {
		Location existing = locationRepository.findOne(id);
		if (existing == null) {
			throw new ResourceNotFoundException("location", "id", id.toString());
		}
		modifyLocation(modified, existing);
	}
	 
	public void modifyLocationByAddressId(Integer addressId, Location modified) {
		Location existing = locationRepository.findByAddress_Id(addressId);
		if (existing == null) {
			throw new ResourceNotFoundException("location", "addressId", addressId.toString());
		}
		modifyLocation(modified, existing);
	}
	
	public void modifyLocationByNeighborhoodId(Integer neighborhoodId, String streetAddress, Location modified) {
		Location existing = locationRepository.findByAddress_Neighborhood_IdAndAddress_StreetAddress(neighborhoodId, streetAddress);
		if (existing == null) {
			throw new ResourceNotFoundException("location", "neighborhoodId, streetAddress", neighborhoodId.toString() + ", " + 
		            streetAddress);
		}
		modifyLocation(modified, existing);
	}
	
	public void modifyLocationByNeighborhoodName(String neighborhoodName, String streetAddress, Location modified) {
		Location existing = locationRepository.findByAddress_Neighborhood_NameAndAddress_StreetAddress(neighborhoodName, streetAddress);
		if (existing == null) {
			throw new ResourceNotFoundException("location", "neighborhoodName, streetAddress", neighborhoodName.toString() +
			        ", " + streetAddress);
		}
		modifyLocation(modified, existing);
	}
	
	public void modifyLocationByCoords(Serializable coords, Location modified) {
		Location existing = locationRepository.findByCoords(coords);
		if (existing == null) {
			throw new ResourceNotFoundException("location", "coords", coords.toString());
		}
		modifyLocation(modified, existing);
	}
	
	public void modifyLocationByBlockNumberAndLotNumber(String blockNumber, String lotNumber, Location modified) {
		Location existing = locationRepository.findByBlockNumberAndLotNumber(blockNumber, lotNumber);
		if (existing == null) {
			throw new ResourceNotFoundException("location", "blockNumber, lotNumber", blockNumber + ", " + lotNumber);
		}
		modifyLocation(modified, existing);
	}
	
	public Long deleteLocationById(Integer id) {
		return locationRepository.removeById(id);
	}
	
	public Long deleteLocationByAddressId(Integer addressId) {
		return locationRepository.removeByAddress_Id(addressId);
	}
	
	public Long deleteLocationByNeighborhoodId(Integer neighborhoodId, String streetAddress) {
		return locationRepository.removeByAddress_Neighborhood_IdAndAddress_StreetAddress(neighborhoodId, streetAddress);
	}
	
	public Long deleteLocationByNeighborhoodName(String neighborhoodName, String streetAddress) {
		return locationRepository.removeByAddress_Neighborhood_NameAndAddress_StreetAddress(neighborhoodName, streetAddress);
	}
	
	public Long deleteLocationByBlockNumberAndLotNumber(String blockNumber, String lotNumber) {
		return locationRepository.removeByBlockNumberAndLotNumber(blockNumber, lotNumber);
	}
	
	public Long deleteLocationByCoords(Serializable coords) {
		return locationRepository.removeByCoords(coords);
	}

}