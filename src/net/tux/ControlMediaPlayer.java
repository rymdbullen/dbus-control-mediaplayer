package net.tux;

import java.util.Iterator;
import java.util.Map;

import org.freedesktop.DBus;
import org.freedesktop.dbus.DBusConnection;
import org.freedesktop.dbus.Variant;
import org.freedesktop.dbus.exceptions.DBusException;
import org.mpris.MediaPlayer2.Player;

public class ControlMediaPlayer {

	private static String serviceBusName = "org.mpris.MediaPlayer2.spotify";
	private static String interfaceName = "org.mpris.MediaPlayer2.Player";
	private static String objectPath = "/org/mpris/MediaPlayer2";
	private static DBusConnection conn;

	public static void main(String[] args) {
		if (args.length != 1) {
			System.exit(1);
		}

		// what to do
		// 1. check if program(s) are playing a tune
		// 2. perform the wanted action
		// 3. return the status
		
		
		
		try {
			conn = DBusConnection.getConnection(DBusConnection.SESSION);
			Player player = conn.getRemoteObject(serviceBusName, objectPath, Player.class);

			String propertyName = null;
			Object value = null;
			boolean running = false;
			
			propertyName = "Metadata";
			value = query(propertyName);
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
			
			String arg = args[0];
			if (arg.equals("playpause")&&!running) 
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
			conn.disconnect();
		}
	}

	private static Object query(String propertyName) {
		try {
			conn = DBusConnection.getConnection(DBusConnection.SESSION);
			DBus.Properties props = conn.getRemoteObject(serviceBusName,
					objectPath, DBus.Properties.class);

			Map<String, Variant> allProperties = props.GetAll(interfaceName);
			Variant property = allProperties.get(propertyName);
			
//			System.out.println(propertyName+": "+property.getValue());

			return property.getValue();
		} catch (DBusException ex) {
			ex.printStackTrace();
		} finally {
			conn.disconnect();
		}
		return null;
	}

}