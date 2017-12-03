# nashorn-data-streamer
## Introduction
This is a Java 9 based sample project that uses Nashorn (JavaScript) to analyze a stream of records that are read from a CSV file.
Java Platform Module System is used to create a custom runtime image. The experimental gradle jigsaw plugin is used to create a modular jar file. 

## Getting started
You need JDK 9 to run this sample project. Steps to run:
1. Clone this repository
2. Define JAVA_HOME and JAVA_LINK variables pointing to JDK 9 home and the jlink executable respectively
3. Execute build.sh script

```bash
./build.sh
```

The script builds a modular jar file and creates a custom java image. You can use the sample CSV and JavaScript files to execute a test run of the custom image.

```bash
nds-image/bin/nds test.csv test.js
```

## Use JavaScript to analyze the data stream
This application streams data from the specified csv file to your custom JavaScript file. You can change the logic of the JavaScript file without the need to recompile the application. The JavaScript file can specify definitions for the following three functions:
1. function onBegin() {}
2. function onRecord(record) {}
3. function onEnd() {}

The onBegin() and onEnd() are optional methods. You can run your JavaScript file without specifying these optional methods. OnRecord(record) should be specified. You analyze the data record here. The record is a dynamic object whose properties are based on the CSV header line (first line). Take a look at [test.js](https://github.com/mubee-tech/nashorn-data-streamer/test.js) to see how the [test.csv](https://github.com/mubee-tech/nashorn-data-streamer/test.csv) data is analyzed.
