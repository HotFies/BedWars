package ro.Aneras.ClashWars;

import java.util.ArrayList;
import java.util.List;

public enum Messages {
	
	PREFIX("§8[§3ClashWars§8]"),
	GAME_NO_GAME("§cThe game is not available!"),
	GAME_FULL("§cGame is full!"),
	GAME_JOIN_ANOTHER_GAME("§cYou can't play in more than 1 game!"),
	GAME_START("§7Game starts in §c%timer%§7 seconds."),
	GAME_JOIN("§a%name% §7has joined the game. §8(§d%size%§8/§d%maxsize%§8)"),
	GAME_LEAVE("§a%name% §7has left the game. §8(§d%size%§8/§d%maxsize%§8)"),
	GAME_YOU_LEFT("§cYou left the game"),
	GAME_COMMAND_NO_PERM("§cYou're not allowed to use this command!"),
	GAME_RESTRICTED_COMMAND("§cYou can't use commands while in game!"),
	GAME_NOGAME_LEAVE("§cYou have to be in game to use this command!"),
	GAME_NO_PLAYERS("§c§lNot enough players for game to continue!"),
	LEAVE_ITEM_NAME("§c§lLeave game §8(§eRight click§8)"),
	PLAY_AGAIN_NAME("§b§lPlay Again §8(§eRight click§8)"),
	GAME_TEAM_RED("Red"),
	GAME_TEAM_BLUE("Blue"),
	GAME_TEAM_GREEN("Green"),
	GAME_TEAM_YELLOW("Yellow"),
	GAME_TEAM_AQUA("Aqua"),
	GAME_TEAM_WHITE("White"),
	GAME_TEAM_PINK("Pink"),
	GAME_TEAM_GRAY("Gray"),
	GAME_TOP_1ST(" §e1st killer§7(§e✬✬✬§7) - %player%"),
	GAME_TOP_2ND(" §62nd killer§7(§e✬✬  §7) - %player%"),
	GAME_TOP_3RD(" §c3rd killer§7(§e✬   §7) - %player%"),
	
	SELECTOR_NAME("Team Selector"),
	SELECTOR_ITEM_NAME("§c§lChoose Team §8(§eRight click§8)"),
	SELECTOR_CLEAN_NAME("§r§lClean Selected."),
	SELECTOR_TEAM_LORE("§7Click to join team %team%§7!"),
	SELECTOR_CHOOSE_TEAM("§aYou chose to be placed in the team %team%"),
	SELECTOR_CLEAN_CHOOSE("§7Click to clean the team selection."),
	SELECTOR_CLEANED_CHOSEN("§aYou chosen to clean the team selection."),
	SELECTOR_RANDOM_REMOVED("§c§lYou were randomly kicked from the voted team due to requirement changes!"),
	SELECTOR_TEAM_REMOVED("§c§lThe team you voted for is no longer used due to the amount of players left!"),
	SELECTOR_ALREADY_IN_TEAM("§cYou are already in this team!"),
	SELECTOR_IS_FULL("§cThe team selector is full"),
	SELECTOR_VOTERS("§7Members: "),
	
	PARTY_ALREADY("§c You are already in a party."),
	PARTY_OWNER_ONLY("§c Only the leader can do this. Use /party leave to leave."),
	PARTY_NONE("§c You're not in a party."),
	PARTY_ALREADY_INVITE("§c %player% is already in a party."),
	PARTY_CREATED("§a The party has been created!"),
	PARTY_LEFT("§c You left the party."),
	PARTY_DELETED("§c The party has been removed."),
	PARTY_OWNER("§c You're not the owner of the party."),
	PARTY_MAX("§c The party limit has been reached."),
	PARTY_ONLINE("§c This player is not online."),
	PARTY_INVITED("§a %player% has invited you to his party. Use /party accept to accept."),
	PARTY_SENT("§a You've sent an invitation to %player%."),
	PARTY_ALREADY_INVITED("§c %player% is already invited."),
	PARTY_DENY("§a You've denied the invitation."),
	PARTY_NO_INVITATION("§c You don't have a pending invitation."),
	PARTY_ACCEPT("§a You've accepted the invitation."),
	PARTY_ACCEPTED("§a %player% has accepted your invitation."),
	PARTY_DENIED("§a %player% has denied your invitation."),
	PARTY_LIST("§eParty List: ", "§cOwner: §a%owner%", "§3Members: §a%members%"),
	
