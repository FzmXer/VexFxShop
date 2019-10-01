package FzmXer.VexFxShop.Data;

public class Custem_Data {
	/* 物品的英文名 */
	private String E_Name;
	/* 物品修改的中文名 */
	private String C_Name;

	/**
	 * @param e_n 英文名, c_n 中文名
	 * */
	public Custem_Data(String e_n, String c_n) {
		this.E_Name = e_n;
		this.C_Name = c_n;
	}

	public void setE_Name(String e_Name) {
		E_Name = e_Name;
	}

	public void setC_Name(String c_Name) {
		C_Name = c_Name;
	}

	public String getE_Name() {
		return E_Name;
	}

	public String getC_Name() {
		return C_Name;
	}

}
