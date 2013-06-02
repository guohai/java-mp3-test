import java.util.ArrayList;
import java.util.List;

public class AlbumInfo {
	private long id;

	private String name;

	private long artist_id;

	private long publish_time;

	private String publisher;

	private String introduction;

	private String cover;

	private ImageInfo image;

	private short language;

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

	public long getArtistId() {
		return artist_id;
	}

	public void setArtistId(long artistId) {
		artist_id = artistId;
	}

	public long getPublishTime() {
		return publish_time;
	}

	public void setPublishTime(long publishTime) {
		publish_time = publishTime;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public void addAudio(AudioInfo audio) {
		if (audios == null) {
			audios = new ArrayList<AudioInfo>();
		}
		audios.add(audio);
	}

	public List<AudioInfo> getAudio() {
		return audios;
	}

	public void addArtist(ArtistInfo artist) {
		this.artist = artist;
	}

	public ArtistInfo getArtist() {
		return artist;
	}

	private ArtistInfo artist;

	private List<AudioInfo> audios;

	public short getLanguage() {
		return language;
	}

	public void setLanguage(short language) {
		this.language = language;
	}
}
