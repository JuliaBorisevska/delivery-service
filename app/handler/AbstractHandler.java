package handler;

import java.io.IOException;
import java.text.ParseException;

/**
 * Created by antonkw on 10.04.2015.
 */
public interface AbstractHandler {
    public void parse() throws ParseException, IOException;
}
