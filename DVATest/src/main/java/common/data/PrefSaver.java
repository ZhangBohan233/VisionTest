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

    public PrefSaver(String prefFile) {
        this.fileName = prefFile;
        map = loadMap(prefFile);

        timer = new Timer();
        timer.schedule(new PrefSaverTask(), 0, AutoSavers.PERIOD);
    }

    public void saveAndStop() {
        saveMapToFile();
        timer.cancel();
    }

    public void storePref(String key, String value) {
        map.put(key, value);
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
