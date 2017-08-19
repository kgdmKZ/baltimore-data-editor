package baltimoredata.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import baltimoredata.model.Area;

public interface AreaService {
    public Long getAreaCount();
    public Page<Area> getAreas(Pageable pageReq);
    public Area getAreaById(Integer id);
    public Area getAreaByCsa2010(String csa2010);
    public Area addArea(Area a);
    public void modifyAreaById(Integer id, Area modified);
    public void modifyAreaByCsa2010(String csa2010, Area modified);
    public Long deleteAreaById(Integer id);
    public Long deleteAreaByCsa2010(String csa2010);
    
}
