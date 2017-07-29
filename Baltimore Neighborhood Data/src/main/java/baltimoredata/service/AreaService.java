package baltimoredata.service;

import java.util.Optional;

public interface AreaService {
    public Integer getAreaCount();
    public void deleteArea(Optional<Integer> id, Optional<String> csa2010);
}
