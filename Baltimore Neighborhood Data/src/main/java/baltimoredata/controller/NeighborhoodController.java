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
import baltimoredata.model.Neighborhood;
import baltimoredata.service.NeighborhoodService;
import baltimoredata.view.NeighborhoodViews;

@RestController 
@RequestMapping(path="/neighborhoods") 
public class NeighborhoodController {
	@Autowired
	private NeighborhoodService neighborhoodService;
	
	@GetMapping(path="/count")
	public Integer getNeighborhoodCount() {
		return neighborhoodService.getNeighborhoodCount().intValue();
	}
	
	@GetMapping(path="")
	@JsonView(NeighborhoodViews.Limited.class)
	public Page<Neighborhood> getNeighborhoods(Pageable pageReq) {
		return neighborhoodService.getNeighborhoods(pageReq);
	}
	
	
	@GetMapping(path={"/area/csa2010/{csa2010}/count", "/area/{id}/count"})
    public Integer getNeighborhoodCountByArea(@PathVariable Optional<Integer> id, 
		@PathVariable Optional<String> csa2010) {
		
	    if (id.isPresent()) {
	    	return neighborhoodService.getNeighborhoodCountByAreaId(id.get());
	    }
	    if (csa2010.isPresent()) {
	    	return neighborhoodService.getNeighborhoodCountByAreaCsa2010(csa2010.get());
	    }
	    throw new BadRequestException("An area id or CSA name is required, but neither was provided.");
    }
    
	@GetMapping(path={"/area/csa2010/{csa2010}", "/area/{id}"})
    @JsonView(NeighborhoodViews.Minimal.class)
    public Page<Neighborhood> getNeighborhoodsByArea(@PathVariable Optional<Integer> id, 
    		@PathVariable Optional<String> csa2010, Pageable pageReq) {

	    if (id.isPresent()) {
	    	return neighborhoodService.getNeighborhoodsByAreaId(id.get(), pageReq);
	    }
	    if (csa2010.isPresent()) {
	    	return neighborhoodService.getNeighborhoodsByAreaCsa2010(csa2010.get(), pageReq);
	    }
	    throw new BadRequestException("An area id or CSA name is required, but neither was provided.");
    }

	
	@GetMapping(path={"/{id}", "/name/{name}"})
	@JsonView(NeighborhoodViews.Limited.class)
	public Neighborhood getNeighborhood(@PathVariable Optional<Integer> id, @PathVariable Optional<String> name) {
		Neighborhood res = null;
		boolean hasId = id.isPresent();
		if (hasId) {
			res = neighborhoodService.getNeighborhoodById(id.get());
		}
		else if (name.isPresent()) {
			res = neighborhoodService.getNeighborhoodByName(name.get());
		}
		else {
			throw new BadRequestException("A neighborhood id or name is required, but neither was provided.");
		}
		
		if (res == null) {
			if (hasId) {
				throw new ResourceNotFoundException("neighborhood", "id", id.get().toString());
			}
			throw new ResourceNotFoundException("neighborhood", "name", name.get());
		}
		
		return res;
	}
	
	
	@PostMapping(path="")
	@JsonView(NeighborhoodViews.Limited.class)
	public ResponseEntity<Neighborhood> addNeighborhood(@RequestBody @Valid Neighborhood n) {
		Neighborhood saved = neighborhoodService.addNeighborhood(n);
		URI location = URI.create("/neighborhoods/" + saved.getId());
		return ResponseEntity.created(location).body(saved);
	}
	
	@PutMapping(path={"/{id}", "/name/{name}"})
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void modifyNeighborhood(@PathVariable Optional<Integer> id, @PathVariable Optional<String> name,
			@RequestBody @Valid Neighborhood n) {
		if (id.isPresent()) {
			neighborhoodService.modifyNeighborhoodById(id.get(), n);
		}
		else if (name.isPresent()) {
			neighborhoodService.modifyNeighborhoodByName(name.get(), n);
		}
		else {
			throw new BadRequestException("A neighborhood id or name is required, but neither was provided.");
		}
	}
	
	@DeleteMapping(path={"/{id}", "/name/{name}"})
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteNeighborhood(@PathVariable Optional<Integer> id, @PathVariable Optional<String> name) {
		Long removed;
		boolean hasId = id.isPresent();
		if (hasId) {
			removed = neighborhoodService.deleteNeighborhoodById(id.get());	
		}
		else if (name.isPresent()) {
			removed = neighborhoodService.deleteNeighborhoodByName(name.get());
		}
		else {
			throw new BadRequestException("No id or name was provided for the neighborhood to remove.");
		}
		
		if (removed == 0) {
			if (hasId) {
			    throw new ResourceNotFoundException("neighborhood", "id", id.get().toString());
			}
			throw new ResourceNotFoundException("neighborhood", "name", name.get());
		}
	}	
	
}