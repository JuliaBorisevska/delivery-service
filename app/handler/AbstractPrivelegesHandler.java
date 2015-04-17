package handler;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by antonkw on 10.04.2015.
 */
public abstract class AbstractPrivelegesHandler implements AbstractHandler {
    protected final String FILE_CONFIG_NAME = "conf/privileges.json";

    ObjectMapper mapper = new ObjectMapper();
}
