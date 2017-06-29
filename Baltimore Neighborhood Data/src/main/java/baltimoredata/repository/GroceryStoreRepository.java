package baltimoredata.repository;

import org.springframework.data.repository.CrudRepository;

import baltimoredata.model.GroceryStore;

public interface GroceryStoreRepository extends CrudRepository<GroceryStore, Integer> {

}