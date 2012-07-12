/*
* Copyright (c) 2012 Research In Motion Limited.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package nfc.sample.llcp.nfc;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.io.Connector;

import net.rim.device.api.io.IOUtilities;
import net.rim.device.api.io.nfc.llcp.LLCPConnection;
import net.rim.device.api.io.nfc.llcp.LLCPConnectionNotifier;
import net.rim.device.api.util.Arrays;
import nfc.sample.llcp.Utilities;
import nfc.sample.llcp.ui.ActivityScreen;

public class LlcpReceiver implements Runnable {

    private ActivityScreen _screen;

    public LlcpReceiver(ActivityScreen screen) {
        _screen = screen;
        _screen.logEvent("Constructing LlcpReceiver");
    }

    public void run() {
        _screen.logEvent("LlcpReceiver starting...");
        LLCPConnectionNotifier llcp_conn_notifier = null;
        LLCPConnection llcp_conn = null;
        InputStream in = null;
        boolean completed_ok = false;
        try {
            // note mode=server
            _screen.logEvent("Obtaining connection notifier");
            llcp_conn_notifier = (LLCPConnectionNotifier) Connector.open("urn:nfc:sn:llcpdemo;mode=server;timeout=120");
            _screen.logEvent("Got LlcpConnectionNotifier");
            llcp_conn = llcp_conn_notifier.acceptAndOpen();
            _screen.logEvent("Got LlcpConnection");
            in = llcp_conn.getInputStream();
            _screen.logEvent("Got InputStream");
            byte[] data = new byte[256];
            int bytesRead = in.read(data, 0, 256);
            _screen.logEvent("Rcvd " + data.length + " bytes of data");
            String text = new String(data, "US-ASCII");
            _screen.logEvent("Rcvd text:" + text);
            completed_ok = true;
        } catch(Exception e) {
            _screen.logEvent(e.getClass().getName() + ":" + e.getMessage());
            _screen.logEvent("LlcpReceiver is exiting abnormally");
        } finally {
            close(llcp_conn_notifier, llcp_conn, in);
        }
        if(completed_ok) {
            _screen.logEvent("LlcpReceiver finished OK");
        }
    }

    public void close(LLCPConnectionNotifier llcp_conn_notifier, LLCPConnection llcp_conn, InputStream in) {
        try {
            if(in != null) {
                in.close();
                _screen.logEvent("Stream closed");
            }
        } catch(Exception e) {
            _screen.logEvent("Stream closed error, " + e.getMessage());
            _screen.logEvent(e.getClass().getName() + ":" + e.getMessage());
        }
        try {
            if(llcp_conn != null) {
                llcp_conn.close();
                _screen.logEvent("Connection closed");
            }
        } catch(Exception e) {
            _screen.logEvent("Connection closed error, " + e.getMessage());
            _screen.logEvent(e.getClass().getName() + ":" + e.getMessage());
        }
        try {
            if(llcp_conn_notifier != null) {
                llcp_conn_notifier.close();
                _screen.logEvent("Connection notifier closed");
            }
        } catch(Exception e) {
            _screen.logEvent("Connection notifier closed error, " + e.getMessage());
            _screen.logEvent(e.getClass().getName() + ":" + e.getMessage());
        }
    }

    public void start() {
        Utilities.log("XXXX " + Thread.currentThread().getName() + " : " + "starting LcpReceiver thread");
        Thread thread = new Thread(this);
        thread.start();
    }

}
