package m2if;

public interface Observable {

	public void addObserver(Observer obs);
	public void clear();
	public void update();
}
