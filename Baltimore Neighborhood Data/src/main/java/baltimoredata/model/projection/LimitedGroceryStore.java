package baltimoredata.model.projection;

public interface LimitedGroceryStore {
	Integer getId();
	LimitedAddress getAddress();
	LimitedGroceryType getGroceryType();
    LimitedZipCode getZipCode();
	String getName();
}
