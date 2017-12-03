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

The script build the module based jar file and creates a custom image. You can use the sample CSV file and the test.js to execute a test run of the custom image.
