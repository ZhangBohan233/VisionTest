package dvaScreen.connection;

import common.EventLogger;
import common.Signals;
import dvaScreen.gui.ScreenMainView;
import dvaScreen.gui.ScreenTestView;
import dvaTest.testCore.TestType;
import dvaTest.testCore.tests.TestUnit;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ServerSideListener extends Thread {

    private final ScreenMainView mainView;
    private final Server server;
    private final Socket client;
    private Stage screenTestStage;
    private ScreenTestView screenTestView;
    private boolean disconnected;

    ServerSideListener(ScreenMainView mainView, Server server, Socket client) {
        this.client = client;
        this.mainView = mainView;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            byte[] buf = new byte[1024];
            InputStream inputStream = client.getInputStream();
            int read;
            while (!(client.isInputShutdown() || disconnected) && (read = inputStream.read(buf)) >= 0) {
                if (read == 1) {  // 单个信号
                    processSignal(buf[0]);
                } else if (read > 0) {
                    byte[] array = new byte[read];
                    System.arraycopy(buf, 0, array, 0, read);
                    processSignal(array);
                }
            }
            server.startListening();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processSignal(byte signal) {
        switch (signal) {
            case Signals.GREET:
                System.out.println("connected! ");
                mainView.setConnectedUi();
                break;
            case Signals.SHOW_SNELLEN:
                showTestScreen(TestType.SNELLEN_CHART);
                break;
            case Signals.SHOW_E:
                showTestScreen(TestType.E_CHART);
                break;
            case Signals.SHOW_C:
                showTestScreen(TestType.C_CHART);
                break;

            case Signals.STOP_TEST:
                Platform.runLater(() -> screenTestStage.close());
                break;
            case Signals.DISCONNECT_FROM_CLIENT:
                disconnected = true;
                mainView.setDisconnectedUi();
                break;
        }
    }

    private void processSignal(byte[] array) {
        switch (array[0]) {
            case Signals.NEXT_TEST_UNIT:
                TestUnit testUnit = TestUnit.fromByteArray(array);
                screenTestView.showGraph(testUnit);
                break;
        }
    }

    private void showTestScreen(TestType testType) {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader =
                        new FXMLLoader(getClass().getResource("/dvaScreen/fxml/screenTestView.fxml"),
                                mainView.getBundle());
                Parent root = loader.load();

                screenTestStage = new Stage();
                screenTestStage.setMaximized(true);
                screenTestStage.setOnHidden(e -> {
                    try {
                        ServerManager.getCurrentServer().sendMessage(Signals.SCREEN_INTERRUPT);
                    } catch (IOException ioException) {
                        throw new RuntimeException(ioException);
                    }
                });

                screenTestView = loader.getController();
                screenTestView.setScreenParams(mainView.getPixelsPerMm(), mainView.getSystemZoom());
//                controller.setStage(primaryStage);

                screenTestStage.setTitle(testType.show(mainView.getBundle()));
                screenTestStage.setScene(new Scene(root));

                screenTestStage.show();
            } catch (IOException e) {
                EventLogger.log(e);
                throw new RuntimeException(e);
            }
        });
    }
}
