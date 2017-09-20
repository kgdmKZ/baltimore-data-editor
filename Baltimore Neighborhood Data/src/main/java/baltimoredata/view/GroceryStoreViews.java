package baltimoredata.view;

public class GroceryStoreViews {
	public interface Minimal extends AddressViews.Minimal {}
	public interface Limited extends Minimal, AddressViews.Limited {}

}
