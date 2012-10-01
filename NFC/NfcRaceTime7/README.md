# NfcRaceTime7

The purpose of this application is to demonstrate how NFC tags can be used to trigger actions.

The use case examines how to use an NFC Tag to trigger the starting and stopping of a timer in the application that could be used in the context of an event like a "fun run".

The sample code for this application is Open Source under the Apache 2.0 License.

Note that there is also a BlackBerry 10 Cascades version of this application. https://github.com/blackberry/Cascades-Community-Samples/tree/master/NfcRaceTimeWay

To create tags for use with this application, use the NfcWriteNdefSmartTag application:

	https://github.com/blackberry/Samples-for-Java/tree/master/NFC/NfcWriteNdefSmartTag
	
Write two "custom" tags, with the following values:

Start Tag: 
  Domain: my.rim.com
  Type: myrecordtype
  Content: start
  
Stop Tag: 
  Domain: my.rim.com
  Type: myrecordtype
  Content: stop
  

The sample code for this application is Open Source under 
the [Apache 2.0 License](http://www.apache.org/licenses/LICENSE-2.0.html).

**Applies To**

* [Cascades for BlackBerry 10](https://bdsc.webapps.blackberry.com/cascades/)
* [BlackBerry Native SDK for Tablet OS](https://bdsc.webapps.blackberry.com/native/)

**Author(s)** 

* [John Murray](https://github.com/jcmurray)
* [Martin Woolley](https://github.com/mdwoolley)


**Release History**
* **V1** - Initial release

**Known Issues**
1. To start a new game it may be necessary to exit and then start again

**Dependencies**

1. BlackBerry Dev Alpha Device Software **10.0.9**
2. BlackBerry 10 Native SDK **10.0.9**

 
**I don't want to build it myself**

If you don't want to build this sample application yourself we've included a 
pre-build and signed BAR files for each version. You can find them in the 
folder "installable-bar-files":


**To contribute code to this repository you must be [signed up as an 
official contributor](http://blackberry.github.com/howToContribute.html).**


## Contributing Changes

Please see the [README](https://github.com/blackberry/Cascades-Community-Samples/blob/master/README.md) 
of the Cascades-Community-Samples repository for instructions on how to add new Samples or 
make modifications to existing Samples.


## Bug Reporting and Feature Requests

If you find a bug in a Sample, or have an enhancement request, simply file 
an [Issue](https://github.com/blackberry/Cascades-Community-Samples/issues) for 
the Sample and send a message (via github messages) to the Sample Author(s) to let 
them know that you have filed an [Issue](https://github.com/blackberry/Cascades-Community-Samples/issues).


## Disclaimer

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING 
BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE 
AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR 
ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR 
OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR 
OTHER DEALINGS IN THE SOFTWARE.