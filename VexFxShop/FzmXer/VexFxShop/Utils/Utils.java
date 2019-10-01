package FzmXer.VexFxShop.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.Vector;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import FzmXer.VexFxShop.Data.BuyItemData;
import FzmXer.VexFxShop.Data.ShopGuiData;
import FzmXer.VexFxShop.Data.UpitemData;
import FzmXer.VexFxShop.Gui.DialogHub;
import FzmXer.VexFxShop.Main.Main;

public class Utils {

	public final static String Path = "[local]VexFxShop/";

	/* 修改数据使用(插入，更新数据可用) */
	public static String Table_Pres = "(item_type, item_name, item_lore, item_nbts, item_enchants, item_durability, "
			+ "item_money, item_point, item_dates, player_name, player_uuid, shop_name, item_number) "
			+ "values (?,?,?,?,?,?,?,?,?,?,?,?,?)";

	/* 创建自定义物品名表 MYSQL */
	public static String CName = "CREATE TABLE IF NOT EXISTS `custem_name` (" + "`id` int(11) NOT NULL AUTO_INCREMENT,"
			+ "`E_name` varchar(255)," + "`C_name` varchar(255)," + "PRIMARY KEY (`id`)"
			+ ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;";

	/* 创建自定义物品名表 SQLITE */
	public static String CName_sql = "CREATE TABLE IF NOT EXISTS `custem_name` ("
			+ "id INTEGER PRIMARY KEY AUTOINCREMENT," + "E_name TEXT NOT NULL," + "C_name TEXT NOT NULL)";

	/* 创建商店信息表 MYSQL */
	public static String getMySQLTable(String str) {
		String table = "CREATE TABLE IF NOT EXISTS `" + str + "` (" + "`id` int(11) NOT NULL AUTO_INCREMENT,"
				+ "`nbt_id` varchar(32)," + "`item_type` varchar(32)," + "`item_name` varchar(255),"
				+ "`item_lore` MEDIUMTEXT," + "`item_nbts` MEDIUMTEXT," + "`item_enchants` MEDIUMTEXT,"
				+ "`item_durability` int(13)," + "`item_money` varchar(9)," + "`item_point` varchar(9),"
				+ "`item_dates` varchar(13)," + "`player_name` varchar(18)," + "`player_uuid` varchar(36),"
				+ "`shop_name` varchar(36)," + "`item_number` int(13)," + "PRIMARY KEY (`id`)"
				+ ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;";
		return table;
	}

	/* 创建商店信息表 SQLITE */
	public static String getSQLiteTable(String str) {
		String table = "CREATE TABLE IF NOT EXISTS " + str + " (id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ " nbt_id TEXT, item_type TEXT NOT NULL, item_name TEXT," + " item_lore TEXT,"
				+ " item_nbts TEXT NOT NULL," + " item_enchants TEXT," + " item_durability INT NOT NULL,"
				+ " item_money TEXT NOT NULL," + " item_point TEXT NOT NULL," + " item_dates TEXT NOT NULL,"
				+ " player_name TEXT NOT NULL," + " player_uuid TEXT NOT NULL," + " shop_name TEXT NOT NULL,"
				+ " item_number INT NOT NULL)";
		return table;
	}

	public static void WLog(String gamename, String log) {
		/* 文件名 */
		String filename = gamename + ".yml";
		String path = Main.instance.getDataFolder() + "/Log/";
		/* 找到文件 */
		File file = new File(path, filename);
		/* 找到目录 */
		int lastIndex = filename.lastIndexOf('/');
		File dir = new File(path,
				filename.substring(0, lastIndex >= 0 ? lastIndex : 0));
		if (!dir.exists()) {// 判断是否存在
			dir.mkdirs();// 创建目录
		}
		
		if (!file.exists()) {// 如果配置文件不存在
			try {
				file.createNewFile();
			} catch (IOException e) {
				Msg("§c[VexFxShop] 创建玩家日志文件失败！");
			}
		}
		
		log = log + "\n";
		
		try {
			// 打开输出流
			 FileOutputStream out = new FileOutputStream(path + filename,true);
			 byte[] data = log.getBytes();
			 out.write(data);
			 out.close();
		} catch (IOException ex) {
			return;
		}

	}

	public static void sendMsg(Player player, String text) {
//		if(player.isOp()) {
		new DialogHub(player, "§l" + text);
//		}else {
//			player.sendTitle(text, "§a[VexFxShop]");
//		}
	}