	CHAT_WAITING("§7%1$s§f: %2$s"),
	CHAT_SPECTATOR("§7[SPECTATOR] %player%§f: %2$s"),
	CHAT_INGAME("§8[%team%§8] §7%1$s§f: %2$s"),
	CHAT_INGAME_SHOUT("§8[§cSHOUT§8] §8[%team%§8] §7%player%§f: %2$s"),
	
	STATE_WAITING("WAITING"),
	STATE_IN_GAME("IN GAME"),
	STATE_RESETING("RESETING"),
	SPECTATOR_GUI("Teleporter"),
	
	SIGN_FIRST("%type%"),
	SIGN_SECOND("%name%"),
	SIGN_THIRD("§5• §f§l%state% §5•"),
	SIGN_FOURTH("§c»§8§l%size%/%max%§c«"),
	
	BUNGEE_MAPVOTE_NAME("Vote a Map"),
	BUNGEE_MAPVOTE("§aVote a Map §8(§eRight Click§8)"),
	BAR_PLAYERS("§cThere should be at least §b%min%§c for game to begin!"),
	
	SCOREBOARD_TITLE("§6§lBed Wars"),
	SCOREBOARD_LOBBY_NAME("§7Map: §a"),
	SCOREBOARD_LOBBY_PLAYERS("§7Players: §a"),
	SCOREBOARD_LOBBY_MODE("§7Mode:"),
	SCOREBOARD_LOBBY_WAITING("§7Waiting..."),
	SCOREBOARD_LOBBY_GAME_START("§7Game starts in: §c"),
	SCOREBOARD_LOBBY_SERVER("§ewww.spigotmc.org"),
	
