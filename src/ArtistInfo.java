public class ArtistInfo {

	private long id;

	private String name;

	private String name_key;

	private int number_of_albums;

	private int number_of_tracks;

	private short gender; // (male|female|band)

	private long genre_id;

	private short region;

	private GenreInfo genre;

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

	public String getName_key() {
		return name_key;
	}

	public void setName_key(String nameKey) {
		name_key = nameKey;
	}

	public int getNumber_of_albums() {
		return number_of_albums;
	}

	public void setNumber_of_albums(int numberOfAlbums) {
		number_of_albums = numberOfAlbums;
	}

	public int getNumber_of_tracks() {
		return number_of_tracks;
	}

	public void setNumber_of_tracks(int numberOfTracks) {
		number_of_tracks = numberOfTracks;
	}

	public short getGender() {
		return gender;
	}

	public void setGender(short gender) {
		this.gender = gender;
	}

	public long getGenre_id() {
		return genre_id;
	}

	public void setGenre_id(long genreId) {
		genre_id = genreId;
	}

	public short getRegion() {
		return region;
	}

	public void setRegion(short region) {
		this.region = region;
	}
}
