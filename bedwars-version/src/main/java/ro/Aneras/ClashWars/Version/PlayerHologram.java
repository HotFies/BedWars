package ro.Aneras.ClashWars.Version;

public interface PlayerHologram {

	void show(String name);

	void remove();

	boolean isVisible();

	void isVisible(boolean value);
	
}
