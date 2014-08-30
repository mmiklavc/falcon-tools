package com.michaelmiklavcic;

import java.io.PrintStream;

import javax.jms.JMSException;

public class SysoutHandler implements Handler {

    private PrintStream out;

    public SysoutHandler(PrintStream out) {
        this.out = out;
    }

    public void dispatch(FalconMessage message) throws JMSException {
        out.println(message.toString());
    }

}
