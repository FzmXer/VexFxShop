package FzmXer.VexFxShop.SQLite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.bukkit.ChatColor;

import FzmXer.VexFxShop.Data.ItemInfo;
import FzmXer.VexFxShop.Main.Main;
import FzmXer.VexFxShop.MySQL.MySQLManager;
import FzmXer.VexFxShop.Utils.Utils;

public class SQLManager {

	private static Connection conn;

	public SQLManager() {
		try {
			Class.forName("org.sqlite.JDBC");
			conn = getConnection();
			Utils.Msg(ChatColor.GREEN + " [VexFxShop] SQLite 初始化成功！");
		} catch (ClassNotFoundException e) {
			Utils.Msg(ChatColor.RED + " 载入SQLite驱动失败！");
		}

	}

	public static Connection getConnection() {
		try {
			if (conn == null) {
				conn = DriverManager.getConnection("jdbc:sqlite:plugins/VexFxShop/VexFxShop.db");
			}
			return conn;
		} catch (SQLException e) {
			Utils.Msg(ChatColor.RED + "连接 SQLite 数据库失败，详细信息：" + e.getErrorCode());
		}
		return null;
	}

	public static boolean CreateTable(String shopname) {
		try {
			/* 创建表 */
			String sql = Utils.getSQLiteTable(shopname);
			conn = getConnection();
			PreparedStatement presta = conn.prepareStatement(sql);
			return presta.execute();
		} catch (SQLException e) {
			Utils.Msg(ChatColor.RED + "创建 " + shopname +" 数据表失败，详细信息：" + e.getErrorCode() + "\n" + e.getMessage());
		}
		return false;
	}

	public static boolean AddItem(ItemInfo info) {
		try {

			String sql = "INSERT INTO " + info.getShop_Name() + Utils.Table_Pres;
			conn = getConnection();
			PreparedStatement presta = conn.prepareStatement(sql);
			
			presta.setString(1, info.getItem_Type());
			presta.setString(2, info.getItem_Name());
			presta.setString(3, info.getItem_Lore());
			presta.setString(4, info.getItem_Nbts());
			presta.setString(5, info.getItem_Enchants());

			presta.setInt(6, info.getItem_Durability());
			presta.setInt(7, info.getItem_Money());
			presta.setInt(8, info.getItem_Point());

			presta.setString(9, info.getItem_Dates());
			presta.setString(10, info.getPlayer_Name());
			presta.setString(11, info.getPlayer_UUID());
			presta.setString(12, info.getShop_Name());

			presta.setInt(13, info.getItem_Number());
			
			presta.execute();
			return true;
		} catch (SQLException e) {
			Utils.Msg(ChatColor.RED + "插入数据失败，详细信息：" + e.getErrorCode() + "\n" + e.getMessage());
		}

		return false;
	}
	
	public static boolean BuyItems(ItemInfo infos, int BuyNumber) {
		try {
			/* 获取数据库的连接 */
			Connection conn = getConnection();
			String sql = "SELECT * FROM " + infos.getShop_Name() + " where item_type='" + infos.getItem_Type()
					+ "'and item_dates='" + infos.getItem_Dates() + "'and player_name='" + infos.getPlayer_Name()
					+ "'and player_uuid='" + infos.getPlayer_UUID() + "'";
			/* 预处理sql语句 */
			PreparedStatement presta = conn.prepareStatement(sql);
			presta.execute();
			ResultSet rs = presta.getResultSet();
			while (rs.next()) {
				if (rs.getInt("item_number") - BuyNumber <= 0) {
					if(rs.getInt("item_number") != -1) {
						return DelItems(rs.getInt("id"), infos.getShop_Name());
					}
					return true;
				} else {
					sql = "UPDATE " + infos.getShop_Name() + " SET item_number='" + (infos.getItem_Number() - BuyNumber)
							+ "' where id=" + rs.getInt("id") + "";
					/* 预处理sql语句 */
					presta = conn.prepareStatement(sql);
					presta.execute();
					/* 重载商品链表 */
					Main.reloadItemInfo_sql(infos.getShop_Name());
					return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean BuyItems_2(ItemInfo infos) {
		try {
			/* 获取数据库的连接 */
			Connection conn = getConnection();
			String sql = "SELECT * FROM " + infos.getShop_Name() + " where item_type='" + infos.getItem_Type()
					+ "'and item_dates='" + infos.getItem_Dates() + "'and player_name='" + infos.getPlayer_Name()
					+ "'and player_uuid='" + infos.getPlayer_UUID() + "'";
			/* 预处理sql语句 */
			PreparedStatement presta = conn.prepareStatement(sql);
			presta.execute();
			ResultSet rs = presta.getResultSet();
			while (rs.next()) {
				sql = "UPDATE " + infos.getShop_Name() + " SET item_number='" + infos.getItem_Number() + "' where id="
						+ rs.getInt("id") + "";
				/* 预处理sql语句 */
				presta = conn.prepareStatement(sql);
				presta.execute();
				/* 重载商品链表 */
				Main.reloadItemInfo_sql(infos.getShop_Name());
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 删除 or 下架商品
	 */
	public static boolean DelItems(int id, String shopname) {
		try {
			/* 获取数据库的连接 */
			Connection conn = getConnection();
			String sql = "delete from " + shopname + " where id=?";
			/* 预处理sql语句 */
			PreparedStatement presta = conn.prepareStatement(sql);
			presta.setInt(1, id);
			/* 执行SQL语句，实现数据删除 */
			presta.execute();
			/* 重载商品链表 */
			Main.reloadItemInfo_sql(shopname);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static Vector<ItemInfo> GetItems_All(String shopname) {
		try {
			Vector<ItemInfo> datas = new Vector<ItemInfo>();
			ItemInfo info = null;
			/* 获取数据库的连接 */
			Connection conn = SQLManager.getConnection();
			String sql = "SELECT * FROM " + shopname;
			/* 预处理sql语句 */
			PreparedStatement presta = conn.prepareStatement(sql);
			presta.execute();
			ResultSet rs = presta.getResultSet();
			int x = 0;
			// 遍历结果集数据库
			while (rs.next()) {
				info = new ItemInfo();

				info.setItem_Type(rs.getString("item_type"));
				info.setItem_Name(rs.getString("item_name"));
				info.setItem_Lore(rs.getString("item_lore"));
				info.setNbt_Id(x);
				info.setItem_Nbts(rs.getString("item_nbts"));
				info.setItem_Enchants(rs.getString("item_enchants"));
				info.setItem_Durability(rs.getInt("item_durability"));
				info.setItem_Money(rs.getInt("item_money"));
				info.setItem_Point(rs.getInt("item_point"));
				info.setItem_Dates(rs.getString("item_dates"));
				info.setPlayer_Name(rs.getString("player_name"));
				info.setPlayer_UUID(rs.getString("player_uuid"));
				info.setShop_Name(rs.getString("shop_name"));
				info.setItem_Number(rs.getInt("item_number"));

				datas.add(info);
			}
			MySQLManager.closeConnection();
			return datas;
		} catch (SQLException e) {
			Utils.Msg(ChatColor.RED + "读取商品信息失败！");
		}
		return null;
	}
	
}
