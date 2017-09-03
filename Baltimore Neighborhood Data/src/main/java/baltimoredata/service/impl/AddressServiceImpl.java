package baltimoredata.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import baltimoredata.exception.ConflictException;
import baltimoredata.exception.ResourceNotFoundException;
import baltimoredata.exception.UnprocessableEntityException;
import baltimoredata.model.Address;
import baltimoredata.model.CouncilDistrict;
import baltimoredata.model.Neighborhood;
import baltimoredata.model.PoliceDistrict;
import baltimoredata.repository.AddressRepository;
import baltimoredata.repository.CouncilDistrictRepository;
import baltimoredata.repository.NeighborhoodRepository;
import baltimoredata.repository.PoliceDistrictRepository;
import baltimoredata.service.AddressService;

@Service
@Transactional(readOnly=true)
public class AddressServiceImpl implements AddressService {
    @Autowired
    NeighborhoodRepository neighborhoodRepository;
    
    @Autowired
    AddressRepository addressRepository;
    
    @Autowired
    PoliceDistrictRepository policeRepository;
    
    @Autowired
    CouncilDistrictRepository councilRepository;
    
    public Long getAddressCount() {
    	return addressRepository.count();
    }
    
	public Page<Address> getAddresses(Pageable pageReq) {
		return addressRepository.findAll(pageReq);
	};
	
	public Integer getAddressCountByNeighborhoodId(Integer id) {
		Neighborhood n = neighborhoodRepository.findOne(id);
		if (n == null) {
			throw new ResourceNotFoundException("neighborhood", "id", id.toString());
		}
		return addressRepository.countByNeighborhood_Id(id);
	}
	
	public Integer getAddressCountByNeighborhoodName(String name) {
		Neighborhood n = neighborhoodRepository.findByName(name);
		if (n == null) {
			throw new ResourceNotFoundException("neighborhood", "name", name);
		}
		return addressRepository.countByNeighborhood_Name(name);
	}
	
	public Integer getAddressCountByPoliceDistrictId(Integer id) {
		PoliceDistrict p = policeRepository.findOne(id);
		if (p == null) {
			throw new ResourceNotFoundException("police district", "id", id.toString());
		}
		return addressRepository.countByPoliceDistrict_Id(id);
	};
	public Integer getAddressCountByPoliceDistrictName(String name) {
		PoliceDistrict p = policeRepository.findByDistrictName(name);
		if (p == null) {
			throw new ResourceNotFoundException("police district", "districtName", name);
		}
		return addressRepository.countByPoliceDistrict_DistrictName(name);
	};
	public Integer getAddressCountByCouncilDistrictId(Integer id) {
		CouncilDistrict c = councilRepository.findOne(id);
		if (c == null) {
			throw new ResourceNotFoundException("council district", "id", id.toString());
		}
		return addressRepository.countByCouncilDistrict_Id(id);
	}
	
	public Integer getAddressCountByCouncilDistrictNumber(Integer number) {
		CouncilDistrict c = councilRepository.findByDistrictNumber(number);
		if (c == null) {
			throw new ResourceNotFoundException("council district", "districtNumber", number.toString());
		}
		return addressRepository.countByCouncilDistrict_DistrictNumber(number);
	}
	
	public Page<Address> getAddressesByNeighborhoodId(Integer id, Pageable pageReq) {
		Neighborhood n = neighborhoodRepository.findOne(id);
    	if (n == null) {
    		throw new ResourceNotFoundException("neighborhood", "id", id.toString());
    	}
    	return addressRepository.findByNeighborhood_Id(id, pageReq);
	}
	
	public Page<Address> getAddressesByNeighborhoodName(String name, Pageable pageReq) {
		Neighborhood n = neighborhoodRepository.findByName(name);
    	if (n == null) {
    		throw new ResourceNotFoundException("neighborhood", "name", name);
    	}
    	return addressRepository.findByNeighborhood_Name(name, pageReq);
	}
	
	public Page<Address> getAddressesByPoliceDistrictId(Integer id, Pageable pageReq) {
		PoliceDistrict p = policeRepository.findOne(id);
    	if (p == null) {
    		throw new ResourceNotFoundException("police district", "id", id.toString());
    	}
    	return addressRepository.findByPoliceDistrict_Id(id, pageReq);
	}
	
	public Page<Address> getAddressesByPoliceDistrictName(String name, Pageable pageReq) {
		PoliceDistrict p = policeRepository.findByDistrictName(name);
    	if (p == null) {
    		throw new ResourceNotFoundException("police district", "districtName", name);
    	}
    	return addressRepository.findByPoliceDistrict_DistrictName(name, pageReq);
	}
	
	public Page<Address> getAddressesByCouncilDistrictId(Integer id, Pageable pageReq) {
		CouncilDistrict c = councilRepository.findOne(id);
    	if (c == null) {
    		throw new ResourceNotFoundException("council district", "id", id.toString());
    	}
    	return addressRepository.findByCouncilDistrict_Id(id, pageReq);
	}
	public Page<Address> getAddressesByCouncilDistrictNumber(Integer number, Pageable pageReq) {
		CouncilDistrict c = councilRepository.findByDistrictNumber(number);
    	if (c == null) {
    		throw new ResourceNotFoundException("council district", "districtNumber", number.toString());
    	}
    	return addressRepository.findByCouncilDistrict_DistrictNumber(number, pageReq);
	}
	
