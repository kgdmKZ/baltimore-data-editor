package baltimoredata.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import baltimoredata.model.Neighborhood;
import baltimoredata.model.projection.LimitedNeighborhood;

public interface NeighborhoodRepository extends PagingAndSortingRepository<Neighborhood, Integer> {
    Optional<LimitedNeighborhood> findByName(String name);
    
    Integer countByArea_Csa2010(String csa2010);
    
    List<LimitedNeighborhood> findByArea_Csa2010(String csa2010, Pageable pageable);
    
}