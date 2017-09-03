package baltimoredata.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import baltimoredata.model.Address;

public interface AddressRepository extends PagingAndSortingRepository<Address, Integer> {
	Address findByNeighborhood_NameAndStreetAddress(String name, String streetAddress);
	
	Integer countByNeighborhood_Id(Integer id);
	Integer countByNeighborhood_Name(String name);
	Integer countByPoliceDistrict_Id(Integer id);
	Integer countByPoliceDistrict_DistrictName(String districtName);
	Integer countByCouncilDistrict_Id(Integer id);
	Integer countByCouncilDistrict_DistrictNumber(Integer districtNumber);
	
	Page<Address> findByNeighborhood_Id(Integer id, Pageable pageable);
	Page<Address> findByNeighborhood_Name(String name, Pageable pageable);
	Page<Address> findByPoliceDistrict_Id(Integer id, Pageable pageable);
	Page<Address> findByPoliceDistrict_DistrictName(String districtName, Pageable pageable);
	Page<Address> findByCouncilDistrict_Id(Integer id, Pageable pageable);
	Page<Address> findByCouncilDistrict_DistrictNumber(Integer districtNumber, Pageable pageable);
    
    Long removeById(Integer id);
    Long removeByNeighborhood_NameAndStreetAddress(String name, String streetAddress);
}