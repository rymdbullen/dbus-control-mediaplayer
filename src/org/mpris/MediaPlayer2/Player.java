package org.mpris.MediaPlayer2;

import org.freedesktop.dbus.DBusInterface;
import org.freedesktop.dbus.DBusInterfaceName;
import org.freedesktop.dbus.DBusSignal;
import org.freedesktop.dbus.exceptions.DBusException;

@DBusInterfaceName("org.mpris.MediaPlayer2.Player")
public interface Player extends DBusInterface {

	public static class Seeked extends DBusSignal {
		public final long Position;

		public Seeked(String path, long Position) throws DBusException {
			super(path, Position);
			this.Position = Position;
		}
	}

	public void Next();

	public void Previous();

	public void Pause();

	public void PlayPause();

	public void Stop();

	public void Play();

	public void Seek(long Offset);

	public void SetPosition(DBusInterface TrackId, long Position);

	public void OpenUri(String Uri);
}