package baltimoredata.model.projection;

import java.io.Serializable;

public interface LimitedLocation {
	Integer getId();
	LimitedAddress getAddress();
	Serializable getCoords();
	String getBlockNumber();
	String getLotNumber();
}
