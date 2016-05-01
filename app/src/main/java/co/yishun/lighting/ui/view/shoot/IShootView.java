package co.yishun.lighting.ui.view.shoot;

import java.io.File;

import co.yishun.lighting.function.Callback;
import co.yishun.lighting.function.Consumer;

/**
 * Created by Carlos on 2015/10/9.
 */
public interface IShootView {
    void releaseCamera();

    void setFlashlightOn(boolean isOn);

    void switchCamera(boolean isBack);

    boolean isFlashlightAvailable();

    boolean isFrontCameraAvailable();

    boolean isBackCamera();

    void record(Callback recordStartCallback, Consumer<File> recordEndConsumer);

    void stop();

    void setSecurityExceptionHandler(SecurityExceptionHandler exceptionHandler);

    interface SecurityExceptionHandler {
        void onHandler(SecurityException e);
    }
}
