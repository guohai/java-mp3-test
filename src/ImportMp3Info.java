import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

import database.ConnectionPools;

public class ImportMp3Info {

	private static Connection connection = null;

	public static void main(String[] args) throws SQLException, IOException {
		String fileDir = true ? "Z:\\fs\\" : "/home/guohai/misc/shared/fs/";

		// http://stackoverflow.com/questions/995298/runtime-getruntime-exec-hide-the-console-screen

		boolean test = false;

		boolean runRTFConvOnWindows = true;

		boolean insertToDatabase = true;

		if (insertToDatabase)
			connection = ConnectionPools.getPooledConnection().getConnection();

		if (test) {
			parseRtf("I:\\DJ\\1\\ATB\\Trilogy [2007]\\readme.rtf",
					"F:\\output\\hello.txt");
			return;
		}

		File path = new File(fileDir);

		if (path.isDirectory()) { // I://
			File[] categories = path.listFiles();
			for (File category : categories) { // Category
				File[] SNs = category.listFiles();

				if (null != SNs) {
					for (File SN : SNs) { // 10
						File[] HDArtists = SN.listFiles();
						AlbumInfo album = null;

						for (File HDArtist : HDArtists) { // Artist or Collection
							if (HDArtist.getName().startsWith("V A - ")) {
								// Collection
								File[] HDDiscs = null;
								if (HDArtist.isDirectory()) {
									HDDiscs = HDArtist.listFiles();

									album = new AlbumInfo();
									// TODO should truncate the year in the album name
									album.setName(HDArtist.getName());
								} else {
									System.err.println("NOT folder, drop " + HDArtist.getName());
								}

								boolean isPart = false;
								if (null != HDDiscs) {
									for (File HDtmp : HDDiscs) {
										if (HDtmp.isDirectory()) {
											isPart = true;
											break;
										}
									}

									if (isPart) { // has MORE THAN ONE discs in the collection
										for (File HDDisc : HDDiscs) {
											if (HDDisc.isDirectory()) {

												File[] cdPart = HDDisc.listFiles();
												if (cdPart != null) {
													for (File filePartMp3 : cdPart) {
														if (filePartMp3.getName().toLowerCase().endsWith(".mp3")) {
															readAudio(filePartMp3.getCanonicalPath(), filePartMp3.getName(), album, HDDisc.getName());
														}
													}
												}
											} else if (HDDisc.getName()
													.endsWith(".rtf")) {
//												System.out.println(OUTPUT_PATH + HDArtist.getCanonicalPath().substring(fileDir.length()) + "\\readme.txt");
												if (runRTFConvOnWindows) {
													String reademePath = OUTPUT_PATH + HDArtist.getCanonicalPath().substring(fileDir.length()) + "\\readme.txt";
													parseRtf(HDDisc.getCanonicalPath(), reademePath);
													AlbumIntroductionParser.parseIntro(reademePath, album);
												}
											} else if (HDDisc.getName().toLowerCase().endsWith(".jpg")) {
												album.setCover(HDDisc.getCanonicalPath());
											} else {
												System.out.println("Unknown type file " + HDDisc.getCanonicalPath());
											}
										}
									} else {
										for (File item : HDDiscs) { // File(ONLY ONE disc in the collection)
											// *.mp3
											// readme.rtf
											// *.jpg
											if (item.getName().endsWith(".rtf")) {
//												System.out.println(OUTPUT_PATH + file20.getCanonicalPath().substring(fileDir.length()) + "\\readme.txt");
												if (runRTFConvOnWindows) {
													String reademePath = OUTPUT_PATH + HDArtist.getCanonicalPath().substring(fileDir.length()) + "\\readme.txt";
													parseRtf(item.getCanonicalPath(), reademePath);
													AlbumIntroductionParser.parseIntro(reademePath, album);
												}
											} else if (item.getName().toLowerCase().endsWith(".mp3")) {
												readAudio(item.getCanonicalPath(), item.getName(), album);
											} else if (item.getName().toLowerCase().endsWith(".jpg")) {
//												System.out.println(item.getCanonicalPath());
												album.setCover(item.getCanonicalPath());
											} else {
												System.out.println("Unknown type file " + item.getCanonicalPath());
											}
										}
									}
								}

								// adjust artist info
								if (!album.getArtist().getName()
										.equalsIgnoreCase(
												HDArtist.getName())) {
									album.getArtist().setName(
											HDArtist.getName());
								}

								// insert album to database
								if (insertToDatabase) {
									insertArtist2Database(album.getArtist());
									insertAlbum2Database(album, album.getArtist());
								}
							} else {
								// Artist
								File[] HDalbums = HDArtist.listFiles();
								if (null != HDalbums) {
									for (File HDalbum : HDalbums) { // Album
										album = null;
										File[] HDItems = null;
										if (HDalbum.isDirectory()) {
											HDItems = HDalbum.listFiles();
											album = new AlbumInfo();
											// TODO should truncate the year in the album name
											album.setName(HDalbum.getName());
										} else {
											System.err.println("NOT folder, drop " + HDalbum.getName());
										}

										boolean isPart = false;

										if (null != HDItems) {
											for (File HDtmp : HDItems) {
												if (HDtmp.isDirectory()) {
													isPart = true;
													break;
												}
											}

											if (isPart) {
												for (File HDDisc : HDItems) {
													if (HDDisc.isDirectory()) {
														File[] cdPart = HDDisc.listFiles();
														if (cdPart != null) {
															for (File filePartMp3 : cdPart) {
																if (filePartMp3.getName().toLowerCase().endsWith(".mp3")) {
																	readAudio(filePartMp3.getCanonicalPath(), filePartMp3.getName(), album, HDDisc.getName());
																}
															}
														}
													} else if (HDDisc.getName()
															.endsWith(".rtf")) {
//														System.out.println(OUTPUT_PATH + HDArtist.getCanonicalPath().substring(fileDir.length()) + "\\readme.txt");
														if (runRTFConvOnWindows) {
															String reademePath = OUTPUT_PATH + HDArtist.getCanonicalPath().substring(fileDir.length()) + "\\readme.txt";
															parseRtf(HDDisc.getCanonicalPath(), reademePath);
															AlbumIntroductionParser.parseIntro(reademePath, album);
														}
													} else if (HDDisc.getName().toLowerCase().endsWith(".jpg")) {
														album.setCover(HDDisc.getCanonicalPath());
													} else {
														System.out.println("Unknown type file " + HDDisc.getCanonicalPath());
													}
												}
											} else {
												for (File item : HDItems) { // File
													// *.mp3
													// readme.rtf
													// *.jpg
													if (item.getName().endsWith(".rtf")) {
//														System.out.println(OUTPUT_PATH + HDArtist.getCanonicalPath().substring(fileDir.length())+ "\\readme.txt");
														if (runRTFConvOnWindows) {
															String reademePath = OUTPUT_PATH + HDArtist.getCanonicalPath().substring(fileDir.length()) + "\\readme.txt";
															parseRtf(item.getCanonicalPath(), reademePath);
															AlbumIntroductionParser.parseIntro(reademePath, album);
														}
													} else if (item.getName().endsWith(".mp3")) {
//														System.out.println(item.getCanonicalPath());
														readAudio(item.getCanonicalPath(), item.getName(), album);
													} else if (item.getName().toLowerCase().endsWith(".jpg")) {
//														System.out.println(item.getCanonicalPath());
														album.setCover(item.getCanonicalPath());
													} else {
														System.out.println("Unknown type file " + item.getCanonicalPath());
													}
												}	
											}
										}

										// adjust artist info
										if (!album.getArtist().getName()
												.equalsIgnoreCase(
														HDArtist.getName())) {
											album.getArtist().setName(
													HDArtist.getName());
										}

										// insert album to database
										if (insertToDatabase) {
											insertArtist2Database(album
													.getArtist());
											insertAlbum2Database(album, album.getArtist());
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	private static String CMD = "C:\\rfc-conv-tools\\rtf.bat ";

	private static String OUTPUT_PATH = "C:\\output\\";

	public static void parseRtf(String in, String out) {
		try {
			createParentDirs(out);
			// System.err.println(CMD + "\"" + in + "\" \"" + out + "\"");
			Process p = Runtime.getRuntime().exec(
					"cmd /c start /min " + CMD + "\"" + in + "\" \"" + out
							+ "\"");
			p.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}
	}

	public static void createParentDirs(String path) {
		File file = new File(path);
		File parent = file.getParentFile();
		if (!parent.exists()) {
			parent.mkdirs();
		}
	}

	public static AudioInfo readAudio(String audioPath, String audioName,
			AlbumInfo album) {
		return readAudio(audioPath, audioName, album, null);
	}

	public static AudioInfo readAudio(String audioPath, String audioName,
			AlbumInfo album, String disc) {
		AudioFile f;
		AudioInfo bean;
		try {
			f = AudioFileIO.read(new File(audioPath));

			Tag tag = f.getTag();
			AudioHeader header = f.getAudioHeader();
			bean = new AudioInfo(album);
			bean.setPath(audioPath);
			bean.setSize((int)f.getFile().getAbsoluteFile().length()); // the max file size should larger than 2147483647 [0x7fffffff]

			if (disc != null)
				bean.setDisc(disc); // Disc 1 is default

			bean.setDuration(header.getTrackLength());
			bean.setSampleRate(header.getSampleRateAsNumber());
			bean.setBitRate(header.getBitRateAsNumber());

//			System.out.println("AudioInfo: " + header.getSampleRateAsNumber());

			/*
			 * so far, can not parse song name from file exactly,
			 * and the title always get garbled from 'tag.getFirst(FieldKey.TITLE)'
			**/
			bean.setName(formatTrackName(audioName));

			short track = parseTrack(audioName);

			String artistName = null;

			if (tag != null) {
				artistName = tag.getFirst(FieldKey.ARTIST);

//				System.out.println("AudioInfo: " + tag.getFirst(FieldKey.ALBUM));

//				System.out.println("AudioInfo: " + tag.getFirst(FieldKey.COMMENT));

				String year = tag.getFirst(FieldKey.YEAR);
				try {
					bean.setYear(Short.parseShort(year));
				} catch (NumberFormatException e) {
				}

//				System.out.println("AudioInfo: " + tag.getFirst(FieldKey.TRACK));

				if (track == 0) {
					try {
						track = Short.parseShort(tag.getFirst(FieldKey.TRACK));
					} catch (NumberFormatException e) {
					}
				}
				bean.setTrack(track);
			}

			appendArtist(album, artistName);

			album.addAudio(bean);
		} catch (CannotReadException e) {
			bean = null;
			e.printStackTrace();
		} catch (IOException e) {
			bean = null;
			e.printStackTrace();
		} catch (TagException e) {
			bean = null;
			e.printStackTrace();
		} catch (ReadOnlyFileException e) {
			bean = null;
			e.printStackTrace();
		} catch (InvalidAudioFrameException e) {
			bean = null;
			e.printStackTrace();
		}
		return bean;
	}

	public static void appendArtist(AlbumInfo album, String artistName) {
		if (null != artistName && album.getArtist() == null) {
			artistName = artistName.trim();

			ArtistInfo artist = new ArtistInfo();
			artist.setName(artistName);

			album.addArtist(artist);
		}
	}

	public static String formatTrackName(String audioName) {
		Matcher m = P.matcher(audioName);

		String formatName = null;

		if (m.find()) {
			formatName = m.group(2).trim();
		} else {
			formatName = audioName;
		}

		if (formatName.startsWith(". ")) {
			formatName = formatName.substring(". ".length());
		} else if (formatName.startsWith("- ")) {
			formatName = formatName.substring(". ".length());
		}

		if (formatName.endsWith(".mp3")) {
			formatName = formatName.substring(0, formatName.length()
					- ".mp3".length());
		}

		return formatName;
	}

	public static short parseTrack(String audioName) {
		short trackNo = 0;
		Matcher m = P.matcher(audioName);

		if (m.find()) {
			try {
				trackNo = Short.parseShort(m.group(1).trim());
			} catch (NumberFormatException e) {
				System.err.println("can not parse track no.(" + m.group(0).trim() + ") @ " + audioName);
			}
		} else {
			System.err.println("track no. not found @ " + audioName);
		}

//		System.out.println("track number: " + trackNo);

		return trackNo;
	}

	static final String REGEX = "^(\\s*\\d+)(.*)"; // blank(s) digit(s) char(s)
	static Pattern P = Pattern.compile(REGEX);

	public static void checkIfArtistAlreadyExist() {

	}

	public static void insertArtist2Database(ArtistInfo artist) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		if (artist != null) {

			try {
				connection.setAutoCommit(false);

				// no genre_id, gender so far

				stmt = connection.prepareStatement(
						"INSERT INTO artist (name) "
			            + "values (?)", Statement.RETURN_GENERATED_KEYS);

				stmt.setString(1, artist.getName());
//				stmt.setShort(2, artist.getRegion()); // so far do not need region()

				stmt.executeUpdate();

				long autoIncKeyFromApi = -1;

				rs = stmt.getGeneratedKeys();

				if (rs.next()) {
					autoIncKeyFromApi = rs.getLong(1);
				} else {
				}

				artist.setId(autoIncKeyFromApi);

				connection.commit();
			} catch (SQLException e) {
				try {
					connection.rollback();
				} catch (SQLException se) {
					se.printStackTrace();
				}
				e.printStackTrace();
			} finally {
				try {
					connection.setAutoCommit(true);
				} catch (SQLException sq) {
					sq.printStackTrace();
				}
				try {
					if (rs != null) {
						rs.close();
						rs = null;
					}

					if (stmt != null) {
						stmt.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void insertAlbum2Database(AlbumInfo album, ArtistInfo artist) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		if (album != null) {
			try {
				connection.setAutoCommit(false);
				stmt = connection.prepareStatement(
						"INSERT INTO album (name, artist_id, publish_time, introduction, cover) "
			            + "values (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

//				stmt = connection.createStatement(
//						java.sql.ResultSet.TYPE_FORWARD_ONLY,
//						java.sql.ResultSet.CONCUR_UPDATABLE);

				stmt.setString(1, album.getName());
				stmt.setLong(2, artist.getId());
				stmt.setLong(3, album.getPublishTime());
				stmt.setString(4, album.getIntroduction());
				stmt.setString(5, album.getCover());

				stmt.executeUpdate();

				long autoIncKeyFromApi = -1;

				rs = stmt.getGeneratedKeys();

				if (rs.next()) {
					autoIncKeyFromApi = rs.getLong(1);
				} else {
				}

				rs.close();
				stmt.close();

				stmt = connection.prepareStatement(
						"INSERT INTO audio (name, path, artist_id, album_id, duration, size, year, sha1, track) "
			            + "values (?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

				List<AudioInfo> audios = album.getAudio();
				for (AudioInfo audio : audios) {
					stmt.setString(1, audio.getName());
					stmt.setString(2, audio.getPath());
					stmt.setLong(3, artist.getId());
					stmt.setLong(4, autoIncKeyFromApi);
					stmt.setLong(5, audio.getDuration());
					stmt.setInt(6, audio.getSize());
					stmt.setInt(7, audio.getYear());
					stmt.setString(8, audio.getSha1());
					stmt.setShort(9, audio.getTrack());
					stmt.addBatch();
				}

				stmt.executeBatch();

				connection.commit();
			} catch (SQLException e) {
				try {
					connection.rollback();
				} catch (SQLException se) {
					se.printStackTrace();
				}
				e.printStackTrace();
			} finally {
				try {
					connection.setAutoCommit(true);
				} catch (SQLException sq) {
					sq.printStackTrace();
				}
				try {
					if (rs != null) {
						rs.close();
						rs = null;
					}

					if (stmt != null) {
						stmt.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
