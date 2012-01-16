package nfc.sample.virtual.target;

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
public interface CommonADPUs {

    public static final byte[] simpleISO14443Capdu = new byte[] { 
              (byte) 0xA0 // CLA - proprietary command type
            , (byte) 0x37 // INS - arbitrary instruction code
            , (byte) 0x00 // P1  - not used
            , (byte) 0x00 // P2  - not used
            , (byte) 0x01 // L(c)  - length of 1 byte 
            , (byte) 0x99 // command payload (1 byte just for fun )
            , (byte) 0x00 // L(e) ( up to 256 expected in response )
    };

    public static final byte[] goodResponseRapdu = new byte[] { 
              (byte) 0x90 // SW1 ...
            , (byte) 0x00 // SW2 ... goodness! Deep joy!
    };

    public static final byte[] errorRapdu = new byte[] { 
              (byte) 0x6F // SW1 ...
            , (byte) 0x00 // SW2 ... No precise diagnosis
    };

    public static final byte[] insNotSupportedRapdu = new byte[] { 
        (byte) 0x6D // SW1 ...
      , (byte) 0x00 // SW2 ... INS Not SUpported
    };

    // ============== NFC Forum Type 4 NDEF Tag Application Select C-APDU =====
    //
    // AID of NDEF Application is D2 76 00 00 85 01 01
    // APDU to send as documented in NFC Forum Type 4 Tag Specification v2.0
    //
    
    public static final byte[] ndefTagApplicationSelectV1Capdu = new byte[] { 
        (byte) 0x00 // CLA - See NFC Forum Type 4 NDEF Tag Application Select C-APDU
      , (byte) 0xA4 // INS - ... " ...
      , (byte) 0x04 // P1  - ... " ... - select by name
      , (byte) 0x00 // P2  - ... " ... - first and only occurrence
      , (byte) 0x07 // L(c)  - length of 7 bytes 
      , (byte) 0xD2 // Per spec (v2.0)
      , (byte) 0x76 // ... " ...
      , (byte) 0x00 // ... " ...
      , (byte) 0x00 // ... " ...
      , (byte) 0x85 // ... " ...
      , (byte) 0x01 // ... " ...
      , (byte) 0x01 // ... " ...
      , (byte) 0x00 // L(e) -- response data expected
    };

    // AID of NDEF Application is D2 76 00 00 85 01 00
    // APDU to send as documented in the older NFC Forum Type 4 Tag Specification v1.0
    
    public static final byte[] ndefTagApplicationSelectV0Capdu = new byte[] { 
        (byte) 0x00 // CLA - See NFC Forum Type 4 NDEF Tag Application Select C-APDU
      , (byte) 0xA4 // INS - ... " ...
      , (byte) 0x04 // P1  - ... " ... - select by name
      , (byte) 0x00 // P2  - ... " ... - first and only occurrence
      , (byte) 0x07 // L(c)  - length of 7 bytes 
      , (byte) 0xD2 // Per spec (v1.0)
      , (byte) 0x76 // ... " ...
      , (byte) 0x00 // ... " ...
      , (byte) 0x00 // ... " ...
      , (byte) 0x85 // ... " ...
      , (byte) 0x01 // ... " ...
      , (byte) 0x00 // ... " ...
                    // L(e) -- not specified
    };

    public static final byte[] okRapdu = new byte[] { 
              (byte) 0x90 // File Control information may be returned
            , (byte) 0x00 // ... " ...
    };
    
    public static final byte[] noNdefSupportRapdu = new byte[] { 
              (byte) 0x6A // NDEF Tag Application not found
            , (byte) 0x82 // ... " ...
    };

}