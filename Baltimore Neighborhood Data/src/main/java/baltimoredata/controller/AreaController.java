package baltimoredata.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
import baltimoredata.model.Area;
import baltimoredata.model.Neighborhood;
import baltimoredata.service.AreaService;
import baltimoredata.view.AreaViews;
import baltimoredata.view.NeighborhoodViews;

@RestController
@RequestMapping(path="/areas")
public class AreaController {
	@Autowired
	private AreaService areaService;
	
	@GetMapping(path="")
	@JsonView(AreaViews.Limited.class)
	public Page<Area> getAreas(Pageable pageReq) {
		return areaService.getAreas(pageReq);
	}
	
	@GetMapping(path="/count")
	public Integer getAreaCount() {
		return areaService.getAreaCount().intValue();
	}
	
	@GetMapping(path={"/{id}", "/csa2010/{csa2010}"})
	@JsonView(AreaViews.Limited.class)
	public Area getArea(@PathVariable Optional<Integer> id, @PathVariable Optional<String> csa2010) {
		Area result = null;
		boolean hasId = id.isPresent();
		if (hasId) {
			result = areaService.getAreaById(id.get());
		}
		else if (csa2010.isPresent()) {
			result = areaService.getAreaByCsa2010(csa2010.get());
		}
		else {
			throw new BadRequestException("An id or CSA name is required, but neither was provided.");
		}
		
		if (result == null) {
			if (hasId) {
			    throw new ResourceNotFoundException("area", "id", id.get().toString());
			}
			throw new ResourceNotFoundException("area", "csa2010", csa2010.get());
		}
		
		return result;
	}
	
	@PostMapping(path="")
	@JsonView(AreaViews.Limited.class)
	public ResponseEntity<Area> addArea(@RequestBody @Valid Area a) {
		Area saved = areaService.addArea(a);
		URI location = URI.create("/areas/" + saved.getId());
		return ResponseEntity.created(location).body(saved);
	}
	
	@PutMapping(path={"/{id}", "/csa2010/{csa2010}"})
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void modifyArea(@PathVariable Optional<Integer> id, @PathVariable Optional<String> csa2010,
			@RequestBody @Valid Area a) {		
		if (id.isPresent()) {
			areaService.modifyAreaById(id.get(), a);
		}
		else if (csa2010.isPresent()) {
			areaService.modifyAreaByCsa2010(csa2010.get(), a);
		}
		else {
			throw new BadRequestException("An id or CSA name for the area to modify is required, but neither was provided.");
		}
	}
	
	@DeleteMapping(path={"/{id}", "/csa2010/{csa2010}"})
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteArea(@PathVariable Optional<Integer> id, @PathVariable Optional<String> csa2010) {
		Long removed;
		boolean hasId = id.isPresent();
		
		if (hasId) {
			removed = areaService.deleteAreaById(id.get());
		}
		else if (csa2010.isPresent()) {
			removed = areaService.deleteAreaByCsa2010(csa2010.get());
		}
		else {
			throw new BadRequestException("An id or CSA name for the area to delete is required, but neither was provided.");
		}
		
		if (removed == 0) {
			if (hasId) {
			    throw new ResourceNotFoundException("area", "id", id.get().toString());
			}
			throw new ResourceNotFoundException("area", "csa2010", csa2010.get());
		}
	}
	
	@GetMapping(path={"/{id}/neighborhoods", "/csa2010/{csa2010}/neighborhoods"})
	@JsonView(NeighborhoodViews.Limited.class)
	public Page<Neighborhood> getNeighborhoodsByArea(@PathVariable Optional<Integer> id, @PathVariable Optional<String> csa2010, 
			Pageable pageReq) {
		Area a = this.getArea(id, csa2010);
		List<Neighborhood> neighborhoodList = new ArrayList<Neighborhood>(a.getNeighborhoods());
		Page<Neighborhood> neighborhoods = new PageImpl<>(neighborhoodList, pageReq, neighborhoodList.size());
		return neighborhoods;
	}
	
	@GetMapping(path={"/{id}/neighborhoods/count", "/csa2010/{csa2010}/neighborhoods/count"})
	public Integer getNeighborhoodCountByArea(@PathVariable Optional<Integer> id, @PathVariable Optional<String> csa2010) {
		Area a = this.getArea(id, csa2010);
		return a.getNeighborhoods().size();
	}
}
