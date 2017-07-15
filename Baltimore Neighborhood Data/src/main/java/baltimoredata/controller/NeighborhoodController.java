package baltimoredata.controller;

import java.util.List;

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

import baltimoredata.model.Area;
import baltimoredata.model.Neighborhood;
import baltimoredata.model.projection.LimitedNeighborhood;
import baltimoredata.repository.AreaRepository;
import baltimoredata.repository.NeighborhoodRepository;

@RestController 
@RequestMapping(path="/neighborhoods") 
public class NeighborhoodController {
	@Autowired
	private NeighborhoodRepository neighborhoodRepository;
	
	@Autowired
	private AreaRepository areaRepository;
	
	@GetMapping(path="")
	public List<LimitedNeighborhood> getNeighborhoods(Pageable pageReq) {
		return neighborhoodRepository.listAll(pageReq);
	}
	
	@GetMapping(path="/count")
	public int getNeighborhoodCount() {
		Long count = neighborhoodRepository.count();
		return count.intValue();
	}
	
	private ResponseEntity<LimitedNeighborhood> neighborhoodIfExists(LimitedNeighborhood res) {
		if (res == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
		return ResponseEntity.ok(res);
	}
	
	@GetMapping(path="/{id}")
	public ResponseEntity<LimitedNeighborhood> getNeighborhoodById(@PathVariable Integer id) {
		LimitedNeighborhood result = neighborhoodRepository.findById(id);
		return neighborhoodIfExists(result);
	}
	
	@GetMapping(path="/name/{name}")
	public ResponseEntity<LimitedNeighborhood> getNeighborhoodByName(@PathVariable String name) {
		LimitedNeighborhood result = neighborhoodRepository.findByName(name);
		return neighborhoodIfExists(result);
	}
	
	private boolean areaExists(String area) {
		Area a = areaRepository.findByCsa2010(area);
		return (a != null);
	}
	
	@GetMapping(path="/area/{area}/count")
	public ResponseEntity<Integer> getNeighborhoodCountByArea(@PathVariable String area) {
		if (!areaExists(area)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
		Integer total = neighborhoodRepository.countByArea_Csa2010(area);
		return ResponseEntity.ok(total);
	}
	
	@GetMapping(path="/area/{area}")
    public ResponseEntity<List<LimitedNeighborhood>> getNeighborhoodsByArea(@PathVariable String area, Pageable pageReq) {
		if (!areaExists(area)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
		List<LimitedNeighborhood> neighborhoods = neighborhoodRepository.findByArea_Csa2010(area, pageReq);
		return ResponseEntity.ok(neighborhoods);
	}
	
	@PostMapping(path="")
	public ResponseEntity<String> addNeighborhood(@RequestBody @Valid Neighborhood n) {
		String name = n.getName();
		String area = n.getArea().getCsa2010();
		Area a = areaRepository.findByCsa2010(area);
		
		if (a == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No area exists with the provided name.");
		}
		
		LimitedNeighborhood old = neighborhoodRepository.findByName(name);
		
		if (old != null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("A neighborhood with the provided name already exists.");
		}
		
		Neighborhood neighborhood = new Neighborhood(a, name);
		neighborhoodRepository.save(neighborhood);
		return ResponseEntity.ok("Saved.");
	}
	
	@PutMapping(path="/{id}")
	public ResponseEntity<String> modifyNeighborhoodById(@PathVariable Integer id, @RequestBody @Valid Neighborhood n) {
		String name = n.getName();
		String area = n.getArea().getCsa2010();
		Area a = areaRepository.findByCsa2010(area);
		
		if (a == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No area exists with the provided name.");
		}
		
		Neighborhood old = neighborhoodRepository.findOne(id);
		if (old == null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("No neighborhood with the provided id already exists.");
		}
		old.setName(name);
		old.setArea(a);
		neighborhoodRepository.save(old);
		return ResponseEntity.ok("Updated.");
		
	}
	
	@PutMapping(path="/name/{name}")
	public ResponseEntity<String> modifyNeighborhoodByName(@PathVariable String name, @RequestBody @Valid Neighborhood n) {
		String newName = n.getName();
		String area = n.getArea().getCsa2010();
		Area a = areaRepository.findByCsa2010(area);
		
		if (a == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No area exists with the provided name.");
		}
		
		Neighborhood old = neighborhoodRepository.findByNameFull(name);
		if (old == null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("No neighborhood with the provided name already exists.");
		}
		old.setName(newName);
		old.setArea(a);
		neighborhoodRepository.save(old);
		return ResponseEntity.ok("Updated.");
	}
	
	@DeleteMapping(path="/{id}")
	public ResponseEntity<String> deleteNeighborhoodById(@PathVariable Integer id) {
		Long removed = neighborhoodRepository.removeById(id);
		if (removed == 0) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No neighborhood with the provided id exists.");
		}
		return ResponseEntity.ok("Deleted.");
	}
	
	@DeleteMapping(path="/name/{name}")
	public ResponseEntity<String> deleteNeighborhoodByName(@PathVariable String name) {
		Long removed = neighborhoodRepository.removeByName(name);
		if (removed == 0) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No neighborhood with the provided name exists.");
		}
		return ResponseEntity.ok("Deleted.");
	}
	
	
}