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
package nfc.sample.llcp;

import net.rim.device.api.ui.UiApplication;
import nfc.sample.llcp.ui.RoleSelectionScreen;

public class LLCPDemo extends UiApplication {

    public static void main(String[] args) {
        Utilities.initLogging();
        LLCPDemo app = new LLCPDemo();
        app.enterEventDispatcher();
    }
    
    private LLCPDemo() {
        pushScreen(new RoleSelectionScreen());
    }

}
