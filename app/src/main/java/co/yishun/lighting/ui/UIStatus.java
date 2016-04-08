package co.yishun.lighting.ui;

import android.support.annotation.IntDef;

/**
 * Created by carlos on 4/8/16.
 */

@IntDef({UIStatus.STATUS_NOTHING, UIStatus.STATUS_NETWORKING})
public @interface UIStatus {
    int STATUS_NETWORKING = 1;
    int STATUS_NOTHING = 0;
}
