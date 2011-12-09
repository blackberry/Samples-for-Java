/*
* Copyright (c) 2011 Research In Motion Limited.
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

package com.blackberry.toolkit.sample.youtube;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BitmapField;

public class BitmapFieldButton extends BitmapField {

	private Runnable _commandAction;

	public BitmapFieldButton(Bitmap bitmap, long style) {
		super(bitmap, style);
	}

	protected boolean invokeAction(int action) {
		if (_commandAction != null) {
			UiApplication.getUiApplication().invokeLater(_commandAction);
			return true;
		}
		return super.invokeAction(action);
	}

	public void setCommandAction(Runnable commandAction) {
		_commandAction = commandAction;
	}

	public Runnable getCommandAction() {
		return _commandAction;
	}
}