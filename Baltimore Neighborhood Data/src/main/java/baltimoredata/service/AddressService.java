package baltimoredata.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import baltimoredata.model.Address;

public interface AddressService {
	public Long getAddressCount();
	public Page<Address> getAddresses(Pageable pageReq);
	
	public Integer getAddressCountByNeighborhoodId(Integer id);
	public Integer getAddressCountByNeighborhoodName(String name);
	public Integer getAddressCountByPoliceDistrictId(Integer id);
	public Integer getAddressCountByPoliceDistrictName(String name);
	public Integer getAddressCountByCouncilDistrictId(Integer id);
	public Integer getAddressCountByCouncilDistrictNumber(Integer number);
	
	public Page<Address> getAddressesByNeighborhoodId(Integer id, Pageable pageReq);
	public Page<Address> getAddressesByNeighborhoodName(String name, Pageable pageReq);
	public Page<Address> getAddressesByPoliceDistrictId(Integer id, Pageable pageReq);
	public Page<Address> getAddressesByPoliceDistrictName(String name, Pageable pageReq);
	public Page<Address> getAddressesByCouncilDistrictId(Integer id, Pageable pageReq);
	public Page<Address> getAddressesByCouncilDistrictNumber(Integer number, Pageable pageReq);
	
	public Address getAddressById(Integer id);
	public Address getAddressByNeighborhoodAndStreetAddress(String neighborhood, String streetAddress);
	
	public Address addAddress(Address a);
	
	public void modifyAddressById(Integer id, Address a);
	public void modifyAddressByNeighborhoodAndStreetAddress(String neighborhood, String streetAddress, Address a);
	
	public Long deleteAddressById(Integer id);
	public Long deleteAddressByNeighborhoodAndStreetAddress(String neighborhood, String streetAddress);
}
