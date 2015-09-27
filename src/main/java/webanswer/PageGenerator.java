package WebAnswer; /**
 * Created by olegermakov on 15.09.15.
 */
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

public class PageGenerator {
    private static String htmlDir = "templates";
    private static Configuration cfg = new Configuration();

    @Nullable
    public static String getPage(String filename, Map<String, Object> data) {
        Writer stream = new StringWriter();
        try
        {
            assert cfg != null;
            Template template = cfg.getTemplate(htmlDir + File.separator + filename);
            assert template != null;
            template.process(data, stream);
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
        return stream.toString();
    }
}
