package baltimoredata.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import baltimoredata.exception.ConflictException;
import baltimoredata.exception.ResourceNotFoundException;
import baltimoredata.model.PoliceDistrict;
import baltimoredata.repository.PoliceDistrictRepository;
import baltimoredata.service.PoliceDistrictService;

@Service
@Transactional
public class PoliceDistrictServiceImpl implements PoliceDistrictService {
    @Autowired
    PoliceDistrictRepository policeRepository;
    
    @Transactional(readOnly=true)
	public Long getPoliceDistrictCount() {
		return policeRepository.count();
	}
	
	@Transactional(readOnly=true)
	public Page<PoliceDistrict> getPoliceDistricts(Pageable pageReq) {
		return policeRepository.findAll(pageReq);
	}
	
	@Transactional(readOnly=true)
	public PoliceDistrict getPoliceDistrictById(Integer id) {
		return policeRepository.findOne(id);
	}
	
	@Transactional(readOnly=true)
	public PoliceDistrict getPoliceDistrictByDistrictName(String districtName) {
		return policeRepository.findByDistrictName(districtName);
	}
	
	public PoliceDistrict addPoliceDistrict(PoliceDistrict p) {
		String districtName = p.getDistrictName();
		PoliceDistrict res = policeRepository.findByDistrictName(districtName);
		if (res != null) {
			throw new ConflictException("police district", "districtName", districtName);
		}
		return policeRepository.save(p);
	}
	
	private void modifyPoliceDistrict(PoliceDistrict modified, PoliceDistrict existing) {
		String newDistrictName = modified.getDistrictName();
		PoliceDistrict conflictPoliceDistrict = policeRepository.findByDistrictName(newDistrictName);
		Integer existingId = existing.getId();
		Integer conflictId = conflictPoliceDistrict == null ? null : conflictPoliceDistrict.getId();
		
		if (conflictId != null && conflictId != existingId) {
			throw new ConflictException("police district", "districtName", newDistrictName);
		}
		
		existing.setDistrictName(newDistrictName);
		policeRepository.save(existing);
	}
	
	public void modifyPoliceDistrictById(Integer id, PoliceDistrict modified) {
		PoliceDistrict existing = policeRepository.findOne(id);
		if (existing == null) {
			throw new ResourceNotFoundException("police district", "id", id.toString());
		}
		modifyPoliceDistrict(modified, existing);
	}
	 
	public void modifyPoliceDistrictByDistrictName(String districtName, PoliceDistrict modified) {
		PoliceDistrict existing = policeRepository.findByDistrictName(districtName);
		if (existing == null) {
			throw new ResourceNotFoundException("police district", "districtName", districtName);
		}
		modifyPoliceDistrict(modified, existing);
	}
	
	public Long deletePoliceDistrictById(Integer id) {
		return policeRepository.removeById(id);
	}
	
	public Long deletePoliceDistrictByDistrictName(String districtName) {
		return policeRepository.removeByDistrictName(districtName);
	}

}
