package ro.Aneras.ClashWars.Games;

public class GameSettings {

	private int min;
	private int max;
	private boolean joinable = true;
	private boolean canDamage = false;
	private boolean canInteract = false;
	private boolean canClickInv = false;
	private boolean canHunger = false;
	private boolean canDrop = false;
	private boolean canHeal = false;
	
	public GameSettings(int min, int max) {
		this.min = min;
		this.max = max;
	}
	
	public int getMin() {
		return min;
	}
	
	public boolean canJoin() {
		return joinable;
	}
	
	public void canJoin(boolean val) {
		this.joinable = val;
	}
	
	public void setMin(int min) {
		this.min = min;
	}
	
	public int getMax() {
		return max;
	}
	
	public void setMax(int max) {
		this.max = max;
	}
	
	public boolean canDrop() {
		return canDrop;
	}
	
	public void canDrop(boolean drop) {
		this.canDrop = drop;
	}
	
	public boolean canDamage() {
		return canDamage;
	}
	
	public void canDamage(boolean damage) {
		this.canDamage = damage;
	}
	
	public boolean canInteract() {
		return canInteract;
	}
	
	public void canInteract(boolean canInteract) {
		this.canInteract = canInteract;
	}
	
	public boolean canClickInv() {
		return canClickInv;
	}
	
	public void canClickInv(boolean canClickInv) {
		this.canClickInv = canClickInv;
	}
	
	public boolean canHunger() {
		return canHunger;
	}
	
	public void canHunger(boolean canHunger) {
		this.canHunger = canHunger;
	}
	
	public boolean canHeal() {
		return canHeal;
	}
	
	public void canHeal(boolean canHeal) {
		this.canHeal = canHeal;
	}
	
}
