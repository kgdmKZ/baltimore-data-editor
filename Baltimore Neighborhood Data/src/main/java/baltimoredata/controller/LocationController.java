package baltimoredata.controller;

import java.io.Serializable;
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
import baltimoredata.model.Location;
import baltimoredata.service.LocationService;
import baltimoredata.view.POIViews;

@RestController
@RequestMapping(path="/locations")
public class LocationController {
	@Autowired
    private LocationService locationService;
    
    @GetMapping(path="")
    @JsonView(POIViews.Limited.class)
    public Page<Location> getLocations(Pageable pageReq) {
    	return locationService.getLocations(pageReq);
    }
    
    @GetMapping(path="/count")
	public Integer getLocationCount() {
		return locationService.getLocationCount().intValue();
	}
    
    
    
    @GetMapping(path={"/{id}", "/address/{addressId}", "/address/neighborhood/{neighborhoodId}/streetaddress/{streetAddress}", 
    		"/address/neighborhood/name/{name}/streetaddress/{streetAddress}", "/coords/{coords}", 
    		"/blocknumber/{blockNumber}/lotnumber/{lotNumber}"})
	@JsonView(POIViews.Limited.class)
	public Location getLocation(@PathVariable Optional<Integer> id, @PathVariable Optional<Integer> addressId,
			@PathVariable Optional<Integer> neighborhoodId, @PathVariable Optional<String> name, 
			@PathVariable Optional<String> streetAddress, @PathVariable Optional<Serializable> coords, 
			@PathVariable Optional<String> blockNumber, @PathVariable Optional<String> lotNumber) {
		Location result = null;
		boolean hasId = id.isPresent();
		boolean hasAddressId = addressId.isPresent();
		boolean hasNeighborhoodId = neighborhoodId.isPresent() && streetAddress.isPresent();
		boolean hasNeighborhoodName = name.isPresent() && streetAddress.isPresent();
		boolean hasCoords = coords.isPresent();
		boolean hasBlockAndLot = blockNumber.isPresent() && lotNumber.isPresent();
		
		if (hasId) {
			result = locationService.getLocationById(id.get());
		}
		else if (hasAddressId) {
			result = locationService.getLocationByAddressId(addressId.get());
		}
		else if (hasNeighborhoodId) {
			result = locationService.getLocationByNeighborhoodId(neighborhoodId.get(), streetAddress.get());
		}
		else if (hasNeighborhoodName) {
			result = locationService.getLocationByNeighborhoodName(name.get(), streetAddress.get());
		}
		else if (hasCoords) {
			result = locationService.getLocationByCoords(coords.get());
		}
		else if (hasBlockAndLot) {
			result = locationService.getLocationByBlockNumberAndLotNumber(blockNumber.get(), lotNumber.get());
		}
		else {
			throw new BadRequestException("Either a location id, an address id, a neighborhood id and street address, a"
					+ " neighborhood name and street address, coordinates, or a block number and lot number must be present, "
					+ "but none of these was provided.");
		}
		
		if (result == null) {
			if (hasId) {
			    throw new ResourceNotFoundException("location", "id", id.get().toString());
			}
			if (hasAddressId) {
				throw new ResourceNotFoundException("location", "addressId", addressId.get().toString());
			}
			if (hasNeighborhoodId) {
				throw new ResourceNotFoundException("location", "neighborhoodId, streetAddress", 
						neighborhoodId.get().toString() + ", " + streetAddress.get());
			}
			if (hasCoords) {
				throw new ResourceNotFoundException("location", "coords", coords.get().toString());
			}
			if (hasBlockAndLot) {
				throw new ResourceNotFoundException("location", "blockNumber, lotNumber", blockNumber.get() + ", " + lotNumber.get());
			}
			throw new ResourceNotFoundException("location", "neighborhoodName, streetAddress", 
					name.get() + ", " + streetAddress.get());
		}
		
		return result;
	}
    
    @PostMapping(path="")
	@JsonView(POIViews.Limited.class)
	public ResponseEntity<Location> addLocation(@RequestBody @Valid Location l) {
		Location saved = locationService.addLocation(l);
		URI location = URI.create("/locations/" + saved.getId());
		return ResponseEntity.created(location).body(saved);
	}
	
