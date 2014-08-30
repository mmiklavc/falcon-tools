package com.michaelmiklavcic;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

import org.junit.*;
import org.mockito.*;

public class JMSClientTest {

    @Mock private TopicConnectionManager manager;
    @Mock private Handler handler;
    @Mock private FalconMessage message;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void starts_manager_and_receives_messages() throws Exception {
        when(manager.receive(anyInt())).thenReturn(message);
        final JMSClient client = new JMSClient(manager, handler);
        Thread thread = new Thread(new Runnable() {
            public void run() {
                client.start(new String[] {
                        "tcp://dest",
                        "jms.topic" });
            }
        });
        thread.start();
        Thread.sleep(10);
        client.shutdown();
        InOrder inOrder = inOrder(manager, handler);
        inOrder.verify(manager, atLeastOnce()).start("tcp://dest", "jms.topic");
        inOrder.verify(manager, atLeastOnce()).receive(1000);
        inOrder.verify(handler, atLeastOnce()).dispatch(message);
        inOrder.verify(manager, atLeastOnce()).close();
    }

    @Test
    public void skips_null_messages() throws Exception {
        when(manager.receive(anyInt())).thenReturn(null);
        final JMSClient client = new JMSClient(manager, handler);
        Thread thread = new Thread(new Runnable() {
            public void run() {
                client.start(new String[] {
                        "tcp://dest",
                        "jms.topic" });
            }
        });
        thread.start();
        Thread.sleep(10);
        client.shutdown();
        verify(handler, never()).dispatch(any(FalconMessage.class));
        verify(manager, atLeastOnce()).close();
    }

}
