package baltimoredata.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import baltimoredata.model.Library;

public interface LibraryService {
	public Long getLibraryCount();
	public Page<Library> getLibraries(Pageable pageReq);
	public Library getLibraryById(Integer id);
	public Library getLibraryByAddressId(Integer addressId);
	public Library getLibraryByNeighborhoodId(Integer neighborhoodId, String streetAddress);
	public Library getLibraryByNeighborhoodName(String neighborhoodName, String streetAddress);
	public Library addLibrary(Library l);
	public void modifyLibraryById(Integer id, Library modified);
	public void modifyLibraryByAddressId(Integer addressId, Library modified);
	public void modifyLibraryByNeighborhoodId(Integer neighborhoodId, String streetAddress, Library modified);
	public void modifyLibraryByNeighborhoodName(String neighborhoodName, String streetAddress, Library modified);
	public Long deleteLibraryById(Integer id);
	public Long deleteLibraryByAddressId(Integer addressId);
	public Long deleteLibraryByNeighborhoodId(Integer neighborhoodId, String streetAddress);
	public Long deleteLibraryByNeighborhoodName(String neighborhoodName, String streetAddress);
}
