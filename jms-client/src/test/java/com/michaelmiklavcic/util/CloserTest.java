package com.michaelmiklavcic.util;

import static org.mockito.Mockito.verify;

import java.io.*;

import javax.jms.*;

import org.junit.*;
import org.mockito.*;

public class CloserTest {

    @Mock private Connection conn;
    @Mock private Closeable closeable;
    private Closer closer;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        closer = new Closer();
    }

    @Test
    public void close_calls_close_for_connection() throws JMSException {
        closer.close(conn);
        verify(conn).close();
    }

    @Test
    public void close_calls_close_for_closeable() throws IOException {
        closer.close(closeable);
        verify(closeable).close();
    }

    @Test
    public void close_silently_handles_null_closeable() {
        Closeable closeable = null;
        closer.close(closeable);
    }

}
