package baltimoredata.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import baltimoredata.exception.ConflictException;
import baltimoredata.exception.ResourceNotFoundException;
import baltimoredata.model.VacantBuilding;
import baltimoredata.repository.VacantBuildingRepository;
import baltimoredata.service.VacantBuildingService;

@Service
@Transactional
public class VacantBuildingServiceImpl implements VacantBuildingService {
	@Autowired
    VacantBuildingRepository vacantRepository;
    
    @Transactional(readOnly=true)
	public Long getVacantBuildingCount() {
		return vacantRepository.count();
	}
	
	@Transactional(readOnly=true)
	public Page<VacantBuilding> getVacantBuildings(Pageable pageReq) {
		return vacantRepository.findAll(pageReq);
	}
	
	@Transactional(readOnly=true)
	public VacantBuilding getVacantBuildingById(Integer id) {
		return vacantRepository.findOne(id);
	}
	
	@Transactional(readOnly=true)
	public VacantBuilding getVacantBuildingByReferenceId(String referenceId) {
		return vacantRepository.findByReferenceid(referenceId);
	}
	
	public VacantBuilding addVacantBuilding(VacantBuilding b) {
		String referenceId = b.getReferenceid();
		VacantBuilding res = vacantRepository.findByReferenceid(referenceId);
		if (res != null) {
			throw new ConflictException("vacant building", "referenceId", referenceId);
		}
		return vacantRepository.save(b);
	}
	
	private void modifyVacantBuilding(VacantBuilding modified, VacantBuilding existing) {
		String newReferenceId = modified.getReferenceid();
		VacantBuilding conflictVacantBuilding = vacantRepository.findByReferenceid(newReferenceId);
		Integer existingId = existing.getId();
		Integer conflictId = conflictVacantBuilding == null ? null : conflictVacantBuilding.getId();
		
		if (conflictId != null && conflictId != existingId) {
			throw new ConflictException("vacant building", "referenceId", newReferenceId);
		}
		
		existing.setReferenceid(newReferenceId);
		existing.setLocation(modified.getLocation());
		existing.setNoticedate(modified.getNoticedate());
		vacantRepository.save(existing);
	}
	
	public void modifyVacantBuildingById(Integer id, VacantBuilding modified) {
		VacantBuilding existing = vacantRepository.findOne(id);
		if (existing == null) {
			throw new ResourceNotFoundException("vacant building", "id", id.toString());
		}
		modifyVacantBuilding(modified, existing);
	}
	 
	public void modifyVacantBuildingByReferenceId(String referenceId, VacantBuilding modified) {
		VacantBuilding existing = vacantRepository.findByReferenceid(referenceId);
		if (existing == null) {
			throw new ResourceNotFoundException("vacant building", "referenceId", referenceId.toString());
		}
		modifyVacantBuilding(modified, existing);
	}
	
	
	public Long deleteVacantBuildingById(Integer id) {
		return vacantRepository.removeById(id);
	}
	
	public Long deleteVacantBuildingByReferenceId(String referenceId) {
		return vacantRepository.removeByReferenceid(referenceId);
	}

}