	BEDWARS_SHOP("§e§lPLAYER SHOP"),
	BEDWARS_SB_YOU(" §7(You)"),
	BEDWARS_START_TITLE("§aGO!"),
	BEDWARS_SOLO_UPGRADE("§e§lSOLO UPGRADE"),
	BEDWARS_TEAM_UPGRADE("§e§lTEAM UPGRADE"),
	BEDWARS_PLACE_DENY("§cYou can't place blocks here!"),
	BEDWARS_BREAK("§cYou can only break blocks placed by players"),
	BEDWARS_TEAM_CHEST("§cYou cannot open this Chest as the %team% Team§c has not been eliminated!"),
	BEDWARS_DIED("%player% §7killed himself."),
	BEDWARS_KILLED_BY_VOID("%player% §7fell into the void."),
	BEDWARS_KILLED_BY_EXPLODE("%player% §7blew off."),
	BEDWARS_KILLED_BY_EXPLODE_BY_PLAYER("%player% §7blew off because of %killer%"),
	BEDWARS_KILLED_BY_PUSHED("%player% §7has been knocked into the void by %killer%"),
	BEDWARS_KILLED("%player% §7was killed by %killer%§7."),
	BEDWARS_NOT_ENOUGH("§cYou don't have enough resources!"),
	BEDWARS_NOT_ENOUGH_MSG(" Need %missing% more!"),
	BEDWARS_ALREADY_OWN("§cYou already own this item!"),
	BEDWARS_PURCHASE("§eClick to purchase!"),
	BEDWARS_UNLOCKED("§aUnlocked!"),
	BEDWARS_TITLE_DIED("§cYou Died"),
	BEDWARS_VICTORY("§6VICTORY!"),
	BEDWARS_GAMEOVER("§cGAME OVER"),
	BEDWARS_TITLE_RESPAWNED("§aRESPAWNED!"),
	BEDWARS_MSG_RESPAWNED("§eYou have respawned!"),
	BEDWARS_RESPAWN_IN("§eYou will spawn in §c%timer%§e seconds!"),
	BEDWARS_SPECTATOR("§eYou died. You can leave by using the §a/leave§e command!"),
	BEDWARS_ELIMINATED("§cYou have been eliminated!"),
	BEDWARS_TEAM_ELIMINATED("§f§lTEAM ELIMINATED > %team% Team §7has been eliminated!"),
	BEDWARS_TEAM_WIN("%team% Team §ahas won the game!"),
	BEDWARS_BED_DESTROYED_TITLE("§cBED DESTROYED"),
	BEDWARS_ALL_BED_DESTROYED_TITLE("§cBED DESTROYED!"),
	BEDWARS_ALL_BED_DESTROYED_SUBTITLE("All beds have been destroyed!"),
	BEDWARS_YOUR_BED_BREAK("§cYou can't break your own bed!"),
	BEDWARS_BED_DESTROYED_SUBTITLE("§fYou will no longer respawn!"),
	BEDWARS_DESTROYED_BED_YOURS("§f§lBED DESTRUCTION > §7Your bed got destroyed by %player%"),
	BEDWARS_BED_DESTROYED("§f§lBED DESTRUCTION > %team% Bed §7was destroyed by §c%player%§7!"),
	BEDWARS_EMERALD_UPGRADE("§aEmerald Generators §ehave been upgraded to Tier §c%tier%"),
	BEDWARS_DIAMOND_UPGRADE("§bDiamond Generators §ehave been upgraded to Tier §c%tier%"),
	BEDWARS_DEFEND_BED_HOLO("§c§lDefend your Bed!"),
	BEDWARS_DESTROYED_BED_HOLO("§7Your bed got destroyed by %player%"),
	BEDWARS_UPGRADE_PURCHASE("§a%player% has purchased §6%upgrade%"),
	BEDWARS_ITEM_PURCHASE("§aYou purchased §6%item%"),
	BEDWARS_TITLE_TRAP_TRIGGERED("§cTRAP TRIGGERED!"),
	BEDWARS_SUBTITLE_MINER_TRIGGER("Someone triggered your mining fatigue trap!"),
	BEDWARS_SUBTITLE_TRAP_TRIGGER("Someone triggered your blindness trap!"),
	BEDWARS_REWARD_IRON("§f+%amount% Irons"),
	BEDWARS_REWARD_GOLD("§6+%amount% Golds"),
	BEDWARS_REWARD_DIAMOND("§b+%amount% Diamonds"),
	BEDWARS_REWARD_EMERALD("§2+%amount% Emeralds"),
	BEDWARS_EVENT_SUDDEN_DEATH("Sudden death:"),
	BEDWARS_EVENT_BED_DESTRUCTION("Bed Destruction:"),
	BEDWARS_EVENT_EMERALD_MAXED("Emerald Maxed:"),
	BEDWARS_EVENT_DIAMOND_MAXED("Diamond Maxed:"),
	BEDWARS_EVENT_DIAMOND_UPGRADE("Diamond Upgrade:"),
	BEDWARS_EVENT_EMERALD_UPGRADE("Emerald Upgrade:"),
	BEDWARS_SB_KILLS("Kills: §a"),
	BEDWARS_SB_FINALKILLS("Final Kills: §a"),
	BEDWARS_SB_DATE_COLOR("§b"),
	BEDWARS_FINAL(" §b§lFINAL KILL!"),
	
	BEDWARS_UPGRADE_ITEM("Upgrades"),
	
