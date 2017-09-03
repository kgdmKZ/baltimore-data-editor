package baltimoredata.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import baltimoredata.model.CouncilDistrict;

public interface CouncilDistrictRepository extends PagingAndSortingRepository<CouncilDistrict, Integer> {
    CouncilDistrict findByDistrictNumber(Integer districtNumber);
	Long removeById(Integer id);
	Long removeByDistrictNumber(Integer districtNumber);
}
