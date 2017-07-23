package baltimoredata.view;

public class AddressViews {
	public interface Minimal {}
	public interface Limited extends Minimal, NeighborhoodViews.Limited {}

}
