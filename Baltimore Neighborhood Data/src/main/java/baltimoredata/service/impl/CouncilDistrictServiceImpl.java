package baltimoredata.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import baltimoredata.exception.ConflictException;
import baltimoredata.exception.ResourceNotFoundException;
import baltimoredata.model.CouncilDistrict;
import baltimoredata.repository.CouncilDistrictRepository;
import baltimoredata.service.CouncilDistrictService;

@Service
@Transactional
public class CouncilDistrictServiceImpl implements CouncilDistrictService {
	@Autowired
    CouncilDistrictRepository councilRepository;
    
    @Transactional(readOnly=true)
	public Long getCouncilDistrictCount() {
		return councilRepository.count();
	}
	
	@Transactional(readOnly=true)
	public Page<CouncilDistrict> getCouncilDistricts(Pageable pageReq) {
		return councilRepository.findAll(pageReq);
	}
	
	@Transactional(readOnly=true)
	public CouncilDistrict getCouncilDistrictById(Integer id) {
		return councilRepository.findOne(id);
	}
	
	@Transactional(readOnly=true)
	public CouncilDistrict getCouncilDistrictByDistrictNumber(Integer districtNumber) {
		return councilRepository.findByDistrictNumber(districtNumber);
	}
	
	public CouncilDistrict addCouncilDistrict(CouncilDistrict c) {
		Integer districtNumber = c.getDistrictNumber();
		CouncilDistrict res = councilRepository.findByDistrictNumber(districtNumber);
		if (res != null) {
			throw new ConflictException("council district", "districtNumber", districtNumber.toString());
		}
		return councilRepository.save(c);
	}
	
	private void modifyCouncilDistrict(CouncilDistrict modified, CouncilDistrict existing) {
		Integer newDistrictNumber = modified.getDistrictNumber();
		CouncilDistrict conflictCouncilDistrict = councilRepository.findByDistrictNumber(newDistrictNumber);
		Integer existingId = existing.getId();
		Integer conflictId = conflictCouncilDistrict == null ? null : conflictCouncilDistrict.getId();
		
		if (conflictId != null && conflictId != existingId) {
			throw new ConflictException("council district", "districtNumber", newDistrictNumber.toString());
		}
		
		existing.setDistrictNumber(newDistrictNumber);
		councilRepository.save(existing);
	}
	
	public void modifyCouncilDistrictById(Integer id, CouncilDistrict modified) {
		CouncilDistrict existing = councilRepository.findOne(id);
		if (existing == null) {
			throw new ResourceNotFoundException("council district", "id", id.toString());
		}
		modifyCouncilDistrict(modified, existing);
	}
	 
	public void modifyCouncilDistrictByDistrictNumber(Integer districtNumber, CouncilDistrict modified) {
		CouncilDistrict existing = councilRepository.findByDistrictNumber(districtNumber);
		if (existing == null) {
			throw new ResourceNotFoundException("council district", "districtNumber", districtNumber.toString());
		}
		modifyCouncilDistrict(modified, existing);
	}
	
	public Long deleteCouncilDistrictById(Integer id) {
		return councilRepository.removeById(id);
	}
	
	public Long deleteCouncilDistrictByDistrictNumber(Integer districtNumber) {
		return councilRepository.removeByDistrictNumber(districtNumber);
	}

}
