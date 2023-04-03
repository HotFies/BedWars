package ro.Aneras.ClashWars;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import ro.Aneras.ClashWars.Games.Game;
import ro.Aneras.ClashWars.Handler.GameState;
import ro.Aneras.ClashWars.Handler.Statusbar.Statusbar;

public class UpdateTask extends BukkitRunnable {

	private Main main;
	private long ticks = 0;

	public UpdateTask(Main main) {
		this.main = main;
		runTaskTimer(main, 1, 1);
	}

	@Override
	public void run() {
		for (Game g : main.getManager().getGames()) {
			try {
				if (ticks % 20 == 0) {
					if (g.isGameStarted()) {
						for (Statusbar board : g.getBoards()) {
							g.updateSidebar(board.getSidebar());
						}
						g.run();
					} else if (g.getState() == GameState.WAITING) {
						for (Player p : g.getPlayers()) {
							main.getVersion().sendActionBar(p, main.getPlaceholder().replace(p, g, Messages.BAR_PLAYERS.toString()));
						}
					}
					if (g.getState() == GameState.WAITING && g.getVoter().isRefresh()) {
						g.getVoter().refresh();
						g.getVoter().setRefresh(false);
					}
				}
				g.tick(ticks);
			} catch (Exception e) {
				e.printStackTrace();
				g.broadcast("§cAn error has happened and the game has been forced to shutdown to prevent any future issues!");
				main.getManager().stopGame(g);
			}
		}
		main.getManager().onTick(ticks);
		if (main.getPapiHook() != null) {
			main.getPapiHook().onTick(ticks);
		}
		ticks++;
	}

}