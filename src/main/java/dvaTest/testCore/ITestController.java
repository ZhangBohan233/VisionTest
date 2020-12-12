package dvaTest.testCore;

public interface ITestController {

    /**
     * 关闭测试输入窗口
     */
    void closeTestView();

    /**
     * 处理屏幕端发送的打断信号
     */
    void interruptByScreen();
}
