package com.michaelmiklavcic;

import javax.jms.*;

/**
 * Extract JMS entries in this class
 */
public class FalconMessage {

    private String stringVal;

    public FalconMessage(MapMessage message) throws JMSException {
        extract(message);
    }

    private void extract(MapMessage msg) throws JMSException {
        stringVal = "Reading message: operation=" + msg.getString("operation") + " || status=" + msg.getString("status")
                + " || workflowid=" + msg.getString("workflowId") + " || feedinstance=" + msg.getString("feedInstancePaths");
    }

    @Override
    public String toString() {
        return stringVal;
    }
}
