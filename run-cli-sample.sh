./gradlew build
cd samples
java -jar ../shaper-cli/build/libs/shaper-cli-0.1.0-SNAPSHOT.jar -i $1.yaml -o $1-out