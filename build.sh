#!/bin/sh


if [[ -z "${JAVA_HOME}" ]]; then
    echo "Please set JAVA_HOME environment variable"
    exit 1
fi

if [[ -z "${JAVA_LINK}" ]]; then
    echo "Please set JAVA_LINK environment variable - point it to jlink executable"
    exit 1
fi

if [ -d nashorn-data-streamer-image ]; then
    rm -rf nds-image
fi

./gradlew clean build

$JAVA_LINK --module-path build/libs/:$JAVA_HOME/jmods \
        --add-modules tech.mubee.nashornDataStreamer \
        --launcher nds=tech.mubee.nashornDataStreamer/tech.mubee.nashorn.data.streamer.Main \
        --output nds-image

GREEN='\033[0;32m'
NC='\033[0m' # No Color

echo "\nBuilt custom nashorn-data-streamer image!"
echo "You can distribute this image without the need to install JDK/JRE."
echo "\nTo test your custom image, run 'nds' in the image's bin folder using the following command:"
echo "${GREEN}nds-image/bin/nds test.csv test.js${NC}\n"
