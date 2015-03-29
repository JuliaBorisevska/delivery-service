package resource;

import java.util.ResourceBundle;

public class MessageManager {
	private  static ResourceBundle resourceBundle = ResourceBundle.getBundle("messages");
    private MessageManager() { }
    public static String getProperty(String key) {
        return resourceBundle.getString(key);
    }
}
