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


 
package com.samples.toolkit.ui.container;


import net.rim.device.api.ui.container.*;

/**
 * 
 */
public class GridFlowFieldManager extends FlowFieldManager {
    
    public GridFlowFieldManager() {
        this( 0 );
    }
    
    public GridFlowFieldManager( long style ) {
        super( style );
    }
    
    /*
    protected int moveFocus(int amount, int status, int time) {
        
        int axis = getNavigationAxis(status);
        // TODO should we realy clamp?
        int amountSign = MathUtilities.clamp( -1, amount, 1 );
        
        int nextFocusIndex = nextFocus( amountSign, axis );
        if( nextFocusIndex < 0) {
            // This will prevent the focus from moving left and right when at the top and bottom row, respectively
            return super.moveFocus( 0, status, time );
        }
        
        return super.moveFocus( amount, status, time );
    }
    */
}

