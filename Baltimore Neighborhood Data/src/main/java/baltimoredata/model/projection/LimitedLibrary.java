package baltimoredata.model.projection;

public interface LimitedLibrary {
	Integer getId();
	LimitedAddress getAddress();
	LimitedZipCode getZipCode();
	String getName();
}
