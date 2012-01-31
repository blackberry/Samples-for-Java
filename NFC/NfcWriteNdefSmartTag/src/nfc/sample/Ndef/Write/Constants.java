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

public interface Constants {

    public static final String MYAPP_VERSION = "1.1.1";

    public static final int BTN_STATE=0;
    
    public static final int EMULATE_SC=0;
    public static final int EMULATE_SR=1;
    
    public static final int TYPE_URI=0;
    public static final int TYPE_SP=1;
    public static final int TYPE_TEXT=2;
    public static final int TYPE_CUSTOM=3;
    
    public static final long LISTENER_STATE_TOKEN = 0x9caefb8a740c66c1L;
    
    public static final String TAG_ATTRIBUTE_URI="URI";
    public static final String TAG_ATTRIBUTE_TEXT="TEXT";
    public static final String TAG_ATTRIBUTE_DOMAIN="DOMAIN";
    public static final String TAG_ATTRIBUTE_CONTENT="CONTENT";
    public static final String TAG_ATTRIBUTE_TYPE="TYPE";

    
}