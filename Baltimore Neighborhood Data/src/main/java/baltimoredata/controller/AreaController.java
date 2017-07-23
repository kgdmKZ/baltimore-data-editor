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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import baltimoredata.model.Area;
import baltimoredata.repository.AreaRepository;
import baltimoredata.view.AreaViews;

@RestController
@RequestMapping(path="/areas")
public class AreaController {
	@Autowired
	private AreaRepository areaRepository;
	
	@GetMapping(path="")
	@JsonView(AreaViews.Limited.class)
	public List<Area> getAreas(Pageable pageReq) {
		return areaRepository.listAll(pageReq);
	}
	
	@GetMapping(path="/count")
	public Integer getAreaCount() {
		Long count = areaRepository.count();
		return count.intValue();
	}
	
	@GetMapping(path={"/{id}", "/csa2010/{csa2010}"})
	@JsonView(AreaViews.Limited.class)
	public ResponseEntity<Area> getArea(@PathVariable Optional<Integer> id, @PathVariable Optional<String> csa2010) {
		Area result = null;
		if (id.isPresent()) {
			result = areaRepository.findOne(id.get());
		}
		else if (csa2010.isPresent()) {
			result = areaRepository.findByCsa2010(csa2010.get());
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
	@JsonView(AreaViews.Limited.class)
	public ResponseEntity<Area> addArea(@RequestHeader String host, @RequestBody @Valid Area a) {
		String csa2010 = a.getCsa2010();
		Area match = areaRepository.findByCsa2010(csa2010);
		
		if (match != null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
		
		Area saved = areaRepository.save(a);
		URI location = URI.create("/areas/" + saved.getId());
		return ResponseEntity.created(location).body(saved);
	}
	
	@PutMapping(path={"/{id}", "/csa2010/{csa2010}"})
	public ResponseEntity<String> modifyArea(@PathVariable Optional<Integer> id, @PathVariable Optional<String> csa2010,
			@RequestBody @Valid Area a) {
		Area existing = null;
		if (id.isPresent()) {
			existing = areaRepository.findOne(id.get());
		}
		else if (csa2010.isPresent()) {
			existing = areaRepository.findByCsa2010(csa2010.get());
		}
		else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No id or csa name was provided for the area to modify.");
		}
		
		if (existing == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The area to modify does not exist.");
		}
		
		String newCsa2010 = a.getCsa2010();
		Area conflictArea = areaRepository.findByCsa2010(newCsa2010);
		Integer existingId = existing.getId();
		Integer conflictId = conflictArea == null ? null : conflictArea.getId();
		if (conflictId != null && conflictId != existingId) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Another area already has the new CSA name.");
		}
		
		a.setId(existing.getId());
		areaRepository.save(a);
		return ResponseEntity.ok("Updated.");
	}
	
	@DeleteMapping(path={"/{id}", "/csa2010/{csa2010}"})
	public ResponseEntity<String> deleteArea(@PathVariable Optional<Integer> id, @PathVariable Optional<String> csa2010) {
		Long removed;
		if (id.isPresent()) {
			removed = areaRepository.removeById(id.get());
		}
		else if (csa2010.isPresent()) {
			removed = areaRepository.removeByCsa2010(csa2010.get());
		}
		else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No id or CSA name for the area to delete was provided.");
		}
		
		if (removed == 0) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The area to be removed does not exist.");
		}
		
		return ResponseEntity.ok("Deleted.");
	}
	
}
