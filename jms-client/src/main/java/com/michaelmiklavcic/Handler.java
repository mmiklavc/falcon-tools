package com.michaelmiklavcic;

import javax.jms.JMSException;

public interface Handler {

    void dispatch(FalconMessage message) throws JMSException;

}
