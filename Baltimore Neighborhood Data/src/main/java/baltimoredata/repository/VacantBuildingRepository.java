package baltimoredata.repository;

import org.springframework.data.repository.CrudRepository;

import baltimoredata.model.VacantBuilding;

public interface VacantBuildingRepository extends CrudRepository<VacantBuilding, Integer> {

}