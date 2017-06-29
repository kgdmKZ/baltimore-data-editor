package baltimoredata.repository;

import org.springframework.data.repository.CrudRepository;

import baltimoredata.model.Neighborhood;
import baltimoredata.model.projection.LimitedNeighborhood;

public interface NeighborhoodRepository extends CrudRepository<Neighborhood, Integer> {
    LimitedNeighborhood findById(Integer id);
}