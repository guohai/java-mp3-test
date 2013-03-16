public class ImageInfo {
	private long id;

	private long album_id;

	private String name;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getAlbum_id() {
		return album_id;
	}

	public void setAlbum_id(long albumId) {
		album_id = albumId;
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

	public String getMime_type() {
		return mime_type;
	}

	public void setMime_type(String mimeType) {
		mime_type = mimeType;
	}

	private String path;

	private String mime_type;
}
