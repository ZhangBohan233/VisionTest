package common.data;

public class AutoSavers {

    public static final long PERIOD = 5000;

    private static PrefSaver prefSaver;
    private static CacheSaver cacheSaver;

    public static void startTestSavers() {
        if (prefSaver != null) throw new RuntimeException("A saver is already running");
        prefSaver = new PrefSaver(PrefSaver.TEST_PREF);

        if (cacheSaver != null) throw new RuntimeException("A saver is already running");
        cacheSaver = new CacheSaver(CacheSaver.TEST_CACHE_FILE);
    }

    public static void startScreenSavers() {
        if (prefSaver != null) throw new RuntimeException("A saver is already running");
        prefSaver = new PrefSaver(PrefSaver.SCREEN_PREF);

        if (cacheSaver != null) throw new RuntimeException("A saver is already running");
        cacheSaver = new CacheSaver(CacheSaver.SCREEN_CACHE_FILE);
    }

    public static void stopAllSavers() {
        if (prefSaver != null) {
            prefSaver.saveAndStop();
        }
        if (cacheSaver != null) {
            cacheSaver.saveAndStop();
        }
    }

    public static PrefSaver getPrefSaver() {
        return prefSaver;
    }

    public static CacheSaver getCacheSaver() {
        return cacheSaver;
    }
}
