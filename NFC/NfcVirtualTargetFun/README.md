# NFC Virtual Target Emulation Sample


The purpose of this application is to demonstrate the emulation and reading of NFC virtual tags. In effect allowing a BlackBerry java application to appear to a reader as a smart card and, building on previous articles, enabling a second BlackBerry device to read the virtual tag emulated on the original device.

The application is liberally instrumented with EventLogger Informational messages using the label "NfcVirtTargFun" that can be examined in the event log of a device. Use Alt-LGLG to set the log level to Information and to examine the event log or use javaloader -u eventlog>eventlog.txt to extract the log to your PC over USB.

The sample code for this application is Open Source under the [Apache 2.0 License](http://www.apache.org/licenses/LICENSE-2.0.html).

**Applies To**

* [BlackBerry Java SDK for Smartphones](http://us.blackberry.com/developers/javaappdev/)


**Author(s)** 

* [John Murray](https://github.com/jcmurray)
* [Martin Woolley](https://github.com/mdwoolley)


**Dependencies**

1. BlackBerry Device Software 7.0 and above.


**Known Issues**

None

**To contribute code to this repository you must be [signed up as an official contributor](http://blackberry.github.com/howToContribute.html).**


## Contributing Changes

Please see the [README](https://github.com/blackberry/Samples-For-Java) of the BlackBerry-Java repository for instructions on how to add new Samples or make modifications to existing Samples.


## Bug Reporting and Feature Requests

If you find a bug in a Sample, or have an enhancement request, simply file an [Issue](https://github.com/blackberry/Samples-For-Java/issues) for the Sample.


## Disclaimer

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.