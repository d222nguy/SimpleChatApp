package default_package;


//Singleton class
public class AppConstants {
	private static AppConstants single_instance = null;
	public int port_number;
	public String NAME_REQUIRED;
	public String NAME_ALREADY_EXISTS;
	public String NAME_ACCEPTED;
	public int WINDOW_WIDTH;
	public int WINDOW_HEIGHT;
	public int CHAT_AREA_WIDTH;
	public int CHAT_AREA_HEIGHT;
	private AppConstants() {
		port_number = 9800;
		NAME_REQUIRED = "400";
		NAME_ALREADY_EXISTS = "401";
		NAME_ACCEPTED = "200";
		WINDOW_WIDTH = 475;
		WINDOW_HEIGHT = 500;
		CHAT_AREA_WIDTH = 22;
		CHAT_AREA_HEIGHT = 40;
		
	}
	public static AppConstants getInstance() {
		if (single_instance == null) {
			single_instance = new AppConstants();
		}
		return single_instance;
	}
	
}
