package baltimoredata.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import baltimoredata.exception.ConflictException;
import baltimoredata.exception.ResourceNotFoundException;
import baltimoredata.model.Area;
import baltimoredata.repository.AreaRepository;

@Service
public class AreaServiceImpl {
    
	@Autowired
	AreaRepository areaRepository;
	
	@Transactional(readOnly=true)
	public Long getAreaCount() {
		return areaRepository.count();
	}
	
	@Transactional(readOnly=true)
	public List<Area> getAreas(Pageable pageReq) {
		return areaRepository.listAll(pageReq);
	}
	
	@Transactional(readOnly=true)
	public Area getAreaById(Integer id) {
		return areaRepository.findOne(id);
	}
	
	@Transactional(readOnly=true)
	public Area getAreaByCsa2010(String csa2010) {
		return areaRepository.findByCsa2010(csa2010);
	}
	
	@Transactional
	public Area addArea(Area a) {
		String csa2010 = a.getCsa2010();
		Area res = areaRepository.findByCsa2010(csa2010);
		if (res != null) {
			throw new ConflictException("area", "csa2010", csa2010);
		}
		return areaRepository.save(a);
	}
	
	private void modifyArea(Area modified, Area existing) {
		String newCsa2010 = modified.getCsa2010();
		Area conflictArea = areaRepository.findByCsa2010(newCsa2010);
		Integer existingId = existing.getId();
		Integer conflictId = conflictArea == null ? null : conflictArea.getId();
		
		if (conflictId != null && conflictId != existingId) {
			throw new ConflictException("area", "csa2010", newCsa2010);
		}
		
		modified.setId(existingId);
		areaRepository.save(modified);
	}
	
	@Transactional
	public void modifyAreaById(Integer id, Area modified) {
		Area existing = areaRepository.findOne(id);
		if (existing == null) {
			throw new ResourceNotFoundException("area", "id", id.toString());
		}
		modifyArea(modified, existing);
	}
	
	@Transactional 
	public void modifyAreaByCsa2010(String csa2010, Area modified) {
		Area existing = areaRepository.findByCsa2010(csa2010);
		if (existing == null) {
			throw new ResourceNotFoundException("area", "csa2010", csa2010);
		}
		modifyArea(modified, existing);
	}
	
	@Transactional
	public Long deleteAreaById(Integer id) {
		return areaRepository.removeById(id);
	}
	
	@Transactional 
	public Long deleteAreaByCsa2010(String csa2010) {
		return areaRepository.removeByCsa2010(csa2010);
	}

}
