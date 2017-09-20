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
import baltimoredata.model.ZipCode;
import baltimoredata.service.ZipCodeService;
import baltimoredata.view.POIViews;

@RestController
@RequestMapping(path="/zipcodes")
public class ZipCodeController {
    @Autowired
    private ZipCodeService zipCodeService;
    
    @GetMapping(path="")
    @JsonView(POIViews.Limited.class)
    public Page<ZipCode> getZipCodes(Pageable pageReq) {
    	return zipCodeService.getZipCodes(pageReq);
    }
    
    @GetMapping(path="/count")
    public Integer getCouncilDistrictCount() {
    	return zipCodeService.getZipCodeCount().intValue();
    }
    
    
    @GetMapping(path={"/{id}", "/zip/{zip}"})
    @JsonView(POIViews.Limited.class)
    public ZipCode getZipCode(@PathVariable Optional<Integer> id, @PathVariable Optional<Integer> zip) {
    	ZipCode result = null;
    	boolean hasId = id.isPresent();
    	if (hasId) {
    		result = zipCodeService.getZipCodeById(id.get());
    	}
    	else if (zip.isPresent()) {
    		result = zipCodeService.getZipCodeByZip(zip.get());
    	}
    	else {
    		throw new BadRequestException("An id or zip code is required, but neither was provided.");
    	}
    	
    	if (result == null) {
    		if (hasId) {
    			throw new ResourceNotFoundException("zip code", "id", id.get().toString());
    		}
    		throw new ResourceNotFoundException("zip code", "zip", zip.get().toString());
    	}
    	
    	return result;
    }
    
    @PostMapping(path="")
    @JsonView(POIViews.Limited.class)
    public ResponseEntity<ZipCode> addZipCode(@RequestBody @Valid ZipCode z) {
    	ZipCode saved = zipCodeService.addZipCode(z);
    	URI location = URI.create("/zipcodes" + saved.getId());
    	return ResponseEntity.created(location).body(saved);
    }
    
    @PutMapping(path={"/{id}", "/zip/{zip}"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void modifyZipCode(@PathVariable Optional<Integer> id, @PathVariable Optional<Integer> zip,
    		@RequestBody @Valid ZipCode z) {
    	if (id.isPresent()) {
    		zipCodeService.modifyZipCodeById(id.get(), z);
    	}
    	else if (zip.isPresent()) {
    		zipCodeService.modifyZipCodeByZip(zip.get(), z);
    	}
    	else {
    		throw new BadRequestException("The id or zip code for the zip code resource to modify is required, but neither was provided.");
    	}
    	
    }
    
    @DeleteMapping(path={"/{id}", "/zip/{zip}"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteZipCode(@PathVariable Optional<Integer> id, @PathVariable Optional<Integer> zip) {
    	Long removed;
    	boolean hasId = id.isPresent();
    	
    	if (hasId) {
    		removed = zipCodeService.deleteZipCodeById(id.get());
    	}
    	else if (zip.isPresent()) {
    		removed = zipCodeService.deleteZipCodeByZip(zip.get());
    	}
    	else {
    		throw new BadRequestException("The id or zip code for the zip code resource to delete is required, but neither was provided.");
    	}
    	
    	if (removed == 0) {
    		if (hasId) {
    			throw new ResourceNotFoundException("zip code", "id", id.get().toString());
    		}
    		throw new ResourceNotFoundException("zip code", "zip", zip.get().toString());
    	}
    }
}

