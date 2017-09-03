package baltimoredata.repository;

import org.springframework.data.repository.CrudRepository;

import baltimoredata.model.CouncilDistrict;

public interface CouncilDistrictRepository extends CrudRepository<CouncilDistrict, Integer> {
    CouncilDistrict findByDistrictNumber(Integer districtNumber);
}
