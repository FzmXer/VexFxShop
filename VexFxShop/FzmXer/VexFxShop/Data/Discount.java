package FzmXer.VexFxShop.Data;

public class Discount {

	private String permiss;
	private double count;
	
	public Discount(String permiss, double count) {
		this.permiss = permiss;
		this.count = count;	
	}
	
	public String getPermiss() {
		return permiss;
	}
	
	public double getCount() {
		return count;
	}
	
	public void setPermiss(String permiss) {
		this.permiss = permiss;
	}
	public void setCount(double count) {
		this.count = count;
	}

}
