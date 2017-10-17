package baltimoredata.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import baltimoredata.exception.ConflictException;
import baltimoredata.exception.ResourceNotFoundException;
import baltimoredata.model.Address;
import baltimoredata.model.Library;
import baltimoredata.repository.LibraryRepository;
import baltimoredata.service.LibraryService;

@Service
@Transactional
public class LibraryServiceImpl implements LibraryService {
	@Autowired
    LibraryRepository libraryRepository;
    
    @Transactional(readOnly=true)
	public Long getLibraryCount() {
		return libraryRepository.count();
	}
	
	@Transactional(readOnly=true)
	public Page<Library> getLibraries(Pageable pageReq) {
		return libraryRepository.findAll(pageReq);
	}
	
	@Transactional(readOnly=true)
	public Library getLibraryById(Integer id) {
		return libraryRepository.findOne(id);
	}
	
	@Transactional(readOnly=true)
	public Library getLibraryByAddressId(Integer addressId) {
		return libraryRepository.findByAddress_Id(addressId);
	}
	
	@Transactional(readOnly=true)
	public Library getLibraryByNeighborhoodId(Integer neighborhoodId, String streetAddress) {
		return libraryRepository.findByAddress_Neighborhood_IdAndAddress_StreetAddress(neighborhoodId, streetAddress);
	}
	
	@Transactional(readOnly=true)
	public Library getLibraryByNeighborhoodName(String neighborhoodName, String streetAddress) {
		return libraryRepository.findByAddress_Neighborhood_NameAndAddress_StreetAddress(neighborhoodName, streetAddress);
	}
	
	public Library addLibrary(Library l) {
		Integer addressId = l.getAddress().getId();
		Library res = libraryRepository.findByAddress_Id(addressId);
		if (res != null) {
			throw new ConflictException("library", "addressId", addressId.toString());
		}
		return libraryRepository.save(l);
	}
	
	private void modifyLibrary(Library modified, Library existing) {
		Address newAddress = modified.getAddress();
		Integer newAddressId = newAddress.getId();
		Library conflictLibrary = libraryRepository.findByAddress_Id(newAddressId);
		Integer existingId = existing.getId();
		Integer conflictId = conflictLibrary == null ? null : conflictLibrary.getId();
		
		if (conflictId != null && conflictId != existingId) {
			throw new ConflictException("library", "addressId", newAddressId.toString());
		}
		
		existing.setAddress(newAddress);
		existing.setZipCode(modified.getZipCode());
		existing.setName(modified.getName());
		libraryRepository.save(existing);
	}
	
	public void modifyLibraryById(Integer id, Library modified) {
		Library existing = libraryRepository.findOne(id);
		if (existing == null) {
			throw new ResourceNotFoundException("library", "id", id.toString());
		}
		modifyLibrary(modified, existing);
	}
	 
	public void modifyLibraryByAddressId(Integer addressId, Library modified) {
		Library existing = libraryRepository.findByAddress_Id(addressId);
		if (existing == null) {
			throw new ResourceNotFoundException("library", "addressId", addressId.toString());
		}
		modifyLibrary(modified, existing);
	}
	
	public void modifyLibraryByNeighborhoodId(Integer neighborhoodId, String streetAddress, Library modified) {
		Library existing = libraryRepository.findByAddress_Neighborhood_IdAndAddress_StreetAddress(neighborhoodId, streetAddress);
		if (existing == null) {
			throw new ResourceNotFoundException("library", "neighborhoodId, streetAddress", neighborhoodId.toString() + ", " + 
		            streetAddress);
		}
		modifyLibrary(modified, existing);
	}
	
	public void modifyLibraryByNeighborhoodName(String neighborhoodName, String streetAddress, Library modified) {
		Library existing = libraryRepository.findByAddress_Neighborhood_NameAndAddress_StreetAddress(neighborhoodName, streetAddress);
		if (existing == null) {
			throw new ResourceNotFoundException("library", "neighborhoodName, streetAddress", neighborhoodName.toString() +
			        ", " + streetAddress);
		}
		modifyLibrary(modified, existing);
	}
	
	public Long deleteLibraryById(Integer id) {
		return libraryRepository.removeById(id);
	}
	
	public Long deleteLibraryByAddressId(Integer addressId) {
		return libraryRepository.removeByAddress_Id(addressId);
	}
	
	public Long deleteLibraryByNeighborhoodId(Integer neighborhoodId, String streetAddress) {
		return libraryRepository.removeByAddress_Neighborhood_IdAndAddress_StreetAddress(neighborhoodId, streetAddress);
	}
	
	public Long deleteLibraryByNeighborhoodName(String neighborhoodName, String streetAddress) {
		return libraryRepository.removeByAddress_Neighborhood_NameAndAddress_StreetAddress(neighborhoodName, streetAddress);
	}

}

