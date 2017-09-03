package baltimoredata.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import baltimoredata.model.CouncilDistrict;

public interface CouncilDistrictService {
	public Long getCouncilDistrictCount();
	public Page<CouncilDistrict> getCouncilDistricts(Pageable pageReq);
	public CouncilDistrict getCouncilDistrictById(Integer id);
	public CouncilDistrict getCouncilDistrictByDistrictNumber(Integer districtNumber);
	public CouncilDistrict addCouncilDistrict(CouncilDistrict c);
	public void modifyCouncilDistrictById(Integer id, CouncilDistrict modified);
	public void modifyCouncilDistrictByDistrictNumber(Integer districtNumber, CouncilDistrict modified);
	public Long deleteCouncilDistrictById(Integer id);
	public Long deleteCouncilDistrictByDistrictNumber(Integer districtNumber);
}
