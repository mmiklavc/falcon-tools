package com.michaelmiklavcic.util;

import java.io.*;

import javax.jms.*;

public class Closer {

    public void close(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (JMSException e) {
            // just a close
        }
    }

    public void close(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
            // just a close
        }
    }

}
