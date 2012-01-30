package nfc.sample.peer2peer;

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

public interface Constants {

    public static final String MYAPP_VERSION = "1.0.4";
    public static final int DISPLAY_COLUMN_WIDTH = 8;

    public static final int VCARD_RESPONDER_BTN_STATE=0;
    public static final int VCARD_RESPONDER=0;

    public static final String VCARD_MIME_TYPE = "text/x-vCard";
    public static final String VCARD_DATA = 
        "BEGIN:VCARD\n" +
        "VERSION:3.0\n" +
        "N:Guy;NFC;;;\n" +
        "FN:NFC Guy\n" + 
        "ADR;TYPE=WORK:;;200, Bath Road;Slough;Berkshire;SL1 3XE;UK\n" + 
        "TEL;TYPE=PREF,WORK:+44 1234 123000\n" +
        "TEL;TYPE=CELL:+44 4321 321000\n" +
        "TEL;TYPE=FAX:+44 1212 123321\n" +
        "EMAIL;TYPE=INTERNET:nfc_guy@rim.com\n" +
        "TITLE:Senior Application Development Consultant\n" +
        "ORG:Developer Relations\n" +
        "CATEGORIES:Business\n" +
        "URL:http://www.rim.com\n" +
        "X-ORG-URL:http://www.rim.com\n" +
        "END:VCARD";
}