package baltimoredata.model.projection;

import java.util.Date;

import baltimoredata.model.projection.LimitedLocation;

public interface LimitedVacantBuilding {
	Integer getId();
	LimitedLocation getLocation();
	String getReferenceid();
	Date getNoticedate();
}
