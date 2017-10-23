package baltimoredata.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import baltimoredata.model.Neighborhood;

public interface NeighborhoodService {
	public Long getNeighborhoodCount();
    public Page<Neighborhood> getNeighborhoods(Pageable pageReq);
    public Neighborhood getNeighborhoodById(Integer id);
    public Neighborhood getNeighborhoodByName(String name);
    public Neighborhood addNeighborhood(Neighborhood n);
    public void modifyNeighborhoodById(Integer id, Neighborhood modified);
    public void modifyNeighborhoodByName(String name, Neighborhood modified);
    public Long deleteNeighborhoodById(Integer id);
    public Long deleteNeighborhoodByName(String name);
}
