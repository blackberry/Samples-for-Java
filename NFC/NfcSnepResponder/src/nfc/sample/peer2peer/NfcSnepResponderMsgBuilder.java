package nfc.sample.peer2peer;

import java.io.UnsupportedEncodingException;

import net.rim.device.api.io.nfc.NFCException;
import net.rim.device.api.io.nfc.ndef.NDEFMessage;
import net.rim.device.api.io.nfc.ndef.NDEFMessageUtils;
import net.rim.device.api.io.nfc.push.NDEFMessageBuilder;
import nfc.sample.peer2peer.ui.NfcSnepResponderScreen;

public class NfcSnepResponderMsgBuilder implements NDEFMessageBuilder {

    private NfcSnepResponderScreen _screen;

    public NfcSnepResponderMsgBuilder(NfcSnepResponderScreen screen) {
        this._screen = screen;
    }

    public NDEFMessage[] buildNDEFMessages() {
        Utilities.log("XXXX NfcSnepResponder Received Push Request");
        _screen.logEvent("rcvd:Received Push Request");

        NDEFMessage[] listOfNdefMessages = null;
        NDEFMessage myNdefMessage;

        try {
            Utilities.log("XXXX NfcSnepResponder Constructing Media Type: " + Constants.VCARD_MIME_TYPE);
            _screen.logEvent("send:Constructing Media Type: " + Constants.VCARD_MIME_TYPE);

            Utilities.log("XXXX NfcSnepResponder Constructing vCard Media Type NDEF Message: " + Constants.VCARD_DATA);
            _screen.logEvent("send:Constructing vCard Media Type NDEF Message: " + Constants.VCARD_DATA);

            myNdefMessage = NDEFMessageUtils.createMediaTypeNDEFMessage(Constants.VCARD_MIME_TYPE,
                    Constants.VCARD_DATA.getBytes("US-ASCII"));

            listOfNdefMessages = new NDEFMessage[] { myNdefMessage };

        } catch(NFCException e) {
            e.printStackTrace();
            Utilities.log("XXXX NfcSnepResponder Exception on creating NDEF Message: " + e.getMessage());
            _screen.logEvent("warn:Failed to create NDEFMessage");

        } catch(UnsupportedEncodingException e) {
            e.printStackTrace();
            Utilities.log("XXXX NfcSnepResponder Exception on conversion of vCard data: " + e.getMessage());
            _screen.logEvent("warn:Exception on conversion of vCard data");
        }

        return listOfNdefMessages;
    }
}
