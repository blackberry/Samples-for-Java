# Titlebar API for BlackBerry&reg; OS 4.2.1+

In the BlackBerry&reg; 6 SDK there is a TitleBar API which provides a full set of indicators, and which can be configured to turn elements on or off as needed. It also provides the capability to include a Title and Icon for the application. The result provides a consistency of user experience with the necessary customization that each application needs.
 
However, if you are building an application in any version before 6.0, this API does not exist. The attached library attempts to address this need for 4.2.1 and above, providing the same API but choosing the appropriate set of indicators for the software version that it is running on. Each indicator field is also separated out into a standard field that could be used outside of the titlebar.

The sample code for this application is Open Source under the [Apache 2.0 License](http://www.apache.org/licenses/LICENSE-2.0.html).

**Applies To**

* [BlackBerry Java SDK for Smartphones](http://us.blackberry.com/developers/javaappdev/)


**Author(s)** 

* [Tim Windsor](https://github.com/timwindsor)


**Dependencies**

1. BlackBerry&reg; Java SDK 4.2.1 or higher required.
2. Images need to be created and used. Sample images can be found posted in the Knowledge Base, but are not Open Source:
http://supportforums.blackberry.com/t5/Java-Development/How-To-Implement-a-TitleBar-with-Signal-Strength-Battery-Level/ta-p/1278395 


**Known Issues**

1. There is no standard TitleBar format in the 4.2.1 through 4.5.0 theme, so this library tries to match the BlackBerry 6 TitleBar API to the theme as best as possible. The placement of indicators is consistent with the new API, rather than the core apps.
2. The outlined font of the 4.2.1 through 4.5.0 theme is not possible, so the blue core colour is matched as best as possible.
3. The icon is scaled to match the size of the banner. Before 5.0, scaling is done without filters and can result in an image with artifacts. The preprocessor will switch to the Bitmap scaling API when built in 5.0 and above.
4. The notifications provided by the library only includes unread mail. The core API shows the full set of notifications including IM messages, SMS, and shows new mail rather than unread mail.
5. The battery indicator in 7.0 is simplified in the library. It shows connected and unconnected only. The core field in 7.0 has graphics for unconnected, charging (with animated lightning bolt), and running on external power.
6. The "flying B's" symbol does not move between Cellular and Wi-Fi&reg; indicators in the library as it does in the core TitleBar on OS 6 and 7. It only appears with the Cellular indicator when a connection is made to the BlackBerry Infrastructure over 3G.


**To contribute code to this repository you must be [signed up as an official contributor](http://blackberry.github.com/howToContribute.html).**

## Library Structure

The library uses a set of images (not open sourced) for each original BlackBerry theme from 4.2.1 through 7.0, with variations for each device size. This means that it won't reflect theme changes, and that there are quite a few images required, making the library large. With some exceptions, images should be sourced in 4 sets:
1. 4.2.1 through 4.5.0
2. 4.6.0 through 5.0
3. 6.0.0
4. 7.0.0

To reduce the size, you can remove images that won't be used if your application starts support from a newer SDK.

The library is in the package com.blackberry.toolkit.ui.component.banner, with helper classes in the packages com.blackberry.toolkit.device.util and com.blackberry.toolkit.ui.images, and a test app in com.blackberry.toolkit.ui.component.banner?.test.
 
The core classes are as follows:
 
Banner: The class that mimics the StandardTitleBar API from the BlackBerry 6 SDK. All methods and constants are duplicated.
 
BannerFont: Helper class that provides the appropriate font face, size, and colour, depending on the device OS.
 
BatteryStatus: Displays the battery icon. Defaults to show only when low or charging. Call setDisplaySetting(BatteryStatus.BATTERY_VISIBLE_ALWAYS) to change it.
 
Notifications: Shows unread total from all message stores.
 
PhoneIndicator: Shows phone call indicator when on a phone call.
 
Roaming Indicator: Shows roaming indicator when cellular network is roaming.
 
TimeDisplay: Shows device time and updates each minute, when displayed.
 
Title: Displays a label using the BannerFont.
 
TitleBar: Preprocessor based class that extends from Banner or StandardTitleBar depending on SDK.
 
WirelessStatus: Displays cellular signal strength, with or without network type (3G, EDGE, 1XEV, etc), or Wi-Fi®? signal strength. Can shrink or maintain size based on boolean provided at creation. Call setDisplaySetting() with one of the following constants to change it's appearance:
DISPLAY_FULL - Both Descriptor and signal strength
DISPLAY_SIGNAL - Signal strength image only
DISPLAY_DESCRIPTOR - Descriptor only (1X, 1XEV, EDGE, 3G, NEXT, etc)
DISPLAY_WIFI - WiFi signal strength only
 
Each class of the element classes follows a fairly similar format. There is a static section which determines which set of images and other settings to use, and as much as possible the conditional logic is done here. The rest of the class paints the text and images following those initial settings. The settings are determined once and the appropriate images loaded once as well for efficiency. Most of the indicators require listeners which are enabled and disabled by the onDisplay() and onUnDisplay() methods, so that no cycles are wasted when the indicator is not visible. Note that many of the indicators will change their size and update their layout automatically .

## How to Include

The best way to include the library is to use the com.blackberry.toolkit.ui.component.banner.TitleBar class. This class uses the preprocessor to define itself based on the SDK that is being used. If built in 6.0 or above, it will be defined as an extension of the StandardTitleBar class from the BlackBerry 6 SDK. When built in an SDK below that, it is defined as an extension of the library's Banner class. This class provides the exact same set of methods as StandardTitleBar, so your application can make consistent calls and you seamlessly take advantage of the right class. The core StandardTitleBar API has some advantages over the library which we will cover below, so it's advantageous to use it when possible.
 
If desired, any of the fields can be added to a Manager like any UI component.


## Contributing Changes

Please see the [README](https://github.com/blackberry/BlackBerry-Java) of the BlackBerry-Java repository for instructions on how to add new Samples or make modifications to existing Samples.


## Bug Reporting and Feature Requests

If you find a bug in a Sample, or have an enhancement request, simply file an [Issue](https://github.com/blackberry/BlackBerry-Java/issues) for the Sample and send a message (via github messages) to the Sample Author(s) to let them know that you have filed an [Issue](https://github.com/blackberry/BlackBerry-Java/issues).


## Disclaimer

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.