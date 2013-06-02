package sys;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DataCache {

	public static Map<Integer, String> mDistrictMappingCache;

	static {
		mDistrictMappingCache = new HashMap<Integer, String>();
		mLanguageMappingCache = new HashMap<Short, String>();

		freshDistrictMappingCache();
		freshLanguageMappingCache();
	}

	private static void freshDistrictMappingCache() {
		mDistrictMappingCache.put(1000, "中国");
		mDistrictMappingCache.put(1001, "香港");
		mDistrictMappingCache.put(1002, "台湾");

		mDistrictMappingCache.put(2000, "美国");
		mDistrictMappingCache.put(3000, "英国");
		mDistrictMappingCache.put(4000, "日本");
	}

	public static String getDistrictName(Integer code) {
		return mDistrictMappingCache.get(code);
	}

	public static Integer getDistrictCode(String name) {
		Set<Map.Entry<Integer, String>> set = mDistrictMappingCache.entrySet();

		for (Map.Entry<Integer, String> entry : set) {
			if (entry.getValue().equalsIgnoreCase(name)) {
				return entry.getKey();
			}
		}

		System.err.println(name + " is not in the district data cache!");
		return 0;
	}

	public static Map<Short, String> mLanguageMappingCache;

	private static void freshLanguageMappingCache() {
		mLanguageMappingCache.put((short) 1000, "普通话");

		mLanguageMappingCache.put((short) 2000, "粤语");
		mLanguageMappingCache.put((short) 3000, "英语");
		mLanguageMappingCache.put((short) 4000, "日语");
	}

	public static String getLanguageName(Short code) {
		return mLanguageMappingCache.get(code);
	}

	public static Short getLanguageCode(String name) {
		Set<Map.Entry<Short, String>> set = mLanguageMappingCache.entrySet();

		for (Map.Entry<Short, String> entry : set) {
			if (entry.getValue().equalsIgnoreCase(name)) {
				return entry.getKey();
			}
		}

		System.err.println(name + " is not in the language data cache!");
		return 0;
	}
}
