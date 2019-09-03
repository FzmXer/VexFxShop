package FzmXer.VexFxShop.Data;

public class ItemInfo {
	/**
	 *  {@value 商品nbtID }
	 *   */
	private int nbt_Id;
	/**
	 *  {@value 商品类型 }
	 *   */
	private String item_Type;
	/**
	 *  {@value 商品显示名 }
	 *   */
	private String item_Name;
	/**
	 *  {@value 商品Lore }
	 *   */
	private String item_Lore;
	/**
	 *  {@value 商品附魔 }
	 *   */
	private String item_Enchants;
	/**
	 *  {@value 商品Nbt }
	 *   */
	private String item_Nbts;
	/**
	 *  {@value 商品耐久 }
	 *   */
	private int item_Durability;
	/**
	 *  {@value 商品单价(金币) }
	 *   */
	private int item_Money;
	/**
	 *  {@value 商品单价(点券) }
	 *   */
	private int item_Point;
	/**
	 *  {@value 商品上架时间 }
	 *   */
	private String item_Dates;
	/**
	 *  {@value 商品上架玩家 }
	 *   */
	private String player_Name;
	/**
	 *  {@value 商品上架玩家 的UUID }
	 *   */
	private String player_UUID;
	/**
	 *  {@value 商品上架数量 }
	 *   */
	private int item_Number;
	/**
	 *  {@value 商店名称 }
	 *   */
	private String Shop_Name;

	
	public ItemInfo() {}
		
	public String getItem_Type() {
		return item_Type;
	}

	public void setItem_Type(String item_Type) {
		this.item_Type = item_Type;
	}

	public int getNbt_Id() {
		return nbt_Id;
	}
	
	public void setNbt_Id(int nbt_Id) {
		this.nbt_Id = nbt_Id;
	}

	public String getItem_Name() {
		return item_Name;
	}

	public void setItem_Name(String item_Name) {
		this.item_Name = item_Name;
	}

	public String getItem_Lore() {
		return item_Lore;
	}

	public void setItem_Lore(String item_Lore) {
		this.item_Lore = item_Lore;
	}

	public String getItem_Enchants() {
		return item_Enchants;
	}

	public void setItem_Enchants(String item_Enchants) {
		this.item_Enchants = item_Enchants;
	}

	public String getItem_Nbts() {
		return item_Nbts;
	}

	public void setItem_Nbts(String item_Nbts) {
		this.item_Nbts = item_Nbts;
	}

	public int getItem_Durability() {
		return item_Durability;
	}

	public void setItem_Durability(int item_Durability) {
		this.item_Durability = item_Durability;
	}

	public int getItem_Money() {
		return item_Money;
	}

	public void setItem_Money(int item_Money) {
		this.item_Money = item_Money;
	}

	public int getItem_Point() {
		return item_Point;
	}

	public void setItem_Point(int item_Point) {
		this.item_Point = item_Point;
	}

	public String getItem_Dates() {
		return item_Dates;
	}

	public void setItem_Dates(String item_Dates) {
		this.item_Dates = item_Dates;
	}

	public String getPlayer_Name() {
		return player_Name;
	}

	public void setPlayer_Name(String player_Name) {
		this.player_Name = player_Name;
	}

	public String getPlayer_UUID() {
		return player_UUID;
	}

	public void setPlayer_UUID(String player_UUID) {
		this.player_UUID = player_UUID;
	}

	public int getItem_Number() {
		return item_Number;
	}

	public void setItem_Number(int item_Number) {
		this.item_Number = item_Number;
	}

	public String getShop_Name() {
		return Shop_Name;
	}

	public void setShop_Name(String shop_Name) {
		Shop_Name = shop_Name;
	}

}