	public Address getAddressById(Integer id) {
		return addressRepository.findOne(id);
	}
	
	public Address getAddressByNeighborhoodAndStreetAddress(String neighborhood, String streetAddress) {
		return addressRepository.findByNeighborhood_NameAndStreetAddress(neighborhood, streetAddress);
	};
	
	@Transactional
	public Address addAddress(Address a) {
		String policeDistrictName = a.getPoliceDistrict().getDistrictName();
		PoliceDistrict p = policeRepository.findByDistrictName(policeDistrictName);
		if (p == null) {
			throw new UnprocessableEntityException("police district", "districtName", policeDistrictName);
		}
		
		Integer councilDistrictNumber = a.getCouncilDistrict().getDistrictNumber();
		CouncilDistrict c = councilRepository.findByDistrictNumber(councilDistrictNumber);
		if (c == null) {
			throw new UnprocessableEntityException("council district", "districtNumber", councilDistrictNumber.toString());
		}
		
		String neighborhoodName = a.getNeighborhood().getName();
		Neighborhood n = neighborhoodRepository.findByName(neighborhoodName);
		if (n == null) {
			throw new UnprocessableEntityException("neighborhood", "name", neighborhoodName);
		}
		
		String streetAddress = a.getStreetAddress();
		Address res = addressRepository.findByNeighborhood_NameAndStreetAddress(neighborhoodName, streetAddress);
		if (res != null) {
			throw new ConflictException("address", "neighborhood, streetAddress", neighborhoodName + ", " + streetAddress);
		}
		
		a.setPoliceDistrict(p);
		a.setCouncilDistrict(c);
		a.setNeighborhood(n);
		
		return addressRepository.save(a);
	};
	
	private void modifyAddress(Address modified, Address existing) {	
		String newNeighborhoodName = modified.getNeighborhood().getName();
		Neighborhood newNeighborhood = neighborhoodRepository.findByName(newNeighborhoodName);
    	if (newNeighborhood == null) {
    		throw new UnprocessableEntityException("neighborhood", "name", newNeighborhoodName);
    	}
		
    	String newPoliceDistrictName = modified.getPoliceDistrict().getDistrictName();
    	PoliceDistrict newPoliceDistrict = policeRepository.findByDistrictName(newPoliceDistrictName);
    	if (newPoliceDistrict == null) {
    		throw new UnprocessableEntityException("police district", "districtName", newPoliceDistrictName);
    	}
		
    	Integer newCouncilDistrictNumber = modified.getCouncilDistrict().getDistrictNumber();
    	CouncilDistrict newCouncilDistrict = councilRepository.findByDistrictNumber(newCouncilDistrictNumber);
    	if (newCouncilDistrict == null) {
    		throw new UnprocessableEntityException("council district", "districtNumber", newCouncilDistrictNumber.toString());
    	}
		
    	Integer existingId = existing.getId();
    	String newStreetAddress = modified.getStreetAddress();
    	Address conflictAddress = addressRepository.findByNeighborhood_NameAndStreetAddress(newNeighborhoodName, newStreetAddress);
    	Integer conflictId = conflictAddress == null ? null : conflictAddress.getId();
    	if (conflictId != null && conflictId != existingId) {
    		throw new ConflictException("address", "neighborhood, streetAddress", newNeighborhoodName + ", " + newStreetAddress);
    	}


		existing.setNeighborhood(newNeighborhood);
		existing.setPoliceDistrict(newPoliceDistrict);
		existing.setCouncilDistrict(newCouncilDistrict);
		existing.setStreetAddress(newStreetAddress);
		addressRepository.save(existing);
	}
	
	@Transactional
	public void modifyAddressById(Integer id, Address a) {
		Address existing = addressRepository.findOne(id);
    	if (existing == null) {
    		throw new ResourceNotFoundException("address", "id", id.toString());
    	}
    	modifyAddress(a, existing);
	}
	
	@Transactional
	public void modifyAddressByNeighborhoodAndStreetAddress(String neighborhood, String streetAddress, Address a) {
		Address existing = addressRepository.findByNeighborhood_NameAndStreetAddress(neighborhood, streetAddress);
    	if (existing == null) {
    		throw new ResourceNotFoundException("address", "neighborhood, streetAddress", neighborhood + ", " + streetAddress);
    	}
    	modifyAddress(a, existing);
	}
	
	@Transactional
	public Long deleteAddressById(Integer id) {
		return addressRepository.removeById(id);
	};
	
	@Transactional
	public Long deleteAddressByNeighborhoodAndStreetAddress(String neighborhood, String streetAddress) {
		return addressRepository.removeByNeighborhood_NameAndStreetAddress(neighborhood, streetAddress);
	};
}
