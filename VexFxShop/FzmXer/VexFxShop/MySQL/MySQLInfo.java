package FzmXer.VexFxShop.MySQL;

public class MySQLInfo {
	private String ip;
	private String username;
	private String password;
	private String databaseName;
	private int port;
	
	public MySQLInfo(String c_ip,String c_username, 
			String c_password, String c_databaseName, int c_port) {
		this.ip = c_ip;
		this.username = c_username;
		this.password = c_password;
		this.databaseName = c_databaseName;
		this.port = c_port;
	}
	
	public String getIp() {
		return ip;
	}
	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}
	public String getDatabaseName() {
		return databaseName;
	}
	public int getPort() {
		return port;
	}
	
}
