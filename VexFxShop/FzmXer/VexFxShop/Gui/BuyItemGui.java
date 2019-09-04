package FzmXer.VexFxShop.Gui;

import java.util.Arrays;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import FzmXer.VexFxShop.Data.Discount;
import FzmXer.VexFxShop.Data.ItemInfo;
import FzmXer.VexFxShop.Main.Main;
import FzmXer.VexFxShop.MySQL.ShopItemManager;
import FzmXer.VexFxShop.Utils.PointApi;
import FzmXer.VexFxShop.Utils.Utils;
import FzmXer.VexFxShop.Utils.VaultApi;
import FzmXer.VexFxShop.NBTUtils.NBTUtils;
import FzmXer.VexFxShop.SQLite.SQLManager;
import lk.vexview.api.VexViewAPI;
import lk.vexview.gui.VexGui;
import lk.vexview.gui.components.ButtonFunction;
import lk.vexview.gui.components.VexButton;
import lk.vexview.gui.components.VexImage;
import lk.vexview.gui.components.VexSlot;
import lk.vexview.gui.components.VexText;
import lk.vexview.gui.components.VexTextField;
import lk.vexview.item.VexItemStack;

public class BuyItemGui extends VexGui {

	double count;

	public BuyItemGui(Main mian, Player player, ItemInfo infos) {
		/* 设置背景图片 */
		super(Utils.Path + Main.byd.getBg_image(), Main.byd.getBg_x(), Main.byd.getBg_y(), Main.byd.getBg_w(),
				Main.byd.getBg_h(), Main.byd.getBg_xs(), Main.byd.getBg_ys());

		/* 用nbt生成物品 */
		ItemStack mojangson = NBTUtils.loadItemStackJson(infos.getItem_Nbts());
		mojangson = VexItemStack.of(mojangson).previewItem() // 开启物品3D模型预览
				.setBack(Utils.Path + Main.byd.getItemstack_image()) // 设置描述框的背景
				.setHeight(Main.byd.getItemstack_h()) // 设置描述框的最大高度(超出高度后将自动进行滚动显示)|
				.build();
		/* 添加物品槽，显示物品用 */
		this.addComponent(new VexImage(Utils.Path + Main.byd.getSlot_image(), Main.byd.getSlot_image_x(),
				Main.byd.getSlot_image_y(), Main.byd.getSlot_image_w(), Main.byd.getSlot_image_h()));
		this.addComponent(new VexSlot(0, Main.byd.getSlot_x(), Main.byd.getSlot_y(), mojangson));

		this.addComponent(new VexText(Main.byd.getName_x(), Main.byd.getName_y(),
				Arrays.asList(Main.byd.getName_text() + infos.getItem_Name()), Main.byd.getName_fontsize()));
		/* 商品数量 */
		String number = infos.getItem_Number() + "";
		if (infos.getItem_Number() == -1) {
			number = "无限";
		}

		count = 1;
		if (infos.getShop_Name().equalsIgnoreCase("systemshop")) {
			for (Discount dc : Main.discount) {
				if (player.hasPermission(dc.getPermiss())) {
					count = dc.getCount();
					break;
				}
			}
		}
		this.addComponent(new VexText(Main.byd.getNumber_x(), Main.byd.getNumber_y(),
				Arrays.asList(Main.byd.getNumber_text() + number), Main.byd.getNumber_fontsize()));
		this.addComponent(new VexText(Main.byd.getMoney_x(), Main.byd.getMoney_y(),
				Arrays.asList(
						Main.byd.getMoney_text() + (infos.getItem_Money() * count) + Main.byd.getMoney_text_suffix()),
				Main.byd.getMoney_fontsize()));
		this.addComponent(new VexText(Main.byd.getPoint_x(), Main.byd.getPoint_y(),
				Arrays.asList(
						Main.byd.getPoint_text() + (infos.getItem_Point() * count) + Main.byd.getPoint_text_suffix()),
				Main.byd.getPoint_fontsize()));
		this.addComponent(new VexText(Main.byd.getBuytext_x(), Main.byd.getBuytext_y(),
				Arrays.asList(Main.byd.getBuytext_text()), Main.byd.getBuytext_fontsize()));
		this.addComponent(new VexTextField(Main.byd.getInput_x(), Main.byd.getInput_y(), Main.byd.getInput_w(),
				Main.byd.getInput_h(), 2, 401));

		/* 返回按钮 */
		this.addComponent(new VexButton(0, Main.byd.getBack_text(), Utils.Path + Main.byd.getBack_image1(),
				Utils.Path + Main.byd.getBack_image2(), Main.byd.getBack_x(), Main.byd.getBack_y(),
				Main.byd.getBack_w(), Main.byd.getBack_h(), new ButtonFunction() {
					@Override
					public void run(Player player) {
						/* 异步调用，减少卡服运算 */
						new BukkitRunnable() {
							@Override
							public void run() {
								VexViewAPI.openGui(player, new SystemShopGui(Main.instance, player));
							}
						}.runTaskAsynchronously(Main.instance);
					}
				}));
		/* 如果卖家为自己 */
		if (infos.getPlayer_Name().equalsIgnoreCase(player.getName())
				&& infos.getPlayer_UUID().equalsIgnoreCase(player.getUniqueId().toString())) {
			/* 下架按钮 */
			this.addComponent(new VexButton(0, Main.byd.getDown_text(), Utils.Path + Main.byd.getDown_image1(),
					Utils.Path + Main.byd.getDown_image2(), Main.byd.getDown_x(), Main.byd.getDown_y(),
					Main.byd.getDown_w(), Main.byd.getDown_h(), new ButtonFunction() {
						@Override
						public void run(Player player) {
							/* 异步调用，减少卡服运算 */
							new BukkitRunnable() {
								@Override
								public void run() {
									/* 取玩家背包第一个空位 */
									int bx = player.getInventory().firstEmpty();
									if (bx == -1) {
										Utils.sendMsg(player, "§c§l下架失败，背包空间不足！");
										/* 关闭背包界面 */
										player.closeInventory();
										return;
									}

									/* 根据NBT信息，生成物品 */
									ItemStack mojangson = NBTUtils.loadItemStackJson(infos.getItem_Nbts());
									/* 设置数量为购买的数量 */
									mojangson.setAmount(infos.getItem_Number());

									/* 从数据库删除物品 */
									if (Main.DataSaveType.equalsIgnoreCase("mysql")) {
										if (ShopItemManager.BuyItems_down(infos, infos.getItem_Number())) {
											Utils.sendMsg(player, "§a§l下架成功，请查看背包！");
											/* 将物品发送到背包 */
											player.getInventory().addItem(mojangson);
										} else {
											Utils.sendMsg(player, "§c§l 数据库出错，请联系作者！");
										}
									} else {
										if (SQLManager.BuyItems_down(infos, infos.getItem_Number())) {
											Utils.sendMsg(player, "§a§l下架成功，请查看背包！");
											/* 将物品发送到背包 */
											player.getInventory().addItem(mojangson);
										} else {
											Utils.sendMsg(player, "§c§l 数据库出错，请联系作者！");
										}
									}

									/* 关闭背包界面 */
									player.closeInventory();
								}
							}.runTaskAsynchronously(Main.instance);
						}
					}));
		}

		/* 购买按钮 */
		this.addComponent(new VexButton(2, Main.byd.getBuy_text(), Utils.Path + Main.byd.getBuy_image1(),
				Utils.Path + Main.byd.getBuy_image2(), Main.byd.getBuy_x(), Main.byd.getBuy_y(), Main.byd.getBuy_w(),
				Main.byd.getBuy_h(), new ButtonFunction() {
					@Override
					public void run(Player player) {
						/* 取玩家背包第一个空位 */
						int bx = player.getInventory().firstEmpty();
						if (bx == -1) {
							Utils.sendMsg(player, "§c§l购买失败，背包空间不足！");
							return;
						}
						/* 取购买的数量 */
						int buynb = 0;
						try {
							buynb = Integer.parseInt(
									VexViewAPI.getPlayerCurrentGui(player).getVexGui().getTextField().getTypedText());
						} catch (NumberFormatException e) {
							Utils.sendMsg(player, "§c§l请输入整数！");
							return;
						}
						/* 判断是否超过物品数量，超过则默认最大 */
						if (buynb <= 0) {
							buynb = 1;
						} else if (buynb >= infos.getItem_Number() && infos.getItem_Number() != -1) {
							buynb = infos.getItem_Number();
						}
						/* 根据NBT信息，生成物品 */
						ItemStack mojangson = NBTUtils.loadItemStackJson(infos.getItem_Nbts());
						/* 设置数量为购买的数量 */
						mojangson.setAmount(buynb);

						if (hasMoeny(player, infos, buynb)) {
							Utils.sendMsg(player, "§a§l购买成功，请查看背包！");
							/* 将物品发送到背包 */
							player.getInventory().addItem(mojangson);
						} else {
							if (hasPoint(player, infos, buynb)) {
								Utils.sendMsg(player, "§a§l购买成功，请查看背包！");
								/* 将物品发送到背包 */
								player.getInventory().addItem(mojangson);
							}
						}
						/* 关闭背包界面 */
						player.closeInventory();
					}
				}));
	}