	BEDWARS_UPGRADE_1("Heal Pool"),
	BEDWARS_UPGRADE_2("Miner Fatigue Trap"),
	BEDWARS_UPGRADE_3("It's a trap!"),
	BEDWARS_UPGRADE_4("Sharpened Swords"),
	BEDWARS_UPGRADE_5("Maniac Miner"),
	BEDWARS_UPGRADE_6("Reinforced Armor"),
	BEDWARS_UPGRADE_7_T1("Iron Forge"),
	BEDWARS_UPGRADE_7_T2("Gold Forge"),
	BEDWARS_UPGRADE_7_T3("Emerald Forge"),
	BEDWARS_UPGRADE_1_LORE("§7Create a Regeneration field", "§7around your base!", "", "§7Cost: §b%money% Diamond"),
	BEDWARS_UPGRADE_2_LORE("§7The next enemy to enter your", "§7base will receive Mining Fatigue", "§7for 10 seconds!", "", "§7Cost: §b%money% Diamond"),
	BEDWARS_UPGRADE_3_LORE("§7The next enemy to enter your", "§7base will receive Blindness and", "§7slowness!", "", "§7Cost: §b%money% Diamond"),
	BEDWARS_UPGRADE_4_LORE("§7Your team gets Sharpness I on", "§7all swords!", "", "§7Cost: §b%money% Diamonds"),
	BEDWARS_UPGRADE_5_LORE("§7All players on your team", "§7permanently gain Haste %tier%.", "", "§7Cost: §b%money% Diamonds"),
	BEDWARS_UPGRADE_6_LORE("§7Your team gets Protection %tier% on", "§7all armor pieces!", "", "§7Cost: §b%money% Diamonds"),
	BEDWARS_UPGRADE_7_LORE_T1("§7Increases the spawn rate of Iron", "§7and Gold by 50%", "", "§7Cost: §b%money% Diamonds"),
	BEDWARS_UPGRADE_7_LORE_T2("§7Increases the spawn rate of Gold", "§7by 100%", "", "§7Cost: §b%money% Diamonds"),
	BEDWARS_UPGRADE_7_LORE_T3("§7Activates the Emerald spawner in", "§7your team's Forge.", "", "§7Cost: §b%money% Diamonds"),
	
	BEDWARS_SHOP_ITEM("Item Shop"),
	BEDWARS_SHOP_ARMOR("Armor"),
	BEDWARS_SHOP_MELEE("Melee"),
	BEDWARS_SHOP_BLOCKS("Blocks"),
	BEDWARS_SHOP_RANGED("Ranged"),
	BEDWARS_SHOP_POTIONS("Potions"),
	BEDWARS_SHOP_TOOLS("Tools"),
	BEDWARS_SHOP_UTILITY("Utility"),
	BEDWARS_SHOP_BACK("§aGo Back"),
	BEDWARS_SHOP_CATEGORIES("§8↑ §7Categories"),
    BEDWARS_SHOP_ITEMS("§8↓ §7Items"),
	
	BEDWARS_SHOP_POTIONS_1("Speed II Potion (45 Seconds)"),
	BEDWARS_SHOP_POTIONS_2("Jump V Potion (45 Seconds)"),
	BEDWARS_SHOP_POTIONS_3("Invisibility Potion (30 seconds)"),
	BEDWARS_SHOP_POTIONS_1_LORE("§8Items", "§8▪ §7Speed II Potion (45 Seconds)", "", "§7Cost: §21 Emerald"),
	BEDWARS_SHOP_POTIONS_2_LORE("§8Items", "§8▪ §7Jump II Potion (45 Seconds)", "", "§7Cost: §21 Emerald"),
	BEDWARS_SHOP_POTIONS_3_LORE("§8Items", "§8▪ §7Invisibility Potion (30 seconds)", "", "§7Cost: §22 Emerald"),
	
	BEDWARS_SHOP_ARMOR_1("Permanent Chainmail Armor"),
	BEDWARS_SHOP_ARMOR_2("Permanent Iron Armor"),
	BEDWARS_SHOP_ARMOR_3("Permanent Diamond Armor"),
	BEDWARS_SHOP_ARMOR_1_LORE("§8Items", "§8▪ §7Permanent Chainmail Armor", "§8▪ §7Chainmail Leggings", "", "§7Cost: §f40 Iron", "", "§8§oYou will not lose this on death!"),
	BEDWARS_SHOP_ARMOR_2_LORE("§8Items", "§8▪ §7Permanent Iron Armor", "§8▪ §7Iron Leggings", "", "§7Cost: §612 Gold", "", "§8§oYou will not lose this on death!"),
	BEDWARS_SHOP_ARMOR_3_LORE("§8Items", "§8▪ §7Permanent Diamond Armor", "§8▪ §7Diamond Leggings", "", "§7Cost: §26 Emerald", "", "§8§oYou will not lose this on death!"),
	
