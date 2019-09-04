package FzmXer.VexFxShop.Main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import FzmXer.VexFxShop.Gui.PlayerShopGui;
import FzmXer.VexFxShop.Gui.SystemShopGui;
import FzmXer.VexFxShop.MySQL.CustemManager;
import lk.vexview.api.VexViewAPI;

public class MyCommand implements CommandExecutor {

	private Main main;

	public MyCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (sender instanceof Player) {
			Player player = ((Player) sender).getPlayer();
			if (cmd.getName().equalsIgnoreCase("fxs")) {
				switch (args.length) {
				case 0:
					if (sender.hasPermission("fzmxer.admin.vexfxshop.admin")) {
						sender.sendMessage("§a---------------------- [VexFxShop] ---------------------");
						sender.sendMessage("§a/fxs open - 打开商店(ss, ps).");
						sender.sendMessage("§a/fxs keys - 开启/关闭  输出按下键位数值.");
						sender.sendMessage("§a/fxs id - 显示手上物品的ID 配合 商品贴图使用.");
						sender.sendMessage("§a/fxs add [物品名] - 添加一个规则(将手上的物品显示名，\n替换为输入的物品名，仅适用于本插件).");
						sender.sendMessage("§a/fxs del - 删除一个规则(删除手上物品的物品名).");
						sender.sendMessage("§a/fxs reload - 重载插件.");
					} else if (sender.hasPermission("fzmxer.admin.vexfxshop.use")) {
						sender.sendMessage("§a---------------------- [VexFxShop] ---------------------");
						sender.sendMessage("§a/fxs open - 打开商店(ss, ps).");
					} else {
						sender.sendMessage("§c你没有权限！");
					}
					break;
				case 1:
					if (sender.hasPermission("fzmxer.admin.vexfxshop.admin")) {
						if (args[0].equalsIgnoreCase("Reload")) {
							/* 异步调用，减少卡服运算 */
							new BukkitRunnable() {
								@Override
								public void run() {
									main.ReadConfig();
									sender.sendMessage(ChatColor.GREEN + "[VexFxShop] 插件重载成功!");
								}
							}.runTaskAsynchronously(main);
						} else if (args[0].equalsIgnoreCase("keys")) {
							Main.KeyFlag = !Main.KeyFlag;
							sender.sendMessage(ChatColor.GREEN + "Keys: " + Main.KeyFlag);
						} else if (args[0].equalsIgnoreCase("id")) {
							Material type = player.getInventory().getItemInMainHand().getType();
							if (type.toString().equalsIgnoreCase(Material.AIR.toString())) {
								sender.sendMessage("请将物品拿在手中！");
							} else {
								String msg = "tellraw " + sender.getName()
										+ " [{\"text\":\"点击复制英文ID\",\"color\":\"dark_green\",\"bold\":true,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\""
										+ type.toString()
										+ "\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"点击复制\"}}]";
								Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), msg);
								// sender.sendMessage(msg);
							}
						} else if (args[0].equalsIgnoreCase("del")) {
							Material type = player.getInventory().getItemInMainHand().getType();
							if (type.toString().equalsIgnoreCase(Material.AIR.toString())) {
								sender.sendMessage("请将物品拿在手中！");
							}
							if (Main.DataSaveType.equalsIgnoreCase("mysql")) {
								sender.sendMessage(CustemManager.DelItemName(type.name()));
							} else {
								sender.sendMessage(CustemManager.DelItemName_sql(type.name()));
							}
						} else if (args[0].equalsIgnoreCase("open") || args[0].equalsIgnoreCase("add")) {
							sender.sendMessage("§c参数错误！");
						}
					}
					break;
				case 2:
					if (args[0].equalsIgnoreCase("open") && sender.hasPermission("fzmxer.admin.vexfxshop.use")) {
						/* 异步调用，减少卡服运算 */
						new BukkitRunnable() {
							@Override
							public void run() {
								if (args[1].equalsIgnoreCase("ss")) {
									VexViewAPI.openGui(player, new SystemShopGui(main, player));
								} else if (args[1].equalsIgnoreCase("ps")) {
									VexViewAPI.openGui(player, new PlayerShopGui(main, player));
								} else {
									sender.sendMessage("§c该商店不存在！");
								}
							}
						}.runTaskAsynchronously(main);
					} else if (args[0].equalsIgnoreCase("add")
							&& sender.hasPermission("fzmxer.admin.vexfxshop.admin")) {
						if (args.length == 2) {
							Material type = player.getInventory().getItemInMainHand().getType();
							if (type.toString().equalsIgnoreCase(Material.AIR.toString())) {
								sender.sendMessage("请将物品拿在手中！");
							}
							if (Main.DataSaveType.equalsIgnoreCase("mysql")) {
								sender.sendMessage(CustemManager.AddItemName(type.name(), args[1]));
							} else {
								sender.sendMessage(CustemManager.AddItemName_sql(type.name(), args[1]));
							}
						}
					}
					break;
				}
			}
		} else {
			if (args.length == 0) {
				sender.sendMessage("§2---------------------- [VexFxShop] ---------------------");
				sender.sendMessage("§2/fxs reload - 重载插件.");
			} else if (args[0].equalsIgnoreCase("Reload")) {
				/* 异步调用，减少卡服运算 */
				new BukkitRunnable() {
					@Override
					public void run() {
						main.ReadConfig();
						sender.sendMessage(ChatColor.GREEN + "[VexFxShop] 插件重载成功!");
					}
				}.runTaskAsynchronously(main);
			}

		}
		return true;
	}

}
