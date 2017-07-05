package baltimoredata.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import baltimoredata.model.projection.LimitedNeighborhood;
import baltimoredata.repository.NeighborhoodRepository;

@Controller 
@RequestMapping(path="/neighborhoods") 
public class MainController {
	@Autowired // get generated repository
	private NeighborhoodRepository neighborhoodRepository;
    	
	@GetMapping(path="/name/{name}")
	public ResponseEntity<LimitedNeighborhood> getNeighborhoodByName(@PathVariable String name) {
		Optional<LimitedNeighborhood> result = neighborhoodRepository.findByName(name);
		if (!result.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
		return ResponseEntity.ok(result.get());
	}
	
	@GetMapping(path="/area/{area}/count")
	public @ResponseBody Integer getNeighborhoodCountByArea(@PathVariable String area) {
		return neighborhoodRepository.countByArea_Csa2010(area);
	}
	
	@GetMapping(path="/area/{area}")
    public @ResponseBody List<LimitedNeighborhood> getNeighborhoodsByArea(@PathVariable String area, Pageable pageReq) {
		return neighborhoodRepository.findByArea_Csa2010(area, pageReq);
	}
	
}