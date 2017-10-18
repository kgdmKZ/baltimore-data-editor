package baltimoredata.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import baltimoredata.model.VacantBuilding;

public interface VacantBuildingRepository extends PagingAndSortingRepository<VacantBuilding, Integer> {
	VacantBuilding findByReferenceid(String referenceId);
	Long removeById(Integer id);
	Long removeByReferenceid(String referenceId);
}