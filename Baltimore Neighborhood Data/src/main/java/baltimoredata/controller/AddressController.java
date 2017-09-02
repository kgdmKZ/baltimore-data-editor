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
import baltimoredata.model.Address;
import baltimoredata.service.AddressService;
import baltimoredata.view.AddressViews;

@RestController
@RequestMapping(path="/addresses")
public class AddressController {
    @Autowired
    private AddressService addressService;
    
    @GetMapping(path="/count")
    public Integer getAddressCount() {
    	return addressService.getAddressCount().intValue();
    }
    
    @GetMapping(path="")
    @JsonView(AddressViews.Limited.class)
    public Page<Address> getAddresses(Pageable pageReq) {
    	return addressService.getAddresses(pageReq);
    }
    
    @GetMapping(path={"/neighborhood/name/{name}/count", "/neighborhood/{id}/count"})
    public Integer getAddressCountByNeighborhood(@PathVariable Optional<Integer> id, @PathVariable Optional<String> name) {
    	if (id.isPresent()) {
    		return addressService.getAddressCountByNeighborhoodId(id.get());
    	}
    	if (name.isPresent()) {
    		return addressService.getAddressCountByNeighborhoodName(name.get());
    	}
    	throw new BadRequestException("A neighborhood id or name is required, but neither was provided.");
    }
    
    @GetMapping(path={"/neighborhood/name/{name}", "/neighborhood/{id}"})
    @JsonView(AddressViews.Limited.class)
    public Page<Address> getAddressesByNeighborhood(@PathVariable Optional<Integer> id, @PathVariable Optional<String> name, Pageable pageReq) {
    	if (id.isPresent()) {
    		return addressService.getAddressesByNeighborhoodId(id.get(), pageReq);
    	}
    	if (name.isPresent()) {
    		return addressService.getAddressesByNeighborhoodName(name.get(), pageReq);
    	}
    	throw new BadRequestException("A neighborhood id or name is required, but neither was provided.");
    }
    
    @GetMapping(path={"/policeDistrict/{id}/count", "/policeDistrict/name/{name}/count"})
    public Integer getAddressCountByPoliceDistrict(@PathVariable Optional<Integer> id, @PathVariable Optional<String> name) {
    	if (id.isPresent()) {
    		return addressService.getAddressCountByPoliceDistrictId(id.get());
    	}
    	if (name.isPresent()) {
    		return addressService.getAddressCountByPoliceDistrictName(name.get());
    	}
    	throw new BadRequestException("A police district id or name is required, but neither was provided.");
    }
    
    @GetMapping(path={"/policeDistrict/{id}", "/policeDistrict/name/{name}"})
    @JsonView(AddressViews.Limited.class)
    public Page<Address> getAddressesByPoliceDistrict(@PathVariable Optional<Integer> id, @PathVariable Optional<String> name, Pageable pageReq) {
    	if (id.isPresent()) {
    		return addressService.getAddressesByPoliceDistrictId(id.get(), pageReq);
    	}
    	if (name.isPresent()) {
    		return addressService.getAddressesByPoliceDistrictName(name.get(), pageReq);
    	}
    	throw new BadRequestException("A police district id or name is required, but neither was provided.");
    }
    
    @GetMapping(path={"/councilDistrict/{id}/count", "/councilDistrict/number/{number}/count"})
    public Integer getAddressCountByCouncilDistrict(@PathVariable Optional<Integer> id, @PathVariable Optional<Integer> number) {
    	if (id.isPresent()) {
    		return addressService.getAddressCountByCouncilDistrictId(id.get());
    	}
    	if (number.isPresent()) {
    		return addressService.getAddressCountByCouncilDistrictNumber(number.get());
    	}
    	throw new BadRequestException("A council district id or number is required, but neither was provided.");
    }
    
