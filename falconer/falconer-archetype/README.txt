To use the archetype it has to be installed first.

$ mvn install

Use the archetype by issuing the following command

$ mvn archetype:generate \
    -DarchetypeGroupId=com.michaelmiklavcic \
    -DarchetypeArtifactId=falconer-project-archetype \
    -DarchetypeVersion=0.1.0-SNAPSHOT \
    -DgroupId=com.YOUR-DOMAIN-HERE \
    -DartifactId=YOUR-ARTIFACT-ID-HERE