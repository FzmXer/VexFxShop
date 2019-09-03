package FzmXer.VexFxShop.Main;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import FzmXer.VexFxShop.Gui.PlayerShopGui;
import FzmXer.VexFxShop.Gui.SystemShopGui;
import lk.vexview.api.VexViewAPI;
import lk.vexview.event.KeyBoardPressEvent;

public class MyListener implements Listener {

	@EventHandler
	public void OnKey(KeyBoardPressEvent e) {
		new BukkitRunnable() {
			@Override
			public void run() {
				/* 按下时不触发，弹起时触发 */
				if (e.getEventKeyState()) {
					return;
				}
				if (Main.KeyFlag) {
					e.getPlayer().sendMessage("你按下了：" + e.getKey());
				} else {
					/* 如果没有打开GUI则触发 */
					if (e.getType().toString().equalsIgnoreCase("NOGUI")) {
						/* 玩家按下按键 */
						if (e.getKey() == Main.System_Key) {
							VexViewAPI.openGui(e.getPlayer(), new SystemShopGui(Main.instance, e.getPlayer()));
						} else if (e.getKey() == Main.Player_Key) {
							VexViewAPI.openGui(e.getPlayer(), new PlayerShopGui(Main.instance, e.getPlayer()));
						}
					}
				}
			}
		}.runTaskAsynchronously(Main.instance);
	}

}