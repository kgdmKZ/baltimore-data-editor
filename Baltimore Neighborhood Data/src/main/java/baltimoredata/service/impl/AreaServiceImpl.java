package baltimoredata.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import baltimoredata.exception.ConflictException;
import baltimoredata.exception.ResourceNotFoundException;
import baltimoredata.model.Area;
import baltimoredata.repository.AreaRepository;
import baltimoredata.service.AreaService;

@Service
@Transactional
public class AreaServiceImpl implements AreaService {
    
	@Autowired
	AreaRepository areaRepository;
	
	@Transactional(readOnly=true)
	public Long getAreaCount() {
		return areaRepository.count();
	}
	
	@Transactional(readOnly=true)
	public Page<Area> getAreas(Pageable pageReq) {
		return areaRepository.findAll(pageReq);
	}
	
	@Transactional(readOnly=true)
	public Area getAreaById(Integer id) {
		return areaRepository.findOne(id);
	}
	
	@Transactional(readOnly=true)
	public Area getAreaByCsa2010(String csa2010) {
		return areaRepository.findByCsa2010(csa2010);
	}
	
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
	
	public void modifyAreaById(Integer id, Area modified) {
		Area existing = areaRepository.findOne(id);
		if (existing == null) {
			throw new ResourceNotFoundException("area", "id", id.toString());
		}
		modifyArea(modified, existing);
	}
	 
	public void modifyAreaByCsa2010(String csa2010, Area modified) {
		Area existing = areaRepository.findByCsa2010(csa2010);
		if (existing == null) {
			throw new ResourceNotFoundException("area", "csa2010", csa2010);
		}
		modifyArea(modified, existing);
	}
	
	public Long deleteAreaById(Integer id) {
		return areaRepository.removeById(id);
	}
	
	public Long deleteAreaByCsa2010(String csa2010) {
		return areaRepository.removeByCsa2010(csa2010);
	}

}
