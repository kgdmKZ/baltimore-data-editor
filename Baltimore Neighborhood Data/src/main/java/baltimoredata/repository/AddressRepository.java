package baltimoredata.repository;

import org.springframework.data.repository.CrudRepository;

import baltimoredata.model.Address;

public interface AddressRepository extends CrudRepository<Address, Integer> {

}