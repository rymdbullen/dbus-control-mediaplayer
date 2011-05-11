package net.tux;

import java.util.Iterator;
import java.util.Map;

import org.freedesktop.DBus;
import org.freedesktop.DBus.Error.ServiceUnknown;
import org.freedesktop.dbus.DBusConnection;
import org.freedesktop.dbus.Variant;
import org.freedesktop.dbus.exceptions.DBusException;
import org.mpris.MediaPlayer2.Player;

public class ControlMediaPlayer {

	private static String serviceBusNameSpotify = "org.mpris.MediaPlayer2.spotify";
	private static String serviceBusNameClementine = "org.mpris.MediaPlayer2.clementine";
	private static String interfaceName = "org.mpris.MediaPlayer2.Player";
	private static String objectPath = "/org/mpris/MediaPlayer2";
	private static DBusConnection conn;

	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("No command line argument provided. Exiting");
			System.exit(1);
		}

		// what to do
		// 1. check if program(s) are playing a tune
		// 2. perform the wanted action
		// 3. return the status
		
		
		String propertyName = null;
		Object value = null;
		boolean running = false;
		
		propertyName = "Metadata";
		String serviceBusName = serviceBusNameSpotify;
		value = getPropertyValue(serviceBusName, propertyName);
		if(value==null) {
			System.out.println("failed to locate spotify");
			serviceBusName = serviceBusNameClementine;
			value = getPropertyValue(serviceBusName, propertyName);
		}
		if(value==null) {
			System.out.println("failed to locate clementine");
			System.out.println("no players found");
			//System.exit(0);
		}
		if(value instanceof Map) {
			Map<String, Variant> allMetadata = (Map<String, Variant>) value;
			if(allMetadata.keySet().size()==0) {
				System.out.println("Player not playing");
				running = false;
			} else {
				running = true;
				Iterator<String> iter = allMetadata.keySet().iterator();
				while (iter.hasNext()) {
					String key = (String) iter.next();
					Object thisValue = allMetadata.get(key);
					System.out.println(key+" "+thisValue);
				}
			}
		}
		
		try {
			conn = DBusConnection.getConnection(DBusConnection.SESSION);
			Player player = conn.getRemoteObject(serviceBusName, objectPath, Player.class);
			
			String arg = args[0];
			if (arg.equals("playpause")) 
			{
				player.PlayPause();
			} 
			else if (arg.equals("stop")&&running) 
			{
				player.Stop();
			}
			else if (arg.equals("play")&&!running) 
			{
				player.Play();
			} 
			else if (arg.equals("next")&&running) 
			{
				player.Next();
			} 
			else if (arg.equals("previous")&&running) 
			{
				player.Previous();
			} 
			else 
			{
				// do nothing
			}
		} catch (DBusException ex) {
			ex.printStackTrace();
		} finally {
			if(conn==null) {
				System.exit(1);
			}
			conn.disconnect();
		}
	}

	private static Object getPropertyValue(String serviceBusName, String propertyName) {
		try {
			conn = DBusConnection.getConnection(DBusConnection.SESSION);
			DBus.Properties props = conn.getRemoteObject(serviceBusName, objectPath, DBus.Properties.class);

			Map<String, Variant> allProperties = props.GetAll(interfaceName);
			Variant property = allProperties.get(propertyName);
			
			return property.getValue();
		} catch (ServiceUnknown e) {
			e.printStackTrace();
		} catch (DBusException ex) {
			ex.printStackTrace();
		} finally {
			if(conn==null) {
				return null;
			}
			conn.disconnect();
		}
		return null;
	}

}