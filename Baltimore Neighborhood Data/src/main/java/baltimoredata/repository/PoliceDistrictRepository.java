package baltimoredata.repository;

import org.springframework.data.repository.CrudRepository;

import baltimoredata.model.PoliceDistrict;

public interface PoliceDistrictRepository extends CrudRepository<PoliceDistrict, Integer> {
    PoliceDistrict findByDistrictName(String districtName);
}