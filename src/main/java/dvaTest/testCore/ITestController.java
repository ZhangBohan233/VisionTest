package dvaTest.testCore;

public interface ITestController {

    /**
     * 关闭测试输入窗口
     */
    void closeTestView();

    /**
     * 如当前正在测试则将其打断
     */
    void interrupt();
}
