package baltimoredata.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import baltimoredata.repository.AreaRepository;

//TODO: finish services and integrate into controllers

@Service
public class AreaServiceImpl {
    
	@Autowired
	AreaRepository areaRepository;
	
	@Transactional(readOnly=true)
	public int getAreaCount() {
		Long count = areaRepository.count();
		return count.intValue();
	}
	
	/*@Transactional
	public void deleteArea(Optional<Integer> id, Optional<String> csa2010) {
		Long removed;
		if (id.isPresent()) {
			removed = areaRepository.removeById(id.get());
		}
		else if (csa2010.isPresent()) {
			removed = areaRepository.removeByCsa2010(csa2010.get());
		}
		else {
			throw new BadRequestException();
		}
		
	}*/
}
