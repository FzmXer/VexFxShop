package FzmXer.VexFxShop.MySQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import org.bukkit.ChatColor;

import FzmXer.VexFxShop.Data.Custem_Data;
import FzmXer.VexFxShop.SQLite.SQLManager;
import FzmXer.VexFxShop.Utils.Utils;

public class CustemManager {

	public static void CreateCustem() {
		try {
			Connection conn = MySQLManager.linkConnection();
			Statement stmt = conn.createStatement();
			/* 执行SQL语句 */
			stmt.execute(Utils.CName);
			/* 关闭数据库连接 */
			MySQLManager.closeConnection();
		} catch (SQLException e) {
			Utils.Msg(ChatColor.RED + "执行 MySQL 语句失败！");
		}
	}
	
	public static void CreateCustem_sql() {
		try {
			Connection conn = SQLManager.getConnection();
			Statement stmt = conn.createStatement();
			/* 执行SQL语句 */
			stmt.execute(Utils.CName_sql);
			/* 关闭数据库连接 */
			MySQLManager.closeConnection();
		} catch (SQLException e) {
			Utils.Msg(ChatColor.RED + "执行 SQLite 语句失败！");
		}
	}
	
	public static String AddItemName(String e_n, String c_n) {
		if (GetItemName(e_n) == null) {
			try {
				/* 获取数据库的连接 */
				Connection conn = MySQLManager.linkConnection();
				String sql = "insert into custem_name(E_Name, C_Name) values (?, ?)";
				PreparedStatement presta;
				presta = conn.prepareStatement(sql);
				presta.setString(1, e_n);
				presta.setString(2, c_n);
				presta.execute();
				MySQLManager.closeConnection();
				return ChatColor.GREEN + "添加物品名成功！";
			} catch (SQLException e) {
				Utils.Msg(ChatColor.RED + "添加物品名失败！");
			}
		} else {
			return UpdateItemNmae(e_n, c_n);
		}
		return "";
	}

	public static String AddItemName_sql(String e_n, String c_n) {
		if (GetItemName_sql(e_n) == null) {
			try {
				/* 获取数据库的连接 */
				Connection conn = SQLManager.getConnection();
				String sql = "insert into custem_name(E_Name, C_Name) values (?, ?)";
				PreparedStatement presta;
				presta = conn.prepareStatement(sql);
				presta.setString(1, e_n);
				presta.setString(2, c_n);
				presta.execute();
				MySQLManager.closeConnection();
				return ChatColor.GREEN + "添加物品名成功！";
			} catch (SQLException e) {
				Utils.Msg(ChatColor.RED + "添加物品名失败！");
			}
		} else {
			return UpdateItemNmae_sql(e_n, c_n);
		}
		return "";
	}
	
	public static String QueryItemName(String e_n) {
		try {
			/* 获取数据库的连接 */
			Connection conn = MySQLManager.linkConnection();
			String sql = "SELECT * FROM custem_name where E_name='" + e_n + "'";
			PreparedStatement presta;
			presta = conn.prepareStatement(sql);
			presta.execute();
			ResultSet rs = presta.getResultSet();
			while (rs.next()) {
				return rs.getString("C_name") == e_n ? "null" : rs.getString("C_name");
			}
			MySQLManager.closeConnection();
		} catch (SQLException e) {
			Utils.Msg(ChatColor.RED + "查询物品名失败！");
		}
		return "null";
	}

	public static String QueryItemName_sql(String e_n) {
		try {
			/* 获取数据库的连接 */
			Connection conn = SQLManager.getConnection();
			String sql = "SELECT * FROM custem_name where E_name='" + e_n + "'";
			PreparedStatement presta;
			presta = conn.prepareStatement(sql);
			presta.execute();
			ResultSet rs = presta.getResultSet();
			while (rs.next()) {
				return rs.getString("C_name") == e_n ? "null" : rs.getString("C_name");
			}
			MySQLManager.closeConnection();
		} catch (SQLException e) {
			Utils.Msg(ChatColor.RED + "查询物品名失败！");
		}
		return "null";
	}
	
	public static String UpdateItemNmae(String e_n, String c_n) {
		try {
			/* 获取数据库的连接 */
			Connection conn = MySQLManager.linkConnection();
			String sql = "update custem_name set E_name=?, C_name=? where E_name='" + e_n + "'";
			PreparedStatement presta;
			presta = conn.prepareStatement(sql);
			presta.setString(1, e_n);
			presta.setString(2, c_n);
			presta.execute();
			MySQLManager.closeConnection();
			return ChatColor.GREEN + "修改物品名成功！";
		} catch (SQLException e) {
			Utils.Msg(ChatColor.RED + "修改物品名失败！");
		}
		return "";
	}

