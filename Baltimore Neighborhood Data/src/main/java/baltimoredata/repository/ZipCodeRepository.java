package baltimoredata.repository;

import org.springframework.data.repository.CrudRepository;

import baltimoredata.model.ZipCode;

public interface ZipCodeRepository extends CrudRepository<ZipCode, Integer> {

}
