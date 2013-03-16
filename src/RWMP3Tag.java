import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

public class RWMP3Tag {

	public static void main(String[] args) {

		parseTrack("33.dsfsdfsdlkj");

		parseTrack("93 .dsfsdfsdlkj");

		parseTrack("03 .dsfsdfsdlkj");

		parseTrack("7 .dsfsdfsdlkj");

		parseTrack(" 366 .dsfsdfsdlkj");

		parseTrack("     365556.dsfsdfsdlkj");

		parseTrack("-4 .dsfsdfsdlkj");

	}

	public static void parseMp3() throws Exception {
		AudioFile f = AudioFileIO
				.read(new File(
						"/home/guohai/dev/x/topics/20120602/fs/港台音乐/2/5566/titi/3. Falling In Love.mp3"));
		Tag tag = f.getTag();
		AudioHeader header = f.getAudioHeader();

		System.out.println(header.getTrackLength());

		System.out.println(header.getSampleRateAsNumber());

		System.out.println(tag.getFirst(FieldKey.ARTIST));
		System.out.println(tag.getFirst(FieldKey.ALBUM));
		System.out.println(tag.getFirst(FieldKey.TITLE));
		System.out.println(tag.getFirst(FieldKey.COMMENT));
		System.out.println(tag.getFirst(FieldKey.YEAR));
		System.out.println(tag.getFirst(FieldKey.TRACK));
		System.out.println(tag.getFirst(FieldKey.DISC_NO));
		System.out.println(tag.getFirst(FieldKey.COMPOSER));
		System.out.println(tag.getFirst(FieldKey.ARTIST_SORT));

		System.out.println(tag.getFirst(FieldKey.MUSICIP_ID));
		System.out.println(tag.getFirst(FieldKey.MUSICBRAINZ_TRACK_ID));
		System.out.println(tag.getFirst(FieldKey.MUSICBRAINZ_ARTISTID));
		System.out.println(tag.getFirst(FieldKey.MUSICBRAINZ_RELEASEARTISTID));
		System.out.println(tag.getFirst(FieldKey.MUSICBRAINZ_RELEASEID));
	}

	public static int parseTrack(String audioName) {
		int trackNo = 0;

		Matcher m = p.matcher(audioName);

		if (m.find()) {
			try {
				trackNo = Integer.parseInt(m.group(0).trim());
			} catch (NumberFormatException e) {
				System.err.println("can not parse track no. " + audioName);
			}
		} else {
			System.err.println("track no. not found " + audioName);
		}

		System.out.println("track number: " + trackNo);

		return trackNo;
	}

	static final String regex = "^\\s*\\d+";
	static Pattern p = Pattern.compile(regex);
}
