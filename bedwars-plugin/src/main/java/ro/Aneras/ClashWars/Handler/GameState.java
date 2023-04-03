package ro.Aneras.ClashWars.Handler;

import ro.Aneras.ClashWars.Messages;

public enum GameState {
	
	WAITING(Messages.STATE_WAITING),
	IN_GAME(Messages.STATE_IN_GAME),
	RESETING(Messages.STATE_RESETING),
	END(Messages.STATE_IN_GAME);

	private Messages state;

	private GameState(Messages state) {
		this.state = state;
	}

	public String getState() {
		return state.toString();
	}

}
