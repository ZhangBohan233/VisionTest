package common.data;

import dvaTest.connection.ClientManager;
import dvaTest.gui.items.ScoreCounting;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;

public class CacheSaver {

    public static final String SCREEN_CACHE_DIR = "cache" + File.separator + "screen";
    public static final String TEST_CACHE_DIR = "cache" + File.separator + "test";
    public static final String SCREEN_CACHE_FILE = SCREEN_CACHE_DIR + File.separator + "cache.json";
    public static final String TEST_CACHE_FILE = TEST_CACHE_DIR + File.separator + "cache.json";

    private static String getTestCacheByKey(String key) {
        return getTestCachesByKeys(key)[0];
    }

    private static String[] getTestCachesByKeys(String... keys) {
        return getCacheByKey(TEST_CACHE_FILE, keys);
    }

    private static String getScreenCacheByKey(String key) {
        return getScreenCachesByKeys(key)[0];
    }

    private static String[] getScreenCachesByKeys(String... keys) {
        return getCacheByKey(SCREEN_CACHE_FILE, keys);
    }

    private static String[] getCacheByKey(String fileName, String... keys) {
        JSONObject root = readJson(fileName);
        String[] res = new String[keys.length];
        for (int i = 0; i < keys.length; i++) {
            // nullable
            if (root.has(keys[i])) res[i] = root.getString(keys[i]);
        }
        return res;
    }

    private static void putTestCache(String... pairs) {
        createTestDirsIfNone();
        putCache(TEST_CACHE_FILE, pairs);
    }

    private static void putScreenCaches(String... pairs) {
        createScreenDirsIfNone();
        putCache(SCREEN_CACHE_FILE, pairs);
    }

    private static void putCache(String fileName, String... pairs) {
        JSONObject root = readJson(fileName);
        if (pairs.length % 2 != 0) throw new RuntimeException("Keys and values are not pairs. ");
        for (int i = 0; i < pairs.length; i += 2) {
            root.put(pairs[i], pairs[i + 1]);
        }
        writeJson(fileName, root);
    }

    private static JSONObject readJson(String fileName) {
        File f = new File(fileName);
        JSONObject root;
        if (f.exists()) {
            String content;
            try {
                BufferedReader br = new BufferedReader(new FileReader(fileName));
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    builder.append(line);
                }
                content = builder.toString();
                br.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try {
                root = new JSONObject(content);
            } catch (JSONException e) {
                root = new JSONObject();
            }
        } else {
            root = new JSONObject();
        }
        return root;
    }

    private static void writeJson(String fileName, JSONObject root) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));

            bw.write(root.toString(2));

            bw.flush();
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void createScreenDirsIfNone() {
        createDir(SCREEN_CACHE_DIR);
    }

    private static void createTestDirsIfNone() {
        createDir(TEST_CACHE_DIR);
    }

    private static void createDir(String path) {
        File f = new File(path);
        if (!f.exists()) {
            if (!f.mkdirs()) throw new RuntimeException("Failed to create dir " + f);
        }
    }

    public static class ScreenCache {
        public static double getLastScreenSize() {
            String sizeScreen = getScreenCacheByKey("screenSize");
            if (sizeScreen == null) {
                return 15.6;
            } else {
                try {
                    return Double.parseDouble(sizeScreen);
                } catch (NumberFormatException e) {
                    return 15.6;
                }
            }
        }

        public static void writeScreenSize(double screenSize) {
            putScreenCaches("screenSize", String.valueOf(screenSize));
        }
    }

    public static class TestCache {
        public static String[] getLastUsedPortAndIp() {
            String[] portIp = getTestCachesByKeys("port", "ip");
            if (portIp[0] == null) portIp[0] = String.valueOf(ClientManager.DEFAULT_PORT);
            if (portIp[1] == null) portIp[1] = ClientManager.DEFAULT_IP;
            return portIp;
        }

        public static void writePortAndIp(String port, String ipAddress) {
            putTestCache("port", port, "ip", ipAddress);
        }

        public static ScoreCounting getLastUsedScoreCounting() {
            String sc = getTestCacheByKey("scoreCounting");
            if (sc == null) {
                return ScoreCounting.FIVE;
            } else {
                try {
                    return ScoreCounting.valueOf(sc);
                } catch (IllegalArgumentException e) {
                    return ScoreCounting.FIVE;
                }
            }
        }

        public static void writeScoreCounting(ScoreCounting sci) {
            putCache("scoreCounting", sci.name());
        }

        public static long getLastUsedTimeInterval() {
            String intervalStr = getTestCacheByKey("interval");
            try {
                return Long.parseLong(intervalStr);
            } catch (NullPointerException | NumberFormatException e) {
                return 3000;
            }
        }

        public static void writeTimeInterval(long interval) {
            putCache("interval", String.valueOf(interval));
        }
    }
}
