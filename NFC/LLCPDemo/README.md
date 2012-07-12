# NFC LLCP Sample


The purpose of this application is to demonstrate how to transfer data from one NFC-enabled BlackBerry to another
using the NFC Logical Link Control Protocol (LLCP).

To use this application, you’ll need to install it on two NFC enabled BlackBerry devices.
On the first device, select the Sender icon and on the second select the Receiver icon.
Adjust the text on the next screen on the sender device and click "Continue" and then place both devices back-to-back.
You should see the data transfer take place and this illustrated by messages on the activity log screen.

The application is liberally instrumented with EventLogger Informational messages using the label "LLCPDemo"
that can be examined in the event log of a device. Use Alt-LGLG to set the log level to Information and to examine the
event log or use javaloader -u eventlog>eventlog.txt to extract the log to your PC over USB.

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

Please see the [README](https://github.com/blackberry/BlackBerry-Java) of the BlackBerry-Java repository for instructions on how to add new Samples or make modifications to existing Samples.


## Bug Reporting and Feature Requests

If you find a bug in a Sample, or have an enhancement request, simply file an [Issue](https://github.com/blackberry/BlackBerry-Java/issues) for the Sample and send a message (via github messages) to the Sample Author(s) to let them know that you have filed an [Issue](https://github.com/blackberry/BlackBerry-Java/issues).


## Disclaimer

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.