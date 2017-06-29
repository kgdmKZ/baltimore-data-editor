package baltimoredata.repository;

import org.springframework.data.repository.CrudRepository;

import baltimoredata.model.GroceryType;

public interface GroceryTypeRepository extends CrudRepository<GroceryType, Integer> {

}
