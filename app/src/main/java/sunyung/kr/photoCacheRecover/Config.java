package sunyung.kr.photoCacheRecover;

import android.content.Context;
import android.graphics.Point;
import android.os.Environment;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

import java.io.File;
import java.util.Calendar;

/**
 * Created by tbzm on 16. 4. 18.
 */
public class Config {
    public static final String NEW_IMAGE_DIRECTORY = Environment.getExternalStorageDirectory() + File.separator + "photoMates";
    public static final int DATA = 1000;
    public static final int FILE_SCAN_HANDLER = DATA * 0x01;
    public static final int FILE_REPAIR_HANDLER = DATA * 0x02;

}
