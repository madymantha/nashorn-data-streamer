package tech.mubee.nashorn.data.streamer;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

class CsvDataStreamer {

    private static final Logger logger = Logger.getLogger(CsvDataStreamer.class.getName());

    private static final String COMMA = ",";
    private static final String SPACE = " ";
    private static final String EMPTY = "";

    private File csvFile;
    private File jsFile;

    private ScriptEngine nashorn;

    CsvDataStreamer(File csvFile, File jsFile) {
        this.csvFile = csvFile;
        this.jsFile = jsFile;

        nashorn = new ScriptEngineManager().getEngineByName("nashorn");
    }

    public File getCsvFile() {
        return csvFile;
    }

    public File getJsFile() {
        return jsFile;
    }

    void stream() throws Exception {
        List<String> fields = getFields();

        if (fields.isEmpty()) {
            throw new Exception("Unable to read field names from the header (first line) of the CSV file: " +
                    csvFile.getName());
        }

        // Load the record prototype.
        nashorn.eval(getRecordPrototype(fields));

        Invocable invocable = (Invocable) nashorn;

        try (Reader reader = new FileReader(jsFile);
             InputStream inputStream = new FileInputStream(csvFile);
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

            // Load the user-defined JavaScript file.
            nashorn.eval(reader);

            optionalInvoke(invocable, "onBegin");

            // Stream the CSV data. Skip the header (first line).
            bufferedReader.lines().skip(1).forEach((String line) -> {
                try {
                    Object record = invocable.invokeFunction("__newRecord", line);
                    invocable.invokeFunction("onRecord", record);

                } catch (ScriptException ex) {
                    logger.severe(ex.toString());

                } catch (NoSuchMethodException ex) {
                    logger.severe("'onRecord' method should be defined in your custom code.");
                    logger.severe(ex.toString());
                }
            });

            optionalInvoke(invocable, "onEnd");
        }
    }

    private List<String> getFields() throws Exception {
        List<String> fields = new ArrayList<>();

        try (InputStream inputStream = new FileInputStream(csvFile);
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

            String header = bufferedReader.readLine();

            if (isNotBlank(header)) {
                for (String item : header.split(COMMA)) {
                    if (isNotBlank(item)) {
                        fields.add(getFieldName(item));
                    }
                }
            }
        }

        return fields;
    }

    private static void optionalInvoke(Invocable invocable, String method) {
        try {
            invocable.invokeFunction(method);

        } catch (Exception ex) {
            // Do nothing.
        }
    }

    private static boolean isNotBlank(String value) {
        return value != null && !value.trim().isEmpty();
    }

    private static String getFieldName(String value) {
        String field = value.trim().replace(SPACE, EMPTY);

        // Make first character lowercase.
        char chars[] = field.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return new String(chars);
    }

    private static String getRecordPrototype(List<String> fields) {
        // NOTE: The "fields to values" mapping is expected to be consistent.
        StringBuilder sb = new StringBuilder("function __Record(values) {")
                                     .append("  for (var v=0; v<values.length; v++) { ")
                                     .append("    this.setValue(v, values[v]);")
                                     .append("  }")
                                     .append("};");

        // Add fields array.
        sb.append("__Record.prototype._fields = [");
        for (String field : fields) {
            sb.append("\"").append(field).append("\", ");
        }
        sb.append("];");

        // Add prototypes.
        // The assumption here is that the field names are valid JS names.
        for (String field : fields) {
            sb.append("__Record.prototype.").append(field).append(" = null;");
        }

        sb.append("__Record.prototype.setValue = function(index, value) { this[this._fields[index]] = value; };");

        sb.append("function __newRecord(line) { return new __Record(line.split(',')); }");

        return sb.toString();
    }
}
