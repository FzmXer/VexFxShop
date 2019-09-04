package FzmXer.VexFxShop.MySQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.bukkit.ChatColor;

import FzmXer.VexFxShop.Data.ItemInfo;
import FzmXer.VexFxShop.Main.Main;
import FzmXer.VexFxShop.Utils.Utils;

public class ShopItemManager {

	public static void Create_table(String str) {
		try {
			Connection conn = MySQLManager.linkConnection();
			Statement stmt = conn.createStatement();
			/* 执行SQL语句 */
			stmt.execute(Utils.getMySQLTable(str));
			/* 关闭数据库连接 */
			MySQLManager.closeConnection();
		} catch (SQLException e) {
			Utils.Msg(ChatColor.RED + "执行 MySQL 语句失败！");
		}
	}

	public static boolean AddItems(ItemInfo infos) {
		try {
			/* 获取数据库的连接 */
			Connection conn = MySQLManager.linkConnection();
			String Sql = "insert into " + infos.getShop_Name() + Utils.Table_Pres;
			PreparedStatement presta = conn.prepareStatement(Sql);
			presta.setString(1, infos.getItem_Type());
			presta.setString(2, infos.getItem_Name());
			presta.setString(3, infos.getItem_Lore());
			presta.setString(4, infos.getItem_Nbts());
			presta.setString(5, infos.getItem_Enchants());

			presta.setInt(6, infos.getItem_Durability());
			presta.setInt(7, infos.getItem_Money());
			presta.setInt(8, infos.getItem_Point());

			presta.setString(9, infos.getItem_Dates());
			presta.setString(10, infos.getPlayer_Name());
			presta.setString(11, infos.getPlayer_UUID());
			presta.setString(12, infos.getShop_Name());

			presta.setInt(13, infos.getItem_Number());

			presta.execute();
			return true;
		} catch (SQLException e) {
			Utils.Msg(ChatColor.RED + "添加商品失败，详细信息：" + e.getErrorCode());
			e.printStackTrace();
		}
		return false;
	}

	public static boolean BuyItems(ItemInfo infos, int BuyNumber) {
		try {
			/* 获取数据库的连接 */
			Connection conn = MySQLManager.linkConnection();
			String sql = "SELECT * FROM " + infos.getShop_Name() + " where item_type='" + infos.getItem_Type()
					+ "'and item_dates='" + infos.getItem_Dates() + "'and player_name='" + infos.getPlayer_Name()
					+ "'and player_uuid='" + infos.getPlayer_UUID() + "'";
			/* 预处理sql语句 */
			PreparedStatement presta = conn.prepareStatement(sql);
			presta.execute();
			ResultSet rs = presta.getResultSet();
			while (rs.next()) {
				if (rs.getInt("item_number") - BuyNumber <= 0) {
					if (rs.getInt("item_number") != -1) {
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
					Main.reloadItemInfo(infos.getShop_Name());
					return true;
				}
			}
			MySQLManager.closeConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean BuyItems_down(ItemInfo infos, int BuyNumber) {
		try {
			/* 获取数据库的连接 */
			Connection conn = MySQLManager.linkConnection();
			String sql = "SELECT * FROM " + infos.getShop_Name() + " where item_type='" + infos.getItem_Type()
					+ "'and item_dates='" + infos.getItem_Dates() + "'and player_name='" + infos.getPlayer_Name()
					+ "'and player_uuid='" + infos.getPlayer_UUID() + "'";
			/* 预处理sql语句 */
			PreparedStatement presta = conn.prepareStatement(sql);
			presta.execute();
			ResultSet rs = presta.getResultSet();
			while (rs.next()) {
				return DelItems(rs.getInt("id"), infos.getShop_Name());
			}
			MySQLManager.closeConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean BuyItems_2(ItemInfo infos) {
		try {
			/* 获取数据库的连接 */
			Connection conn = MySQLManager.linkConnection();
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
				Main.reloadItemInfo(infos.getShop_Name());
				return true;
			}
			MySQLManager.closeConnection();
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
			Connection conn = MySQLManager.linkConnection();
			String sql = "delete from " + shopname + " where id=?";
			/* 预处理sql语句 */
			PreparedStatement presta = conn.prepareStatement(sql);
			presta.setInt(1, id);
			/* 执行SQL语句，实现数据删除 */
			presta.execute();
			/* 重载商品链表 */
			Main.reloadItemInfo(shopname);
			MySQLManager.closeConnection();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static ArrayList<ItemInfo> GetItems_All(String shopname) {
		try {
			ArrayList<ItemInfo> datas = new ArrayList<ItemInfo>();
			ItemInfo info = null;
			/* 获取数据库的连接 */
			Connection conn = MySQLManager.linkConnection();
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
