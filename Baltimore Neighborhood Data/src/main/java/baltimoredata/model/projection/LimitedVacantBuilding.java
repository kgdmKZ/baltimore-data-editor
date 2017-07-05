package baltimoredata.model.projection;

import java.util.Date;

public interface LimitedVacantBuilding {
	Integer getId();
	LimitedLocation getLocation();
	String getReferenceid();
	Date getNoticedate();
}
