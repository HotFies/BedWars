package ro.Aneras.ClashWars.Handler.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ro.Aneras.ClashWars.Messages;

import java.util.List;

public class CommandExe implements CommandExecutor {

	private String name;
	private List<CommandInterface> commands;
	
	public CommandExe(String name, String cmd, List<CommandInterface> commands) {
		//super(cmd, "", "", Arrays.asList(alias));
		this.name = name;
		this.commands = commands;
		commands.add(new CommandHelp(cmd, commands));
	}

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		if (args.length == 0) {
			sender.sendMessage("§8[§3"+name+"§8]" + " §7Plugin made by §aFr33styler§7.");
			sender.sendMessage("§8[§3"+name+"§8]" + " §7Type §c/"+label+" help§7 for help.");
		} else {
			if (!(sender instanceof Player)) {
				sender.sendMessage(Messages.PREFIX + " §cYou can't use commands from console!");
				return false;
			}
			Player p = (Player) sender;
			for (CommandInterface command : commands) {
				if (args[0].equalsIgnoreCase(command.getCommand()) && command.hasPermission(p)) {
					if (args.length == command.getArguments().length+1) {
						command.executeCommand(p, args);
					} else {
						String arguments = "";
						for (String argument : command.getArguments()) {
							arguments = arguments + " " + argument;
						}
						p.sendMessage(Messages.PREFIX + " §7Invalid arguments! Use §a/"+label+" " + command.getCommand() + arguments);
					}
					return true;
				}
			}
			sender.sendMessage(Messages.PREFIX + " §7Unknown command! Type §c/"+label+" help§7 for help.");
		}
		return false;
	}
}
