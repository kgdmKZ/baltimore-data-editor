package baltimoredata.repository;

import org.springframework.data.repository.CrudRepository;

import baltimoredata.model.Library;

public interface LibraryRepository extends CrudRepository<Library, Integer> {

}