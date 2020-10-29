package common.data;

import dvaTest.gui.items.ScoreCounting;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.Timer;
import java.util.TimerTask;

public class CacheSaver {

    public static final String SCREEN_CACHE_DIR = "cache" + File.separator + "screen";
    public static final String TEST_CACHE_DIR = "cache" + File.separator + "test";
    public static final String SCREEN_CACHE_FILE = SCREEN_CACHE_DIR + File.separator + "cache.json";
    public static final String TEST_CACHE_FILE = TEST_CACHE_DIR + File.separator + "cache.json";

    /**
     * Keys
     */
    public static final String TEST_PORT = "port";
    public static final String TEST_IP = "ip";
    public static final String TEST_SCORE_COUNTING = "scoreCounting";
    public static final String TEST_INTERVAL = "interval";
    public static final String TEST_DISTANCE = "distance";

    public static final String SCREEN_SIZE = "screenSize";

    private final String fileName;
    private final JSONObject root;
    private final Timer timer;

    CacheSaver(String fileName) {
        this.fileName = fileName;
        root = readJson(fileName);

        timer = new Timer();
        timer.schedule(new AutoSaveTask(), 0, AutoSavers.PERIOD);
    }

//    private static String getTestCacheByKey(String key) {
//        return getTestCachesByKeys(key)[0];
//    }
//
//    private static String[] getTestCachesByKeys(String... keys) {
//        return getCacheByKey(TEST_CACHE_FILE, keys);
//    }
//
//    private static String getScreenCacheByKey(String key) {
//        return getScreenCachesByKeys(key)[0];
//    }
//
//    private static String[] getScreenCachesByKeys(String... keys) {
//        return getCacheByKey(SCREEN_CACHE_FILE, keys);
//    }
//
//    private static String[] getCacheByKey(String fileName, String... keys) {
//        JSONObject root = readJson(fileName);
//        String[] res = new String[keys.length];
//        for (int i = 0; i < keys.length; i++) {
//            // nullable
//            if (root.has(keys[i])) res[i] = root.getString(keys[i]);
//        }
//        return res;
//    }
//
//    private static void putTestCache(String... pairs) {
//        createTestDirsIfNone();
//        putCache(TEST_CACHE_FILE, pairs);
//    }
//
//    private static void putScreenCaches(String... pairs) {
//        createScreenDirsIfNone();
//        putCache(SCREEN_CACHE_FILE, pairs);
//    }
//
//    private static void putCache(String fileName, String... pairs) {
//        JSONObject root = readJson(fileName);
//        if (pairs.length % 2 != 0) throw new RuntimeException("Keys and values are not pairs. ");
//        for (int i = 0; i < pairs.length; i += 2) {
//            root.put(pairs[i], pairs[i + 1]);
//        }
//        writeJson(fileName, root);
//    }

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

//    private static void createScreenDirsIfNone() {
//        createDir(SCREEN_CACHE_DIR);
//    }
//
//    private static void createTestDirsIfNone() {
//        createDir(TEST_CACHE_DIR);
//    }

    private static void createDir(String path) {
        File f = new File(path);
        if (!f.exists()) {
            if (!f.mkdirs()) throw new RuntimeException("Failed to create dir " + f);
        }
    }

    public void saveAndStop() {
        writeJson(root);
        timer.cancel();
    }

    public void putCache(String key, Object value) {
        root.put(key, value);
    }

    public String getCache(String key) {
        Object obj = root.get(key);
        if (obj == null) return null;
        if (obj instanceof String) return (String) obj;
        else return String.valueOf(obj);
    }

    public long getLong(String key) {
        String s = getCache(key);
        try {
            return Long.parseLong(s);
        } catch (NullPointerException | NumberFormatException e) {
            return -1;
        }
    }

    public double getDouble(String key) {
        String s = getCache(key);
        try {
            return Double.parseDouble(s);
        } catch (NullPointerException | NumberFormatException e) {
            return Double.NaN;
        }
    }

    private void writeJson(JSONObject root) {
        createDir(fileName);
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));

            bw.write(root.toString(2));

            bw.flush();
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //    public static class ScreenCache {
//        public static double getLastScreenSize() {
//            String sizeScreen = getScreenCacheByKey("screenSize");
//            if (sizeScreen == null) {
//                return 15.6;
//            } else {
//                try {
//                    return Double.parseDouble(sizeScreen);
//                } catch (NumberFormatException e) {
//                    return 15.6;
//                }
//            }
//        }
//
//        public static void writeScreenSize(double screenSize) {
//            putScreenCaches("screenSize", String.valueOf(screenSize));
//        }
//    }
//
//    public static class TestCache {
//        public static String[] getLastUsedPortAndIp() {
//            String[] portIp = getTestCachesByKeys("port", "ip");
//            if (portIp[0] == null) portIp[0] = String.valueOf(ClientManager.DEFAULT_PORT);
//            if (portIp[1] == null) portIp[1] = ClientManager.DEFAULT_IP;
//            return portIp;
//        }
//
//        public static void writePortAndIp(String port, String ipAddress) {
//            putTestCache("port", port, "ip", ipAddress);
//        }
//

    /*
    The following two method is only usable for test cache
     */
    public MainViewCache getMainViewCache() {
//        String[] scIntDt = getTestCachesByKeys("scoreCounting", "interval", "distance");
        String scStr = getCache(TEST_SCORE_COUNTING);
        long intervalOri = getLong(TEST_INTERVAL);
        double dis = getDouble(TEST_DISTANCE);

        ScoreCounting sc;
        try {
            sc = ScoreCounting.valueOf(scStr);
        } catch (NullPointerException | IllegalArgumentException e) {
            sc = ScoreCounting.FIVE;
        }

        long interval = intervalOri == -1 ? 3000 : intervalOri;
        double distance = Double.isNaN(dis) ? 5.0 : dis;

        return new MainViewCache(sc, interval, distance);
    }

    public void writeMainViewCache(ScoreCounting sc, long timeInterval, double distance) {
        putCache(TEST_SCORE_COUNTING, sc.name());
        putCache(TEST_INTERVAL, String.valueOf(timeInterval));
        putCache(TEST_DISTANCE, String.valueOf(distance));
    }
//    }

    public static class MainViewCache {
        public final ScoreCounting scoreCounting;
        public final long timeInterval;
        public final double testDistance;

        private MainViewCache(ScoreCounting scoreCounting, long timeInterval, double testDistance) {
            this.scoreCounting = scoreCounting;
            this.timeInterval = timeInterval;
            this.testDistance = testDistance;
        }
    }

    private class AutoSaveTask extends TimerTask {

        @Override
        public void run() {
            writeJson(root);
        }
    }
}
