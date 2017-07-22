package baltimoredata.controller;

import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import baltimoredata.model.Area;
import baltimoredata.model.Neighborhood;
import baltimoredata.model.projection.LimitedArea;
import baltimoredata.repository.AreaRepository;
import baltimoredata.view.AreaViews;
import baltimoredata.view.NeighborhoodViews;

@RestController
@RequestMapping(path="/areas")
public class AreaController {
	@Autowired
	private AreaRepository areaRepository;
	
	@GetMapping(path="")
	public List<LimitedArea> getAreas(Pageable pageReq) {
		return areaRepository.listAll(pageReq);
	}
	
	@GetMapping(path="/{id}")
	@JsonView(AreaViews.Limited.class)
	public Area getArea(@PathVariable Integer id) {
		return areaRepository.findOne(id);
	}
	
	@PostMapping(path="")
	public ResponseEntity<String> addArea(@RequestBody @Valid Area a) {
		String csa2010 = a.getCsa2010();
		Area match = areaRepository.findByCsa2010(csa2010);
		
		if (match != null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("An area already exists with the provided name.");
		}
		
		areaRepository.save(a);
		return ResponseEntity.ok("Saved.");
	}
	
	@GetMapping(path="/name/{area}/neighborhoods/count")
	public ResponseEntity<Integer> getNeighborhoodCount(@PathVariable String area) {
		Area a = areaRepository.findByCsa2010(area);
		if (a == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
		Integer total = a.getNeighborhoods().size();
		return ResponseEntity.ok(total);
	}
	
	@GetMapping(path="/name/{area}/neighborhoods")
	@JsonView(NeighborhoodViews.Minimal.class)
    public ResponseEntity<Set<Neighborhood>> getNeighborhoods(@PathVariable String area, Pageable pageReq) {
		Area a = areaRepository.findByCsa2010(area);
		if (a == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
		Set<Neighborhood> neighborhoods = a.getNeighborhoods();
		return ResponseEntity.ok(neighborhoods);
	}
}
