package mypackage;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.decor.BackgroundFactory;
import net.rim.device.api.ui.decor.BorderFactory;

public class SendDialog extends Dialog{
	public SendDialog(int type, String message, int defaultChoice, Bitmap bitmap){
		super(type, message, defaultChoice, bitmap, Dialog.NO_HORIZONTAL_SCROLL);		
		setBackground(BackgroundFactory.createSolidTransparentBackground(Color.BLACK, 200));
		setBorder(BorderFactory.createBitmapBorder(new XYEdges(9, 9, 9, 9), Bitmap.getBitmapResource("border.png")));		
	}

	public static void inform(int type, String message, int defaultChoice, Bitmap bitmap){
		new SendDialog(type, message, defaultChoice, bitmap).show();
	} 
}