	BEDWARS_SHOP_MELEE_1("Stone Sword"),
	BEDWARS_SHOP_MELEE_2("Iron Sword"),
	BEDWARS_SHOP_MELEE_3("Diamond Sword"),
	BEDWARS_SHOP_MELEE_4("Stick (Knockback I)"),
	BEDWARS_SHOP_MELEE_1_LORE("§8Items", "§8▪ §7Stone Sword", "", "§7Cost: §f10 Iron"),
	BEDWARS_SHOP_MELEE_2_LORE("§8Items", "§8▪ §7Iron Sword", "", "§7Cost: §67 Gold"),
	BEDWARS_SHOP_MELEE_3_LORE("§8Items", "§8▪ §7Diamond Sword", "", "§7Cost: §24 Emerald"),
	BEDWARS_SHOP_MELEE_4_LORE("§8Items", "§8▪ §7Stick (Knockback I)", "", "§7Cost: §65 Gold"),
	
	BEDWARS_SHOP_RANGED_1("Arrow"),
	BEDWARS_SHOP_RANGED_2("Bow"),
	BEDWARS_SHOP_RANGED_3("Bow (Power I)"),
	BEDWARS_SHOP_RANGED_4("Bow (Power I, Punch I)"),
	BEDWARS_SHOP_RANGED_1_LORE("§8Items", "§8▪ §7Arrow", "", "§7Cost: §62 Gold"),
	BEDWARS_SHOP_RANGED_2_LORE("§8Items", "§8▪ §7Bow", "", "§7Cost: §612 Gold"),
	BEDWARS_SHOP_RANGED_3_LORE("§8Items", "§8▪ §7Bow (Power I)", "", "§7Cost: §624 Gold"),
	BEDWARS_SHOP_RANGED_4_LORE("§8Items", "§8▪ §7Bow (Power I, Punch I)", "", "§7Cost: §26 Emerald"),
	
	BEDWARS_SHOP_UTILITY_1("Golden Apple"),
	BEDWARS_SHOP_UTILITY_4("Fireball"),
	BEDWARS_SHOP_UTILITY_5("TNT"),
	BEDWARS_SHOP_UTILITY_6("Ender Pearl"),
	BEDWARS_SHOP_UTILITY_7("Water Bucket"),
	BEDWARS_SHOP_UTILITY_8("Bridge Egg"),
	BEDWARS_SHOP_UTILITY_9("Compact Pop-up Tower"),
	BEDWARS_SHOP_UTILITY_10("Magic Milk"),
	BEDWARS_SHOP_UTILITY_1_LORE("§8Items", "§8▪ §7Golden Apple", "", "§7Cost: §63 Gold"),
	BEDWARS_SHOP_UTILITY_4_LORE("§8Items", "§8▪ §7Fireball", "", "§7Cost: §f40 Iron", "", "§7Right-Click to launch"),
	BEDWARS_SHOP_UTILITY_5_LORE("§8Items", "§8▪ §7TNT", "", "§7Cost: §6%money% Gold", "", "§7Instantly ignites, appropriate", "§7to explode things!"),
	BEDWARS_SHOP_UTILITY_6_LORE("§8Items", "§8▪ §7Ender Pearl", "", "§7Cost: §24 Emerald", "", "§7§oPretty useful for invading", "§7§oenemy bases."),
	BEDWARS_SHOP_UTILITY_7_LORE("§8Items", "§8▪ §7Water Bucket", "", "§7Cost: §21 Emerald"),
	BEDWARS_SHOP_UTILITY_8_LORE("§8Items", "§8▪ §7Bridge Egg", "", "§7Cost: §2%money% Emerald", "", "§7This egg creates a bridge in", "§7its trail after being thrown."),
	BEDWARS_SHOP_UTILITY_9_LORE("§8Items", "§8▪ §7Compact Pop-up Tower", "", "§7Cost: §f24 Iron", "", "§7Place a pop-up defence!"),
	BEDWARS_SHOP_UTILITY_10_LORE("§8Items", "§8▪ §7Magic Milk", "", "§7Cost: §64 Gold", "", "§7Avoid triggering traps for 30", "§7seconds after consuming."),
	
