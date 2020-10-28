package common.data;

public class AutoSavers {

    public static final long PERIOD = 5000;

    private static PrefSaver prefSaver;

    public static void startTestSavers() {
        if (prefSaver != null) throw new RuntimeException("A saver is already running");
        prefSaver = new PrefSaver(PrefSaver.TEST_PREF);
    }

    public static void startScreenSavers() {
        if (prefSaver != null) throw new RuntimeException("A saver is already running");
        prefSaver = new PrefSaver(PrefSaver.SCREEN_PREF);
    }

    public static void stopAllSavers() {
        if (prefSaver != null) {
            prefSaver.saveAndStop();
        }
    }

    public static PrefSaver getPrefSaver() {
        return prefSaver;
    }
}
