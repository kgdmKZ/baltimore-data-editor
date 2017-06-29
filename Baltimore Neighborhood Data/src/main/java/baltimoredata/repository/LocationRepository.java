package baltimoredata.repository;

import org.springframework.data.repository.CrudRepository;

import baltimoredata.model.Location;

public interface LocationRepository extends CrudRepository<Location, Integer> {

}