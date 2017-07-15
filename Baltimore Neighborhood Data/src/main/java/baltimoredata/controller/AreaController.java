package baltimoredata.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import baltimoredata.model.Area;
import baltimoredata.model.projection.LimitedArea;
import baltimoredata.repository.AreaRepository;

@RestController
@RequestMapping(path="/areas")
public class AreaController {
	@Autowired
	private AreaRepository areaRepository;
	
	@GetMapping(path="")
	public List<LimitedArea> getAreas(Pageable pageReq) {
		return areaRepository.listAll(pageReq);
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
}
