package FzmXer.VexFxShop.Gui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import FzmXer.VexFxShop.Data.ItemInfo;
import FzmXer.VexFxShop.Main.Main;
import FzmXer.VexFxShop.MySQL.ShopItemManager;
import FzmXer.VexFxShop.Utils.Utils;
import FzmXer.VexFxShop.NBTUtils.NBTUtils;
import FzmXer.VexFxShop.SQLite.SQLManager;
import lk.vexview.api.VexViewAPI;
import lk.vexview.gui.VexInventoryGui;
import lk.vexview.gui.components.ButtonFunction;
import lk.vexview.gui.components.VexButton;
import lk.vexview.gui.components.VexCheckBox;
import lk.vexview.gui.components.VexComponents;
import lk.vexview.gui.components.VexSlot;
import lk.vexview.gui.components.VexText;
import lk.vexview.gui.components.VexTextField;

public class UpItemGui extends VexInventoryGui {

	private List<VexComponents> vc;

	public UpItemGui(Main main, Player player, String shopname) {
		super(Main.upd.getBg_image(), Main.upd.getBg_x(), Main.upd.getBg_y(), Main.upd.getBg_w(), Main.upd.getBg_h(),
				Main.upd.getBg_xs(), Main.upd.getBg_ys(), Main.upd.getBg_slotLeft(), Main.upd.getBg_slotTop());

		vc = new ArrayList<VexComponents>();

		vc.add(new VexSlot(0, Main.upd.getSlot_x(), Main.upd.getSlot_y(), new ItemStack(Material.AIR)));

		vc.add(new VexButton(10, "", Main.upd.getExit_image1(), Main.upd.getExit_image2(), Main.upd.getExit_x(),
				Main.upd.getExit_y(), Main.upd.getExit_w(), Main.upd.getExit_h(), new ButtonFunction() {
					@Override
					public void run(Player player) {
						player.closeInventory();
					}
				}));

		vc.add(new VexText(Main.upd.getDj_show_x(), Main.upd.getDj_show_y(), Arrays.asList(Main.upd.getDj_show_text()),
				Main.upd.getDj_show_fontsize()));
		vc.add(new VexTextField(Main.upd.getInput_x(), Main.upd.getInput_y(), Main.upd.getInput_w(),
				Main.upd.getInput_h(), 9, 301));

		vc.add(new VexCheckBox(20, Main.upd.getMoney_image1(), Main.upd.getMoney_image2(), Main.upd.getMoney_x(),
				Main.upd.getMoney_y(), Main.upd.getMoney_w(), Main.upd.getMoney_h(), true));

		vc.add(new VexText(Main.upd.getMoney_text_x(), Main.upd.getMoney_text_y(),
				Arrays.asList(Main.upd.getMoney_text()), Main.upd.getMoney_text_fontsize()));

		if (Main.isPoint) {
			vc.add(new VexCheckBox(21, Main.upd.getPoint_image1(), Main.upd.getPoint_image2(), Main.upd.getPoint_x(),
					Main.upd.getPoint_y(), Main.upd.getPoint_w(), Main.upd.getPoint_h(), false));
			vc.add(new VexText(Main.upd.getPoint_text_x(), Main.upd.getPoint_text_y(),
					Arrays.asList(Main.upd.getPoint_text()), Main.upd.getPoint_text_fontsize()));
		}

		if (player.isOp() && ((shopname != "playershop" || !shopname.equalsIgnoreCase("playershop")))) {
			vc.add(new VexCheckBox(22, Main.upd.getUn_image1(), Main.upd.getUn_image2(), Main.upd.getUn_x(),
					Main.upd.getUn_y(), Main.upd.getUn_w(), Main.upd.getUn_h(), false));
			vc.add(new VexText(Main.upd.getUn_text_x(), Main.upd.getUn_text_y(), Arrays.asList(Main.upd.getUn_text()),
					Main.upd.getUn_text_fontsize()));
		}

		vc.add(new VexButton(11, "", Main.upd.getOk_image1(), Main.upd.getOk_image2(), Main.upd.getOk_x(),
				Main.upd.getOk_y(), Main.upd.getOk_w(), Main.upd.getOk_h(), new ButtonFunction() {
					@Override
					public void run(Player player) {
						/* 异步调用，减少卡服运算 */
						new BukkitRunnable() {
							@Override
							public void run() {
								/* 获取文本框的内容 */
								int money = 0;
								try {
									money = Integer.parseInt(VexViewAPI.getPlayerCurrentGui(player).getVexGui()
											.getTextField().getTypedText());
								} catch (NumberFormatException e) {
									Utils.sendMsg(player, "§c§l错误，请输入纯整数！");
									return;
								}

								/* 获取插槽内的物品 */
								final VexSlot vs1;
								final int number;
								try {
									vs1 = VexViewAPI.getPlayerCurrentGui(player).getVexGui().getSlotById(0);
									number = vs1.getItem().getAmount();
									if (vs1.getItem().getType() == Material.AIR) {
										Utils.sendMsg(player, "§c§l错误，请放入要上架的物品！");
										return;
									}
								} catch (NullPointerException e) {
									Utils.sendMsg(player, "§c§l错误，获取上架物品失败！");
									return;
								}

								ItemStack items = vs1.getItem();
								ItemInfo infos = new ItemInfo();
								boolean m_flag = false, p_flag = false, u_flag = false;
								try {
									m_flag = VexViewAPI.getPlayerCurrentGui(player).getVexGui().getCheckBoxById(20)
											.isChecked();
								} catch (NullPointerException e) {
									m_flag = true;
								}

								if (Main.isPoint) {
									try {
										p_flag = VexViewAPI.getPlayerCurrentGui(player).getVexGui().getCheckBoxById(21)
												.isChecked();
									} catch (NullPointerException e) {
										p_flag = false;
									}
								}
								if (player.isOp()
										&& (shopname != "playershop" || !shopname.equalsIgnoreCase("playershop"))) {
									try {
										u_flag = VexViewAPI.getPlayerCurrentGui(player).getVexGui().getCheckBoxById(22)
												.isChecked();
									} catch (NullPointerException e) {
										u_flag = false;
									}
								}

								if (items.getItemMeta().getLore() != null) {
									for (String lores : Main.banlore) {
										for (String lore : items.getItemMeta().getLore()) {
											if (lore.indexOf(lores) != -1) {
												Utils.sendMsg(player, "§c§l商品含有禁止的Lore: " + lores + "， 上架失败！");
												return;
											}
										}
									}
								}

								/* 保存商品单价: 金币 */
								if (m_flag) {
									infos.setItem_Money(money);
								} else {
									infos.setItem_Money(0);
								}
								if (Main.isPoint) {
									/* 保存商品单价: 点券 */
									if (p_flag) {
										infos.setItem_Point(money);
									} else {
										infos.setItem_Point(0);
									}
								}

								/* 如果都未选中 */
								if (!m_flag && !p_flag) {
									Utils.sendMsg(player, "§c§l价格不允许为空，请选中价格类型！");
									return;
								}

								/* 保存商品数量 */
								if (u_flag) {
									infos.setItem_Number(-1);
								} else {
									infos.setItem_Number(items.getAmount());
								}
								/* 将物品设置为1个，方便保存nbt信息 */
								items.setAmount(1);

								/* 保存商品类型 */
								infos.setItem_Type(items.getType().toString());

								/* 保存商品显示名 */
								if (items.getItemMeta().getDisplayName() == ""
										|| items.getItemMeta().getDisplayName() == null
										|| items.getItemMeta().getDisplayName().length() <= 0) {
									infos.setItem_Name(items.getType().name());
								} else {
									infos.setItem_Name(items.getItemMeta().getDisplayName());
								}
								infos.setItem_Durability(items.getDurability());
								/* 转换物品的NBT信息为JSON */
								String mojangson = NBTUtils.getItemStackJson(items);
								infos.setItem_Nbts(mojangson);
								/* 保存商品的Lore */
								if (items.getItemMeta().getLore() != null) {
									infos.setItem_Lore(items.getItemMeta().getLore().toString());
								}
								/* 保存商品的附魔 */
								if (items.getEnchantments() != null) {
									infos.setItem_Enchants(items.getEnchantments().toString());
								}
								/* 保存商品的上架时间 */
								infos.setItem_Dates("" + System.currentTimeMillis() / 1000);
								/* 保存商品的上架玩家名 */
								infos.setPlayer_Name(player.getName());
								/* 保存商品的上架玩家UUID */
								infos.setPlayer_UUID(player.getUniqueId().toString());
								/* 保存商品的店铺名 */
								infos.setShop_Name(shopname);

								items.setAmount(number);
								/* 判断玩家背包是否含有指定数量的物品(防止玩家使用作弊客户端欺骗插件！) */
								if (player.getInventory().containsAtLeast(vs1.getItem(), vs1.getItem().getAmount())) {
									/* 调用保存的方法 */
									if (Main.DataSaveType.equalsIgnoreCase("mysql")) {
										if (ShopItemManager.AddItems(infos)) {
											/* 判断是否为OP */
											if (!player.isOp()) {
												/* 删除背包中 与 VexSlot 中物品信息一致的物品 */
												Utils.Msg("数量: " + vs1.getItem().getAmount());
												player.getInventory().removeItem(vs1.getItem());
											}
											SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
											String time = format.format(Calendar.getInstance().getTime());
											Utils.WLog(player.getName(),
													time + ": " + " 玩家 " + player.getName() + " 上架了 "
															+ infos.getItem_Number() + "个" + infos.getItem_Name()
															+ " 单价 " + infos.getItem_Money() + "金币， "
															+ infos.getItem_Point() + "点券。");
											Utils.sendMsg(player, "§c§l上架成功！");
											/* 重新读取信息 */
											Main.reloadItemInfo(shopname);
										} else {
											Utils.sendMsg(player, "§c§l上架失败，保存商品失败！");
										}

									} else {
										if (SQLManager.AddItem(infos)) {
											/* 判断是否为OP */
											if (!player.isOp()) {
												/* 删除背包中 与 VexSlot 中物品信息一致的物品 */
												Utils.Msg("数量: " + vs1.getItem().getAmount());
												player.getInventory().removeItem(vs1.getItem());
											}
											SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
											String time = format.format(Calendar.getInstance().getTime());
											Utils.WLog(player.getName(),
													time + ": " + " 玩家 " + player.getName() + " 上架了 "
															+ infos.getItem_Number() + "个" + infos.getItem_Name()
															+ " 单价 " + infos.getItem_Money() + "金币， "
															+ infos.getItem_Point() + "点券。");
											Utils.sendMsg(player, "§c§l上架成功！");
											/* 重新读取信息 */
											Main.reloadItemInfo_sql(shopname);
										} else {
											Utils.sendMsg(player, "§c§l上架失败，保存商品失败！");
										}
									}
								} else {
									Utils.sendMsg(player, "§c§l上架失败，你的背包内没有该物品！");
									return;
								}
							}
						}.runTaskAsynchronously(main);
						/* 关闭背包 */
						player.closeInventory();
					}
				}));
		this.addAllComponents(vc);
	}

}
