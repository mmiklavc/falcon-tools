package com.michaelmiklavcic;

import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import javax.jms.*;

import org.junit.Test;

public class FalconMessageTest {

    @Test
    public void toString_prints_args() throws JMSException {
        MapMessage mapMessage = mock(MapMessage.class);
        when(mapMessage.getString("operation")).thenReturn("opABC");
        when(mapMessage.getString("status")).thenReturn("statABC");
        when(mapMessage.getString("workflowId")).thenReturn("idABC");
        when(mapMessage.getString("feedInstancePaths")).thenReturn("instanceABC");
        assertThat(new FalconMessage(mapMessage).toString(),
                   stringContainsInOrder(Arrays.asList("opABC", "statABC", "idABC", "instanceABC")));
    }

}