	/* 金币购买逻辑 */
	private boolean hasMoeny(Player player, ItemInfo infos, int sl) {
		/* 如果该商品不需要金币，则返回，向后执行 */
		if (infos.getItem_Money() == 0) {
			return false;
		}
		/* 物品的总价 */
		int sum = (int) (infos.getItem_Money() * sl * count);
		/* 如果买家金币数大于等于物品价格 或 (玩家名与UUID 与 数据库中的商品上架玩家保持一致) */
		if (VaultApi.hasMoney(player, sum) || (infos.getPlayer_Name().equalsIgnoreCase(player.getName())
				&& infos.getPlayer_UUID().equalsIgnoreCase(player.getUniqueId().toString()))) {
			/* 如果该物品，既需要金币又需要点券，则让后面处理 */
			if (infos.getItem_Point() <= 0) {
				/* 数据存储方式 */
				if (Main.DataSaveType.equalsIgnoreCase("mysql")) {
					/* 数据库删除物品 */
					if (ShopItemManager.BuyItems(infos, sl)) {
						/* 如果玩家名与UUID 与数据库中的商品上架玩家保持一致，则直接获得物品 */
						if (infos.getPlayer_Name().equalsIgnoreCase(player.getName())
								&& infos.getPlayer_UUID().equalsIgnoreCase(player.getUniqueId().toString())) {
							return true;
						}
						/* 扣款成功！ */
						if (VaultApi.takeMoney(player, sum)) {
							/* 添加金币成功！ */
							if (VaultApi.giveMoney(Utils.loadPlayer(UUID.fromString(infos.getPlayer_UUID())), sum)) {
								return true;
							} else {
								/* 将钱还给玩家 */
								VaultApi.giveMoney(player, sum);
								/* 物品重新上架 */
								ShopItemManager.BuyItems_2(infos);
								Utils.sendMsg(player, "§c§l 付款失败，请稍后重试！");
							}
						} else {
							/* 物品重新上架 */
							ShopItemManager.BuyItems_2(infos);
							Utils.sendMsg(player, "§c§l 扣款失败，请稍后重试！");
						}
					} else {
						VaultApi.giveMoney(player, sum);
						Utils.sendMsg(player, "§c§l 数据库出错，请联系作者！");
					}
				} else {
					/* 数据库删除物品 */
					if (SQLManager.BuyItems(infos, sl)) {
						/* 如果玩家名与UUID 与数据库中的商品上架玩家保持一致，则直接获得物品 */
						if (infos.getPlayer_Name().equalsIgnoreCase(player.getName())
								&& infos.getPlayer_UUID().equalsIgnoreCase(player.getUniqueId().toString())) {
							return true;
						}
						/* 扣款成功！ */
						if (VaultApi.takeMoney(player, sum)) {
							/* 添加金币成功！ */
							if (VaultApi.giveMoney(Utils.loadPlayer(UUID.fromString(infos.getPlayer_UUID())), sum)) {
								return true;
							} else {
								/* 将钱还给玩家 */
								VaultApi.giveMoney(player, sum);
								/* 物品重新上架 */
								SQLManager.BuyItems_2(infos);
								Utils.sendMsg(player, "§c§l 付款失败，请稍后重试！");
							}
						} else {
							/* 物品重新上架 */
							SQLManager.BuyItems_2(infos);
							Utils.sendMsg(player, "§c§l 扣款失败，请稍后重试！");
						}
					} else {
						VaultApi.giveMoney(player, sum);
						Utils.sendMsg(player, "§c§l 数据库出错，请联系作者！");
					}
				}

			}
		} else {
			Utils.sendMsg(player, "§c§l 你没有足够的金钱购买！");
		}
		return false;
	}

