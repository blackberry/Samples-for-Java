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
package nfc.sample.tictactoe.commands;
import net.rim.device.api.command.AlwaysExecutableCommand;
import net.rim.device.api.command.ReadOnlyCommandMetadata;
import net.rim.device.api.ui.UiApplication;
import nfc.sample.tictactoe.Constants;
import nfc.sample.tictactoe.Utilities;
import nfc.sample.tictactoe.ui.GameScreen;
import nfc.sample.tictactoe.ui.SymbolSelectionScreen;

public class ChooseCrossCommand extends AlwaysExecutableCommand {

    
    private SymbolSelectionScreen current_screen;
    
    public ChooseCrossCommand(SymbolSelectionScreen ss_screen) {
        current_screen = ss_screen;
    }
    
	public void execute(ReadOnlyCommandMetadata metadata, Object context) {
	    
        Utilities.log("XXXX ChooseCrossCommand - execute");

	    // only ever called by player 1 from the symbol selection screen
	    GameScreen screen = GameScreen.getInstance(Constants.PLAYER_1,Constants.PLAYER_SYMBOL_CROSS);
	    
		synchronized(UiApplication.getUiApplication().getEventLock()) {
		    synchronized (UiApplication.getEventLock()) {
                UiApplication.getUiApplication().popScreen(current_screen);
		        UiApplication.getUiApplication().pushScreen(screen);
		    }
		}
	}

}
