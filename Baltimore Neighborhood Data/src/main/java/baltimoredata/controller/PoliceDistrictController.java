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
import baltimoredata.model.PoliceDistrict;
import baltimoredata.service.PoliceDistrictService;
import baltimoredata.view.AddressViews;

@RestController
@RequestMapping(path="/policedistricts")
public class PoliceDistrictController {
    @Autowired
    private PoliceDistrictService policeService;
    
    @GetMapping(path="")
    @JsonView(AddressViews.Limited.class)
    public Page<PoliceDistrict> getPoliceDistricts(Pageable pageReq) {
    	return policeService.getPoliceDistricts(pageReq);
    }
    
    @GetMapping(path="/count")
	public Integer getPoliceDistrictCount() {
		return policeService.getPoliceDistrictCount().intValue();
	}
    
    @GetMapping(path={"/{id}", "/districtname/{districtName}"})
	@JsonView(AddressViews.Limited.class)
	public PoliceDistrict getPoliceDistrict(@PathVariable Optional<Integer> id, @PathVariable Optional<String> districtName) {
		PoliceDistrict result = null;
		boolean hasId = id.isPresent();
		if (hasId) {
			result = policeService.getPoliceDistrictById(id.get());
		}
		else if (districtName.isPresent()) {
			result = policeService.getPoliceDistrictByDistrictName(districtName.get());
		}
		else {
			throw new BadRequestException("An id or district name is required, but neither was provided.");
		}
		
		if (result == null) {
			if (hasId) {
			    throw new ResourceNotFoundException("police district", "id", id.get().toString());
			}
			throw new ResourceNotFoundException("police district", "districtName", districtName.get());
		}
		
		return result;
	}
    
    @PostMapping(path="")
	@JsonView(AddressViews.Limited.class)
	public ResponseEntity<PoliceDistrict> addPoliceDistrict(@RequestBody @Valid PoliceDistrict p) {
		PoliceDistrict saved = policeService.addPoliceDistrict(p);
		URI location = URI.create("/policedistricts/" + saved.getId());
		return ResponseEntity.created(location).body(saved);
	}
	
	@PutMapping(path={"/{id}", "/districtname/{districtName}"})
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void modifyPoliceDistrict(@PathVariable Optional<Integer> id, @PathVariable Optional<String> districtName,
			@RequestBody @Valid PoliceDistrict p) {		
		if (id.isPresent()) {
			policeService.modifyPoliceDistrictById(id.get(), p);
		}
		else if (districtName.isPresent()) {
			policeService.modifyPoliceDistrictByDistrictName(districtName.get(), p);
		}
		else {
			throw new BadRequestException("An id or district name for the police district to modify is required, but neither was provided.");
		}
	}
	
	@DeleteMapping(path={"/{id}", "/districtname/{districtName}"})
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletePoliceDistrict(@PathVariable Optional<Integer> id, @PathVariable Optional<String> districtName) {
		Long removed;
		boolean hasId = id.isPresent();
		
		if (hasId) {
			removed = policeService.deletePoliceDistrictById(id.get());
		}
		else if (districtName.isPresent()) {
			removed = policeService.deletePoliceDistrictByDistrictName(districtName.get());
		}
		else {
			throw new BadRequestException("An id or district name for the police district to delete is required, but neither was provided.");
		}
		
		if (removed == 0) {
			if (hasId) {
			    throw new ResourceNotFoundException("police district", "id", id.get().toString());
			}
			throw new ResourceNotFoundException("police district", "districtName", districtName.get());
		}
	}
	
}