	BEDWARS_SHOP_TOOLS_1("Permanent Shears"),
	BEDWARS_SHOP_TOOLS_1_LORE("§8Items", "§8▪ Permanent Shears", "", "§7Cost: §f20 Iron", "", "§8§oYou will not lose this on death!"),
	BEDWARS_SHOP_AXE_TIER1("Wooden Axe [Tier I] (Efficiency I)"),
	BEDWARS_SHOP_AXE_TIER2("Stone Axe [Tier II] (Efficiency I)"),
	BEDWARS_SHOP_AXE_TIER3("Iron Axe [Tier III] (Efficiency II)"),
	BEDWARS_SHOP_AXE_TIER4("Diamond Axe [Tier MAX] (Efficiency III)"),
	BEDWARS_SHOP_AXE_TIER1_LORE("§8Items", "§8▪ §7Wooden Axe [Tier I] (Efficiency I)", "", "§7Cost: §f10 Iron", "", "§8§oThis is an upgradable item.", "§8§oIf above tier I, the item will", "§8§odrop 1 tier upon death!"),
	BEDWARS_SHOP_AXE_TIER2_LORE("§8Items", "§8▪ §7Stone Axe [Tier II] (Efficiency I)", "", "§7Cost: §f10 Iron", "", "§8§oThis is an upgradable item.", "§8§oIf above tier I, the item will", "§8§odrop 1 tier upon death!"),
	BEDWARS_SHOP_AXE_TIER3_LORE("§8Items", "§8▪ §7Iron Axe [Tier III] (Efficiency II)", "", "§7Cost: §63 Gold", "", "§8§oThis is an upgradable item.", "§8§oIf above tier I, the item will", "§8§odrop 1 tier upon death!"),
	BEDWARS_SHOP_AXE_TIER4_LORE("§8Items", "§8▪ §7Diamond Axe [Tier MAX] (Efficiency III)", "", "§7Cost: §66 Gold", "", "§8§oThis is an upgradable item.", "§8§oIf above tier I, the item will", "§8§odrop 1 tier upon death!"),
	BEDWARS_SHOP_PICKAXE_TIER1("Wooden Pickaxe [Tier I] (Efficiency I)"),
	BEDWARS_SHOP_PICKAXE_TIER2("Stone Pickaxe [Tier II] (Efficiency I)"),
	BEDWARS_SHOP_PICKAXE_TIER3("Iron Pickaxe [Tier III] (Efficiency II)"),
	BEDWARS_SHOP_PICKAXE_TIER4("Diamond Pickaxe [Tier MAX] (Efficiency III)"),
	BEDWARS_SHOP_PICKAXE_TIER1_LORE("§8Items", "§8▪ §7Wooden Pickaxe [Tier I] (Efficiency I)", "", "§7Cost: §f10 Iron", "", "§8§oThis is an upgradable item.", "§8§oIf above tier I, the item will", "§8§odrop 1 tier upon death!"),
	BEDWARS_SHOP_PICKAXE_TIER2_LORE("§8Items", "§8▪ §7Stone Pickaxe [Tier II] (Efficiency I)", "", "§7Cost: §f10 Iron", "", "§8§oThis is an upgradable item.", "§8§oIf above tier I, the item will", "§8§odrop 1 tier upon death!"),
	BEDWARS_SHOP_PICKAXE_TIER3_LORE("§8Items", "§8▪ §7Iron Pickaxe [Tier III] (Efficiency II)", "", "§7Cost: §63 Gold", "", "§8§oThis is an upgradable item.", "§8§oIf above tier I, the item will", "§8§odrop 1 tier upon death!"),
	BEDWARS_SHOP_PICKAXE_TIER4_LORE("§8Items", "§8▪ §7Diamond Pickaxe [Tier MAX] (Efficiency III)", "", "§7Cost: §66 Gold", "", "§8§oThis is an upgradable item.", "§8§oIf above tier I, the item will", "§8§odrop 1 tier upon death!"),
	