	public static String UpdateItemNmae_sql(String e_n, String c_n) {
		try {
			/* 获取数据库的连接 */
			Connection conn = SQLManager.getConnection();
			String sql = "update custem_name set E_name=?, C_name=? where E_name='" + e_n + "'";
			PreparedStatement presta;
			presta = conn.prepareStatement(sql);
			presta.setString(1, e_n);
			presta.setString(2, c_n);
			presta.execute();
			MySQLManager.closeConnection();
			return ChatColor.GREEN + "修改物品名成功！";
		} catch (SQLException e) {
			Utils.Msg(ChatColor.RED + "修改物品名失败！");
		}
		return "";
	}
	
	public static Custem_Data GetItemName(String e_n) {
		try {
			Custem_Data info = null;
			/* 获取数据库的连接 */
			Connection conn = MySQLManager.linkConnection();
			String sql = "SELECT * FROM custem_name where E_name='" + e_n + "'";
			PreparedStatement presta;
			presta = conn.prepareStatement(sql);
			presta.execute();
			ResultSet rs = presta.getResultSet();
			// 遍历结果集数据库
			while (rs.next()) {
				info = new Custem_Data(rs.getString(1), rs.getString(2));
			}
			MySQLManager.closeConnection();
			return info;
		} catch (SQLException e) {
			Utils.Msg(ChatColor.RED + "获取物品名失败！");
			e.printStackTrace();
		}
		return null;
	}
	
	public static Custem_Data GetItemName_sql(String e_n) {
		try {
			Custem_Data info = null;
			/* 获取数据库的连接 */
			Connection conn = SQLManager.getConnection();
			String sql = "SELECT * FROM custem_name where E_name='" + e_n + "'";
			PreparedStatement presta;
			presta = conn.prepareStatement(sql);
			presta.execute();
			ResultSet rs = presta.getResultSet();
			// 遍历结果集数据库
			while (rs.next()) {
				info = new Custem_Data(rs.getString(1), rs.getString(2));
			}
			MySQLManager.closeConnection();
			return info;
		} catch (SQLException e) {
			Utils.Msg(ChatColor.RED + "获取物品名失败！");
			e.printStackTrace();
		}
		return null;
	}

	public static String DelItemName(String e_n) {
		try {
			/* 获取数据库的连接 */
			Connection conn = MySQLManager.linkConnection();
			String sql = "delete from custem_name where E_name='" + e_n + "'";
			PreparedStatement presta;
			presta = conn.prepareStatement(sql);
			presta.execute();
			MySQLManager.closeConnection();
			return ChatColor.GREEN + "删除物品名成功！";
		} catch (SQLException e) {
			Utils.Msg(ChatColor.RED + "删除物品名失败！");
		}
		return "";
	}
	
	public static String DelItemName_sql(String e_n) {
		try {
			/* 获取数据库的连接 */
			Connection conn = SQLManager.getConnection();
			String sql = "delete from custem_name where E_name='" + e_n + "'";
			PreparedStatement presta;
			presta = conn.prepareStatement(sql);
			presta.execute();
			MySQLManager.closeConnection();
			return ChatColor.GREEN + "删除物品名成功！";
		} catch (SQLException e) {
			Utils.Msg(ChatColor.RED + "删除物品名失败！");
		}
		return "";
	}
	
	public static Vector<Custem_Data> GetItemName_All() {
		try {
			Vector<Custem_Data> datas = new Vector<Custem_Data>();
			Custem_Data info = null;
			/* 获取数据库的连接 */
			Connection conn = MySQLManager.linkConnection();
			String sql = "SELECT * FROM custem_name";
			/* 预处理sql语句 */
			PreparedStatement presta = conn.prepareStatement(sql);
			presta.execute();
			ResultSet rs = presta.getResultSet();
			// 遍历结果集数据库
			while (rs.next()) {
				info = new Custem_Data(rs.getString(1), rs.getString(2));

				datas.add(info);
			}
			MySQLManager.closeConnection();
			return datas;
		} catch (SQLException e) {
			Utils.Msg(ChatColor.RED + "获取物品名信息失败！");
		}
		return null;
	}
	
	public static Vector<Custem_Data> GetItemName_All_sql() {
		try {
			Vector<Custem_Data> datas = new Vector<Custem_Data>();
			Custem_Data info = null;
			/* 获取数据库的连接 */
			Connection conn = SQLManager.getConnection();
			String sql = "SELECT * FROM custem_name";
			/* 预处理sql语句 */
			PreparedStatement presta = conn.prepareStatement(sql);
			presta.execute();
			ResultSet rs = presta.getResultSet();
			// 遍历结果集数据库
			while (rs.next()) {
				info = new Custem_Data(rs.getString(1), rs.getString(2));

				datas.add(info);
			}
			MySQLManager.closeConnection();
			return datas;
		} catch (SQLException e) {
			Utils.Msg(ChatColor.RED + "获取物品名信息失败！");
		}
		return null;
	}
	
}
