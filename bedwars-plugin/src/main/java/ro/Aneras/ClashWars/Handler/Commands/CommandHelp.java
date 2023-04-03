package ro.Aneras.ClashWars.Handler.Commands;

import java.util.List;

import org.bukkit.entity.Player;

import ro.Aneras.ClashWars.Messages;

public class CommandHelp implements CommandInterface {

	private String name;
	private List<CommandInterface> commands;

	public CommandHelp(String name, List<CommandInterface> commands) {
		this.name = name;
		this.commands = commands;
	}

	@Override
	public String getCommand() {
		return "help";
	}
	
	@Override
	public String[] getArguments() {
		return new String[0];
	}
	
	@Override
	public boolean hasPermission(Player p) {
		return true;
	}
	
	@Override
	public void executeCommand(Player p, String[] args) {
		p.sendMessage(Messages.PREFIX + " §7- §dHelp");
		for (CommandInterface c : commands) {
			if (c != this && c.hasPermission(p)) {
				String arguments = "";
				for (String arg : c.getArguments()) {
					arguments = arguments + " " + (arg.replace("<", "§8<§a").replace(">", "§8>"));
				}
				p.sendMessage("- §7/§c"+name+" " + c.getCommand() + arguments);
			}
		}
	}
}
