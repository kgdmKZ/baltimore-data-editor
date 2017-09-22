package baltimoredata.view;

public class POIViews {
	public interface Minimal extends AddressViews.POIMinimal {}
	public interface Limited extends Minimal, AddressViews.Limited {}

}
