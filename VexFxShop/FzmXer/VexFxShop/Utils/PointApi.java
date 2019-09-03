package FzmXer.VexFxShop.Utils;

import org.black_ixx.playerpoints.PlayerPoints;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

public class PointApi {
	public static PlayerPoints points = null;

	public static boolean hookPlayerPoints() {
		Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("PlayerPoints");
		points = (PlayerPoints) PlayerPoints.class.cast(plugin);
		return points != null;
	}

	public static boolean hasPoint(OfflinePlayer player, double minimum) {
		if (points == null) {
			throw new IllegalStateException();
		} else if (minimum < 0.0D) {
			throw new IllegalArgumentException("无效的金额: " + minimum);
		} else {
			return points.getAPI().look(player.getName()) >= minimum;
		}
	}

	public static int getPoints(OfflinePlayer player) {
		return points.getAPI().look(player.getName());
	}

	public static boolean setPoints(OfflinePlayer player, int point) {
		
		return points.getAPI().set(player.getName(), point);
	}

	public static boolean takePoints(OfflinePlayer player, int point) {
		return points.getAPI().take(player.getName(), point);
	}

	public static boolean givePoints(OfflinePlayer player, int point) {
		return points.getAPI().give(player.getName(), point);
	}

}
