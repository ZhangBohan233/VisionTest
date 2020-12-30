package dvaTest.testCore;

/**
 * 测试控制器，用于实际控制测试并发送数据给屏幕端
 */
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
