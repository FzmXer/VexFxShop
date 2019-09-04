package FzmXer.VexFxShop.Gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import FzmXer.VexFxShop.Data.ItemInfo;
import FzmXer.VexFxShop.Main.Main;
import FzmXer.VexFxShop.MySQL.CustemManager;
import FzmXer.VexFxShop.Utils.PointApi;
import FzmXer.VexFxShop.Utils.Utils;
import FzmXer.VexFxShop.Utils.VaultApi;
import lk.vexview.api.VexViewAPI;
import lk.vexview.gui.VexGui;
import lk.vexview.gui.components.ButtonFunction;
import lk.vexview.gui.components.VexButton;
import lk.vexview.gui.components.VexComponents;
import lk.vexview.gui.components.VexImage;
import lk.vexview.gui.components.VexScrollingList;
import lk.vexview.gui.components.VexText;

public class PlayerShopGui extends VexGui {
	private List<VexComponents> vc;

	public PlayerShopGui(Main main, Player player) {
		/* 设置背景图片 */
		super(Utils.Path + Main.syshopg.get(1).getBg_image(), Main.syshopg.get(1).getBg_x(),
				Main.syshopg.get(1).getBg_y(), Main.syshopg.get(1).getBg_w(), Main.syshopg.get(1).getBg_h(),
				Main.syshopg.get(1).getBg_xs(), Main.syshopg.get(1).getBg_ys());
		vc = new ArrayList<VexComponents>();
		vc.add(new VexText(Main.syshopg.get(1).getTitle_x(), Main.syshopg.get(1).getTitle_y(),
				Arrays.asList(Main.syshopg.get(1).getTitle_text())));
		vc.add(new VexButton(0, "", Utils.Path + Main.syshopg.get(1).getExit_image1(),
				Utils.Path + Main.syshopg.get(1).getExit_image2(), Main.syshopg.get(1).getExit_x(),
				Main.syshopg.get(1).getExit_y(), Main.syshopg.get(1).getExit_w(), Main.syshopg.get(1).getExit_h(),
				new ButtonFunction() {
					@Override
					public void run(Player player) {
						/* 异步调用，减少卡服运算 */
						new BukkitRunnable() {
							@Override
							public void run() {
								player.closeInventory();
							}
						}.runTaskAsynchronously(main);
					}
				}));
		vc.add(new VexImage(Utils.Path + Main.syshopg.get(1).getInfo_money_image(),
				Main.syshopg.get(1).getInfo_money_x(), Main.syshopg.get(1).getInfo_money_y(),
				Main.syshopg.get(1).getInfo_money_w(), Main.syshopg.get(1).getInfo_money_h()));
		vc.add(new VexText(Main.syshopg.get(1).getInfo_money_text_x(), Main.syshopg.get(1).getInfo_money_text_y(),
				Arrays.asList(Main.syshopg.get(1).getInfo_money_text() + VaultApi.getMoney(player)),
				Main.syshopg.get(1).getInfo_money_text_fontsize()));
		vc.add(new VexImage(Utils.Path + Main.syshopg.get(1).getInfo_point_image(),
				Main.syshopg.get(1).getInfo_point_x(), Main.syshopg.get(1).getInfo_point_y(),
				Main.syshopg.get(1).getInfo_point_w(), Main.syshopg.get(1).getInfo_point_h()));
		vc.add(new VexText(Main.syshopg.get(1).getInfo_point_text_x(), Main.syshopg.get(1).getInfo_point_text_y(),
				Arrays.asList(Main.syshopg.get(1).getInfo_point_text() + PointApi.getPoints(player)),
				Main.syshopg.get(1).getInfo_point_text_fontsize()));

		/* 如果是OP，添加上架按钮 */
		if (player.isOp()) {
			/* 上架按钮 */
			vc.add(new VexButton(1, Main.syshopg.get(1).getUpitem_text(),
					Utils.Path + Main.syshopg.get(1).getUpitem_image1(),
					Utils.Path + Main.syshopg.get(1).getUpitem_image2(), Main.syshopg.get(1).getUpitem_x(),
					Main.syshopg.get(1).getUpitem_y(), Main.syshopg.get(1).getUpitem_w(),
					Main.syshopg.get(1).getUpitem_h(), new ButtonFunction() {
						@Override
						public void run(Player player) {
							/* 异步调用，减少卡服运算 */
							new BukkitRunnable() {
								@Override
								public void run() {
									VexViewAPI.openGui(player, new UpItemGui(main, player, "playershop"));
								}
							}.runTaskAsynchronously(main);
						}
					}));
		}

		/* 如果链表不为null */
		if (Main.playerlist != null) {
			/* 滚动列表 */
			VexScrollingList vsl = new VexScrollingList(Main.syshopg.get(1).getList_list_x(),
					Main.syshopg.get(1).getList_list_y(), Main.syshopg.get(1).getList_list_w(),
					Main.syshopg.get(1).getList_list_h(), Main.syshopg.get(1).getList_list_h()
							+ ((Main.playerlist.size() - 4) * Main.syshopg.get(1).getList_itembg_h()));
//			int size;
			int y = 0;
			for (ItemInfo info : Main.playerlist) {
				/* 商品背景图 */
				vsl.addComponent(new VexImage(Utils.Path + Main.syshopg.get(1).getList_itembg_image(),
						Main.syshopg.get(1).getList_itembg_x(), Main.syshopg.get(1).getList_itembg_y() * y,
						Main.syshopg.get(1).getList_itembg_w(), Main.syshopg.get(1).getList_itembg_h()));

//				/* 商品显示贴图 */
//				size = Main.syshopg.get(0).getList_item_size();
//				String images;
//				if (Main.syshopg.get(0).getList_item_image().contentEquals("minecraft:textures/items/")) {
//					images = "minecraft:textures/items/" + info.getItem_Type().toString().toLowerCase() + ".png";
//				} else {
//				images = Main.syshopg.get(0).getList_item_image();
//				}

//				vsl.addComponent(new VexMcImage(images, Main.syshopg.get(0).getList_item_x(),
//						Main.syshopg.get(0).getList_item_y() * y + Main.syshopg.get(0).getList_item_b(), size, size,
//						size, size, size, size, size, size));

//				vsl.addComponent(
//						new VexImage(Utils.Path + images + info.getItem_Type().toString().toLowerCase() + ".png", 6,
//								26 * y + Main.syshopg.get(0).getList_item_b(), size, size));

				/* 商品名 */
				String item_names = "null";
				if (Main.DataSaveType.equalsIgnoreCase("mysql")) {
					item_names = CustemManager.QueryItemName(Material.valueOf(info.getItem_Type()).name());
				} else {
					item_names = CustemManager.QueryItemName_sql(Material.valueOf(info.getItem_Type()).name());
				}

				if (item_names == "null") {
					item_names = info.getItem_Name();
				}
				vsl.addComponent(new VexText(Main.syshopg.get(1).getList_itemname_x(),
						Main.syshopg.get(1).getList_itemname_y() * y + Main.syshopg.get(1).getList_itemname_b(),
						Arrays.asList(Main.syshopg.get(1).getList_itemname_text() + "§f" + item_names),
						Main.syshopg.get(1).getList_itemname_fontsize()));
				/* 商品单价 */
				vsl.addComponent(new VexText(Main.syshopg.get(1).getList_itemdj_money_x(),
						Main.syshopg.get(1).getList_itemdj_money_y() * y + Main.syshopg.get(1).getList_itemdj_money_b(),
						Arrays.asList(Main.syshopg.get(1).getList_itemdj_money_text() + (info.getItem_Money())),
						Main.syshopg.get(1).getList_itemdj_money_fontsize()));
				vsl.addComponent(new VexText(Main.syshopg.get(1).getList_itemdj_point_x(),
						Main.syshopg.get(1).getList_itemdj_point_y() * y + Main.syshopg.get(1).getList_itemdj_point_b(),
						Arrays.asList(Main.syshopg.get(1).getList_itemdj_point_text() + (info.getItem_Point())),
						Main.syshopg.get(1).getList_itemdj_point_fontsize()));
				/* 商品上架玩家 */
				vsl.addComponent(new VexText(Main.syshopg.get(1).getList_itemsname_x(),
						Main.syshopg.get(1).getList_itemsname_y() * y + Main.syshopg.get(1).getList_itemsname_b(),
						Arrays.asList(Main.syshopg.get(1).getList_itemsname_text() + info.getPlayer_Name()),
						Main.syshopg.get(1).getList_itemsname_fontsize()));
				/* 商品上架时间 */
				vsl.addComponent(new VexText(Main.syshopg.get(1).getList_itemdate_x(),
						Main.syshopg.get(1).getList_itemdate_y() * y + Main.syshopg.get(1).getList_itemdate_b(),
						Arrays.asList(Main.syshopg.get(1).getList_itemdate_text()
								+ Utils.timeStamp2Date(info.getItem_Dates(), "yyyy-MM-dd")),
						Main.syshopg.get(1).getList_itemdate_fontsize()));
				/* 商品数量 */
				String number = info.getItem_Number() + "";
				if (info.getItem_Number() == -1) {
					number = "无限";
				}

				vsl.addComponent(new VexText(Main.syshopg.get(1).getList_itemnumber_x(),
						Main.syshopg.get(1).getList_itemnumber_y() * y + Main.syshopg.get(1).getList_itemnumber_b(),
						Arrays.asList(Main.syshopg.get(1).getList_itemnumber_text() + number),
						Main.syshopg.get(1).getList_itemnumber_fontsize()));

				/* 购买按钮 */
				VexButton buy = new VexButton(y + 2, Main.syshopg.get(1).getList_itembuy_text() + "§f",
						Utils.Path + Main.syshopg.get(1).getList_itembuy_image1(),
						Utils.Path + Main.syshopg.get(1).getList_itembuy_image2(),
						Main.syshopg.get(1).getList_itembuy_x(),
						Main.syshopg.get(1).getList_itembuy_y() * y + Main.syshopg.get(1).getList_itembuy_b(),
						Main.syshopg.get(1).getList_itembuy_w(), Main.syshopg.get(1).getList_itembuy_h());
				final int x = y;
				buy.setFunction(new ButtonFunction() {

					@Override
					public void run(Player player) {
						VexViewAPI.openGui(player, new BuyItemGui(Main.instance, player, Main.playerlist.get(x)));
					}
				});

				vsl.addComponent(buy);

				y++;
			}

			vc.add(vsl);
		}

		this.addAllComponents(vc);

	}

}
