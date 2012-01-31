package nfc.sample.Ndef.Write;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;

import javax.microedition.io.Connector;

import net.rim.device.api.io.nfc.NFCException;
import net.rim.device.api.io.nfc.ndef.NDEFMessage;
import net.rim.device.api.io.nfc.ndef.NDEFMessageUtils;
import net.rim.device.api.io.nfc.ndef.NDEFRecord;
import net.rim.device.api.io.nfc.ndef.NDEFTagConnection;
import net.rim.device.api.io.nfc.readerwriter.DetectionListener;
import net.rim.device.api.io.nfc.readerwriter.Target;
import nfc.sample.Ndef.Write.ui.TagCreatorScreen;

/**
 * 
 * This class implement the DetectionListener interface and it's primary function is to receive notifications when a smart tag is
 * brought into the NFC antenna's range
 */
public class NfcWriteNdefSmartTagListener implements DetectionListener {

    private TagCreatorScreen _screen;

    private int _tag_type;

    private Hashtable _tag_attrs;

    private final String URL_TEXT_LOCALE = "en-US";

    public NfcWriteNdefSmartTagListener() {
        super();
    }

    public NfcWriteNdefSmartTagListener(TagCreatorScreen screen, int type) {
        this();
        this._screen = screen;
        _tag_type = type;
    }

    /*
     * This is where we get informed of a tag in the proximity of NFC antenna
     */
    public void onTargetDetected(Target smartTagTarget) {

        Utilities.log("XXXX NfcWriteNdefSmartTagListener detected a tag");

        String target_uri = smartTagTarget.getUri(Target.NDEF_TAG);

        _tag_attrs = _screen.getTagAttributes();

        switch(_tag_type) {
        case Constants.TYPE_SP:
            writeSpTag(target_uri);
            break;
        case Constants.TYPE_CUSTOM:
            writeCustomTag(target_uri);
            break;
        case Constants.TYPE_TEXT:
            writeTextTag(target_uri);
            break;
        case Constants.TYPE_URI:
            writeUriTag(target_uri);
            break;
        default:
            Utilities.log("XXXX Unrecognised type requested:" + _tag_type);
        }

    }

    private void writeUriTag(String target_uri) {

        Utilities.log("XXXX Writing URI tag");
        NDEFTagConnection tagConnection = null;
        String uri = (String) _tag_attrs.get(Constants.TAG_ATTRIBUTE_URI);

        try {
            NDEFMessage uriNdefMessage = NDEFMessageUtils.createUriNDEFMessage(uri);
            tagConnection = (NDEFTagConnection) Connector.open(target_uri);
            tagConnection.write(uriNdefMessage);
            Utilities.log("XXXX NfcWriteNdefSmartTagListener URI Tag written successfully");
            _screen.logEvent("Custom Tag written successfully");
        } catch(Exception e) {
            _screen.logEvent(e.getClass().getName() + ":" + e.getMessage());
            Utilities.log("XXXX " + e.getClass().getName() + ":" + e.getMessage());
        }

    }

    private void writeTextTag(String target_uri) {

        Utilities.log("XXXX Writing Text tag");
        NDEFTagConnection tagConnection = null;
        String text = (String) _tag_attrs.get(Constants.TAG_ATTRIBUTE_TEXT);

        try {
            NDEFMessage externalNdefMessage = NDEFMessageUtils.createTextNDEFMessage(text, URL_TEXT_LOCALE);
            tagConnection = (NDEFTagConnection) Connector.open(target_uri);
            tagConnection.write(externalNdefMessage);
            Utilities.log("XXXX NfcWriteNdefSmartTagListener Text Tag written successfully");
            _screen.logEvent("Custom Tag written successfully");
        } catch(Exception e) {
            _screen.logEvent(e.getClass().getName() + ":" + e.getMessage());
            Utilities.log("XXXX " + e.getClass().getName() + ":" + e.getMessage());
        }

    }

    private void writeCustomTag(String target_uri) {

        Utilities.log("XXXX Writing Custom tag");
        NDEFTagConnection tagConnection = null;
        String content = (String) _tag_attrs.get(Constants.TAG_ATTRIBUTE_CONTENT);
        String domain = (String) _tag_attrs.get(Constants.TAG_ATTRIBUTE_DOMAIN);
        String type = (String) _tag_attrs.get(Constants.TAG_ATTRIBUTE_TYPE);

        Utilities.log("XXXX domain:" + domain);
        Utilities.log("XXXX type:" + type);
        Utilities.log("XXXX content:" + content);

        try {
            NDEFMessage externalNdefMessage = NDEFMessageUtils.createExternalTypeMessage(domain, type,
                    content.getBytes("US-ASCII"));
            tagConnection = (NDEFTagConnection) Connector.open(target_uri);
            tagConnection.write(externalNdefMessage);
            Utilities.log("XXXX NfcWriteNdefSmartTagListener Custom Tag written successfully");
            _screen.logEvent("Custom Tag written successfully");
        } catch(Exception e) {
            _screen.logEvent(e.getClass().getName() + ":" + e.getMessage());
            Utilities.log("XXXX " + e.getClass().getName() + ":" + e.getMessage());
        }

    }

