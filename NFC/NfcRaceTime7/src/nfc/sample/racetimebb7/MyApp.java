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
import net.rim.device.api.ui.UiApplication;

public class MyApp extends UiApplication {

    public static void main(String[] args) {
        MyApp app = new MyApp();
        NdefListenerManager ndef_mgr = NdefListenerManager.getInstance();
        Utilities.initLogging();
        if  (args.length > 0) {
            if (args[0].equals("auto")) {
                Listener listener = new Listener();
                ndef_mgr.registerListener(listener);
            }
        } else {
            Listener listener = new Listener();
            ndef_mgr.registerListener(listener);
            app.pushScreen(TimerScreen.getTimerScreen());
            app.enterEventDispatcher();
        }
        Utilities.log("XXXX exiting");
    }

    public MyApp() {
    }

}
