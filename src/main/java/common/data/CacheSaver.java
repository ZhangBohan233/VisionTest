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
    public static final String TEST_HIDING_INTERVAL = "hidingInterval";
    public static final String TEST_DISTANCE = "distance";
    public static final String TEST_LEFT_EYE = "leftEye";
    public static final String TEST_RIGHT_EYE = "rightEye";
    public static final String TEST_DUAL_EYES = "bothEyes";

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

    /**
     * 创建一个文件的目录
     *
     * @param filePath 文件路径，此文件的父目录将被创建
     */
    private static void createParentDirOfFile(String filePath) {
        File f = new File(filePath);
        if (!f.exists()) {
            if (!f.getParentFile().mkdirs()) throw new RuntimeException("Failed to create dir " + f.getParentFile());
        }
    }

    public void saveAndStop() {
        writeJson(root);
        timer.cancel();
        System.out.println("cache saver stopped");
    }

    public void putCache(String key, Object value) {
        root.put(key, value);
    }

    public String getCache(String key) {
        if (!root.has(key)) return null;
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

    public boolean getBoolean(String key) {
        String s = getCache(key);
        return Boolean.parseBoolean(s);
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
        createParentDirOfFile(fileName);
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));

            bw.write(root.toString(2));

            bw.flush();
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*
    The following two method is only usable for test cache
     */
    public MainViewCache getMainViewCache() {
//        String[] scIntDt = getTestCachesByKeys("scoreCounting", "interval", "distance");
        String scStr = getCache(TEST_SCORE_COUNTING);
        long intervalOri = getLong(TEST_INTERVAL);
        long hidingOri = getLong(TEST_HIDING_INTERVAL);
        double dis = getDouble(TEST_DISTANCE);

        ScoreCounting sc;
        try {
            sc = ScoreCounting.valueOf(scStr);
        } catch (NullPointerException | IllegalArgumentException e) {
            sc = ScoreCounting.FIVE;
        }

        long interval = intervalOri == -1 ? 3000 : intervalOri;
        long hiding = hidingOri == -1 ? 1000 : hidingOri;
        double distance = Double.isNaN(dis) ? 5.0 : dis;

        return new MainViewCache(sc, interval, hiding, distance,
                getBoolean(TEST_LEFT_EYE), getBoolean(TEST_RIGHT_EYE), getBoolean(TEST_DUAL_EYES));
    }

    public static class MainViewCache {
        public final ScoreCounting scoreCounting;
        public final long timeInterval;
        public final long hidingInterval;
        public final double testDistance;
        public final boolean leftEye;
        public final boolean rightEye;
        public final boolean dualEyes;

        private MainViewCache(ScoreCounting scoreCounting, long timeInterval, long hidingInterval,
                              double testDistance, boolean leftEye, boolean rightEye, boolean dualEyes) {
            this.scoreCounting = scoreCounting;
            this.timeInterval = timeInterval;
            this.hidingInterval = hidingInterval;
            this.testDistance = testDistance;

            this.leftEye = leftEye;
            this.rightEye = rightEye;
            this.dualEyes = dualEyes;
        }
    }

    private class AutoSaveTask extends TimerTask {

        @Override
        public void run() {
            writeJson(root);
        }
    }
}
