package baltimoredata.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import baltimoredata.model.ZipCode;

public interface ZipCodeRepository extends PagingAndSortingRepository<ZipCode, Integer> {
	ZipCode findByZip(Integer zip);
	Long removeById(Integer id);
	Long removeByZip(Integer zip);
}
