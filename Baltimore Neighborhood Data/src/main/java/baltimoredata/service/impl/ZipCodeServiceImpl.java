package baltimoredata.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import baltimoredata.exception.ConflictException;
import baltimoredata.exception.ResourceNotFoundException;
import baltimoredata.model.ZipCode;
import baltimoredata.repository.ZipCodeRepository;
import baltimoredata.service.ZipCodeService;

@Service
@Transactional
public class ZipCodeServiceImpl implements ZipCodeService {
	@Autowired
    ZipCodeRepository zipCodeRepository;
    
    @Transactional(readOnly=true)
    public Long getZipCodeCount() {
    	return zipCodeRepository.count();
    }
    
    @Transactional(readOnly=true)
	public Page<ZipCode> getZipCodes(Pageable pageReq) {
		return zipCodeRepository.findAll(pageReq);
	}
	
    @Transactional(readOnly=true)
	public ZipCode getZipCodeById(Integer id) {
		return zipCodeRepository.findOne(id);
	}
	
	@Transactional(readOnly=true)
	public ZipCode getZipCodeByZip(Integer zip) {
		return zipCodeRepository.findByZip(zip);
	}
	
	
	public ZipCode addZipCode(ZipCode z) {
		Integer zip = z.getZip();
		ZipCode res = zipCodeRepository.findByZip(zip);
		if (res != null) {
			throw new ConflictException("zip code", "zip", zip.toString());
		}
		return zipCodeRepository.save(z);
	}
	
	private void modifyZipCode(ZipCode modified, ZipCode existing) {
		Integer newZip = modified.getZip();
		ZipCode conflictZipCode = zipCodeRepository.findByZip(newZip);
		Integer existingId = existing.getId();
		Integer conflictId = conflictZipCode == null ? null : conflictZipCode.getId();
		
		if (conflictId != null && conflictId != existingId) {
			throw new ConflictException("zip code", "zip", newZip.toString());
		}
		
		existing.setZip(newZip);
		zipCodeRepository.save(existing);
	}
	
	public void modifyZipCodeById(Integer id, ZipCode modified) {
		ZipCode existing = zipCodeRepository.findOne(id);
		if (existing == null) {
			throw new ResourceNotFoundException("zip code", "id", id.toString());
		}
		modifyZipCode(modified, existing);
	}
	
	public void modifyZipCodeByZip(Integer zip, ZipCode modified) {
		ZipCode existing = zipCodeRepository.findByZip(zip);
		if (existing == null) {
			throw new ResourceNotFoundException("zip code", "zip", zip.toString());
		}
		modifyZipCode(modified, existing);
	}
	
	public Long deleteZipCodeById(Integer id) {
		return zipCodeRepository.removeById(id);
	}
	
	public Long deleteZipCodeByZip(Integer zip) {
		return zipCodeRepository.removeByZip(zip);
	}
}
