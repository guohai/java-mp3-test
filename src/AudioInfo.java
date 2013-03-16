public class AudioInfo {

	public AudioInfo(AlbumInfo album) {
		this.album = album;
	}

	private long id;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getYear() {
		return year;
	}

	public void setYear(short year) {
		this.year = year;
	}

	public String getSha1() {
		return sha1;
	}

	public void setSha1(String sha1) {
		this.sha1 = sha1;
	}

	public long getArtist_id() {
		return artist_id;
	}

	public void setArtist_id(long artistId) {
		artist_id = artistId;
	}

	public long getAlbum_id() {
		return album_id;
	}

	public void setAlbum_id(long albumId) {
		album_id = albumId;
	}

	public long getLyrics_id() {
		return lyrics_id;
	}

	public void setLyrics_id(long lyricsId) {
		lyrics_id = lyricsId;
	}
	
	public short getTrack() {
		return track;
	}

	public void setTrack(short track) {
		this.track = track;
	}

	private String name;
	private String path;
	private long duration;
	private int size;
	private short year;
	private String sha1;

	private String disc;

	public String getDisc() {
		return disc;
	}

	public void setDisc(String disc) {
		this.disc = disc;
	}

	private short track;

	private int sampleRate;

	private long bitRate;

	public long getBitRate() {
		return bitRate;
	}

	public void setBitRate(long bitRate) {
		this.bitRate = bitRate;
	}

	public int getSampleRate() {
		return sampleRate;
	}

	public void setSampleRate(int sampleRate) {
		this.sampleRate = sampleRate;
	}

	private long artist_id;
	private long album_id;
	private long lyrics_id;

	private ArtistInfo artist;

	private AlbumInfo album;
}
