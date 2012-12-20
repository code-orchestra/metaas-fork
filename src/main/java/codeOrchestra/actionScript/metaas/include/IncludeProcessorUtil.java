package codeOrchestra.actionScript.metaas.include;

import uk.co.badgersinfoil.metaas.impl.util.UnicodeInputStream;

import java.io.*;
import java.nio.charset.Charset;

/**
 * @author Alexander Eliseyev
 */
public final class IncludeProcessorUtil {

    public static String readFile(String file) throws IOException {
        InputStream stream = new UnicodeInputStream(new FileInputStream(file));

        try {
            Reader reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder builder = new StringBuilder();
            char[] buffer = new char[8192];
            int read;
            while ((read = reader.read(buffer, 0, buffer.length)) > 0) {
                builder.append(buffer, 0, read);
            }
            return builder.toString();
        } finally {
            stream.close();
        }
    }

}
