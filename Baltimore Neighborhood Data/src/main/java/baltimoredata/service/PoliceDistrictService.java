package baltimoredata.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import baltimoredata.model.PoliceDistrict;

public interface PoliceDistrictService {
	public Long getPoliceDistrictCount();
	public Page<PoliceDistrict> getPoliceDistricts(Pageable pageReq);
	public PoliceDistrict getPoliceDistrictById(Integer id);
	public PoliceDistrict getPoliceDistrictByDistrictName(String districtName);
	public PoliceDistrict addPoliceDistrict(PoliceDistrict p);
	public void modifyPoliceDistrictById(Integer id, PoliceDistrict modified);
	public void modifyPoliceDistrictByDistrictName(String districtName, PoliceDistrict modified);
	public Long deletePoliceDistrictById(Integer id);
	public Long deletePoliceDistrictByDistrictName(String districtName);
}