    @PutMapping(path={"/{id}", "/address/{addressId}", "/address/neighborhood/{neighborhoodId}/streetaddress/{streetAddress}", 
    		"/address/neighborhood/name/{name}/streetaddress/{streetAddress}", "/coords/{coords}", 
    		"/blocknumber/{blockNumber}/lotnumber/{lotNumber}"})
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void modifyLocation(@PathVariable Optional<Integer> id, @PathVariable Optional<Integer> addressId, 
			@PathVariable Optional<Integer> neighborhoodId, @PathVariable Optional<String> streetAddress, 
			@PathVariable Optional<String> name, @PathVariable Optional<Serializable> coords, 
			@PathVariable Optional<String> blockNumber, @PathVariable Optional<String> lotNumber, @RequestBody @Valid Location l) {		
		if (id.isPresent()) {
			locationService.modifyLocationById(id.get(), l);
		}
		else if (addressId.isPresent()) {
			locationService.modifyLocationByAddressId(addressId.get(), l);
		}
		else if (neighborhoodId.isPresent() && streetAddress.isPresent()) {
			locationService.modifyLocationByNeighborhoodId(neighborhoodId.get(), streetAddress.get(), l);
		}
		else if (name.isPresent() && streetAddress.isPresent()) {
			locationService.modifyLocationByNeighborhoodName(name.get(), streetAddress.get(), l);
		}
		else if (coords.isPresent()) {
			locationService.modifyLocationByCoords(coords.get(), l);
		}
		else if (blockNumber.isPresent() && lotNumber.isPresent()) {
			locationService.modifyLocationByBlockNumberAndLotNumber(blockNumber.get(), lotNumber.get(), l);
		}
		else {
			throw new BadRequestException("Either a location id, an address id, a neighborhood id and street address, a"
					+ " neighborhood name and street address, coordinates, or a block number and lot number must be present for the"
					+ " location to modify, but none of these was provided.");
		}
		
	}
	
    @DeleteMapping(path={"/{id}", "/address/{addressId}", "/address/neighborhood/{neighborhoodId}/streetaddress/{streetAddress}", 
    		"/address/neighborhood/name/{name}/streetaddress/{streetAddress}", "/coords/{coords}", 
    		"/blocknumber/{blockNumber}/lotnumber/{lotNumber}"})
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteLocation(@PathVariable Optional<Integer> id, @PathVariable Optional<Integer> addressId, 
			@PathVariable Optional<Integer> neighborhoodId, @PathVariable Optional<String> streetAddress, 
			@PathVariable Optional<String> name, @PathVariable Optional<Serializable> coords, 
			@PathVariable Optional<String> blockNumber, @PathVariable Optional<String> lotNumber) {
		Long removed;
		boolean hasId = id.isPresent();
		boolean hasAddressId = addressId.isPresent();
		boolean hasNeighborhoodId = neighborhoodId.isPresent() && streetAddress.isPresent();
		boolean hasNeighborhoodName = name.isPresent() && streetAddress.isPresent();
		boolean hasCoords = coords.isPresent();
		boolean hasBlockAndLot = blockNumber.isPresent() && lotNumber.isPresent();
		
		if (hasId) {
			removed = locationService.deleteLocationById(id.get());
		}
		else if (hasAddressId) {
			removed = locationService.deleteLocationByAddressId(addressId.get());
		}
		else if (hasNeighborhoodId) {
			removed = locationService.deleteLocationByNeighborhoodId(neighborhoodId.get(), streetAddress.get());
		}
		else if (hasNeighborhoodName) {
			removed = locationService.deleteLocationByNeighborhoodName(name.get(), streetAddress.get());
		}
		else if (hasCoords) {
			removed = locationService.deleteLocationByCoords(coords.get());
		}
		else if (hasBlockAndLot) {
			removed = locationService.deleteLocationByBlockNumberAndLotNumber(blockNumber.get(), lotNumber.get());
		}
		else {
			throw new BadRequestException("Either a location id, an address id, a neighborhood id and street address, a"
					+ " neighborhood name and street address, coordinates, or a block number and lot number must be present for the"
					+ "location to delete, but none of these was provided.");
		}
		
		if (removed == 0) {
			if (hasId) {
			    throw new ResourceNotFoundException("location", "id", id.get().toString());
			}
			if (hasAddressId) {
				throw new ResourceNotFoundException("location", "addressId", addressId.get().toString());
			}
			if (hasNeighborhoodId) {
				throw new ResourceNotFoundException("location", "neighborhoodId, streetAddress", 
						neighborhoodId.get().toString() + ", " + streetAddress.get());
			}
			if (hasCoords) {
				throw new ResourceNotFoundException("location", "coords", coords.get().toString());
			}
			if (hasBlockAndLot) {
				throw new ResourceNotFoundException("location", "blockNumber, lotNumber", blockNumber.get() + ", " + lotNumber.get());
			}
			throw new ResourceNotFoundException("location", "neighborhoodName, streetAddress", 
					name.get() + ", " + streetAddress.get());
		}
	}
}

