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
import baltimoredata.model.GroceryType;
import baltimoredata.service.GroceryTypeService;
import baltimoredata.view.GroceryStoreViews;

@RestController
@RequestMapping(path="/grocerytypes")
public class GroceryTypeController {
    @Autowired
    private GroceryTypeService groceryTypeService;
    
    @GetMapping(path="")
    @JsonView(GroceryStoreViews.Limited.class)
    public Page<GroceryType> getGroceryTypes(Pageable pageReq) {
    	return groceryTypeService.getGroceryTypes(pageReq);
    }
    
    @GetMapping(path="/count")
    public Integer getCouncilDistrictCount() {
    	return groceryTypeService.getGroceryTypeCount().intValue();
    }
    
    
    @GetMapping(path={"/{id}", "/name/{name}"})
    @JsonView(GroceryStoreViews.Limited.class)
    public GroceryType getGroceryType(@PathVariable Optional<Integer> id, @PathVariable Optional<String> name) {
    	GroceryType result = null;
    	boolean hasId = id.isPresent();
    	if (hasId) {
    		result = groceryTypeService.getGroceryTypeById(id.get());
    	}
    	else if (name.isPresent()) {
    		result = groceryTypeService.getGroceryTypeByName(name.get());
    	}
    	else {
    		throw new BadRequestException("An id or grocery type name is required, but neither was provided.");
    	}
    	
    	if (result == null) {
    		if (hasId) {
    			throw new ResourceNotFoundException("grocery type", "id", id.get().toString());
    		}
    		throw new ResourceNotFoundException("grocery type", "name", name.get());
    	}
    	
    	return result;
    }
    
    @PostMapping(path="")
    @JsonView(GroceryStoreViews.Limited.class)
    public ResponseEntity<GroceryType> addGroceryType(@RequestBody @Valid GroceryType g) {
    	GroceryType saved = groceryTypeService.addGroceryType(g);
    	URI location = URI.create("/grocerytypes" + saved.getId());
    	return ResponseEntity.created(location).body(saved);
    }
    
    @PutMapping(path={"/{id}", "/name/{name}"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void modifyGroceryType(@PathVariable Optional<Integer> id, @PathVariable Optional<String> name,
    		@RequestBody @Valid GroceryType g) {
    	if (id.isPresent()) {
    		groceryTypeService.modifyGroceryTypeById(id.get(), g);
    	}
    	else if (name.isPresent()) {
    		groceryTypeService.modifyGroceryTypeByName(name.get(), g);
    	}
    	else {
    		throw new BadRequestException("An id or grocery type name for the grocery type to modify is required, but neither was provided.");
    	}
    	
    }
    
    @DeleteMapping(path={"/{id}", "/name/{name}"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGroceryType(@PathVariable Optional<Integer> id, @PathVariable Optional<String> name) {
    	Long removed;
    	boolean hasId = id.isPresent();
    	
    	if (hasId) {
    		removed = groceryTypeService.deleteGroceryTypeById(id.get());
    	}
    	else if (name.isPresent()) {
    		removed = groceryTypeService.deleteGroceryTypeByName(name.get());
    	}
    	else {
    		throw new BadRequestException("An id or grocery type name for the grocery type to delete is required, but neither was provided.");
    	}
    	
    	if (removed == 0) {
    		if (hasId) {
    			throw new ResourceNotFoundException("grocery type", "id", id.get().toString());
    		}
    		throw new ResourceNotFoundException("grocery type", "name", name.get());
    	}
    }
}
