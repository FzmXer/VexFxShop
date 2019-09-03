package FzmXer.VexFxShop.Utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

public class VaultApi {

	public static Economy econ = null;

	public static boolean setupEconomy() {
		RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServer().getServicesManager()
				.getRegistration(Economy.class);
		if (economyProvider != null) {
			econ = (Economy) economyProvider.getProvider();
		}
		return econ != null;
	}

	public static double getMoney(OfflinePlayer player) {
		if (econ == null) {
			throw new IllegalStateException();
		} else {
			return econ.getBalance(player);
		}
	}

	public static boolean hasMoney(OfflinePlayer player, double minimum) {
		if (econ == null) {
			Utils.Msg(ChatColor.RED + "调用经济API失败！");
			return false;
			// throw new IllegalStateException();
		} else if (minimum < 0.0D) {
			Utils.Msg(ChatColor.RED + "无效的金额: " + minimum);
			return false;
			// throw new IllegalArgumentException("无效的金额: " + minimum);
		} else {
			double balance = econ.getBalance(player);
			return balance >= minimum;
		}
	}

	public static boolean takeMoney(OfflinePlayer player, double amount) {
		if (econ == null) {
			throw new IllegalStateException();
		} else if (amount < 0.0D) {
			throw new IllegalArgumentException("无效的金额: " + amount);
		} else {
			EconomyResponse response = econ.withdrawPlayer(player, amount);
			boolean result = response.transactionSuccess();
			return result;
		}
	}

	public static boolean giveMoney(OfflinePlayer player, double amount) {
		if (econ == null) {
			throw new IllegalStateException();
		} else if (amount < 0.0D) {
			throw new IllegalArgumentException("无效的金额: " + amount);
		} else {
			EconomyResponse response = econ.depositPlayer(player, amount);
			boolean result = response.transactionSuccess();
			return result;
		}
	}

}
