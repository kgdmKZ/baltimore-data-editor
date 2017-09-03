package baltimoredata.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import baltimoredata.model.PoliceDistrict;

public interface PoliceDistrictRepository extends PagingAndSortingRepository<PoliceDistrict, Integer> {
    PoliceDistrict findByDistrictName(String districtName);
	Long removeById(Integer id);
	Long removeByDistrictName(String districtName);
}