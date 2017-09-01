package baltimoredata.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import baltimoredata.model.Area;

public interface AreaRepository extends PagingAndSortingRepository<Area, Integer> {
	Area findByCsa2010(String csa2010);
	Long removeById(Integer id);
	Long removeByCsa2010(String csa2010);
}
