package baltimoredata.controller;

import java.net.URI;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import baltimoredata.exception.BadRequestException;
import baltimoredata.exception.ResourceNotFoundException;
import baltimoredata.model.Library;
import baltimoredata.service.LibraryService;
import baltimoredata.view.POIViews;

@RestController
@RequestMapping(path="/libraries")
public class LibraryController {
	@Autowired
    private LibraryService libraryService;
    
    @GetMapping(path="")
    @JsonView(POIViews.Limited.class)
    public Page<Library> getLibraries(Pageable pageReq) {
    	return libraryService.getLibraries(pageReq);
    }
    
    @GetMapping(path="/count")
	public Integer getLibraryCount() {
		return libraryService.getLibraryCount().intValue();
	}
    
    @GetMapping(path={"/{id}", "/address/{addressId}", "/address/neighborhood/{neighborhoodId}/streetaddress/{streetAddress}", 
    		"/address/neighborhood/name/{name}/streetaddress/{streetAddress}"})
	@JsonView(POIViews.Limited.class)
	public Library getLibrary(@PathVariable Optional<Integer> id, @PathVariable Optional<Integer> addressId,
			@PathVariable Optional<Integer> neighborhoodId, @PathVariable Optional<String> name, 
			@PathVariable Optional<String> streetAddress) {
		Library result = null;
		boolean hasId = id.isPresent();
		boolean hasAddressId = addressId.isPresent();
		boolean hasNeighborhoodId = neighborhoodId.isPresent() && streetAddress.isPresent();
		boolean hasNeighborhoodName = name.isPresent() && streetAddress.isPresent();
		
		if (hasId) {
			result = libraryService.getLibraryById(id.get());
		}
		else if (hasAddressId) {
			result = libraryService.getLibraryByAddressId(addressId.get());
		}
		else if (hasNeighborhoodId) {
			result = libraryService.getLibraryByNeighborhoodId(neighborhoodId.get(), streetAddress.get());
		}
		else if (hasNeighborhoodName) {
			result = libraryService.getLibraryByNeighborhoodName(name.get(), streetAddress.get());
		}
		else {
			throw new BadRequestException("Either a library id, an address id, a neighborhood id and street address, or a"
					+ " neighborhood name and street address must be present, but none of these was provided.");
		}
		
		if (result == null) {
			if (hasId) {
			    throw new ResourceNotFoundException("library", "id", id.get().toString());
			}
			if (hasAddressId) {
				throw new ResourceNotFoundException("library", "addressId", addressId.get().toString());
			}
			if (hasNeighborhoodId) {
				throw new ResourceNotFoundException("library", "neighborhoodId, streetAddress", 
						neighborhoodId.get().toString() + ", " + streetAddress.get());
			}
			throw new ResourceNotFoundException("library", "neighborhoodName, streetAddress", 
					name.get() + ", " + streetAddress.get());
		}
		
		return result;
	}
    
    @PostMapping(path="")
	@JsonView(POIViews.Limited.class)
	public ResponseEntity<Library> addLibrary(@RequestBody @Valid Library l) {
		Library saved = libraryService.addLibrary(l);
		URI location = URI.create("/libraries/" + saved.getId());
		return ResponseEntity.created(location).body(saved);
	}
	
    @PutMapping(path={"/{id}", "/address/{addressId}", "/address/neighborhood/{neighborhoodId}/streetaddress/{streetAddress}", 
	"/address/neighborhood/name/{name}/streetaddress/{streetAddress}"})
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void modifyLibrary(@PathVariable Optional<Integer> id, @PathVariable Optional<Integer> addressId, 
			@PathVariable Optional<Integer> neighborhoodId, @PathVariable Optional<String> streetAddress, 
			@PathVariable Optional<String> name, @RequestBody @Valid Library l) {		
		if (id.isPresent()) {
			libraryService.modifyLibraryById(id.get(), l);
		}
		else if (addressId.isPresent()) {
			libraryService.modifyLibraryByAddressId(addressId.get(), l);
		}
		else if (neighborhoodId.isPresent() && streetAddress.isPresent()) {
			libraryService.modifyLibraryByNeighborhoodId(neighborhoodId.get(), streetAddress.get(), l);
		}
		else if (name.isPresent() && streetAddress.isPresent()) {
			libraryService.modifyLibraryByNeighborhoodName(name.get(), streetAddress.get(), l);
		}
		else {
			throw new BadRequestException("Either a library id, an address id, a neighborhood id and street address, or a"
					+ " neighborhood name and street address must be present for the library to modify, but none of these "
					+ "was provided.");
		}
		
	}
	
    @DeleteMapping(path={"/{id}", "/address/{addressId}", "/address/neighborhood/{neighborhoodId}/streetaddress/{streetAddress}", 
	"/address/neighborhood/name/{name}/streetaddress/{streetAddress}"})
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteLibrary(@PathVariable Optional<Integer> id, @PathVariable Optional<Integer> addressId, 
			@PathVariable Optional<Integer> neighborhoodId, @PathVariable Optional<String> streetAddress, 
			@PathVariable Optional<String> name) {
		Long removed;
		boolean hasId = id.isPresent();
		boolean hasAddressId = addressId.isPresent();
		boolean hasNeighborhoodId = neighborhoodId.isPresent() && streetAddress.isPresent();
		boolean hasNeighborhoodName = name.isPresent() && streetAddress.isPresent();
		
		if (hasId) {
			removed = libraryService.deleteLibraryById(id.get());
		}
		else if (hasAddressId) {
			removed = libraryService.deleteLibraryByAddressId(addressId.get());
		}
		else if (hasNeighborhoodId) {
			removed = libraryService.deleteLibraryByNeighborhoodId(neighborhoodId.get(), streetAddress.get());
		}
		else if (hasNeighborhoodName) {
			removed = libraryService.deleteLibraryByNeighborhoodName(name.get(), streetAddress.get());
		}
		else {
			throw new BadRequestException("Either a library id, an address id, a neighborhood id and street address, or a"
					+ " neighborhood name and street address must be present for the library to delete, but none of these "
					+ "was provided.");
		}
		
		if (removed == 0) {
			if (hasId) {
			    throw new ResourceNotFoundException("library", "id", id.get().toString());
			}
			if (hasAddressId) {
				throw new ResourceNotFoundException("library", "addressId", addressId.get().toString());
			}
			if (hasNeighborhoodId) {
				throw new ResourceNotFoundException("library", "neighborhoodId, streetAddress", 
						neighborhoodId.get().toString() + ", " + streetAddress.get());
			}
			throw new ResourceNotFoundException("library", "neighborhoodName, streetAddress", 
					name.get() + ", " + streetAddress.get());
		}
	}
}
