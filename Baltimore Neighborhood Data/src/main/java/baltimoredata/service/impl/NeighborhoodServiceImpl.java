package baltimoredata.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import baltimoredata.exception.ConflictException;
import baltimoredata.exception.ResourceNotFoundException;
import baltimoredata.exception.UnprocessableEntityException;
import baltimoredata.model.Area;
import baltimoredata.model.Neighborhood;
import baltimoredata.repository.AreaRepository;
import baltimoredata.repository.NeighborhoodRepository;
import baltimoredata.service.NeighborhoodService;

@Service
@Transactional
public class NeighborhoodServiceImpl implements NeighborhoodService {
	@Autowired
	NeighborhoodRepository neighborhoodRepository;
	
	@Autowired
	AreaRepository areaRepository;
	
	public Long getNeighborhoodCount() {
		return neighborhoodRepository.count();
	}
	
    public Page<Neighborhood> getNeighborhoods(Pageable pageReq) {
    	return neighborhoodRepository.findAll(pageReq);
    }
    
    public Neighborhood getNeighborhoodById(Integer id) {
        return neighborhoodRepository.findOne(id);	
    }
    
    
    public Neighborhood getNeighborhoodByName(String name) {
    	return neighborhoodRepository.findByName(name);
    }
    
    public Neighborhood addNeighborhood(Neighborhood n) {
		String area = n.getArea().getCsa2010();
		Area a = areaRepository.findByCsa2010(area);
		if (a == null) {
			throw new UnprocessableEntityException("area", "csa2010", area);
		}
		
		String name = n.getName();
		Neighborhood res = neighborhoodRepository.findByName(name);
		if (res != null) {
			throw new ConflictException("neighborhood", "name", name);
		}
		
		n.setArea(a);
		return neighborhoodRepository.save(n);
    }
    
    private void modifyNeighborhood(Neighborhood modified, Neighborhood existing) {
    	Integer existingId = existing.getId();
		String newName = modified.getName();
		Neighborhood conflictNeighborhood = neighborhoodRepository.findByName(newName);
		Integer conflictId = conflictNeighborhood == null ? null : conflictNeighborhood.getId();
		if (conflictNeighborhood != null && conflictId != existingId) {
			throw new ConflictException("neighborhood", "name", newName);
		}
		
		String newAreaName = modified.getArea().getCsa2010();
		Area newArea = areaRepository.findByCsa2010(newAreaName);
		if (newArea == null) {
			throw new UnprocessableEntityException("area", "csa2010", newAreaName);
		}
		
		existing.setName(newName);
		existing.setArea(newArea);
		neighborhoodRepository.save(existing);
	}
    
    public void modifyNeighborhoodById(Integer id, Neighborhood modified) {
    	Neighborhood existing = neighborhoodRepository.findOne(id);
    	if (existing == null) {
    		throw new ResourceNotFoundException("neighborhood", "id", id.toString());
    	}
    	modifyNeighborhood(modified, existing);
    }
    
    public void modifyNeighborhoodByName(String name, Neighborhood modified) {
    	Neighborhood existing = neighborhoodRepository.findByName(name);
    	if (existing == null) {
    		throw new ResourceNotFoundException("neighborhood", "name", name);
    	}
    	modifyNeighborhood(modified, existing);
    }
    
    public Long deleteNeighborhoodById(Integer id) {
    	return neighborhoodRepository.removeById(id);
    };
    
    public Long deleteNeighborhoodByName(String name) {
    	return neighborhoodRepository.removeByName(name);
    };
}
