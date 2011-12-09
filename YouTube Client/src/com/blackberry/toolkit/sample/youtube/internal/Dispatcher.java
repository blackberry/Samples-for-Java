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

package com.blackberry.toolkit.sample.youtube.internal;

import java.util.Hashtable;
import java.util.Vector;

import net.rim.device.api.io.MalformedURIException;
import net.rim.device.api.io.URI;
import net.rim.device.api.io.messaging.BlockingSenderDestination;
import net.rim.device.api.io.messaging.ByteMessage;
import net.rim.device.api.io.messaging.Context;
import net.rim.device.api.io.messaging.Destination;
import net.rim.device.api.io.messaging.DestinationFactory;
import net.rim.device.api.io.messaging.Message;
import net.rim.device.api.io.messaging.MessageFailureException;
import net.rim.device.api.io.messaging.MessageListener;
import net.rim.device.api.io.messaging.MessagingException;
import net.rim.device.api.io.messaging.NonBlockingSenderDestination;
import net.rim.device.api.io.parser.xml.XMLHashtable;
import net.rim.device.api.io.parser.xml.XMLHashtableMessageProcessor;
import net.rim.device.api.io.transport.ConnectionFactory;
import net.rim.device.api.io.transport.TransportInfo;
import net.rim.device.api.system.DeviceInfo;
import net.rim.device.api.system.EncodedImage;

import com.blackberry.toolkit.sample.youtube.YoutubeClient;

public class Dispatcher {

	private static Dispatcher _dispatcher;
	private ConnectionFactory _factory;
	private Context _context;
	private XMLHashtableMessageProcessor _processor;

	private static String QUERY_BASE = "http://gdata.youtube.com/feeds/mobile/videos?v=2&fields=entry[link/@rel='http://gdata.youtube.com/schemas/2007%23mobile']&format=6";
	private static String QUERY_START_INDEX = "&start-index=";
	private static String QUERY_MAX_RESULTS = "&max-results=";

	public static synchronized Dispatcher getInstance() {
		if (_dispatcher == null) {
			_dispatcher = new Dispatcher();
		}
		return _dispatcher;
	}

	public Dispatcher() {
		_factory = new ConnectionFactory();
		_factory.setAttemptsLimit(3);
		if (DeviceInfo.isSimulator()) {
			_factory.setPreferredTransportTypes(new int[] { TransportInfo.TRANSPORT_TCP_WIFI,
					TransportInfo.TRANSPORT_TCP_CELLULAR });
		}

		_context = new Context("YoutubeDispatcher", _factory);

		_processor = new XMLHashtableMessageProcessor(false, false);
	}

	public boolean search(String query, int index, int max, ResponseListener listener) {
		try {
			BlockingSenderDestination destination = DestinationFactory.createBlockingSenderDestination(_context,
					URI.create(buildQueryURL(query, index, max)), _processor);
			new MessageThread(destination, listener).start();
			return true;
		} catch (Throwable t) {
			YoutubeClient.log(t.toString());
			return false;
		}
	}

	private String buildQueryURL(String query, int index, int max) {
		StringBuffer buffer = new StringBuffer(QUERY_BASE);
		buffer.append('&').append('q').append('=').append(query);
		buffer.append(QUERY_START_INDEX).append(index);
		buffer.append(QUERY_MAX_RESULTS).append(max);
		YoutubeClient.log("Query: " + buffer.toString());
		return buffer.toString();
	}

	private class MessageThread extends Thread implements MessageListener {
		private BlockingSenderDestination _destination;
		private ResponseListener _listener;
		private Hashtable _thumbnailDestinations = new Hashtable();

		public MessageThread(BlockingSenderDestination destination, ResponseListener listener) {
			_destination = destination;
			_listener = listener;
		}

		public void run() {
			try {
				_thumbnailDestinations.clear();
				Message message = _destination.sendReceive();
				XMLHashtable contents = (XMLHashtable) message.getObjectPayload();
				Vector videos = parseContents(contents);
				_listener.searchResponse(videos);

			} catch (Throwable t) {
				YoutubeClient.log(t.toString());
			} finally {
				_destination.destroy();
			}
		}

		private Vector parseContents(XMLHashtable contents) {
			String[] titles = contents.getStringArray("/feed/entry/title");
			String[] urls = contents.getStringArray("/feed/entry/group/content/@url");
			String[] thumbs = contents.getStringArray("/feed/entry/group/thumbnail/@url");
			int[] thumbWidth = contents.getIntegerArray("/feed/entry/group/thumbnail/@width");
			int[] thumbHeight = contents.getIntegerArray("/feed/entry/group/thumbnail/@height");
			String[] descriptions = contents.getStringArray("/feed/entry/group/description");
			int results = titles.length;
			Vector videosResults = new Vector(results);
			for (int i = 0; i < titles.length; i++) {
				VideoData data = new VideoData();
				data.setTitle(titles[i]);
				data.setUrl(urls[(i * 2) + 1]);
				data.setDescription(descriptions[i]);
				data.setThumbWidth(thumbWidth[i * 5]);
				data.setThumbHeight(thumbHeight[i * 5]);
				try {
					downloadThumbnail(thumbs[i * 5], data);
				} catch (Throwable t) {
					YoutubeClient.log(t.toString());
				}
				videosResults.addElement(data);
			}
			return videosResults;
		}

		private void downloadThumbnail(String url, VideoData data) throws IllegalArgumentException, MessagingException,
				MalformedURIException {
			NonBlockingSenderDestination destination = DestinationFactory.createNonBlockingSenderDestination(_context,
					URI.create(url), this);
			destination.send();
			_thumbnailDestinations.put(destination, data);
		}

		public void onMessage(Destination destination, Message message) {
			if (message instanceof ByteMessage) {
				byte[] results = ((ByteMessage) message).getBytePayload();
				EncodedImage image = EncodedImage.createEncodedImage(results, 0, -1);
				VideoData data = (VideoData) _thumbnailDestinations.get(destination);
				data.setThumbnail(image.getBitmap());
				_listener.thumbRetrieved(data);
				destination.destroy();
			}

		}

		public void onMessageCancelled(Destination destination, int cancelledMessageId) {
			YoutubeClient.log("Failure downloading thumbnail: Cancelled");
		}

		public void onMessageFailed(Destination destination, MessageFailureException exception) {
			YoutubeClient.log("Failure downloading thumbnail: " + exception.toString());
		}
	}

}
