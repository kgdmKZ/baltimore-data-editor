package baltimoredata.model.projection;

import baltimoredata.model.projection.LimitedAddress;
import baltimoredata.model.projection.LimitedGroceryType;
import baltimoredata.model.projection.LimitedZipCode;

public interface LimitedGroceryStore {
	Integer getId();
	LimitedAddress getAddress();
	LimitedGroceryType getGroceryType();
    LimitedZipCode getZipCode();
	String getName();
}
