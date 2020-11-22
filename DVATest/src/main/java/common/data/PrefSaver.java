package common.data;

import java.io.*;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

public class PrefSaver {

    public static final String TEST_PREF = "test_pref.cfg";
    public static final String SCREEN_PREF = "screen_pref.cfg";

    private final String fileName;
    private final Map<String, String> map;
    private final Timer timer;

    PrefSaver(String prefFile) {
        this.fileName = prefFile;
        map = loadMap(prefFile);

        timer = new Timer();
        timer.schedule(new PrefSaverTask(), 0, AutoSavers.PERIOD);
    }

    private static Map<String, String> loadMap(String prefFile) {
        Map<String, String> map = new TreeMap<>();
        if (new File(prefFile).exists()) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(prefFile));
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.length() > 0) {
                        if (line.charAt(0) != '#') {
                            String[] kv = line.split("=");
                            if (kv.length == 2) {
                                map.put(kv[0].strip(), kv[1].strip());
                            }
                        }
                    }
                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    public void saveAndStop() {
        saveMapToFile();
        timer.cancel();
        System.out.println("pref saver stopped");
    }

    public void storePref(String key, String value) {
        map.put(key, value);
    }

    public void storePref(String key, Object value) {
        map.put(key, value.toString());
    }

    public String getPref(String key) {
        return map.get(key);
    }

    public int getInt(String key) {
        String s = map.get(key);
        try {
            return Integer.parseInt(s);
        } catch (NullPointerException | NumberFormatException e) {
            return -1;
        }
    }

    /**
     * 获取一个整数。如果该键不存在，返回{@code defaultValue}并且将其保存
     *
     * @param key          key
     * @param defaultValue default value
     * @return value correspond to key if key exists. Otherwise return the default value. 1506600552
     */
    public int getInt(String key, int defaultValue) {
        String s = map.get(key);
        try {
            return Integer.parseInt(s);
        } catch (NullPointerException | NumberFormatException e) {
            map.put(key, String.valueOf(defaultValue));
            return defaultValue;
        }
    }

    void saveMapToFile() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));

            for (Map.Entry<String, String> entry : map.entrySet()) {
                String line = entry.getKey() + "=" + entry.getValue() + "\n";
                bw.write(line);
            }

            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class PrefSaverTask extends TimerTask {
        @Override
        public void run() {
            saveMapToFile();
        }
    }
}
