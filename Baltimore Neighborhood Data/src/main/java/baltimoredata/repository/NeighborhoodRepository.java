package baltimoredata.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import baltimoredata.model.Neighborhood;

public interface NeighborhoodRepository extends PagingAndSortingRepository<Neighborhood, Integer> {
	Neighborhood findByName(String name);
    Long removeById(Integer id);
    Long removeByName(String name);
}