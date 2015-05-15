
import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.Logger.ALogger;
import search.ClientProvider;


public class Global extends GlobalSettings {
	private static ALogger logger = Logger.of(Global.class);
	
	@Override
	public void onStart(Application arg0) {
		ClientProvider.instance().prepareClient();
		logger.info("Application started");
		super.onStart(arg0);
	}

	@Override
	public void onStop(Application arg0) {
		ClientProvider.instance().closeNode();
		logger.info("Application stopped");
		super.onStop(arg0);
	}
	
}
