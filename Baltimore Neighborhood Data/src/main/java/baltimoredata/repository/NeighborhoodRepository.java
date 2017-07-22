package baltimoredata.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import baltimoredata.model.Neighborhood;

public interface NeighborhoodRepository extends PagingAndSortingRepository<Neighborhood, Integer> {
	@Query(value="SELECT n.id AS id, n.area AS area, n.name AS name FROM Neighborhood n")
	List<Neighborhood> listAll(Pageable pageable);
	
	Neighborhood findByName(String name);
	
    Integer countByArea_Csa2010(String csa2010);
    List<Neighborhood> findByArea_Csa2010(String csa2010, Pageable pageable);
    
    Long removeById(Integer id);
    Long removeByName(String name);
    
}