	/* 点券购买逻辑 */
	private boolean hasPoint(Player player, ItemInfo infos, int sl) {
		/* 物品的总价 */
		int msum = (int) (infos.getItem_Money() * sl * count);
		int psum = (int) (infos.getItem_Point() * sl * count);
		/* 如果买家点券数大于物品价格 */
		if (PointApi.hasPoint(player, psum) || (infos.getPlayer_Name().equalsIgnoreCase(player.getName())
				&& infos.getPlayer_UUID().equalsIgnoreCase(player.getUniqueId().toString()))) {
			/* 数据存储方式 */
			if (Main.DataSaveType.equalsIgnoreCase("mysql")) {
				/* 从数据库删除物品 */
				if (ShopItemManager.BuyItems(infos, sl)) {
					/* 如果玩家名与UUID 与数据库中的商品上架玩家保持一致，则直接获得物品 */
					if (infos.getPlayer_Name().equalsIgnoreCase(player.getName())
							&& infos.getPlayer_UUID().equalsIgnoreCase(player.getUniqueId().toString())) {
						return true;
					}
					/* 如果该商品不需要金币 */
					if (infos.getItem_Money() == 0) {
						/* 扣除点券成功 */
						if (PointApi.takePoints(player, psum)) {
							/* 添加点券成功！ */
							if (PointApi.givePoints(Utils.loadPlayer(UUID.fromString(infos.getPlayer_UUID())), psum)) {
								return true;
							} else {
								/* 物品重新上架 */
								ShopItemManager.BuyItems_2(infos);
								/* 将钱还给玩家 */
								PointApi.givePoints(player, psum);
							}
						} else {
							/* 物品重新上架 */
							ShopItemManager.BuyItems_2(infos);
							Utils.sendMsg(player, "§c§l付款失败，请稍后重试！");
						}
					} else {
						/* 如果买家金币数大于等于物品价格 */
						if (VaultApi.hasMoney(player, msum)
								|| (infos.getPlayer_Name().equalsIgnoreCase(player.getName())
										&& infos.getPlayer_UUID().equalsIgnoreCase(player.getUniqueId().toString()))) {
							/* 扣除点券成功 */
							if (PointApi.takePoints(player, psum)) {
								/* 添加点券成功！ */
								if (PointApi.givePoints(Utils.loadPlayer(UUID.fromString(infos.getPlayer_UUID())),
										psum)) {
									/* 扣除金币成功！ */
									if (VaultApi.takeMoney(player, msum)) {
										/* 添加金币成功！ */
										if (VaultApi.giveMoney(
												Utils.loadPlayer(UUID.fromString(infos.getPlayer_UUID())), msum)) {
											return true;
										} else {
											/* 将钱还给玩家 */
											VaultApi.giveMoney(player, msum);
											/* 物品重新上架 */
											ShopItemManager.BuyItems_2(infos);
											Utils.sendMsg(player, "§c§l付款失败，请稍后重试！");
										}
									} else {
										/* 物品重新上架 */
										ShopItemManager.BuyItems_2(infos);
										Utils.sendMsg(player, "§c§l扣款失败，请稍后重试！");
									}
								} else {
									/* 物品重新上架 */
									ShopItemManager.BuyItems_2(infos);
									Utils.sendMsg(player, "§c§l付款失败，请稍后重试！");
									PointApi.givePoints(player, psum);
								}
							} else {
								/* 物品重新上架 */
								ShopItemManager.BuyItems_2(infos);
								Utils.sendMsg(player, "§c§l扣款失败，请稍后重试！");
							}
						} else {
							Utils.sendMsg(player, "§c§l 你没有足够的金钱购买！");
						}
					}
				} else {
					Utils.sendMsg(player, "§c§l数据库出错，请联系作者！");
				}
			} else {
				/* 从数据库删除物品 */
				if (SQLManager.BuyItems(infos, sl)) {
					/* 如果玩家名与UUID 与数据库中的商品上架玩家保持一致，则直接获得物品 */
					if (infos.getPlayer_Name().equalsIgnoreCase(player.getName())
							&& infos.getPlayer_UUID().equalsIgnoreCase(player.getUniqueId().toString())) {
						return true;
					}
					/* 如果该商品不需要金币 */
					if (infos.getItem_Money() == 0) {
						/* 扣除点券成功 */
						if (PointApi.takePoints(player, psum)) {
							/* 添加点券成功！ */
							if (PointApi.givePoints(Utils.loadPlayer(UUID.fromString(infos.getPlayer_UUID())), psum)) {
								return true;
							} else {
								/* 物品重新上架 */
								SQLManager.BuyItems_2(infos);
								/* 将钱还给玩家 */
								PointApi.givePoints(player, psum);
							}
						} else {
							/* 物品重新上架 */
							SQLManager.BuyItems_2(infos);
							Utils.sendMsg(player, "§c§l付款失败，请稍后重试！");
						}
					} else {
						/* 如果买家金币数大于等于物品价格 */
						if (VaultApi.hasMoney(player, msum)
								|| (infos.getPlayer_Name().equalsIgnoreCase(player.getName())
										&& infos.getPlayer_UUID().equalsIgnoreCase(player.getUniqueId().toString()))) {
							/* 扣除点券成功 */
							if (PointApi.takePoints(player, psum)) {
								/* 添加点券成功！ */
								if (PointApi.givePoints(Utils.loadPlayer(UUID.fromString(infos.getPlayer_UUID())),
										psum)) {
									/* 扣除金币成功！ */
									if (VaultApi.takeMoney(player, msum)) {
										/* 添加金币成功！ */
										if (VaultApi.giveMoney(
												Utils.loadPlayer(UUID.fromString(infos.getPlayer_UUID())), msum)) {
											return true;
										} else {
											/* 将钱还给玩家 */
											VaultApi.giveMoney(player, msum);
											/* 物品重新上架 */
											SQLManager.BuyItems_2(infos);
											Utils.sendMsg(player, "§c§l付款失败，请稍后重试！");
										}
									} else {
										/* 物品重新上架 */
										SQLManager.BuyItems_2(infos);
										Utils.sendMsg(player, "§c§l扣款失败，请稍后重试！");
									}
								} else {
									/* 物品重新上架 */
									SQLManager.BuyItems_2(infos);
									Utils.sendMsg(player, "§c§l付款失败，请稍后重试！");
									PointApi.givePoints(player, psum);
								}
							} else {
								/* 物品重新上架 */
								SQLManager.BuyItems_2(infos);
								Utils.sendMsg(player, "§c§l扣款失败，请稍后重试！");
							}
						} else {
							Utils.sendMsg(player, "§c§l 你没有足够的金钱购买！");
						}
					}
				} else {
					Utils.sendMsg(player, "§c§l数据库出错，请联系作者！");
				}
			}

		} else {
			Utils.sendMsg(player, "§c§l你没有足够的点券购买！");
		}
		return false;
	}

}
