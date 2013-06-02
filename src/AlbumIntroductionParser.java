import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xkit.cc.utils.StringUtils;

import sys.DataCache;

public class AlbumIntroductionParser {

	public static void main(String[] args) {
		AlbumInfo album = new AlbumInfo();

		try {
			parseIntro("C:\\output\\港台音乐\\1\\大幅度\\readme.txt", album);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void parseIntro(String introFile, AlbumInfo album)
			throws IOException {

		String comment = "";
		boolean isCommentBegin = false;

		boolean isTracksBegin = false;

		String tracks = "";

		try {
			// FIXME p.waitFor() seems does not work
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println(new java.util.Date());

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(introFile), "UTF-8"));

		while (reader.ready()) {
			String line = reader.readLine();

			if (line.startsWith(ALBUM_NAME_0x00)
					|| line.startsWith(ALBUM_NAME_0x01)) {
				if (album != null) {
					album.setName(parseName(line));
				}
			} else if (line.startsWith(ISSUE_TIME)) {
				if (album != null) {
					album.setPublishTime(StringUtils
							.stringToYear(parseTime(line)));
				}
			} else if (line.startsWith(ARTIST)) {
				if (album != null) {
					ArtistInfo artist = new ArtistInfo();
					artist.setName(parseArtist(line));
					album.addArtist(artist);
				}
			} else if (line.startsWith(DISTRICT)) {
				if (album != null) {
					DataCache.getDistrictCode(parseDistrict(line));
				}
			} else if (line.startsWith(LANGUAGE)) {
				if (album != null) {
					album.setLanguage(DataCache
							.getLanguageCode(parseLanguage(line)));
				}
			} else if (line.startsWith(INTRO)) {
				isCommentBegin = true;
				isTracksBegin = false;
				continue;
			} else if (line.startsWith(TRACKS)) {
				isTracksBegin = true;
				isCommentBegin = false;
				continue;
			}

			if (isCommentBegin) {
				comment += (line + "\r\n");
			}

			if (isTracksBegin) {
				tracks += (line + "\r\n");
			}
		}

		if (album != null) {
			album.setIntroduction(comment);
		}

		printAlbum(album);
	}

	public static String parseLanguage(String rawLanguage) {

		Matcher m = P_REGEX_KEY_LANGUAGE_VALUE.matcher(rawLanguage);

		if (m.find()) {
			return m.group(3).trim();
		}

		return null;
	}

	public static String parseDistrict(String rawDistrict) {

		Matcher m = P_REGEX_KEY_DISTRICT_VALUE.matcher(rawDistrict);

		if (m.find()) {
			return m.group(3).trim();
		}

		return null;
	}

	public static String parseArtist(String rawArtist) {

		Matcher m = P_REGEX_KEY_ARTIST_VALUE.matcher(rawArtist);

		if (m.find()) {
			return m.group(3).trim();
		}

		return null;
	}

	public static String parseTime(String rawTime) {

		Matcher m = P_REGEX_KEY_TIME_VALUE.matcher(rawTime);

		if (m.find()) {
			return m.group(3).trim();
		}

		return null;
	}

	public static String parseName(String rawName) {

		Matcher m = P_REGEX_KEY_NAME_VALUE.matcher(rawName);

		if (m.find()) {
			return m.group(3).trim();
		}

		return null;
	}

	private static void printAlbum(AlbumInfo album) {
		if (album == null)
			return;
		System.out.println(album.getName());

		System.out.println(album.getArtist().getName());

		System.out.println(album.getPublishTime());

		System.out.println(DataCache.getLanguageName(album.getLanguage()));

		System.out.println(album.getIntroduction());

//		for (AudioInfo audio : album.getAudio()) {
//			System.out.println(audio.getName());
//		}
	}

	static final String SEPARATOR = ":";

	static final String SEPARATOR_CHINESE = "：";

	static final String ALBUM_NAME_0x00 = "中文名称";

	static final String ALBUM_NAME_0x01 = "专辑名称";

	static final String ISSUE_TIME = "发行时间";

	static final String ARTIST = "专辑歌手";

	static final String INTRO = "专辑介绍";

	static final String DISTRICT = "地区";

	static final String LANGUAGE = "语言";

	static final String TRACKS = "专辑曲目";

	static final String REGEX_KEY_NAME_VALUE = "^(" + ALBUM_NAME_0x00 + "|"
			+ ALBUM_NAME_0x01 + ")(" + SEPARATOR + "|" + SEPARATOR_CHINESE
			+ ")(.*)$"; // ^(中文名称|专辑名称)(：|:)(.*)$

	static Pattern P_REGEX_KEY_NAME_VALUE = Pattern
			.compile(REGEX_KEY_NAME_VALUE);

	static final String REGEX_KEY_TIME_VALUE = "^(" + ISSUE_TIME + ")("
			+ SEPARATOR + "|" + SEPARATOR_CHINESE + ")(.*)$"; // ^(发行时间)(：|:)(.*)$

	static Pattern P_REGEX_KEY_TIME_VALUE = Pattern
			.compile(REGEX_KEY_TIME_VALUE);

	static final String REGEX_KEY_ARTIST_VALUE = "^(" + ARTIST + ")("
			+ SEPARATOR + "|" + SEPARATOR_CHINESE + ")(.*)$"; // ^(专辑歌手)(：|:)(.*)$

	static Pattern P_REGEX_KEY_ARTIST_VALUE = Pattern
			.compile(REGEX_KEY_ARTIST_VALUE);

	static final String REGEX_KEY_DISTRICT_VALUE = "^(" + DISTRICT + ")("
			+ SEPARATOR + "|" + SEPARATOR_CHINESE + ")(.*)$"; // ^(地区)(：|:)(.*)$

	static Pattern P_REGEX_KEY_DISTRICT_VALUE = Pattern
			.compile(REGEX_KEY_DISTRICT_VALUE);

	static final String REGEX_KEY_LANGUAGE_VALUE = "^(" + LANGUAGE + ")("
			+ SEPARATOR + "|" + SEPARATOR_CHINESE + ")(.*)$"; // ^(语言)(：|:)(.*)$

	static Pattern P_REGEX_KEY_LANGUAGE_VALUE = Pattern
			.compile(REGEX_KEY_LANGUAGE_VALUE);
}
