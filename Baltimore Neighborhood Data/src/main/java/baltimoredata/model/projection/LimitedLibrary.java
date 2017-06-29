package baltimoredata.model.projection;

import baltimoredata.model.projection.LimitedAddress;
import baltimoredata.model.projection.LimitedZipCode;

public interface LimitedLibrary {
	Integer getId();
	LimitedAddress getAddress();
	LimitedZipCode getZipCode();
	String getName();
}
