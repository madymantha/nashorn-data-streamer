package tech.mubee.nashorn.data.streamer;

public class Main {

    public static void main(String... arguments) {
        Args args = new Args(arguments);

        if (args.isValid()) {
            CsvDataStreamer dataStreamer = new CsvDataStreamer(args.getCsvFile(), args.getJsFile());

            try {
                dataStreamer.stream();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
