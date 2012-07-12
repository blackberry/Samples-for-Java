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

import java.io.OutputStream;
import javax.microedition.io.Connector;
import net.rim.device.api.io.nfc.llcp.LLCPConnection;
import nfc.sample.llcp.ui.ActivityScreen;

public class LlcpSender implements Runnable {

    private ActivityScreen _screen;
    private String _message;
    
    public LlcpSender(ActivityScreen screen, String message) {
        _screen = screen;
        _message = message;
    }

    public void run() {
        _screen.logEvent("LlcpSender starting...");
        LLCPConnection llcp_conn = null;
        OutputStream out = null;
        boolean completed_ok = false;
        try {
            // note mode=client
            _screen.logEvent("Obtaining connection");
            llcp_conn = (LLCPConnection) Connector.open("urn:nfc:sn:llcpdemo;mode=client");
            _screen.logEvent("Got LlcpConnection");
            out = llcp_conn.getOutputStream();
            _screen.logEvent("Got OutputStream");
            byte[] data = _message.getBytes("US-ASCII");
            out.write(data, 0, data.length);
            out.flush();
            _screen.logEvent("Sent " + data.length + " bytes of data");
            completed_ok = true;
        } catch(Exception e) {
            _screen.logEvent(e.getClass().getName() + ":" + e.getMessage());
            _screen.logEvent("LlcpSender is exiting abnormally");
        } finally {
            _screen.logEvent("Closing resources......");
            // make sure we don't close and interrupt the receiver before the data we sent has been read 
            delay(1000);
            close(llcp_conn, out);
        }
        if (completed_ok) {
            _screen.logEvent("LlcpSender finished OK");
        }
    }

    private void delay(int delay_ms) {
        try {
            Thread.sleep(delay_ms);
        } catch(InterruptedException e) {
        }
    }

    public void close(LLCPConnection llcp_conn, OutputStream out) {
        try {
            if(out != null) {
                out.close();
                _screen.logEvent("Stream closed");
            }
        } catch(Exception e) {
            _screen.logEvent(e.getClass().getName() + ":" + e.getMessage());
            _screen.logEvent("LlcpSender stream close error");
        }
        try {
            if(llcp_conn != null) {
                llcp_conn.close();
                _screen.logEvent("Connection closed");
            }
        } catch(Exception e) {
            _screen.logEvent(e.getClass().getName() + ":" + e.getMessage());
            _screen.logEvent("LlcpSender connection close error");
        }
    }

    public void start() {
        Thread thread = new Thread(this);
        thread.start();
    }

}
