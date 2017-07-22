package baltimoredata.view;

public class  NeighborhoodViews {
    public interface Minimal {}
    public interface Limited extends Minimal, AreaViews.Minimal {}
}
