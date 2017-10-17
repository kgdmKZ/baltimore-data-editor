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
import baltimoredata.model.GroceryStore;
import baltimoredata.service.GroceryStoreService;
import baltimoredata.view.POIViews;

@RestController
@RequestMapping(path="/grocerystores")
public class GroceryStoreController {
	@Autowired
    private GroceryStoreService groceryService;
    
    @GetMapping(path="")
    @JsonView(POIViews.Limited.class)
    public Page<GroceryStore> getGroceryStores(Pageable pageReq) {
    	return groceryService.getGroceryStores(pageReq);
    }
    
    @GetMapping(path="/count")
	public Integer getGroceryStoreCount() {
		return groceryService.getGroceryStoreCount().intValue();
	}
    
    @GetMapping(path={"/{id}", "/address/{addressId}", "/address/neighborhood/{neighborhoodId}/streetaddress/{streetAddress}", 
    		"/address/neighborhood/name/{name}/streetaddress/{streetAddress}"})
	@JsonView(POIViews.Limited.class)
	public GroceryStore getGroceryStore(@PathVariable Optional<Integer> id, @PathVariable Optional<Integer> addressId,
			@PathVariable Optional<Integer> neighborhoodId, @PathVariable Optional<String> name, 
			@PathVariable Optional<String> streetAddress) {
		GroceryStore result = null;
		boolean hasId = id.isPresent();
		boolean hasAddressId = addressId.isPresent();
		boolean hasNeighborhoodId = neighborhoodId.isPresent() && streetAddress.isPresent();
		boolean hasNeighborhoodName = name.isPresent() && streetAddress.isPresent();
		
		if (hasId) {
			result = groceryService.getGroceryStoreById(id.get());
		}
		else if (hasAddressId) {
			result = groceryService.getGroceryStoreByAddressId(addressId.get());
		}
		else if (hasNeighborhoodId) {
			result = groceryService.getGroceryStoreByNeighborhoodId(neighborhoodId.get(), streetAddress.get());
		}
		else if (hasNeighborhoodName) {
			result = groceryService.getGroceryStoreByNeighborhoodName(name.get(), streetAddress.get());
		}
		else {
			throw new BadRequestException("Either a grocery store id, an address id, a neighborhood id and street address, or a"
					+ " neighborhood name and street address must be present, but none of these was provided.");
		}
		
		if (result == null) {
			if (hasId) {
			    throw new ResourceNotFoundException("grocery store", "id", id.get().toString());
			}
			if (hasAddressId) {
				throw new ResourceNotFoundException("grocery store", "addressId", addressId.get().toString());
			}
			if (hasNeighborhoodId) {
				throw new ResourceNotFoundException("grocery store", "neighborhoodId, streetAddress", 
						neighborhoodId.get().toString() + ", " + streetAddress.get());
			}
			throw new ResourceNotFoundException("grocery store", "neighborhoodName, streetAddress", 
					name.get() + ", " + streetAddress.get());
		}
		
		return result;
	}
    
    @PostMapping(path="")
	@JsonView(POIViews.Limited.class)
	public ResponseEntity<GroceryStore> addGroceryStore(@RequestBody @Valid GroceryStore g) {
		GroceryStore saved = groceryService.addGroceryStore(g);
		URI location = URI.create("/grocerystores/" + saved.getId());
		return ResponseEntity.created(location).body(saved);
	}
	
    @PutMapping(path={"/{id}", "/address/{addressId}", "/address/neighborhood/{neighborhoodId}/streetaddress/{streetAddress}", 
	"/address/neighborhood/name/{name}/streetaddress/{streetAddress}"})
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void modifyGroceryStore(@PathVariable Optional<Integer> id, @PathVariable Optional<Integer> addressId, 
			@PathVariable Optional<Integer> neighborhoodId, @PathVariable Optional<String> streetAddress, 
			@PathVariable Optional<String> name, @RequestBody @Valid GroceryStore g) {		
		if (id.isPresent()) {
			groceryService.modifyGroceryStoreById(id.get(), g);
		}
		else if (addressId.isPresent()) {
			groceryService.modifyGroceryStoreByAddressId(addressId.get(), g);
		}
		else if (neighborhoodId.isPresent() && streetAddress.isPresent()) {
			groceryService.modifyGroceryStoreByNeighborhoodId(neighborhoodId.get(), streetAddress.get(), g);
		}
		else if (name.isPresent() && streetAddress.isPresent()) {
			groceryService.modifyGroceryStoreByNeighborhoodName(name.get(), streetAddress.get(), g);
		}
		else {
			throw new BadRequestException("Either a grocery store id, an address id, a neighborhood id and street address, or a"
					+ " neighborhood name and street address must be present for the grocery store to modify, but none of these "
					+ "was provided.");
		}
		
	}
	
    @DeleteMapping(path={"/{id}", "/address/{addressId}", "/address/neighborhood/{neighborhoodId}/streetaddress/{streetAddress}", 
	"/address/neighborhood/name/{name}/streetaddress/{streetAddress}"})
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteGroceryStore(@PathVariable Optional<Integer> id, @PathVariable Optional<Integer> addressId, 
			@PathVariable Optional<Integer> neighborhoodId, @PathVariable Optional<String> streetAddress, 
			@PathVariable Optional<String> name) {
		Long removed;
		boolean hasId = id.isPresent();
		boolean hasAddressId = addressId.isPresent();
		boolean hasNeighborhoodId = neighborhoodId.isPresent() && streetAddress.isPresent();
		boolean hasNeighborhoodName = name.isPresent() && streetAddress.isPresent();
		
		if (hasId) {
			removed = groceryService.deleteGroceryStoreById(id.get());
		}
		else if (hasAddressId) {
			removed = groceryService.deleteGroceryStoreByAddressId(addressId.get());
		}
		else if (hasNeighborhoodId) {
			removed = groceryService.deleteGroceryStoreByNeighborhoodId(neighborhoodId.get(), streetAddress.get());
		}
		else if (hasNeighborhoodName) {
			removed = groceryService.deleteGroceryStoreByNeighborhoodName(name.get(), streetAddress.get());
		}
		else {
			throw new BadRequestException("Either a grocery store id, an address id, a neighborhood id and street address, or a"
					+ " neighborhood name and street address must be present for the grocery store to delete, but none of these "
					+ "was provided.");
		}
		
		if (removed == 0) {
			if (hasId) {
			    throw new ResourceNotFoundException("grocery store", "id", id.get().toString());
			}
			if (hasAddressId) {
				throw new ResourceNotFoundException("grocery store", "addressId", addressId.get().toString());
			}
			if (hasNeighborhoodId) {
				throw new ResourceNotFoundException("grocery store", "neighborhoodId, streetAddress", 
						neighborhoodId.get().toString() + ", " + streetAddress.get());
			}
			throw new ResourceNotFoundException("grocery store", "neighborhoodName, streetAddress", 
					name.get() + ", " + streetAddress.get());
		}
	}
}
