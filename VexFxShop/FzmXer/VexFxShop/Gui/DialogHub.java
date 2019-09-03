package FzmXer.VexFxShop.Gui;

import java.util.Arrays;

import org.bukkit.entity.Player;

import lk.vexview.api.VexViewAPI;
import lk.vexview.hud.VexShow;
import lk.vexview.hud.VexTextShow;

public class DialogHub {

	public DialogHub(Player player, String text) {
		VexShow vs = new VexTextShow("Dialog", -1, 20, Arrays.asList(text), 2);
		VexViewAPI.sendHUD(player, vs);
	}

}