    private void writeSpTag(String target_uri) {
        Utilities.log("XXXX Writing SP tag");
        NDEFTagConnection tagConnection = null;
        String willWriteMsg = "Writing Smart Poster Tag " + _screen.getTagDetails() + ",Language: " + URL_TEXT_LOCALE;

        _screen.logEvent(willWriteMsg);

        try {
            NDEFMessage smartPosterTag = createSmartPosterTag();
            tagConnection = (NDEFTagConnection) Connector.open(target_uri);
            tagConnection.write(smartPosterTag);
            Utilities.log("XXXX NfcWriteNdefSmartTagListener SP Tag written successfully");
            _screen.logEvent("SP tag written successfully");
        } catch(NFCException e) {
            e.printStackTrace();
            Utilities.log("XXXX NfcWriteNdefSmartTagListener NFCException");

        } catch(IOException e) {
            e.printStackTrace();
            Utilities.log("XXXX NfcWriteNdefSmartTagListener IOException");
        }

    }

    private NDEFMessage createSmartPosterTag() throws IOException {

        // do this the old school way!

        String uri = (String) _tag_attrs.get(Constants.TAG_ATTRIBUTE_URI);
        String text = (String) _tag_attrs.get(Constants.TAG_ATTRIBUTE_TEXT);

        NDEFMessage rootMessage = new NDEFMessage(); // (SP (TEXT, URL) message
        NDEFMessage ndefMessage = new NDEFMessage(); // (TEXT, URL) message

        NDEFRecord rootRecord = new NDEFRecord(); // Smart Poster Record
        NDEFRecord tagTitleRecord = new NDEFRecord(); // Tag Title TEXT record
        NDEFRecord tagUrlRecord = new NDEFRecord(); // Tag URL record

        ByteArrayOutputStream titlePayload = new ByteArrayOutputStream(); // to build title
        ByteArrayOutputStream urlPayload = new ByteArrayOutputStream(); // to build URL

        /*
         * ================ Record 0 ===========================================
         * 
         * This is the NDEF record that represents the title associated with the URL that will the URL part of the Smart Poster
         * Tag
         */
        titlePayload.write((byte) URL_TEXT_LOCALE.length()); // status byte: character encoding indicator bit plus length of
                                                             // locale language field
        titlePayload.write(URL_TEXT_LOCALE.getBytes("US-ASCII")); // locale language
        /*
         * This is the text to be associated with the Smart Poster Tag
         */
        titlePayload.write(text.getBytes("UTF-8")); // Text
        titlePayload.flush();
        /*
         * Construct the record itself
         */
        tagTitleRecord.setId("0"); // record Id
        tagTitleRecord.setType(NDEFRecord.TNF_WELL_KNOWN, "T"); // It's TEXT type
        tagTitleRecord.setPayload(titlePayload.toByteArray()); // construct the record

        /*
         * ================ Record 1 ===========================================
         * 
         * This is the NDEF record that represents the URL associated with the title that will the Text part of the Smart Poster
         * Tag
         */
        urlPayload.write((byte) 0x01); // coded abbreviation for "http://www."
        urlPayload.write(uri.getBytes()); // The rest of the URL
        urlPayload.flush();
        /*
         * Construct the record itself
         */
        tagUrlRecord.setId("1"); // record Id
        tagUrlRecord.setType(NDEFRecord.TNF_WELL_KNOWN, "U"); // It's a URL(I) type
        tagUrlRecord.setPayload(urlPayload.toByteArray()); // construct the record

        /*
         * ================ Construct an NDEF MEssage ==========================
         * 
         * This NDEF Message comprises the Title and URL records (TEXT, URL)
         */
        ndefMessage.setRecords(new NDEFRecord[] { tagTitleRecord, tagUrlRecord });

        /*
         * ================ Wrap the message as a Smart Poster Tag ============
         * 
         * What we have now is a single NDEF message with two records, a URL and some text associated with it. We now need to make
         * that into a Smart poster Tag which is a well known type: "Sp"
         */
        rootRecord.setType(NDEFRecord.TNF_WELL_KNOWN, "Sp"); // Smart Poster Type
        rootRecord.setPayload(ndefMessage.getBytes()); // construct the record

        /*
         * ================ Construct an NDEF MEssage ==========================
         * 
         * This NDEF message contains a single record encoding the Smart Poster Tag
         */
        rootMessage.setRecords(new NDEFRecord[] { rootRecord }); // (SP, (TEXT, URL))

        /*
         * Return the Smart Poster Tag
         */
        return rootMessage;
    }
}
