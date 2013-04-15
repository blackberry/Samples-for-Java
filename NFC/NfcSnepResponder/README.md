# NFC SNEP Responder Sample


The purpose of this application is to demonstrate how to transfer NDEF messages from one NFC-enabled BlackBerry to another using the Simple NDEF Exchange Protocol (SNEP).

To use this application, launch it from the home screen, click the icon on the first screen and this will cause the application's own event log  screen to be displayed. Now simply bring another NFC enabled device which supports SNEP into the NFC field of this device and you will see a vCard being served from the device running this application to the second. If the second device is a BlackBerry smart phone running at least version 7.1 of the BlackBerry OS then the vCard will be received and you will be asked to store it as a contact.

The application is liberally instrumented with EventLogger Informational messages using the label "NfcSnepResponder" that can be examined in the event log of a device. Use Alt-LGLG to set the log level to Information and to examine the event log or use javaloader -u eventlog>eventlog.txt to extract the log to your PC over USB.

The sample code for this application is Open Source under the [Apache 2.0 License](http://www.apache.org/licenses/LICENSE-2.0.html).

**Applies To**

* [BlackBerry Java SDK for Smartphones](http://us.blackberry.com/developers/javaappdev/)


**Author(s)** 

* [John Murray](https://github.com/jcmurray)
* [Martin Woolley](https://github.com/mdwoolley)


**Dependencies**

1. BlackBerry Device Software 7.1 and above.


**Known Issues**

None

**To contribute code to this repository you must be [signed up as an official contributor](http://blackberry.github.com/howToContribute.html).**


## Contributing Changes

Please see the [README](https://github.com/blackberry/Samples-For-Java) of the BlackBerry-Java repository for instructions on how to add new Samples or make modifications to existing Samples.


## Bug Reporting and Feature Requests

If you find a bug in a Sample, or have an enhancement request, simply file an [Issue](https://github.com/blackberry/Samples-For-Java/issues) for the Sample.


## Disclaimer

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.