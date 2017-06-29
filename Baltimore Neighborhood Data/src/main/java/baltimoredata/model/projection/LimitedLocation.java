package baltimoredata.model.projection;

import java.io.Serializable;

import baltimoredata.model.projection.LimitedAddress;

public interface LimitedLocation {
	Integer getId();
	LimitedAddress getAddress();
	Serializable getCoords();
	String getBlockNumber();
	String getLotNumber();
}
