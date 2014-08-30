package com.michaelmiklavcic;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;

import java.io.PrintStream;

import javax.jms.JMSException;

import org.junit.*;
import org.mockito.*;

public class SysoutHandlerTest {
    @Mock private PrintStream out;
    @Mock private FalconMessage message;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void prints_properties_to_sysout() throws JMSException {
        SysoutHandler handler = new SysoutHandler(out);
        handler.dispatch(message);
        verify(out).println(anyString());
    }

}
