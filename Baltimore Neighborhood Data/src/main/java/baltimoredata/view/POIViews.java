package baltimoredata.view;

public class POIViews {
	public interface Minimal extends AddressViews.Minimal {}
	public interface Limited extends Minimal, AddressViews.Limited {}

}