    @GetMapping(path={"/councilDistrict/{id}", "/councilDistrict/number/{number}"})
    @JsonView(AddressViews.Limited.class)
    public Page<Address> getAddressesByCouncilDistrict(@PathVariable Optional<Integer> id, @PathVariable Optional<Integer> number, Pageable pageReq) {
    	if (id.isPresent()) {
    		return addressService.getAddressesByCouncilDistrictId(id.get(), pageReq);
    	}
    	if (number.isPresent()) {
    		return addressService.getAddressesByCouncilDistrictNumber(number.get(), pageReq);
    	}
    	throw new BadRequestException("A council district id or number is required, but neither was provided.");
    }
    
    
    @GetMapping(path={"/{id}", "/neighborhood/{neighborhood}/streetAddress/{streetAddress}"})
    @JsonView(AddressViews.Limited.class)
    public Address getAddress(@PathVariable Optional<Integer> id, @PathVariable Optional<String> streetAddress, @PathVariable Optional<String> neighborhood) {
    	Address res = null;
    	boolean hasId = id.isPresent();
    	if (hasId) {
    		res = addressService.getAddressById(id.get());
    	}
    	else if (neighborhood.isPresent() && streetAddress.isPresent()) {
    		res = addressService.getAddressByNeighborhoodAndStreetAddress(neighborhood.get(), streetAddress.get());
    	}
    	else {
    		throw new BadRequestException("An address id or a combination of the neighborhood name and street address must be provided.");
    	}
    	
    	if (res == null) {
    		if (hasId) {
    			throw new ResourceNotFoundException("address", "id", id.get().toString());
    		}
    		throw new ResourceNotFoundException("address", "neighborhood, streetAddress", neighborhood.get() + ", " + streetAddress.get());
    	}
    	
    	return res;
    }
    
    @PostMapping(path="")
    @JsonView(AddressViews.Limited.class)
    public ResponseEntity<Address> addAddress(@RequestBody @Valid Address a) {
    	Address saved = addressService.addAddress(a);
    	URI location = URI.create("/addresses/" + saved.getId());
    	return ResponseEntity.created(location).body(saved);
    }
    
    @PutMapping(path={"/{id}", "/neighborhood/{neighborhood}/streetAddress/{streetAddress}"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void modifyAddress(@PathVariable Optional<Integer> id, @PathVariable Optional<String> neighborhood, @PathVariable Optional<String> streetAddress,
    		@RequestBody @Valid Address a) {
    	if (id.isPresent()) {
    		addressService.modifyAddressById(id.get(), a);
    	}
    	else if (neighborhood.isPresent() && streetAddress.isPresent()) {
    		addressService.modifyAddressByNeighborhoodAndStreetAddress(neighborhood.get(), streetAddress.get(), a);
    	}
    	else {
    		throw new BadRequestException("An address id or a combination of the neighborhood name and street address must be provided.");
    	}
    }
    
    @DeleteMapping(path={"/{id}", "/neighborhood/{neighborhood}/streetAddress/{streetAddress}"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAddress(@PathVariable Optional<Integer> id, @PathVariable Optional<String> neighborhood, @PathVariable Optional<String> streetAddress) {
    	Long removed;
    	boolean hasId = id.isPresent();
    	if (hasId) {
    		removed = addressService.deleteAddressById(id.get());
    	}
    	else if (neighborhood.isPresent() && streetAddress.isPresent()) {
    		removed = addressService.deleteAddressByNeighborhoodAndStreetAddress(neighborhood.get(), streetAddress.get());
    	}
    	else {
    		throw new BadRequestException("An address id or a combination of the neighborhood name and street address must be provided.");
    	}
    	
    	if (removed == 0) {
    		if (hasId) {
    			throw new ResourceNotFoundException("address", "id", id.get().toString());
    		}
    		throw new ResourceNotFoundException("address", "neighborhood, streetAddress", neighborhood.get() + ", " + streetAddress.get());
    	}
    }
} 