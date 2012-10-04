package nfc.sample.racetimebb7;
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
import java.io.UnsupportedEncodingException;
import java.util.Timer;

import net.rim.device.api.io.nfc.ndef.NDEFMessage;
import net.rim.device.api.io.nfc.ndef.NDEFMessageListener;
import net.rim.device.api.io.nfc.ndef.NDEFRecord;

public class Listener implements NDEFMessageListener {

    Timer timer;

    public void onNDEFMessageDetected(NDEFMessage msg) {
        Utilities.log("XXXX onNDEFMessageDetected");
        NDEFRecord[] records = msg.getRecords();
        int numRecords = records.length;
        if(numRecords > 0) {
            byte[] payloadBytes = records[0].getPayload();
            try {
                String ascii_payload = new String(payloadBytes,"US-ASCII");
                Utilities.log("XXXX payload="+ascii_payload);
                if (ascii_payload.equals("start")) {
                    startTimer();
                }
                if (ascii_payload.equals("stop")) {
                    stopTimer();
                }
            } catch(UnsupportedEncodingException e) {
                Utilities.log("XXXX "+e.getClass().getName()+":"+e.getMessage());
            }
        }

    }
    
    private void startTimer() {
        RaceTimer race_timer = new RaceTimer();
        timer = new Timer();
        timer.scheduleAtFixedRate(race_timer, 1000, 1000);
    }

    private void stopTimer() {
        timer.cancel();
    }

}
