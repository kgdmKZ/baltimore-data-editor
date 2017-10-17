package baltimoredata.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import baltimoredata.exception.ConflictException;
import baltimoredata.exception.ResourceNotFoundException;
import baltimoredata.model.Address;
import baltimoredata.model.GroceryStore;
import baltimoredata.repository.GroceryStoreRepository;
import baltimoredata.service.GroceryStoreService;

@Service
@Transactional
public class GroceryStoreServiceImpl implements GroceryStoreService {
	@Autowired
    GroceryStoreRepository groceryRepository;
    
    @Transactional(readOnly=true)
	public Long getGroceryStoreCount() {
		return groceryRepository.count();
	}
	
	@Transactional(readOnly=true)
	public Page<GroceryStore> getGroceryStores(Pageable pageReq) {
		return groceryRepository.findAll(pageReq);
	}
	
	@Transactional(readOnly=true)
	public GroceryStore getGroceryStoreById(Integer id) {
		return groceryRepository.findOne(id);
	}
	
	@Transactional(readOnly=true)
	public GroceryStore getGroceryStoreByAddressId(Integer addressId) {
		return groceryRepository.findByAddress_Id(addressId);
	}
	
	@Transactional(readOnly=true)
	public GroceryStore getGroceryStoreByNeighborhoodId(Integer neighborhoodId, String streetAddress) {
		return groceryRepository.findByAddress_Neighborhood_IdAndAddress_StreetAddress(neighborhoodId, streetAddress);
	}
	
	@Transactional(readOnly=true)
	public GroceryStore getGroceryStoreByNeighborhoodName(String neighborhoodName, String streetAddress) {
		return groceryRepository.findByAddress_Neighborhood_NameAndAddress_StreetAddress(neighborhoodName, streetAddress);
	}
	
	public GroceryStore addGroceryStore(GroceryStore g) {
		Integer addressId = g.getAddress().getId();
		GroceryStore res = groceryRepository.findByAddress_Id(addressId);
		if (res != null) {
			throw new ConflictException("grocery store", "addressId", addressId.toString());
		}
		return groceryRepository.save(g);
	}
	
	private void modifyGroceryStore(GroceryStore modified, GroceryStore existing) {
		Address newAddress = modified.getAddress();
		Integer newAddressId = newAddress.getId();
		GroceryStore conflictGroceryStore = groceryRepository.findByAddress_Id(newAddressId);
		Integer existingId = existing.getId();
		Integer conflictId = conflictGroceryStore == null ? null : conflictGroceryStore.getId();
		
		if (conflictId != null && conflictId != existingId) {
			throw new ConflictException("grocery store", "addressId", newAddressId.toString());
		}
		
		existing.setAddress(newAddress);
		existing.setGroceryType(modified.getGroceryType());
		existing.setZipCode(modified.getZipCode());
		existing.setName(modified.getName());
		groceryRepository.save(existing);
	}
	
	public void modifyGroceryStoreById(Integer id, GroceryStore modified) {
		GroceryStore existing = groceryRepository.findOne(id);
		if (existing == null) {
			throw new ResourceNotFoundException("grocery store", "id", id.toString());
		}
		modifyGroceryStore(modified, existing);
	}
	 
	public void modifyGroceryStoreByAddressId(Integer addressId, GroceryStore modified) {
		GroceryStore existing = groceryRepository.findByAddress_Id(addressId);
		if (existing == null) {
			throw new ResourceNotFoundException("grocery store", "addressId", addressId.toString());
		}
		modifyGroceryStore(modified, existing);
	}
	
	public void modifyGroceryStoreByNeighborhoodId(Integer neighborhoodId, String streetAddress, GroceryStore modified) {
		GroceryStore existing = groceryRepository.findByAddress_Neighborhood_IdAndAddress_StreetAddress(neighborhoodId, streetAddress);
		if (existing == null) {
			throw new ResourceNotFoundException("grocery store", "neighborhoodId, streetAddress", neighborhoodId.toString() + ", " + 
		            streetAddress);
		}
		modifyGroceryStore(modified, existing);
	}
	
	public void modifyGroceryStoreByNeighborhoodName(String neighborhoodName, String streetAddress, GroceryStore modified) {
		GroceryStore existing = groceryRepository.findByAddress_Neighborhood_NameAndAddress_StreetAddress(neighborhoodName, streetAddress);
		if (existing == null) {
			throw new ResourceNotFoundException("grocery store", "neighborhoodName, streetAddress", neighborhoodName.toString() +
			        ", " + streetAddress);
		}
		modifyGroceryStore(modified, existing);
	}
	
	public Long deleteGroceryStoreById(Integer id) {
		return groceryRepository.removeById(id);
	}
	
	public Long deleteGroceryStoreByAddressId(Integer addressId) {
		return groceryRepository.removeByAddress_Id(addressId);
	}
	
	public Long deleteGroceryStoreByNeighborhoodId(Integer neighborhoodId, String streetAddress) {
		return groceryRepository.removeByAddress_Neighborhood_IdAndAddress_StreetAddress(neighborhoodId, streetAddress);
	}
	
	public Long deleteGroceryStoreByNeighborhoodName(String neighborhoodName, String streetAddress) {
		return groceryRepository.removeByAddress_Neighborhood_NameAndAddress_StreetAddress(neighborhoodName, streetAddress);
	}

}
