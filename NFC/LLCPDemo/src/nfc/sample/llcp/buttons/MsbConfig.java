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
package nfc.sample.llcp.buttons;

import java.util.Hashtable;


/**
 * A MultiStateButtonField has a series of states, each of which has multiple visual indications depending on
 * 	focused/unfocused
 * 	click/unclick
 *
 */
public class MsbConfig {

	private Hashtable states = new Hashtable();
	
	public void addState(MsbState state) {
		Integer key = new Integer(state.getState_id());
		states.put(key,state);
	}
	
	public MsbState getState(int state_id) {
		MsbState state = (MsbState) states.get(new Integer(state_id));
		return state;
	}
	
}
