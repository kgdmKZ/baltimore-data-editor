package baltimoredata.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import baltimoredata.repository.NeighborhoodRepository;
import baltimoredata.model.projection.LimitedNeighborhood;

@Controller 
@RequestMapping(path="") 
public class MainController {
	@Autowired // get generated repository
	private NeighborhoodRepository neighborhoodRepository;
    
	@GetMapping(path="")
	public @ResponseBody LimitedNeighborhood displayNeighborhood(Integer id) {
		return neighborhoodRepository.findById(id);
	}
	
}