falcon-tools
============
Utilities for Apache Falcon

feedbuilder - build feeds dynamically

 - modify 'feedlist' in 'replace.sh' to include values for each specific feed. Defaults are set in the default_load function
 - exec replace.sh, change env vars accordingly for each feed you wish to create

jms-client - subscribe to a JMS topic and handle messages

  - args - <dest name> <topic name>
  - java -cp .:jms-client-0.0.1-SNAPSHOT.jar com.michaelmiklavcic.JMSClient tcp://sandbox.hortonworks.com:61616 FALCON.ENTITY.TOPIC
  - To see messages to stdout, use the defaults. Otherwise create your own handler by overriding com.michaelmiklavcic.Handler
  - Modify com.michaelmiklavcic.FalconMessage to extract other parameters from the JMS messages
  - Subscribe to catch-all Falcon topic - FALCON.ENTITY.TOPIC, or feed/process-specific, e.g. FALCON.rawEmailIngestProcess
