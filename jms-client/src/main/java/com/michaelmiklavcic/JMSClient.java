package com.michaelmiklavcic;

import javax.jms.JMSException;

import com.michaelmiklavcic.util.Closer;

/**
 * All current available properties can be seen in the Falcon source here: 
 * https://github.com/apache/incubator-falcon/blob/master/common/src/main/java/org/apache/falcon/workflow/WorkflowExecutionArgs.java
 */
public class JMSClient {
    private TopicConnectionManager manager;
    private Handler handler;
    private boolean running;

    public static void main(String[] args) {
        int exitCode = new JMSClient(new TopicConnectionManager(), new SysoutHandler(System.out)).start(args);
        System.exit(exitCode);
    }

    public JMSClient(TopicConnectionManager manager, Handler handler) {
        this.manager = manager;
        this.handler = handler;
    }

    public int start(String[] args) {
        if (args.length != 2) {
            System.out.println("Program takes two arguments: <dest_name> <topic_name>");
            return 1;
        }
        final String destName = args[0];
        final String topicName = args[1];
        printArgs(destName, topicName);
        try {
            manager.start(destName, topicName);
            running = true;
            while (isRunning()) {
                FalconMessage message = manager.receive(1000);
                if (message != null) {
                    handler.dispatch(message);
                }
            }
        } catch (JMSException e) {
            System.out.println("Exception occurred: " + e.toString());
            return 1;
        } finally {
            new Closer().close(manager);
        }
        return 0;
    }

    private void printArgs(final String destName, final String topicName) {
        System.out.println("Destination name is " + destName);
        System.out.println("Topic name is " + topicName);
    }

    private boolean isRunning() {
        return running;
    }

    public void shutdown() {
        running = false;
    }
}
