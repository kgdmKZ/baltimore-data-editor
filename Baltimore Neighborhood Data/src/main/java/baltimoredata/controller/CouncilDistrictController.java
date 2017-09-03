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
import baltimoredata.model.CouncilDistrict;
import baltimoredata.service.CouncilDistrictService;
import baltimoredata.view.AddressViews;

@RestController
@RequestMapping(path="/councildistricts")
public class CouncilDistrictController {
	@Autowired
    private CouncilDistrictService councilService;
    
    @GetMapping(path="")
    @JsonView(AddressViews.Limited.class)
    public Page<CouncilDistrict> getCouncilDistricts(Pageable pageReq) {
    	return councilService.getCouncilDistricts(pageReq);
    }
    
    @GetMapping(path="/count")
	public Integer getCouncilDistrictCount() {
		return councilService.getCouncilDistrictCount().intValue();
	}
    
    @GetMapping(path={"/{id}", "/districtnumber/{districtNumber}"})
	@JsonView(AddressViews.Limited.class)
	public CouncilDistrict getCouncilDistrict(@PathVariable Optional<Integer> id, @PathVariable Optional<Integer> districtNumber) {
		CouncilDistrict result = null;
		boolean hasId = id.isPresent();
		if (hasId) {
			result = councilService.getCouncilDistrictById(id.get());
		}
		else if (districtNumber.isPresent()) {
			result = councilService.getCouncilDistrictByDistrictNumber(districtNumber.get());
		}
		else {
			throw new BadRequestException("An id or district number is required, but neither was provided.");
		}
		
		if (result == null) {
			if (hasId) {
			    throw new ResourceNotFoundException("council district", "id", id.get().toString());
			}
			throw new ResourceNotFoundException("council district", "districtNumber", districtNumber.get().toString());
		}
		
		return result;
	}
    
    @PostMapping(path="")
	@JsonView(AddressViews.Limited.class)
	public ResponseEntity<CouncilDistrict> addCouncilDistrict(@RequestBody @Valid CouncilDistrict c) {
		CouncilDistrict saved = councilService.addCouncilDistrict(c);
		URI location = URI.create("/councildistricts/" + saved.getId());
		return ResponseEntity.created(location).body(saved);
	}
	
	@PutMapping(path={"/{id}", "/districtnumber/{districtNumber}"})
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void modifyCouncilDistrict(@PathVariable Optional<Integer> id, @PathVariable Optional<Integer> districtNumber,
			@RequestBody @Valid CouncilDistrict c) {		
		if (id.isPresent()) {
			councilService.modifyCouncilDistrictById(id.get(), c);
		}
		else if (districtNumber.isPresent()) {
			councilService.modifyCouncilDistrictByDistrictNumber(districtNumber.get(), c);
		}
		else {
			throw new BadRequestException("An id or district number for the council district to modify is required, but neither was provided.");
		}
	}
	
	@DeleteMapping(path={"/{id}", "/districtnumber/{districtNumber}"})
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteCouncilDistrict(@PathVariable Optional<Integer> id, @PathVariable Optional<Integer> districtNumber) {
		Long removed;
		boolean hasId = id.isPresent();
		
		if (hasId) {
			removed = councilService.deleteCouncilDistrictById(id.get());
		}
		else if (districtNumber.isPresent()) {
			removed = councilService.deleteCouncilDistrictByDistrictNumber(districtNumber.get());
		}
		else {
			throw new BadRequestException("An id or district number for the council district to delete is required, but neither was provided.");
		}
		
		if (removed == 0) {
			if (hasId) {
			    throw new ResourceNotFoundException("council district", "id", id.get().toString());
			}
			throw new ResourceNotFoundException("council district", "districtNumber", districtNumber.get().toString());
		}
	}
}
