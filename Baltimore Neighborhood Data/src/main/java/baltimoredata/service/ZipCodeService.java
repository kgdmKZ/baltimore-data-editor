package baltimoredata.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import baltimoredata.model.ZipCode;

public interface ZipCodeService {
	public Long getZipCodeCount();
	public Page<ZipCode> getZipCodes(Pageable pageReq);
	public ZipCode getZipCodeById(Integer id);
	public ZipCode getZipCodeByZip(Integer zip);
	public ZipCode addZipCode(ZipCode z);
	public void modifyZipCodeById(Integer id, ZipCode modified);
	public void modifyZipCodeByZip(Integer zip, ZipCode modified);
	public Long deleteZipCodeById(Integer id);
	public Long deleteZipCodeByZip(Integer zip);
}
