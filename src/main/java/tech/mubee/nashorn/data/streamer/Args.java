package tech.mubee.nashorn.data.streamer;

import java.io.File;

public class Args {

    private File csvFile;
    private File jsFile;

    private boolean valid;

    public Args(String... args) {
        tryParse(args);
    }

    public File getCsvFile() {
        return csvFile;
    }

    public File getJsFile() {
        return jsFile;
    }

    public boolean isValid() {
        return valid;
    }

    private void tryParse(String... args) {
        valid = true;

        if (args.length != 2) {
            valid = false;
            helpMessage();

        } else {
            try {
                csvFile = new File(args[0]);

                if (!csvFile.exists() || !csvFile.isFile()) {
                    System.out.println("CSV file does not exist. Please provide the path to an existing CSV file.");
                    valid = false;
                }

                jsFile = new File(args[1]);

                if (!jsFile.exists() || !jsFile.isFile()) {
                    System.out.println("JavaScript file does not exist. Please provide the path to an existing JavaScript file.");
                    valid = false;
                }

            } catch (Exception ex) {
                valid = false;
                helpMessage();
            }
        }
    }

    private static void helpMessage() {
        System.out.println("The application expects 2 arguments...");
        System.out.println("1. Provide the path to the CSV file");
        System.out.println("2. Provide the path to your custom JavaScript file");
    }
}
