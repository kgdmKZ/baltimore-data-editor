package baltimoredata.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import baltimoredata.exception.ConflictException;
import baltimoredata.exception.ResourceNotFoundException;
import baltimoredata.model.GroceryType;
import baltimoredata.repository.GroceryTypeRepository;
import baltimoredata.service.GroceryTypeService;

@Service
@Transactional
public class GroceryTypeServiceImpl implements GroceryTypeService {
    @Autowired
    GroceryTypeRepository groceryTypeRepository;
    
    @Transactional(readOnly=true)
    public Long getGroceryTypeCount() {
    	return groceryTypeRepository.count();
    }
    
    @Transactional(readOnly=true)
	public Page<GroceryType> getGroceryTypes(Pageable pageReq) {
		return groceryTypeRepository.findAll(pageReq);
	}
	
    @Transactional(readOnly=true)
	public GroceryType getGroceryTypeById(Integer id) {
		return groceryTypeRepository.findOne(id);
	}
	
	@Transactional(readOnly=true)
	public GroceryType getGroceryTypeByName(String name) {
		return groceryTypeRepository.findByName(name);
	}
	
	
	public GroceryType addGroceryType(GroceryType g) {
		String name = g.getName();
		GroceryType res = groceryTypeRepository.findByName(name);
		if (res != null) {
			throw new ConflictException("grocery type", "name", name);
		}
		return groceryTypeRepository.save(g);
	}
	
	private void modifyGroceryType(GroceryType modified, GroceryType existing) {
		String newName = modified.getName();
		GroceryType conflictGroceryType = groceryTypeRepository.findByName(newName);
		Integer existingId = existing.getId();
		Integer conflictId = conflictGroceryType == null ? null : conflictGroceryType.getId();
		
		if (conflictId != null && conflictId != existingId) {
			throw new ConflictException("grocery type", "name", newName);
		}
		
		existing.setName(newName);
		groceryTypeRepository.save(existing);
	}
	
	public void modifyGroceryTypeById(Integer id, GroceryType modified) {
		GroceryType existing = groceryTypeRepository.findOne(id);
		if (existing == null) {
			throw new ResourceNotFoundException("grocery type", "id", id.toString());
		}
		modifyGroceryType(modified, existing);
	}
	
	public void modifyGroceryTypeByName(String name, GroceryType modified) {
		GroceryType existing = groceryTypeRepository.findByName(name);
		if (existing == null) {
			throw new ResourceNotFoundException("grocery type", "name", name);
		}
		modifyGroceryType(modified, existing);
	}
	
	public Long deleteGroceryTypeById(Integer id) {
		return groceryTypeRepository.removeById(id);
	}
	
	public Long deleteGroceryTypeByName(String name) {
		return groceryTypeRepository.removeByName(name);
	}
}
