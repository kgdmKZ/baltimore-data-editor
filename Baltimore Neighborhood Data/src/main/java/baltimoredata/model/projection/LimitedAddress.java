package baltimoredata.model.projection;

public interface LimitedAddress {
	Integer getId();
	LimitedCouncilDistrict getCouncilDistrict();
	LimitedNeighborhood getNeighborhood();
	LimitedPoliceDistrict getPoliceDistrict();
	String getStreetAddress();
}
