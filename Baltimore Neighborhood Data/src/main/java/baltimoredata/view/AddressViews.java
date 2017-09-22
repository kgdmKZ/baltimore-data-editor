package baltimoredata.view;

public class AddressViews {
	public interface POIMinimal {}
	public interface Minimal extends POIMinimal, NeighborhoodViews.Minimal {}
	public interface Limited extends Minimal, NeighborhoodViews.Limited {}

}
