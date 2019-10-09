package FzmXer.VexFxShop.Main;

import java.util.Vector;

import org.black_ixx.playerpoints.PlayerPoints;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import FzmXer.VexFxShop.Data.BuyItemData;
import FzmXer.VexFxShop.Data.Custem_Data;
import FzmXer.VexFxShop.Data.Discount;
import FzmXer.VexFxShop.Data.ItemInfo;
import FzmXer.VexFxShop.Data.ShopGuiData;
import FzmXer.VexFxShop.Data.UpitemData;
import FzmXer.VexFxShop.MySQL.CustemManager;
import FzmXer.VexFxShop.MySQL.MySQLInfo;
import FzmXer.VexFxShop.MySQL.MySQLManager;
import FzmXer.VexFxShop.MySQL.ShopItemManager;
import FzmXer.VexFxShop.SQLite.SQLManager;
import FzmXer.VexFxShop.Utils.Metrics;
import FzmXer.VexFxShop.Utils.PointApi;
import FzmXer.VexFxShop.Utils.Utils;
import FzmXer.VexFxShop.Utils.VaultApi;
import lk.vexview.api.VexViewAPI;
import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin {
	/* Main */
	public static Main instance;
	/* 调试输出用 */
	public static boolean KeyFlag = false;
	/* 经济系统 */
	public static Economy econ = null;
	public static PlayerPoints points = null;
	/* 数据存储方式 */
	public static boolean UUID;
	/* 自定义数据库信息 */
	public static MySQLInfo info = null;
	/* 为商店绑定按钮 */
	public static int System_Key = -1, Player_Key = -1;
	/* 商店商品信息链表 */
	public static Vector<ItemInfo> systemlist = null;
	public static Vector<ItemInfo> playerlist = null;
	public static Vector<ItemInfo> sellitemlist = null;
	/* 自定义物品名 */
	public static Vector<Custem_Data> Custem_name = null;
	/* MySQL数据库 */
	public MySQLManager mysql = null;
	/* SQLite数据库 */
	public SQLManager sqlite = null;
	/* 商店打折标识 */
	public static Vector<Discount> discount = null;
	/* 存储方式 */
	public static String DataSaveType = "mysql";
	/* ShopGUI */
	public static Vector<ShopGuiData> syshopg;
	/* UpitemGui*/
	public static UpitemData upd;
	/* BuyItemGui*/
	public static BuyItemData byd;
	/* BanLore*/
	public static Vector<String> banlore;
	/* 手续费*/
	public static int brokerage = 5;
	
	public static boolean isPoint;
	
	
	@Override
	public void onEnable() {
		init();
		/* 添加统计 */
		@SuppressWarnings("unused")
		Metrics metrics = new Metrics(this);
	}

	@Override
	public void onDisable() {
		/* 结束所有异步运算 */
		Bukkit.getScheduler().cancelTasks(this);
		/* 注销监听器 */
		HandlerList.unregisterAll();
		/* 输出文字 */
		Utils.Msg(ChatColor.GREEN + "[VexFxShop] 注销成功!");
	}

	private void init() {
		instance = this;
		Utils.Msg(ChatColor.GREEN + "============ [VexFxShop > 正在启动] ============");
		if (Double.parseDouble(VexViewAPI.getVexView().getVersion().trim().substring(0, 3)) < 2.4) {
			Utils.Msg(ChatColor.RED + "请使用VexView2.4 及以上");
			Utils.Msg("§a============ [VexFxShop] > 启动失败  ============");
			Bukkit.getPluginManager().disablePlugin(instance);
			return;
		} else {
			Utils.Msg("§a [VexView] " + VexViewAPI.getVexView().getVersion());
		}
		if (Bukkit.getPluginManager().isPluginEnabled("Vault")) {
			Utils.Msg("§a [Vault] 已成功兼容！");
			VaultApi.setupEconomy();
		} else {
			Utils.Msg("§c [Vault] 未找到此插件！");
		}

		if (Bukkit.getPluginManager().isPluginEnabled("PlayerPoints")) {
			isPoint = true;
			Utils.Msg("§a [PlayerPoints] 已成功兼容！");
			PointApi.hookPlayerPoints();
		} else {
			isPoint = false;
			Utils.Msg("§c [PlayerPoints] 未找到此插件！");
		}
		/* 读取插件配置 */
		ReadConfig();

		/* 读取商品信息 */
		if (DataSaveType.equalsIgnoreCase("mysql")) {
			/* 连接数据库 */
			if (mysql == null) {
				mysql = new MySQLManager(instance, info);
				/* 创建自定义物品名表 */
				CustemManager.CreateCustem();
				/* 创建商店表 */
				ShopItemManager.Create_table("systemshop");
				ShopItemManager.Create_table("playershop");
			}
			ReadItemInfo();

		} else {
			if (sqlite == null) {
				sqlite = new SQLManager();
				/* 创建自定义物品名表 */
				CustemManager.CreateCustem_sql();

				SQLManager.CreateTable("systemshop");

				SQLManager.CreateTable("playershop");
			}
			ReadItemInfo_SQL();
		}

		/* 设置打折信息 */
		discount = new Vector<Discount>();
		Discount dc;
		dc = new Discount("VexFxShop.v5", 0.75);
		discount.add(dc);
		Utils.Msg(" 添加打折权限 VexFxShop.v5 成功，打折 75% (仅限系统商店)");
		dc = new Discount("VexFxShop.v4", 0.8);
		discount.add(dc);
		Utils.Msg(" 添加打折权限 VexFxShop.v4 成功，打折 80% (仅限系统商店)");
		dc = new Discount("VexFxShop.v3", 0.85);
		discount.add(dc);
		Utils.Msg(" 添加打折权限 VexFxShop.v3 成功，打折 85% (仅限系统商店)");
		dc = new Discount("VexFxShop.v2", 0.9);
		discount.add(dc);
		Utils.Msg(" 添加打折权限 VexFxShop.v2 成功，打折 90% (仅限系统商店)");
		dc = new Discount("VexFxShop.v1", 0.95);
		discount.add(dc);
		Utils.Msg(" 添加打折权限 VexFxShop.v1 成功，打折 95% (仅限系统商店)");
		/* 注册命令 */
		Bukkit.getPluginCommand("fxs").setExecutor(new MyCommand(this));
		/* 注册事件监听器 */
		Bukkit.getPluginManager().registerEvents(new MyListener(), this);
		/* 输出插件载入状态 */
		Utils.Msg(ChatColor.GREEN + "============ [VexFxShop > 启动完成] ============");
	}

	public static void ReadItemInfo() {
		Custem_name = new Vector<Custem_Data>();
		Custem_name.addAll(CustemManager.GetItemName_All());
		if (Custem_name.size() > 0)
			Utils.Msg(ChatColor.GREEN + " 读取 Custem_name " + Custem_name.size() + " 个自定义商品名");

		systemlist = new Vector<ItemInfo>();
		systemlist.addAll(ShopItemManager.GetItems_All("systemshop"));
		if (systemlist.size() > 0)
			Utils.Msg(ChatColor.GREEN + " 读取 System_Shop " + systemlist.size() + " 个商品");

		playerlist = new Vector<ItemInfo>();
		playerlist.addAll(ShopItemManager.GetItems_All("playershop"));
		if (playerlist.size() > 0)
			Utils.Msg(ChatColor.GREEN + " 读取 Player_Shop " + playerlist.size() + " 个商品");

	}

	public static void reloadItemInfo(String shopname) {
		if (shopname.contentEquals("systemshop")) {
			systemlist = new Vector<ItemInfo>();
			systemlist.addAll(ShopItemManager.GetItems_All("systemshop"));
			Utils.Msg(ChatColor.GREEN + " 读取 System_Shop " + systemlist.size() + " 个商品");
		} else if (shopname.contentEquals("playershop")) {
			playerlist = new Vector<ItemInfo>();
			playerlist.addAll(ShopItemManager.GetItems_All("playershop"));
			Utils.Msg(ChatColor.GREEN + " 读取 Player_Shop " + playerlist.size() + " 个商品");
		}
	}

	public static void ReadItemInfo_SQL() {
		Custem_name = new Vector<Custem_Data>();
		Custem_name.addAll(CustemManager.GetItemName_All_sql());
		if (Custem_name.size() > 0)
			Utils.Msg(ChatColor.GREEN + " 读取 Custem_name " + Custem_name.size() + " 个自定义商品名");

		systemlist = new Vector<ItemInfo>();
		systemlist.addAll(SQLManager.GetItems_All("systemshop"));
		if (systemlist.size() > 0)
			Utils.Msg(ChatColor.GREEN + " 读取 System_Shop " + systemlist.size() + " 个商品");

		playerlist = new Vector<ItemInfo>();
		playerlist.addAll(SQLManager.GetItems_All("playershop"));
		if (playerlist.size() > 0)
			Utils.Msg(ChatColor.GREEN + " 读取 Player_Shop " + playerlist.size() + " 个商品");
	}

	public static void reloadItemInfo_sql(String shopname) {
		if (shopname.contentEquals("systemshop")) {
			systemlist = new Vector<ItemInfo>();
			systemlist.addAll(SQLManager.GetItems_All("systemshop"));
			Utils.Msg(ChatColor.GREEN + " 读取 System_Shop " + systemlist.size() + " 个商品");
		} else if (shopname.contentEquals("playershop")) {
			playerlist = new Vector<ItemInfo>();
			playerlist.addAll(SQLManager.GetItems_All("playershop"));
			Utils.Msg(ChatColor.GREEN + " 读取 Player_Shop " + playerlist.size() + " 个商品");
		}
	}

	public void ReadConfig() {
		Utils.saveDefaultConfig(this, "config.yml", "");
		Utils.saveDefaultConfig(this, "banlore.yml", "");
		Utils.saveDefaultConfig(this, "systemshop.yml", "/Gui/");
		Utils.saveDefaultConfig(this, "playershop.yml", "/Gui/");
		Utils.saveDefaultConfig(this, "upitem.yml", "/Gui/");
		Utils.saveDefaultConfig(this, "buyitem.yml", "/Gui/");
		this.reloadConfig();
		DataSaveType = getConfigs("VexFxShop.DataSaveType");
		/* 读取配置项 */
		info = new MySQLInfo(getConfigs("VexFxShop.MySQL.IP"), getConfigs("VexFxShop.MySQL.UserName"),
				getConfigs("VexFxShop.MySQL.PassWord"), getConfigs("VexFxShop.MySQL.DatabaseName"),
				this.getConfig().getInt("VexFxShop.MySQL.Port"));
		System_Key = this.getConfig().getInt("VexFxShop.SystemShop");
		Player_Key = this.getConfig().getInt("VexFxShop.PlayerShop");
		brokerage = this.getConfig().getInt("VexFxShop.brokerage");
		Utils.Msg(ChatColor.GREEN + " 当前手续费率为 "+ brokerage +"%");
		
		Utils.ReadShopGui();
	}



	private String getConfigs(String str) {
		return this.getConfig().getString(str);
	}

}
