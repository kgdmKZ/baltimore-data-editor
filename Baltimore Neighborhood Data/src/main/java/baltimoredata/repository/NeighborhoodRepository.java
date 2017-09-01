package baltimoredata.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import baltimoredata.model.Neighborhood;

public interface NeighborhoodRepository extends PagingAndSortingRepository<Neighborhood, Integer> {
	Neighborhood findByName(String name);
	
    Integer countByArea_Id(Integer id);
    Page<Neighborhood> findByArea_Id(Integer id, Pageable pageable);
    
    Long removeById(Integer id);
    Long removeByName(String name);
    
}