package baltimoredata.model.projection;

import baltimoredata.model.projection.LimitedCouncilDistrict;
import baltimoredata.model.projection.LimitedNeighborhood;
import baltimoredata.model.projection.LimitedPoliceDistrict;

public interface LimitedAddress {
	Integer getId();
	LimitedCouncilDistrict getCouncilDistrict();
	LimitedNeighborhood getNeighborhood();
	LimitedPoliceDistrict getPoliceDistrict();
	String getStreetAddress();
}
