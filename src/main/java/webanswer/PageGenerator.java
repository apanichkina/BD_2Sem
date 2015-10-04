package webanswer; /**
 * Created by olegermakov on 15.09.15.
 */
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;
/* TODO разобраться */
public class PageGenerator {
    @NotNull
    private static final String HTMLDIT = "templates";
    @NotNull
    private static Configuration s_cfg = new Configuration();

    @NotNull
    public static Object getPage(@NotNull String filename, Map<String, Object> data) {
        Writer stream = new StringWriter();
        try {
            Template template = s_cfg.getTemplate(HTMLDIT + File.separator + filename);
            //noinspection ConstantConditions
            template.process(data, stream);
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
        return stream;
    }
}
