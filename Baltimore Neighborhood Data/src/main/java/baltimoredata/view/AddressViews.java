package baltimoredata.view;

public class AddressViews {
	public interface Minimal extends NeighborhoodViews.Minimal {}
	public interface Limited extends Minimal, NeighborhoodViews.Limited {}

}
