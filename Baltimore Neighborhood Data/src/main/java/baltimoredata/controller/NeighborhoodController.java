package baltimoredata.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import baltimoredata.model.Area;
import baltimoredata.model.Neighborhood;
import baltimoredata.repository.AreaRepository;
import baltimoredata.repository.NeighborhoodRepository;
import baltimoredata.view.NeighborhoodViews;

@RestController 
@RequestMapping(path="/neighborhoods") 
public class NeighborhoodController {
	@Autowired
	private NeighborhoodRepository neighborhoodRepository;
	
	@Autowired
	private AreaRepository areaRepository;
	
	@GetMapping(path="/count")
	public Integer getNeighborhoodCount() {
		Long count = neighborhoodRepository.count();
		return count.intValue();
	}
	
	@GetMapping(path="")
	@JsonView(NeighborhoodViews.Limited.class)
	public List<Neighborhood> getNeighborhoods(Pageable pageReq) {
		return neighborhoodRepository.listAll(pageReq);
	}
	
	
	@GetMapping(path={"/area/csa2010/{csa2010}/count", "/area/{id}/count"})
    public ResponseEntity<Integer> getNeighborhoodCountByArea(@PathVariable Optional<Integer> id, 
		@PathVariable Optional<String> csa2010) {
	    Area a = null;
	    if (id.isPresent()) {
	    	a = areaRepository.findOne(id.get());
	    }
	    else if (csa2010.isPresent()) {
	    	a = areaRepository.findByCsa2010(csa2010.get());
	    }
	    else {
	    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	    }
	    
	    if (a == null) {
	    	return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	    }
        Integer total = neighborhoodRepository.countByArea_Id(a.getId());
        return ResponseEntity.ok(total);
    }
    
	@GetMapping(path={"/area/csa2010/{csa2010}", "/area/{id}"})
    @JsonView(NeighborhoodViews.Minimal.class)
    public ResponseEntity<List<Neighborhood>> getNeighborhoodsByArea(@PathVariable Optional<Integer> id, 
    		@PathVariable Optional<String> csa2010, Pageable pageReq) {
		Area a = null;
	    if (id.isPresent()) {
	    	a = areaRepository.findOne(id.get());
	    }
	    else if (csa2010.isPresent()) {
	    	a = areaRepository.findByCsa2010(csa2010.get());
	    }
	    else {
	    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	    }
	    
	    if (a == null) {
	    	return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	    }
        List<Neighborhood> neighborhoods = neighborhoodRepository.findByArea_Id(a.getId(), pageReq);
        return ResponseEntity.ok(neighborhoods);
    }

	
	@GetMapping(path={"/{id}", "/name/{name}"})
	@JsonView(NeighborhoodViews.Limited.class)
	public ResponseEntity<Neighborhood> getNeighborhood(@PathVariable Optional<Integer> id, @PathVariable Optional<String> name) {
		Neighborhood result;
		if (id.isPresent()) {
			result = neighborhoodRepository.findOne(id.get());
		}
		else if (name.isPresent()) {
			result = neighborhoodRepository.findByName(name.get());
		}
		else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		
		if (result == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
		return ResponseEntity.ok(result);
	}
	
	
	@PostMapping(path="")
	@JsonView(NeighborhoodViews.Limited.class)
	public ResponseEntity<Neighborhood> addNeighborhood(@RequestBody @Valid Neighborhood n) {
		String name = n.getName();
		String area = n.getArea().getCsa2010();
		Area a = areaRepository.findByCsa2010(area);
		if (a == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
		
		Neighborhood old = neighborhoodRepository.findByName(name);
		if (old != null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
		}
		
		n.setArea(a);
		Neighborhood saved = neighborhoodRepository.save(n);
		URI location = URI.create("/neighborhoods/" + saved.getId());
		return ResponseEntity.created(location).body(saved);
	}
	
	@PutMapping(path={"/{id}", "/name/{name}"})
	public ResponseEntity<String> modifyNeighborhood(@PathVariable Optional<Integer> id, @PathVariable Optional<String> name,
			@RequestBody @Valid Neighborhood n) {
		Neighborhood old;
		if (id.isPresent()) {
			old = neighborhoodRepository.findOne(id.get());
		}
		else if (name.isPresent()) {
			old = neighborhoodRepository.findByName(name.get());
		}
		else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No id or name was provided for the neighborhood to modify.");
		}
		
		if (old == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No neighborhood with the provided id or name exists.");
		}
		
		Integer oldId = old.getId();
		String newName = n.getName();
		Neighborhood conflictNeighborhood = neighborhoodRepository.findByName(newName);
		Integer conflictId = conflictNeighborhood == null ? null : conflictNeighborhood.getId();
		if (conflictNeighborhood != null && conflictId != oldId) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Another neighborhood already has the provided new name.");
		}
		
		String newAreaName = n.getArea().getCsa2010();
		Area newArea = areaRepository.findByCsa2010(newAreaName);
		if (newArea == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No area exists with the provided name.");
		}
		
		old.setName(newName);
		old.setArea(newArea);
		neighborhoodRepository.save(old);
		return ResponseEntity.ok("Updated.");
		
	}
	
	@DeleteMapping(path={"/{id}", "/name/{name}"})
	public ResponseEntity<String> deleteNeighborhood(@PathVariable Optional<Integer> id, @PathVariable Optional<String> name) {
		Long removed;
		if (id.isPresent()) {
			removed = neighborhoodRepository.removeById(id.get());	
		}
		else if (name.isPresent()) {
			removed = neighborhoodRepository.removeByName(name.get());
		}
		else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No id or name was provided for the neighborhood to remove.");
		}
		
		if (removed == 0) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No neighborhood with the provided id or name exists.");
		}
		return ResponseEntity.ok("Deleted.");
	}	
	
}