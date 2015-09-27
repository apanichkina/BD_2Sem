package webanswer; /**
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
/* TODO разобраться */
public class PageGenerator {
    private static final String HTMLDIT = "templates";
    private static Configuration s_cfg = new Configuration();

    @Nullable
    public static Object getPage(String filename, Map<String, Object> data) {
        Writer stream = new StringWriter();
        try {
            assert s_cfg != null;
            Template template = s_cfg.getTemplate(HTMLDIT + File.separator + filename);
            assert template != null;
            template.process(data, stream);
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
        return stream;
    }
}
