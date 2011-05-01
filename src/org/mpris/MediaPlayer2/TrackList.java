package org.mpris.MediaPlayer2;

import java.util.List;
import java.util.Map;
import org.freedesktop.dbus.DBusInterface;
import org.freedesktop.dbus.DBusSignal;
import org.freedesktop.dbus.Variant;
import org.freedesktop.dbus.exceptions.DBusException;

public interface TrackList extends DBusInterface {
	public static class TrackListReplaced extends DBusSignal {
		public final List<DBusInterface> Tracks;
		public final DBusInterface CurrentTrack;

		public TrackListReplaced(String path, List<DBusInterface> Tracks,
				DBusInterface CurrentTrack) throws DBusException {
			super(path, Tracks, CurrentTrack);
			this.Tracks = Tracks;
			this.CurrentTrack = CurrentTrack;
		}
	}

	public static class TrackAdded extends DBusSignal {
		public final Map<String, Variant> Metadata;
		public final DBusInterface AfterTrack;

		public TrackAdded(String path, Map<String, Variant> Metadata,
				DBusInterface AfterTrack) throws DBusException {
			super(path, Metadata, AfterTrack);
			this.Metadata = Metadata;
			this.AfterTrack = AfterTrack;
		}
	}

	public static class TrackRemoved extends DBusSignal {
		public final DBusInterface TrackId;

		public TrackRemoved(String path, DBusInterface TrackId)
				throws DBusException {
			super(path, TrackId);
			this.TrackId = TrackId;
		}
	}

	public static class TrackMetadataChanged extends DBusSignal {
		public final DBusInterface TrackId;
		public final Map<String, Variant> Metadata;

		public TrackMetadataChanged(String path, DBusInterface TrackId,
				Map<String, Variant> Metadata) throws DBusException {
			super(path, TrackId, Metadata);
			this.TrackId = TrackId;
			this.Metadata = Metadata;
		}
	}

	public List<Map<String, Variant>> GetTracksMetadata(
			List<DBusInterface> TrackIds);

	public void AddTrack(String Uri, DBusInterface AfterTrack,
			boolean SetAsCurrent);

	public void RemoveTrack(DBusInterface TrackId);

	public void GoTo(DBusInterface TrackId);

}