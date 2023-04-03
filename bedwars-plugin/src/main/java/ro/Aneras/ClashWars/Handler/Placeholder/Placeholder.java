package ro.Aneras.ClashWars.Handler.Placeholder;

import org.bukkit.entity.Player;

import ro.Aneras.ClashWars.Games.Game;

public class Placeholder {
	
	public String replace(Player p, Game g, String text) {
		if (p != null) {
			text = text.replace("%name%", p.getName());
		}
		text = text.replace("%min%", String.valueOf(g.getSettings().getMin()));
		text = text.replace("%size%", String.valueOf(g.getPlayers().size()));
		text = text.replace("%maxsize%", String.valueOf(g.getSettings().getMax()));
		text = text.replace("%id%", String.valueOf(g.getID()));
		text = text.replace("%timer%", String.valueOf(g.getTimer()));
		return text;
	}
	
	public String replace(Game g, String text) {
		text = text.replace("%name%", g.getName());
		text = text.replace("%type%", g.getType().getColor()+"§l"+g.getType().getName());
		text = text.replace("%min%", String.valueOf(g.getSettings().getMin()));
		text = text.replace("%size%", String.valueOf(g.getPlayers().size()));
		text = text.replace("%max%", String.valueOf(g.getSettings().getMax()));
		text = text.replace("%id%", String.valueOf(g.getID()));
		text = text.replace("%state%", String.valueOf(g.getState().getState()));
		text = text.replace("%timer%", String.valueOf(g.getTimer()));
		return text;
	}
	
	public String centerMessage(String message) {
		int messagePxSize = 0;
		boolean previousCode = false;
		boolean isBold = false;

		for (char c : message.toCharArray()) {
			if (c == '§') {
				previousCode = true;
				continue;
			} else if (previousCode) {
				previousCode = false;
				if (c == 'l' || c == 'L') {
					isBold = true;
					continue;
				} else {
					isBold = false;
				}
			} else {
				FontInfo dFI = FontInfo.getDefaultFontInfo(c);
				messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
				messagePxSize++;
			}
		}
		
		int toCompensate = 154 - (messagePxSize / 2);
		int spaceLength = FontInfo.SPACE.getLength() + 1;
		int compensated = 0;
		StringBuilder sb = new StringBuilder();
		while (compensated < toCompensate) {
			sb.append(" ");
			compensated += spaceLength;
		}
		return sb.toString() + message;
	}
	
}
