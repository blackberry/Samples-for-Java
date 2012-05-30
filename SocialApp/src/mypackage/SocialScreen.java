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

package mypackage;

import net.rim.blackberry.api.sendmenu.SendCommand;
import net.rim.blackberry.api.sendmenu.SendCommandContextKeys;
import net.rim.blackberry.api.sendmenu.SendCommandException;
import net.rim.blackberry.api.sendmenu.SendCommandRepository;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.decor.BorderFactory;
import net.rim.device.api.ui.picker.FilePicker;

import org.json.me.JSONException;
import org.json.me.JSONObject;

/**
 * A class extending the MainScreen class, which provides default standard
 * behavior for BlackBerry GUI applications.
 */
public final class SocialScreen extends MainScreen {
	/** Figured out by experimentation with SendCommand.getId(). */
	private final String TWITTER_TEXT_ID = "Twitter_text";
	private final String TWITTER_PATH_ID = "Twitter_path";
	private final String FACEBOOK_TEXT_ID = "Facebook_text";
	private final String FACEBOOK_PATH_ID = "Facebook_path";
	
	private ButtonField shareTextButton;
	private EditField textField;
	private ButtonField filePickButton;
	private SendCommand[] commandsAll;
	private SendCommand[] commands;

	/**
	 * Creates a new MyScreen object
	 */
	public SocialScreen() {
		setBorder(BorderFactory.createBitmapBorder(new XYEdges(9, 9, 9, 9), Bitmap.getBitmapResource("border.png")));
		setTitle(new LabelField("(: The Social App :)", DrawStyle.HCENTER|Field.FIELD_HCENTER));	

		filePickButton = new ButtonField("Photo Share") {
			protected boolean navigationClick(int status, int time) {
				FilePicker picker = FilePicker.getInstance();
				picker.setFilter(".jpg:.png");
				picker.setView(FilePicker.VIEW_PICTURES);
				String path = picker.show();

				if (path != null) {
					// Creating the data context as a JSONObject
					JSONObject context = new JSONObject();
					try {
						context.put(SendCommandContextKeys.PATH, path);
						/**
						 * Since we are sharing a file path, you CANNOT add a
						 * SUBJECT or TEXT type data. Only PATH seems to be
						 * allowed.
						 */
					} catch (JSONException e) {

					}

					/**
					 * Getting the available SendCommand(s) for our SendCommand
					 * type TYPE_TEXT by providing our data context. The 3rd
					 * parameter is a boolean that indicates whether you want
					 * all results or just the results that are possible based on the context.
					 * Ideally this boolean param would be set to false,
					 * but there seems to be a bug with BBM as it throws an
					 * exception during the query. As a workaround, I am getting
					 * all first and then filtering out the ones I dont need.
					 */
					commandsAll = SendCommandRepository.getInstance().get(SendCommand.TYPE_PATH, context, true);
					commands = new SendCommand[2];
										
					for (int i = 0; i < commandsAll.length; i++) {						
						if (commandsAll[i].getId().equals(TWITTER_PATH_ID)) {
							commands[0] = commandsAll[i];
						}
						if (commandsAll[i].getId().equals(FACEBOOK_PATH_ID)) {
							commands[1] = commandsAll[i];
						}
					}
					// Displays our SendCommands to the user.
					displayCommands();
				}

				return true;
			}

			public int getPreferredWidth() {
				return Display.getWidth();
			}
		};

		add(filePickButton);

		add(new SeparatorField());
		add(new SeparatorField());
		add(new SeparatorField());

		// An EditField to let the user type in the text to share.
		textField = new EditField("", "Share this please.");
		textField.setBorder(BorderFactory.createBitmapBorder(new XYEdges(9, 9, 9, 9), Bitmap.getBitmapResource("border.png")));
		textField.setTextFillColor(Color.WHITE);
		textField.setTextStrokeColor(Color.WHITE);

		// A button that initiates the Text Share
		shareTextButton = new ButtonField("Text Share") {
			protected boolean navigationClick(int arg0, int arg1) {
				// Creating the data context as a JSONObject
				JSONObject context = new JSONObject();
				try {
					context.put(SendCommandContextKeys.TEXT, textField.getText());
					/**
					 * For text sharing, you can also add a subject as follows. This is useful for sharing via email.
					 * context.put(SendCommandContextKeys.SUBJECT, "your subject");
					 */
				} catch (JSONException e) {

				}

				commandsAll = SendCommandRepository.getInstance().get(SendCommand.TYPE_TEXT, context, true);				
				commands = new SendCommand[2];

				for (int i = 0; i < commandsAll.length; i++) {					
					if (commandsAll[i].getId().equals(TWITTER_TEXT_ID)) {
						commands[0] = commandsAll[i];
					}
					if (commandsAll[i].getId().equals(FACEBOOK_TEXT_ID)) {
						commands[1] = commandsAll[i];
					}
				} 
				// Displays our SendCommands to the user.
				displayCommands();

				return true;
			}

			public int getPreferredWidth() {
				return Display.getWidth();
			}
		};

		add(textField);
		add(shareTextButton);

	}

	/**
	 * Displays our SendCommands to the user. Note that you can also use the
	 * prebuilt SendCommandScreen or SendCommandMenu to show the SendCommands.
	 * But I wanted to demo how you can add the SendCommands in a Screen that
	 * you own.
	 * 
	 */
	private void displayCommands() {
		SendDialog dialog = new SendDialog(Dialog.D_OK, "Lets be social!", Dialog.OK, null);
		
		if(commands[0]!=null){
			dialog.add(new ButtonField("Twitter") {
				protected boolean navigationClick(int status, int time) {
					try {
						commands[0].run();
					} catch (SendCommandException e) {
						SendDialog.inform(Dialog.D_OK, e.toString(), Dialog.OK, null);
					}
					return true;
				}
	
				public int getPreferredWidth() {
					return Display.getWidth();
				}	
			});
		}
		
		if(commands[1]!=null){
			dialog.add(new ButtonField("Facebook") {	
				protected boolean navigationClick(int status, int time) {
					try {
						commands[1].run();
					} catch (SendCommandException e) {
						SendDialog.inform(Dialog.D_OK, e.toString(), Dialog.OK, null);
					}
					return true;
				}
	
				public int getPreferredWidth() {
					return Display.getWidth();
				}	
			});
		}
		
		UiApplication.getUiApplication().pushScreen(dialog);
	}
}
