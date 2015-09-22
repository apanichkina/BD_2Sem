package WebAnswer; /**
 * Created by olegermakov on 15.09.15.
 */
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

public class PageGenerator {
    static private String HTML_DIR = "templates";
    static private Configuration CFG = new Configuration();

    static public String getPage(String filename, Map<String, Object> data) {
        Writer stream = new StringWriter();
        try
        {
            Template template = CFG.getTemplate(HTML_DIR + File.separator + filename);
            template.process(data, stream);
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
        return stream.toString();
    }
}
