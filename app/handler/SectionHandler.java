package handler;

import com.fasterxml.jackson.databind.JsonNode;
import play.Logger;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

/**
 * Created by antonkw on 10.04.2015.
 */
public class SectionHandler extends AbstractPrivelegesHandler {
    private static Logger.ALogger logger = Logger.of(SectionHandler.class);

    private JsonNode sectionNode = null;

    protected SectionHandler() throws IOException, ParseException {
        parse();
    }

    @Override
    public void parse() throws ParseException, IOException {
        JsonNode rootNode = mapper.readTree(new File(FILE_CONFIG_NAME));
        sectionNode = rootNode.path("sections");
        logger.info("Section node: {}", sectionNode.toString());
    }

    public JsonNode getSectionNode() {
        return sectionNode;
    }
}
