package baltimoredata.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import baltimoredata.model.VacantBuilding;

public interface VacantBuildingService {
	public Long getVacantBuildingCount();
	public Page<VacantBuilding> getVacantBuildings(Pageable pageReq);
	public VacantBuilding getVacantBuildingById(Integer id);
	public VacantBuilding getVacantBuildingByReferenceId(String referenceId);
	public VacantBuilding addVacantBuilding(VacantBuilding b);
	public void modifyVacantBuildingById(Integer id, VacantBuilding modified);
	public void modifyVacantBuildingByReferenceId(String referenceId, VacantBuilding modified);
	public Long deleteVacantBuildingById(Integer id);
	public Long deleteVacantBuildingByReferenceId(String referenceId);
}
