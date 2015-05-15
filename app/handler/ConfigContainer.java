package handler;

import play.Logger;

import java.io.IOException;
import java.text.ParseException;

/**
 * Created by antonkw on 10.04.2015.
 */
public class ConfigContainer implements AbstractHandler {
    private static Logger.ALogger logger = Logger.of(ConfigContainer.class);

    private static volatile ConfigContainer instance;

    private RolesHandler permissionsHandler;

    private SectionHandler sectionHandler;

    private StatusHandler statusHandler;

    private ConfigContainer() throws IOException, ParseException {
        try {
            statusHandler = new StatusHandler();
            sectionHandler = new SectionHandler();
            permissionsHandler = new RolesHandler(statusHandler);
            logger.info("config container start");
        } catch (IOException | ParseException e) {
            throw e;
        }
    }

    public static void reload() throws IOException, ParseException {
        instance = null;
        getInstance();
    }

    public static ConfigContainer getInstance() throws IOException, ParseException {
        ConfigContainer localContainer = instance;
        if (localContainer == null) {
            synchronized (ConfigContainer.class) {
                localContainer = instance;
                if (localContainer == null) {
                    instance = localContainer = new ConfigContainer();
                }
            }
        }
        return localContainer;
    }

    public RolesHandler getRolesHandler() {
        return permissionsHandler;
    }

    @Override
    public void parse() {
        try {
            permissionsHandler.parse();
            sectionHandler.parse();
            statusHandler.parse();
        } catch (ParseException | IOException e) {
            logger.error("exception during reloading all of handlers: {}", e);
        }

    }

    public SectionHandler getSectionHandler() {
        return sectionHandler;
    }

    public StatusHandler getStatusHandler() {
        return statusHandler;
    }
}
