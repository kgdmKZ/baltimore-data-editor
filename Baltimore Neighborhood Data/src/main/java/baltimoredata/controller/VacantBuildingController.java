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
import baltimoredata.model.VacantBuilding;
import baltimoredata.service.VacantBuildingService;
import baltimoredata.view.POIViews;

@RestController
@RequestMapping(path="/vacantbuildings")
public class VacantBuildingController {
	@Autowired
    private VacantBuildingService vacantService;
    
    @GetMapping(path="")
    @JsonView(POIViews.Limited.class)
    public Page<VacantBuilding> getVacantBuildings(Pageable pageReq) {
    	return vacantService.getVacantBuildings(pageReq);
    }
    
    @GetMapping(path="/count")
	public Integer getVacantBuildingCount() {
		return vacantService.getVacantBuildingCount().intValue();
	}
    
    @GetMapping(path={"/{id}", "/referenceid/{referenceId}"})
	@JsonView(POIViews.Limited.class)
	public VacantBuilding getVacantBuilding(@PathVariable Optional<Integer> id, @PathVariable Optional<String> referenceId) {
		VacantBuilding result = null;
		boolean hasReferenceId = referenceId.isPresent();
		
		if (id.isPresent()) {
			result = vacantService.getVacantBuildingById(id.get());
		}
		else if (hasReferenceId) {
			result = vacantService.getVacantBuildingByReferenceId(referenceId.get());
		}
		else {
			throw new BadRequestException("Either a vacant building id or a reference id must be present, but neither was provided.");
		}
		
		if (result == null) {
			if (hasReferenceId) {
				throw new ResourceNotFoundException("vacant building", "referenceId", referenceId.get());
			}
			throw new ResourceNotFoundException("vacant building", "id", id.get().toString());
		}
		
		return result;
	}
    
    @PostMapping(path="")
	@JsonView(POIViews.Limited.class)
	public ResponseEntity<VacantBuilding> addVacantBuilding(@RequestBody @Valid VacantBuilding b) {
		VacantBuilding saved = vacantService.addVacantBuilding(b);
		URI location = URI.create("/vacantbuildings/" + saved.getId());
		return ResponseEntity.created(location).body(saved);
	}
	
    @PutMapping(path={"/{id}", "/referenceid/{referenceId}"})
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void modifyVacantBuilding(@PathVariable Optional<Integer> id, @PathVariable Optional<String> referenceId, 
			@RequestBody @Valid VacantBuilding b) {		
		if (id.isPresent()) {
			vacantService.modifyVacantBuildingById(id.get(), b);
		}
		else if (referenceId.isPresent()) {
			vacantService.modifyVacantBuildingByReferenceId(referenceId.get(), b);
		}
		else {
			throw new BadRequestException("Either a vacant building id or a reference id must be present, but neither was provided.");
		}
		
	}
	
    @DeleteMapping(path={"/{id}", "/referenceid/{referenceId}"})
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteVacantBuilding(@PathVariable Optional<Integer> id, @PathVariable Optional<String> referenceId) {
		Long removed;
		boolean hasReferenceId = referenceId.isPresent();
		
		if (id.isPresent()) {
			removed = vacantService.deleteVacantBuildingById(id.get());
		}
		else if (hasReferenceId) {
			removed = vacantService.deleteVacantBuildingByReferenceId(referenceId.get());
		}
		else {
			throw new BadRequestException("Either a vacant building id or a reference id must be present, but neither was provided.");
		}
		
		if (removed == 0) {
			if (hasReferenceId) {
				throw new ResourceNotFoundException("vacant building", "referenceId", referenceId.get());
			}
			throw new ResourceNotFoundException("vacant building", "id", id.get().toString());
		}
	}
}
