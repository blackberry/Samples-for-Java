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

import com.samples.toolkit.ui.component.*;

import net.rim.device.api.ui.*;

/**
 * 
 */
public class PillButtonSet extends JustifiedEvenlySpacedHorizontalFieldManager 
{   
    private Field _selectedField;
    
    public PillButtonSet()
    {
    }
    
    protected void sublayout( int maxWidth, int maxHeight )
    {
        Field child;
        int numChildren = this.getFieldCount();
        int index = 0;
        if( numChildren == 1 ) {
            child = getField( index );
            if( child instanceof PillButtonField ) {
                ( (PillButtonField) child ).setDrawPosition( PillButtonField.DRAWPOSITION_SINGLE );
            }
        } else {
            child = getField( index );
            if( child instanceof PillButtonField ) {
                ( (PillButtonField) child ).setDrawPosition( PillButtonField.DRAWPOSITION_LEFT );
            }
            for( index = 1; index < numChildren - 1 ; index++ ) {
                child = getField( index );
                if( child instanceof PillButtonField ) {
                    ( (PillButtonField) child ).setDrawPosition( PillButtonField.DRAWPOSITION_MIDDLE );
                }
            }
            child = getField( index );
            if( child instanceof PillButtonField ) {
                ( (PillButtonField) child ).setDrawPosition( PillButtonField.DRAWPOSITION_RIGHT );
            }
        }
        super.sublayout( maxWidth, maxHeight );
    }
    
    public void setSelectedField( Field selectedField ) 
    {
        if( _selectedField == selectedField ) {
            return; // already selected
        }
        
        // Clear old one
        if( _selectedField instanceof PillButtonField ) {
            ( (PillButtonField) _selectedField ).setSelected( false );
        }
        
        _selectedField = selectedField;
        
        // Select New Field
        if( _selectedField instanceof PillButtonField ) {
            ( (PillButtonField) _selectedField ).setSelected( true );
        }
    }
}
