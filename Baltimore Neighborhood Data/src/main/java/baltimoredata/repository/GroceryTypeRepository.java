package baltimoredata.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import baltimoredata.model.GroceryType;

public interface GroceryTypeRepository extends PagingAndSortingRepository<GroceryType, Integer> {
	GroceryType findByName(String name);
	Long removeById(Integer id);
	Long removeByName(String name);
}