	public static void Msg(String text) {
		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + text);
	}

	public static void saveDefaultConfig(JavaPlugin plugin, String fileName, String path) {
		if (!new File(plugin.getDataFolder() + path, fileName).exists()) {// 判断配置文件是否存在
			InputStream in = plugin.getResource(fileName);// 读取jar文件里面的配置文件
			if (in == null) {// jar包里面没有这个配置文件
				Msg("插件配置文件 " + fileName + " 不存在,释放文件失败！");
				return;
			}
			// 找到配置文件
			File file = new File(plugin.getDataFolder() + path, fileName);
			int lastIndex = fileName.lastIndexOf('/');
			// 找到目录
			File dir = new File(plugin.getDataFolder() + path, fileName.substring(0, lastIndex >= 0 ? lastIndex : 0));

			if (!dir.exists()) {// 判断是否存在
				dir.mkdirs();// 创建目录
			}
			try {
				// 打开输出流
				OutputStream out = new FileOutputStream(file);
				byte[] buf = new byte[1024];
				int len;
				// 循环写入到外面的配置去
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				// 关闭流
				out.close();
				in.close();
			} catch (IOException ex) {
				return;
			}
		}
	}

	/**
	 * 用于获取一个String的md5值
	 * 
	 * @param string
	 * @return
	 */
	public static String getMd5(String str) {
		try {
			if (str.length() <= 0) {
				return "";
			}
			// 生成实现指定摘要算法的 MessageDigest 对象。
			MessageDigest md = MessageDigest.getInstance("MD5");
			// 使用指定的字节数组更新摘要。
			md.update(str.getBytes());
			// 通过执行诸如填充之类的最终操作完成哈希计算。
			byte b[] = md.digest();
			// 生成具体的md5密码到buf数组
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			return buf.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 时间戳转换成日期格式字符串
	 * 
	 * @param seconds   精确到秒的字符串
	 * @param formatStr
	 * @return
	 */
	public static String timeStamp2Date(String seconds, String format) {
		if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
			return "";
		}
		if (format == null || format.isEmpty()) {
			format = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date(Long.valueOf(seconds + "000")));
	}

	/**
	 * 使用UUID获取玩家
	 * 
	 * @param uuid 玩家的UUID
	 * @return Player 返回玩家实体
	 */
	public static OfflinePlayer loadPlayer(UUID uuid) {
		try {
			OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
			if (player == null || !player.hasPlayedBefore()) {
				return null;
			}
			return player;
		} catch (Exception e) {
			Msg("§c加载离线玩家失败！");
		}
		return null;
	}

	public static void ReadShopGui() {
		Main.syshopg = new Vector<ShopGuiData>();
		ShopGuiData ssgui = new ShopGuiData();

		FileConfiguration config = YamlConfiguration
				.loadConfiguration(new File(Main.instance.getDataFolder() + "", "banlore.yml"));

		Main.banlore = new Vector<String>();
		Main.banlore.addAll(config.getStringList("VexFxShop.BanLore"));

		config = YamlConfiguration
				.loadConfiguration(new File(Main.instance.getDataFolder() + "/Gui/", "systemshop.yml"));
		/* 背景 */
		ssgui.setBg_image(config.getString("VexFxShop.SystemShop-bg.image").replace("[local]", Path));
		ssgui.setBg_x(config.getInt("VexFxShop.SystemShop-bg.x"));
		ssgui.setBg_y(config.getInt("VexFxShop.SystemShop-bg.y"));
		ssgui.setBg_w(config.getInt("VexFxShop.SystemShop-bg.w"));
		ssgui.setBg_h(config.getInt("VexFxShop.SystemShop-bg.h"));
		ssgui.setBg_xs(config.getInt("VexFxShop.SystemShop-bg.xs"));
		ssgui.setBg_ys(config.getInt("VexFxShop.SystemShop-bg.ys"));
		/* 标题 */
		ssgui.setTitle_text(config.getString("VexFxShop.SystemShop-title.text"));
		ssgui.setTitle_x(config.getInt("VexFxShop.SystemShop-title.x"));
		ssgui.setTitle_y(config.getInt("VexFxShop.SystemShop-title.y"));
		/* 退出按钮 */
		ssgui.setExit_image1(config.getString("VexFxShop.SystemShop-exit.image1").replace("[local]", Path));
		ssgui.setExit_image2(config.getString("VexFxShop.SystemShop-exit.image2").replace("[local]", Path));
		ssgui.setExit_x(config.getInt("VexFxShop.SystemShop-exit.x"));
		ssgui.setExit_y(config.getInt("VexFxShop.SystemShop-exit.y"));
		ssgui.setExit_w(config.getInt("VexFxShop.SystemShop-exit.w"));
		ssgui.setExit_h(config.getInt("VexFxShop.SystemShop-exit.h"));
		/* 信息显示 */
		ssgui.setInfo_money_image(config.getString("VexFxShop.SystemShop-info.money.image").replace("[local]", Path));
		ssgui.setInfo_money_x(config.getInt("VexFxShop.SystemShop-info.money.x"));
		ssgui.setInfo_money_y(config.getInt("VexFxShop.SystemShop-info.money.y"));
		ssgui.setInfo_money_w(config.getInt("VexFxShop.SystemShop-info.money.w"));
		ssgui.setInfo_money_h(config.getInt("VexFxShop.SystemShop-info.money.h"));
		ssgui.setInfo_money_text(config.getString("VexFxShop.SystemShop-info.money-text.text"));
		ssgui.setInfo_money_text_x(config.getInt("VexFxShop.SystemShop-info.money-text.x"));
		ssgui.setInfo_money_text_y(config.getInt("VexFxShop.SystemShop-info.money-text.y"));
		ssgui.setInfo_money_text_fontsize(config.getDouble("VexFxShop.SystemShop-info.money-text.fontsize"));
		/* 信息显示 */
		ssgui.setInfo_point_image(config.getString("VexFxShop.SystemShop-info.point.image").replace("[local]", Path));
		ssgui.setInfo_point_x(config.getInt("VexFxShop.SystemShop-info.point.x"));
		ssgui.setInfo_point_y(config.getInt("VexFxShop.SystemShop-info.point.y"));
		ssgui.setInfo_point_w(config.getInt("VexFxShop.SystemShop-info.point.w"));
		ssgui.setInfo_point_h(config.getInt("VexFxShop.SystemShop-info.point.h"));
		ssgui.setInfo_point_text(config.getString("VexFxShop.SystemShop-info.point-text.text"));
		ssgui.setInfo_point_text_x(config.getInt("VexFxShop.SystemShop-info.point-text.x"));
		ssgui.setInfo_point_text_y(config.getInt("VexFxShop.SystemShop-info.point-text.y"));
		ssgui.setInfo_point_text_fontsize(config.getDouble("VexFxShop.SystemShop-info.point-text.fontsize"));
		/* 上传按钮 */
		ssgui.setUpitem_text(config.getString("VexFxShop.SystemShop-Upitem.text"));
		ssgui.setUpitem_image1(config.getString("VexFxShop.SystemShop-Upitem.image1").replace("[local]", Path));
		ssgui.setUpitem_image2(config.getString("VexFxShop.SystemShop-Upitem.image2").replace("[local]", Path));
		ssgui.setUpitem_x(config.getInt("VexFxShop.SystemShop-Upitem.x"));
		ssgui.setUpitem_y(config.getInt("VexFxShop.SystemShop-Upitem.y"));
		ssgui.setUpitem_w(config.getInt("VexFxShop.SystemShop-Upitem.w"));
		ssgui.setUpitem_h(config.getInt("VexFxShop.SystemShop-Upitem.h"));
		/* 列表框 */
		ssgui.setList_list_x(config.getInt("VexFxShop.SystemShop-list.list.x"));
		ssgui.setList_list_y(config.getInt("VexFxShop.SystemShop-list.list.y"));
		ssgui.setList_list_w(config.getInt("VexFxShop.SystemShop-list.list.w"));
		ssgui.setList_list_h(config.getInt("VexFxShop.SystemShop-list.list.h"));
		ssgui.setList_list_b(config.getInt("VexFxShop.SystemShop-list.list.b"));
		/* 商品背景 */
		ssgui.setList_itembg_image(
				config.getString("VexFxShop.SystemShop-list.item-bg.image").replace("[local]", Path));
		ssgui.setList_itembg_x(config.getInt("VexFxShop.SystemShop-list.item-bg.x"));
		ssgui.setList_itembg_y(config.getInt("VexFxShop.SystemShop-list.item-bg.y"));
		ssgui.setList_itembg_w(config.getInt("VexFxShop.SystemShop-list.item-bg.w"));
		ssgui.setList_itembg_h(config.getInt("VexFxShop.SystemShop-list.item-bg.h"));
		ssgui.setList_itembg_b(config.getInt("VexFxShop.SystemShop-list.item-bg.b"));
		/* 贴图 */
		ssgui.setList_item_image(
				config.getString("VexFxShop.SystemShop-list.item-image.image").replace("[local]", Path));
		ssgui.setList_item_x(config.getInt("VexFxShop.SystemShop-list.item-image.x"));
		ssgui.setList_item_y(config.getInt("VexFxShop.SystemShop-list.item-image.y"));
		ssgui.setList_item_b(config.getInt("VexFxShop.SystemShop-list.item-image.b"));
		ssgui.setList_item_size(config.getInt("VexFxShop.SystemShop-list.item-image.size"));
		/* 商品名 */
		ssgui.setList_itemname_text(config.getString("VexFxShop.SystemShop-list.item-name.text"));
		ssgui.setList_itemname_x(config.getInt("VexFxShop.SystemShop-list.item-name.x"));
		ssgui.setList_itemname_y(config.getInt("VexFxShop.SystemShop-list.item-name.y"));
		ssgui.setList_itemname_b(config.getInt("VexFxShop.SystemShop-list.item-name.b"));
		ssgui.setList_itemname_fontsize(config.getDouble("VexFxShop.SystemShop-list.item-name.fontsize"));
		/* 单价(金币) */
		ssgui.setList_itemdj_money_text(config.getString("VexFxShop.SystemShop-list.item-dj.money.text"));
		ssgui.setList_itemdj_money_x(config.getInt("VexFxShop.SystemShop-list.item-dj.money.x"));
		ssgui.setList_itemdj_money_y(config.getInt("VexFxShop.SystemShop-list.item-dj.money.y"));
		ssgui.setList_itemdj_money_b(config.getInt("VexFxShop.SystemShop-list.item-dj.money.b"));
		ssgui.setList_itemdj_money_fontsize(config.getDouble("VexFxShop.SystemShop-list.item-dj.money.fontsize"));
		/* 单价(点券) */
		ssgui.setList_itemdj_point_text(config.getString("VexFxShop.SystemShop-list.item-dj.point.text"));
		ssgui.setList_itemdj_point_x(config.getInt("VexFxShop.SystemShop-list.item-dj.point.x"));
		ssgui.setList_itemdj_point_y(config.getInt("VexFxShop.SystemShop-list.item-dj.point.y"));
		ssgui.setList_itemdj_point_b(config.getInt("VexFxShop.SystemShop-list.item-dj.point.b"));
		ssgui.setList_itemdj_point_fontsize(config.getDouble("VexFxShop.SystemShop-list.item-dj.point.fontsize"));
		/* 卖家名 */
		ssgui.setList_itemsname_text(config.getString("VexFxShop.SystemShop-list.item-sname.text"));
		ssgui.setList_itemsname_x(config.getInt("VexFxShop.SystemShop-list.item-sname.x"));
		ssgui.setList_itemsname_y(config.getInt("VexFxShop.SystemShop-list.item-sname.y"));
		ssgui.setList_itemsname_b(config.getInt("VexFxShop.SystemShop-list.item-sname.b"));
		ssgui.setList_itemsname_fontsize(config.getDouble("VexFxShop.SystemShop-list.item-sname.fontsize"));
		/* 上架日期 */
		ssgui.setList_itemdate_text(config.getString("VexFxShop.SystemShop-list.item-date.text"));
		ssgui.setList_itemdate_x(config.getInt("VexFxShop.SystemShop-list.item-date.x"));
		ssgui.setList_itemdate_y(config.getInt("VexFxShop.SystemShop-list.item-date.y"));
		ssgui.setList_itemdate_b(config.getInt("VexFxShop.SystemShop-list.item-date.b"));
		ssgui.setList_itemdate_fontsize(config.getDouble("VexFxShop.SystemShop-list.item-date.fontsize"));
		/* 数量 */
		ssgui.setList_itemnumber_text(config.getString("VexFxShop.SystemShop-list.item-number.text"));
		ssgui.setList_itemnumber_x(config.getInt("VexFxShop.SystemShop-list.item-number.x"));
		ssgui.setList_itemnumber_y(config.getInt("VexFxShop.SystemShop-list.item-number.y"));
		ssgui.setList_itemnumber_b(config.getInt("VexFxShop.SystemShop-list.item-number.b"));
		ssgui.setList_itemnumber_fontsize(config.getDouble("VexFxShop.SystemShop-list.item-number.fontsize"));
		/* 购买按钮 */
		ssgui.setList_itembuy_text(config.getString("VexFxShop.SystemShop-list.item-buy.text"));
		ssgui.setList_itembuy_image1(
				config.getString("VexFxShop.SystemShop-list.item-buy.image1").replace("[local]", Path));
		ssgui.setList_itembuy_image2(
				config.getString("VexFxShop.SystemShop-list.item-buy.image2").replace("[local]", Path));
		ssgui.setList_itembuy_x(config.getInt("VexFxShop.SystemShop-list.item-buy.x"));
		ssgui.setList_itembuy_y(config.getInt("VexFxShop.SystemShop-list.item-buy.y"));
		ssgui.setList_itembuy_w(config.getInt("VexFxShop.SystemShop-list.item-buy.w"));
		ssgui.setList_itembuy_h(config.getInt("VexFxShop.SystemShop-list.item-buy.h"));
		ssgui.setList_itembuy_b(config.getInt("VexFxShop.SystemShop-list.item-buy.b"));

		Main.syshopg.add(ssgui);

		ssgui = new ShopGuiData();

		config = YamlConfiguration
				.loadConfiguration(new File(Main.instance.getDataFolder() + "/Gui/", "playershop.yml"));
		/* 背景 */
		ssgui.setBg_image(config.getString("VexFxShop.PlayerShop-bg.image").replace("[local]", Path));
		ssgui.setBg_x(config.getInt("VexFxShop.PlayerShop-bg.x"));
		ssgui.setBg_y(config.getInt("VexFxShop.PlayerShop-bg.y"));
		ssgui.setBg_w(config.getInt("VexFxShop.PlayerShop-bg.w"));
		ssgui.setBg_h(config.getInt("VexFxShop.PlayerShop-bg.h"));
		ssgui.setBg_xs(config.getInt("VexFxShop.PlayerShop-bg.xs"));
		ssgui.setBg_ys(config.getInt("VexFxShop.PlayerShop-bg.ys"));
		/* 标题 */
		ssgui.setTitle_text(config.getString("VexFxShop.PlayerShop-title.text"));
		ssgui.setTitle_x(config.getInt("VexFxShop.PlayerShop-title.x"));
		ssgui.setTitle_y(config.getInt("VexFxShop.PlayerShop-title.y"));
		/* 退出按钮 */
		ssgui.setExit_image1(config.getString("VexFxShop.PlayerShop-exit.image1").replace("[local]", Path));
		ssgui.setExit_image2(config.getString("VexFxShop.PlayerShop-exit.image2").replace("[local]", Path));
		ssgui.setExit_x(config.getInt("VexFxShop.PlayerShop-exit.x"));
		ssgui.setExit_y(config.getInt("VexFxShop.PlayerShop-exit.y"));
		ssgui.setExit_w(config.getInt("VexFxShop.PlayerShop-exit.w"));
		ssgui.setExit_h(config.getInt("VexFxShop.PlayerShop-exit.h"));
		/* 信息显示 */
		ssgui.setInfo_money_image(config.getString("VexFxShop.PlayerShop-info.money.image").replace("[local]", Path));
		ssgui.setInfo_money_x(config.getInt("VexFxShop.PlayerShop-info.money.x"));
		ssgui.setInfo_money_y(config.getInt("VexFxShop.PlayerShop-info.money.y"));
		ssgui.setInfo_money_w(config.getInt("VexFxShop.PlayerShop-info.money.w"));
		ssgui.setInfo_money_h(config.getInt("VexFxShop.PlayerShop-info.money.h"));
		ssgui.setInfo_money_text(config.getString("VexFxShop.PlayerShop-info.money-text.text"));
		ssgui.setInfo_money_text_x(config.getInt("VexFxShop.PlayerShop-info.money-text.x"));
		ssgui.setInfo_money_text_y(config.getInt("VexFxShop.PlayerShop-info.money-text.y"));
		ssgui.setInfo_money_text_fontsize(config.getDouble("VexFxShop.PlayerShop-info.money-text.fontsize"));
		/* 信息显示 */
		ssgui.setInfo_point_image(config.getString("VexFxShop.PlayerShop-info.point.image").replace("[local]", Path));
		ssgui.setInfo_point_x(config.getInt("VexFxShop.PlayerShop-info.point.x"));
		ssgui.setInfo_point_y(config.getInt("VexFxShop.PlayerShop-info.point.y"));
		ssgui.setInfo_point_w(config.getInt("VexFxShop.PlayerShop-info.point.w"));
		ssgui.setInfo_point_h(config.getInt("VexFxShop.PlayerShop-info.point.h"));
		ssgui.setInfo_point_text(config.getString("VexFxShop.PlayerShop-info.point-text.text"));
		ssgui.setInfo_point_text_x(config.getInt("VexFxShop.PlayerShop-info.point-text.x"));
		ssgui.setInfo_point_text_y(config.getInt("VexFxShop.PlayerShop-info.point-text.y"));
		ssgui.setInfo_point_text_fontsize(config.getDouble("VexFxShop.PlayerShop-info.point-text.fontsize"));
		/* 上架按钮 */
		ssgui.setUpitem_text(config.getString("VexFxShop.PlayerShop-Upitem.text"));
		ssgui.setUpitem_image1(config.getString("VexFxShop.PlayerShop-Upitem.image1").replace("[local]", Path));
		ssgui.setUpitem_image2(config.getString("VexFxShop.PlayerShop-Upitem.image2").replace("[local]", Path));
		ssgui.setUpitem_x(config.getInt("VexFxShop.PlayerShop-Upitem.x"));
		ssgui.setUpitem_y(config.getInt("VexFxShop.PlayerShop-Upitem.y"));
		ssgui.setUpitem_w(config.getInt("VexFxShop.PlayerShop-Upitem.w"));
		ssgui.setUpitem_h(config.getInt("VexFxShop.PlayerShop-Upitem.h"));
		/* 列表框 */
		ssgui.setList_list_x(config.getInt("VexFxShop.PlayerShop-list.list.x"));
		ssgui.setList_list_y(config.getInt("VexFxShop.PlayerShop-list.list.y"));
		ssgui.setList_list_w(config.getInt("VexFxShop.PlayerShop-list.list.w"));
		ssgui.setList_list_h(config.getInt("VexFxShop.PlayerShop-list.list.h"));
		ssgui.setList_list_b(config.getInt("VexFxShop.PlayerShop-list.list.b"));
		/* 商品背景 */
		ssgui.setList_itembg_image(
				config.getString("VexFxShop.PlayerShop-list.item-bg.image").replace("[local]", Path));
		ssgui.setList_itembg_x(config.getInt("VexFxShop.PlayerShop-list.item-bg.x"));
		ssgui.setList_itembg_y(config.getInt("VexFxShop.PlayerShop-list.item-bg.y"));
		ssgui.setList_itembg_w(config.getInt("VexFxShop.PlayerShop-list.item-bg.w"));
		ssgui.setList_itembg_h(config.getInt("VexFxShop.PlayerShop-list.item-bg.h"));
		ssgui.setList_itembg_b(config.getInt("VexFxShop.PlayerShop-list.item-bg.b"));
		/* 贴图 */
		ssgui.setList_item_image(
				config.getString("VexFxShop.PlayerShop-list.item-image.image").replace("[local]", Path));
		ssgui.setList_item_x(config.getInt("VexFxShop.PlayerShop-list.item-image.x"));
		ssgui.setList_item_y(config.getInt("VexFxShop.PlayerShop-list.item-image.y"));
		ssgui.setList_item_b(config.getInt("VexFxShop.PlayerShop-list.item-image.b"));
		ssgui.setList_item_size(config.getInt("VexFxShop.PlayerShop-list.item-image.size"));
		/* 商品名 */
		ssgui.setList_itemname_text(config.getString("VexFxShop.PlayerShop-list.item-name.text"));
		ssgui.setList_itemname_x(config.getInt("VexFxShop.PlayerShop-list.item-name.x"));
		ssgui.setList_itemname_y(config.getInt("VexFxShop.PlayerShop-list.item-name.y"));
		ssgui.setList_itemname_b(config.getInt("VexFxShop.PlayerShop-list.item-name.b"));
		ssgui.setList_itemname_fontsize(config.getDouble("VexFxShop.PlayerShop-list.item-name.fontsize"));
		/* 单价(金币) */
		ssgui.setList_itemdj_money_text(config.getString("VexFxShop.PlayerShop-list.item-dj.money.text"));
		ssgui.setList_itemdj_money_x(config.getInt("VexFxShop.PlayerShop-list.item-dj.money.x"));
		ssgui.setList_itemdj_money_y(config.getInt("VexFxShop.PlayerShop-list.item-dj.money.y"));
		ssgui.setList_itemdj_money_b(config.getInt("VexFxShop.PlayerShop-list.item-dj.money.b"));
		ssgui.setList_itemdj_money_fontsize(config.getDouble("VexFxShop.PlayerShop-list.item-dj.money.fontsize"));
		/* 单价(点券) */
		ssgui.setList_itemdj_point_text(config.getString("VexFxShop.PlayerShop-list.item-dj.point.text"));
		ssgui.setList_itemdj_point_x(config.getInt("VexFxShop.PlayerShop-list.item-dj.point.x"));
		ssgui.setList_itemdj_point_y(config.getInt("VexFxShop.PlayerShop-list.item-dj.point.y"));
		ssgui.setList_itemdj_point_b(config.getInt("VexFxShop.PlayerShop-list.item-dj.point.b"));
		ssgui.setList_itemdj_point_fontsize(config.getDouble("VexFxShop.PlayerShop-list.item-dj.point.fontsize"));
		/* 卖家名 */
		ssgui.setList_itemsname_text(config.getString("VexFxShop.PlayerShop-list.item-sname.text"));
		ssgui.setList_itemsname_x(config.getInt("VexFxShop.PlayerShop-list.item-sname.x"));
		ssgui.setList_itemsname_y(config.getInt("VexFxShop.PlayerShop-list.item-sname.y"));
		ssgui.setList_itemsname_b(config.getInt("VexFxShop.PlayerShop-list.item-sname.b"));
		ssgui.setList_itemsname_fontsize(config.getDouble("VexFxShop.PlayerShop-list.item-sname.fontsize"));
		/* 上架日期 */
		ssgui.setList_itemdate_text(config.getString("VexFxShop.PlayerShop-list.item-date.text"));
		ssgui.setList_itemdate_x(config.getInt("VexFxShop.PlayerShop-list.item-date.x"));
		ssgui.setList_itemdate_y(config.getInt("VexFxShop.PlayerShop-list.item-date.y"));
		ssgui.setList_itemdate_b(config.getInt("VexFxShop.PlayerShop-list.item-date.b"));
		ssgui.setList_itemdate_fontsize(config.getDouble("VexFxShop.PlayerShop-list.item-date.fontsize"));
		/* 数量 */
		ssgui.setList_itemnumber_text(config.getString("VexFxShop.PlayerShop-list.item-number.text"));
		ssgui.setList_itemnumber_x(config.getInt("VexFxShop.PlayerShop-list.item-number.x"));
		ssgui.setList_itemnumber_y(config.getInt("VexFxShop.PlayerShop-list.item-number.y"));
		ssgui.setList_itemnumber_b(config.getInt("VexFxShop.PlayerShop-list.item-number.b"));
		ssgui.setList_itemnumber_fontsize(config.getDouble("VexFxShop.PlayerShop-list.item-number.fontsize"));
		/* 购买按钮 */
		ssgui.setList_itembuy_text(config.getString("VexFxShop.PlayerShop-list.item-buy.text"));
		ssgui.setList_itembuy_image1(
				config.getString("VexFxShop.PlayerShop-list.item-buy.image1").replace("[local]", Path));
		ssgui.setList_itembuy_image2(
				config.getString("VexFxShop.PlayerShop-list.item-buy.image2").replace("[local]", Path));
		ssgui.setList_itembuy_x(config.getInt("VexFxShop.PlayerShop-list.item-buy.x"));
		ssgui.setList_itembuy_y(config.getInt("VexFxShop.PlayerShop-list.item-buy.y"));
		ssgui.setList_itembuy_w(config.getInt("VexFxShop.PlayerShop-list.item-buy.w"));
		ssgui.setList_itembuy_h(config.getInt("VexFxShop.PlayerShop-list.item-buy.h"));
		ssgui.setList_itembuy_b(config.getInt("VexFxShop.PlayerShop-list.item-buy.b"));

		Main.syshopg.add(ssgui);

		UpitemData ups = new UpitemData();
		config = YamlConfiguration.loadConfiguration(new File(Main.instance.getDataFolder() + "/Gui/", "upitem.yml"));
		/* 背景 */
		ups.setBg_image(config.getString("VexFxShop.Upitem-bg.image").replace("[local]", Path));
		ups.setBg_x(config.getInt("VexFxShop.Upitem-bg.x"));
		ups.setBg_y(config.getInt("VexFxShop.Upitem-bg.y"));
		ups.setBg_w(config.getInt("VexFxShop.Upitem-bg.w"));
		ups.setBg_h(config.getInt("VexFxShop.Upitem-bg.h"));
		ups.setBg_xs(config.getInt("VexFxShop.Upitem-bg.xs"));
		ups.setBg_ys(config.getInt("VexFxShop.Upitem-bg.ys"));
		ups.setBg_slotLeft(config.getInt("VexFxShop.Upitem-bg.slotLeft"));
		ups.setBg_slotTop(config.getInt("VexFxShop.Upitem-bg.slotTop"));
		/* 插槽 */
		ups.setSlot_x(config.getInt("VexFxShop.Upitem-Slot.x"));
		ups.setSlot_y(config.getInt("VexFxShop.Upitem-Slot.y"));
		/* 单价 */
		ups.setDj_show_text(config.getString("VexFxShop.Upitem-dj-show.text"));
		ups.setDj_show_x(config.getInt("VexFxShop.Upitem-dj-show.x"));
		ups.setDj_show_y(config.getInt("VexFxShop.Upitem-dj-show.y"));
		ups.setDj_show_fontsize(config.getDouble("VexFxShop.Upitem-dj-show.fontsize"));
		/* 输入框 */
		ups.setInput_x(config.getInt("VexFxShop.Upitem-Input.x"));
		ups.setInput_y(config.getInt("VexFxShop.Upitem-Input.y"));
		ups.setInput_w(config.getInt("VexFxShop.Upitem-Input.w"));
		ups.setInput_h(config.getInt("VexFxShop.Upitem-Input.h"));
		/* 关闭按钮 */
		ups.setExit_image1(config.getString("VexFxShop.Upitem-exit.image1").replace("[local]", Path));
		ups.setExit_image2(config.getString("VexFxShop.Upitem-exit.image2").replace("[local]", Path));
		ups.setExit_x(config.getInt("VexFxShop.Upitem-exit.x"));
		ups.setExit_y(config.getInt("VexFxShop.Upitem-exit.y"));
		ups.setExit_w(config.getInt("VexFxShop.Upitem-exit.w"));
		ups.setExit_h(config.getInt("VexFxShop.Upitem-exit.h"));
		/* 价格（金币） */
		ups.setMoney_text(config.getString("VexFxShop.Upitem-money-text.text"));
		ups.setMoney_text_x(config.getInt("VexFxShop.Upitem-money-text.x"));
		ups.setMoney_text_y(config.getInt("VexFxShop.Upitem-money-text.y"));
		ups.setMoney_text_fontsize(config.getDouble("VexFxShop.Upitem-money-text.fontsize"));

		ups.setMoney_image1(config.getString("VexFxShop.Upitem-money.image1").replace("[local]", Path));
		ups.setMoney_image2(config.getString("VexFxShop.Upitem-money.image2").replace("[local]", Path));
		ups.setMoney_x(config.getInt("VexFxShop.Upitem-money.x"));
		ups.setMoney_y(config.getInt("VexFxShop.Upitem-money.y"));
		ups.setMoney_w(config.getInt("VexFxShop.Upitem-money.w"));
		ups.setMoney_h(config.getInt("VexFxShop.Upitem-money.h"));
		/* 价格（点券） */
		ups.setPoint_text(config.getString("VexFxShop.Upitem-point-text.text"));
		ups.setPoint_text_x(config.getInt("VexFxShop.Upitem-point-text.x"));
		ups.setPoint_text_y(config.getInt("VexFxShop.Upitem-point-text.y"));
		ups.setPoint_text_fontsize(config.getDouble("VexFxShop.Upitem-point-text.fontsize"));

		ups.setPoint_image1(config.getString("VexFxShop.Upitem-point.image1").replace("[local]", Path));
		ups.setPoint_image2(config.getString("VexFxShop.Upitem-point.image2").replace("[local]", Path));
		ups.setPoint_x(config.getInt("VexFxShop.Upitem-point.x"));
		ups.setPoint_y(config.getInt("VexFxShop.Upitem-point.y"));
		ups.setPoint_w(config.getInt("VexFxShop.Upitem-point.w"));
		ups.setPoint_h(config.getInt("VexFxShop.Upitem-point.h"));
		/* 无限 */
		ups.setUn_text(config.getString("VexFxShop.Upitem-un-text.text"));
		ups.setUn_text_x(config.getInt("VexFxShop.Upitem-un-text.x"));
		ups.setUn_text_y(config.getInt("VexFxShop.Upitem-un-text.y"));
		ups.setUn_text_fontsize(config.getDouble("VexFxShop.Upitem-un-text.fontsize"));

		ups.setUn_image1(config.getString("VexFxShop.Upitem-un.image1").replace("[local]", Path));
		ups.setUn_image2(config.getString("VexFxShop.Upitem-un.image2").replace("[local]", Path));
		ups.setUn_x(config.getInt("VexFxShop.Upitem-un.x"));
		ups.setUn_y(config.getInt("VexFxShop.Upitem-un.y"));
		ups.setUn_w(config.getInt("VexFxShop.Upitem-un.w"));
		ups.setUn_h(config.getInt("VexFxShop.Upitem-un.h"));
		/* 上架 */
		ups.setOk_text(config.getString("VexFxShop.Upitem-ok.text"));
		ups.setOk_image1(config.getString("VexFxShop.Upitem-ok.image1").replace("[local]", Path));
		ups.setOk_image2(config.getString("VexFxShop.Upitem-ok.image2").replace("[local]", Path));
		ups.setOk_x(config.getInt("VexFxShop.Upitem-ok.x"));
		ups.setOk_y(config.getInt("VexFxShop.Upitem-ok.y"));
		ups.setOk_w(config.getInt("VexFxShop.Upitem-ok.w"));
		ups.setOk_h(config.getInt("VexFxShop.Upitem-ok.h"));

		Main.upd = ups;

		BuyItemData bys = new BuyItemData();
		config = YamlConfiguration.loadConfiguration(new File(Main.instance.getDataFolder() + "/Gui/", "buyitem.yml"));

		bys.setBg_image(config.getString("VexFxShop.Buyitem-bg.image").replace("[local]", Path));
		bys.setBg_x(config.getInt("VexFxShop.Buyitem-bg.x"));
		bys.setBg_y(config.getInt("VexFxShop.Buyitem-bg.y"));
		bys.setBg_w(config.getInt("VexFxShop.Buyitem-bg.w"));
		bys.setBg_h(config.getInt("VexFxShop.Buyitem-bg.h"));
		bys.setBg_xs(config.getInt("VexFxShop.Buyitem-bg.xs"));
		bys.setBg_ys(config.getInt("VexFxShop.Buyitem-bg.ys"));

		bys.setItemstack_image(config.getString("VexFxShop.Vexitemstack.image").replace("[local]", Path));
		bys.setItemstack_h(config.getInt("VexFxShop.Vexitemstack.h"));

		bys.setSlot_image(config.getString("VexFxShop.Buyitem-Slot-image.image").replace("[local]", Path));
		bys.setSlot_image_x(config.getInt("VexFxShop.Buyitem-Slot-image.x"));
		bys.setSlot_image_y(config.getInt("VexFxShop.Buyitem-Slot-image.y"));
		bys.setSlot_image_w(config.getInt("VexFxShop.Buyitem-Slot-image.w"));
		bys.setSlot_image_h(config.getInt("VexFxShop.Buyitem-Slot-image.h"));

		bys.setSlot_x(config.getInt("VexFxShop.Buyitem-Slot.x"));
		bys.setSlot_y(config.getInt("VexFxShop.Buyitem-Slot.y"));

		bys.setName_text(config.getString("VexFxShop.Buyitem-name.text"));
		bys.setName_x(config.getInt("VexFxShop.Buyitem-name.x"));
		bys.setName_y(config.getInt("VexFxShop.Buyitem-name.y"));
		bys.setName_fontsize(config.getDouble("VexFxShop.Buyitem-name.fontsize"));

		bys.setNumber_text(config.getString("VexFxShop.Buyitem-number.text"));
		bys.setNumber_x(config.getInt("VexFxShop.Buyitem-number.x"));
		bys.setNumber_y(config.getInt("VexFxShop.Buyitem-number.y"));
		bys.setNumber_fontsize(config.getDouble("VexFxShop.Buyitem-number.fontsize"));

		bys.setMoney_text(config.getString("VexFxShop.Buyitem-money.text"));
		bys.setMoney_text_suffix(config.getString("VexFxShop.Buyitem-money.suffix"));
		bys.setMoney_x(config.getInt("VexFxShop.Buyitem-money.x"));
		bys.setMoney_y(config.getInt("VexFxShop.Buyitem-money.y"));
		bys.setMoney_fontsize(config.getDouble("VexFxShop.Buyitem-money.fontsize"));

		bys.setPoint_text(config.getString("VexFxShop.Buyitem-point.text"));
		bys.setPoint_text_suffix(config.getString("VexFxShop.Buyitem-point.suffix"));
		bys.setPoint_x(config.getInt("VexFxShop.Buyitem-point.x"));
		bys.setPoint_y(config.getInt("VexFxShop.Buyitem-point.y"));
		bys.setPoint_fontsize(config.getDouble("VexFxShop.Buyitem-point.fontsize"));

		bys.setBuytext_text(config.getString("VexFxShop.Buyitem-text.text"));
		bys.setBuytext_x(config.getInt("VexFxShop.Buyitem-text.x"));
		bys.setBuytext_y(config.getInt("VexFxShop.Buyitem-text.y"));
		bys.setBuytext_fontsize(config.getDouble("VexFxShop.Buyitem-text.fontsize"));

		bys.setInput_x(config.getInt("VexFxShop.Buyitem-input.x"));
		bys.setInput_y(config.getInt("VexFxShop.Buyitem-input.y"));
		bys.setInput_w(config.getInt("VexFxShop.Buyitem-input.w"));
		bys.setInput_h(config.getInt("VexFxShop.Buyitem-input.h"));

		bys.setBack_text(config.getString("VexFxShop.Buyitem-back.text"));
		bys.setBack_image1(config.getString("VexFxShop.Buyitem-back.image1").replace("[local]", Path));
		bys.setBack_image2(config.getString("VexFxShop.Buyitem-back.image2").replace("[local]", Path));
		bys.setBack_x(config.getInt("VexFxShop.Buyitem-back.x"));
		bys.setBack_y(config.getInt("VexFxShop.Buyitem-back.y"));
		bys.setBack_w(config.getInt("VexFxShop.Buyitem-back.w"));
		bys.setBack_h(config.getInt("VexFxShop.Buyitem-back.h"));

		bys.setDown_text(config.getString("VexFxShop.Buyitem-down.text"));
		bys.setDown_image1(config.getString("VexFxShop.Buyitem-down.image1").replace("[local]", Path));
		bys.setDown_image2(config.getString("VexFxShop.Buyitem-down.image2").replace("[local]", Path));
		bys.setDown_x(config.getInt("VexFxShop.Buyitem-down.x"));
		bys.setDown_y(config.getInt("VexFxShop.Buyitem-down.y"));
		bys.setDown_w(config.getInt("VexFxShop.Buyitem-down.w"));
		bys.setDown_h(config.getInt("VexFxShop.Buyitem-down.h"));

		bys.setBuy_text(config.getString("VexFxShop.Buyitem-buy.text"));
		bys.setBuy_image1(config.getString("VexFxShop.Buyitem-buy.image1").replace("[local]", Path));
		bys.setBuy_image2(config.getString("VexFxShop.Buyitem-buy.image2").replace("[local]", Path));
		bys.setBuy_x(config.getInt("VexFxShop.Buyitem-buy.x"));
		bys.setBuy_y(config.getInt("VexFxShop.Buyitem-buy.y"));
		bys.setBuy_w(config.getInt("VexFxShop.Buyitem-buy.w"));
		bys.setBuy_h(config.getInt("VexFxShop.Buyitem-buy.h"));

		Main.byd = bys;
	}

}