	BEDWARS_SHOP_BLOCKS_1("Wool"),
	BEDWARS_SHOP_BLOCKS_2("Hardened Clay"),
	BEDWARS_SHOP_BLOCKS_3("Blast-Proof Glass"),
	BEDWARS_SHOP_BLOCKS_4("End Stone"),
	BEDWARS_SHOP_BLOCKS_5("Ladder"),
	BEDWARS_SHOP_BLOCKS_6("Oak Wood Planks"),
	BEDWARS_SHOP_BLOCKS_7("Obsidian"),
	BEDWARS_SHOP_BLOCKS_7_LORE("§8Items", "§8▪ §7Obsidian x4", "", "§7Cost: §24 Emerald"),
	BEDWARS_SHOP_BLOCKS_6_LORE("§8Items", "§8▪ §7Oak Wood Planks x16", "", "§7Cost: §64 Gold"),
	BEDWARS_SHOP_BLOCKS_5_LORE("§8Items", "§8▪ §7Ladder x16", "", "§7Cost: §f4 Iron"),
	BEDWARS_SHOP_BLOCKS_4_LORE("§8Items", "§8▪ §7End Stone x12", "", "§7Cost: §f24 Iron"),
	BEDWARS_SHOP_BLOCKS_3_LORE("§8Items", "§8▪ §7Blast-Proof Glass x4", "", "§7Cost: §f12 Iron"),
	BEDWARS_SHOP_BLOCKS_2_LORE("§8Items", "§8▪ §7Stained Clay x16", "", "§7Cost: §f12 Iron"),
	BEDWARS_SHOP_BLOCKS_1_LORE("§8Items", "§8▪ §7Wool x16", "", "§7Cost: §f4 Iron"),
	
	BEDWARS_DIAMOND("§eTier §c%tier%", "§b§lDIAMOND", "§eSpawns in §c%time%§e seconds"),
	BEDWARS_EMERALD("§eTier §c%tier%", "§a§lEMERALD", "§eSpawns in §c%time%§e seconds"),
	BEDWARS_SHOP_ARMOR_LORE("§7Available", "§8▪ Chainmail Boots", "§8▪ Chainmail Leggings", "§8▪ Iron Boots", "§8▪ Iron Leggings", "§8▪ Diamond Boots", "§8▪ Diamond Leggings", "", "§eClick to browse!"),
	BEDWARS_SHOP_BLOCKS_LORE("§7Available", "§8▪ Wool", "§8▪ Stained Clay", "§8▪ Blast-Proof Glass", "§8▪ End Stone", "§8▪ Ladder", "§8▪ Oak Wood Planks", "§8▪ Obsidian", "", "§eClick to browse!"),
	BEDWARS_SHOP_MELEE_LORE("§7Available", "§8▪ Stone Sword", "§8▪ Iron Sword", "§8▪ Diamond Sword", "§8▪ Stick", "", "§eClick to browse!"),
	BEDWARS_SHOP_TOOLS_LORE("§7Available", "§8▪ Shears", "§8▪ Pickaxe", "§8▪ Axe", "", "§eClick to browse!"),
	BEDWARS_SHOP_RANGED_LORE("§7Available", "§8▪ Arrow", "§8▪ Bow", "§8▪ Bow (Power I)", "§8▪ Bow (Power I, Punch I)", "", "§eClick to browse!"),
	BEDWARS_SHOP_POTIONS_LORE("§7Available", "§8▪ Speed II Potion (45 Seconds)", "§8▪ Jump V Potion (45 Seconds)", "§8▪ Invisibility Potion (30 seconds)", "", "§eClick to browse!"),
	BEDWARS_SHOP_UTILITY_LORE("§7Available", "§8▪ Golden Apple", "§8▪ Bedbug", "§8▪ Dream Defender", "§8▪ Fireball", "§8▪ TNT", "§8▪ Ender Pearl", "§8▪ Water Bucket", "§8▪ Bridge Egg", "", "§eClick to browse!"),
	
	BEDWARS_START("§f§lBedWars",""," §e§lProtect your bed and destroy the enemy beds."," §e§lUpgrade yourself and your team by collecting"," §e§lIron, Gold, Emerald, Diamond from generators", " §e§lto access powerful upgrades");
	

	private String msg;
	private List<String> listMSG;
	
	private Messages(String msg) {
		this.msg = msg;
	}
	
	private Messages(String... msg) {
		this.listMSG = new ArrayList<String>();
		for (String l : msg) {
			listMSG.add(l);
		}
	}
	
	public List<String> getList() {
		return listMSG;
	}
	
	public void setMessage(String msg) {
		this.msg = msg.replace('&', '§');
	}
	
	@Override
	public String toString() {
		return msg;
	}
	
	public static Messages getEnum(String name) {
		for (Messages msg : values()) {
			if (msg.name().equals(name)) {
				return msg;
			}
		}
		return null;
	}
}