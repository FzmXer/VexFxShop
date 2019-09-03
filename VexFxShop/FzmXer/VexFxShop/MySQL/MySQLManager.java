package FzmXer.VexFxShop.MySQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.bukkit.ChatColor;

import FzmXer.VexFxShop.Main.Main;
import FzmXer.VexFxShop.Utils.Utils;

public class MySQLManager {
	private static Connection conn;

	private static MySQLInfo mysqlinfo = null;

	public MySQLManager(Main main, MySQLInfo info) {
		try {
			/* 初始化数据库驱动 */
			Class.forName("com.mysql.jdbc.Driver");
			MySQLManager.mysqlinfo = info;
			conn = linkConnection();
			/* 关闭数据库连接 */
			closeConnection();
			Utils.Msg(ChatColor.GREEN + " [VexFxShop] MySQL 初始化成功！");
		} catch (ClassNotFoundException e) {
			Utils.Msg(ChatColor.RED + "加载 MySQL 驱动出错！");
		}

	}

	public static Connection linkConnection() {
		try {
			/* 连接数据库 */
			Connection conns = DriverManager
					.getConnection(
							"jdbc:mysql://" + mysqlinfo.getIp() + ":" + mysqlinfo.getPort() + "/"
									+ mysqlinfo.getDatabaseName() + "?useSSL=false&serverTimezone=UTC",
							mysqlinfo.getUsername(), mysqlinfo.getPassword());
			//Utils.Msg(ChatColor.GREEN + "连接 MySQL 数据库成功！");
			return conns;
		} catch (SQLException e) {
			Utils.Msg(ChatColor.RED + "连接 MySQL 数据库失败！");
		}
		return null;
	}

	/**
	 * 关闭数据库连接
	 */
	public static void closeConnection() {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				Utils.Msg(ChatColor.RED + "断开 MySQL 数据库失败！");
			}
		}
	}


}
