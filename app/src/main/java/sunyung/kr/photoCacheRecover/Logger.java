package sunyung.kr.photoCacheRecover;

import android.content.Context;
import android.util.Log;

public class Logger {
	
	public static final String PREF_LOG = "PREF_LOG";
	
	private static boolean isVerboseEnable = true;
	private static boolean isDebugEnable = true;
	private static boolean isInfoEnable = true;
	private static boolean isWarnEnable = true;
	private static boolean isErrorEnable = true;

	public static void setVerboseEnabled(boolean isEnable) {
		isVerboseEnable = isEnable;
	}
	
	public static void setDebugEnable(boolean isEnable) {
		isDebugEnable = isEnable;
	}
	
	public static void setInfoEnable(boolean isEnable) {
		isInfoEnable = isEnable;
	}
	
	public static void setWarnEnable(boolean isEnable) {
		isWarnEnable = isEnable;
	}
	
	public static void setErrorEnable(boolean isEnable) {
		isErrorEnable = isEnable;
	}
	
	public static void setAllLogEnable(Context context, boolean isEnable) {
		isVerboseEnable = isEnable;
		isDebugEnable = isEnable;
		isInfoEnable = isEnable;
		isWarnEnable = isEnable;
		isErrorEnable = isEnable;
	}

	public static void v(String tag, String log) {
		if (isVerboseEnable) {;
			Log.v(tag, log);
		}
	}
	
	public static void v(String tag, String log, Object... param) {
		if (isVerboseEnable) {
			Log.v(tag, String.format(log, param));
		}
	}
	
	public static void v(String tag, String log, Throwable t) {
		if (isVerboseEnable) {
			Log.v(tag, log, t);
		}
	}
	
	public static void d(String tag, String log) {
		if (isDebugEnable) {
			Log.d(tag, log);
		}
	}
	
	public static void d(String tag, String log, Object... param) {
		if (isDebugEnable) {
			Log.d(tag, String.format(log, param));
		}
	}
	
	public static void d(String tag, String log, Throwable t) {
		if (isDebugEnable) {
			Log.d(tag, log, t);
		}
	}
	
	public static void i(String tag, String log) {
		if (isInfoEnable) {
			Log.i(tag, log);
		}
	}
	
	public static void i(String tag, String log, Object... param) {
		if (isInfoEnable) {
			Log.i(tag, String.format(log, param));
		}
	}
	
	public static void i(String tag, String log, Throwable t) {
		if (isInfoEnable) {
			Log.i(tag, log, t);
		}
	}
	
	public static void w(String tag, String log) {
		if (isWarnEnable) {
			Log.w(tag, log);
		}
	}
	
	public static void w(String tag, String log, Object... param) {
		if (isWarnEnable) {
			Log.w(tag, String.format(log, param));
		}
	}
	
	public static void w(String tag, String log, Throwable t) {
		if (isWarnEnable) {
			Log.w(tag, log, t);
		}
	}
	
	public static void e(String tag, String log) {
		if (isErrorEnable) {
			Log.e(tag, log);
		}
	}
	
	public static void e(String tag, String log, Object... param) {
		if (isErrorEnable) {
			Log.e(tag, String.format(log, param));
		}
	}
	
	public static void e(String tag, String log, Throwable t) {
		if (isErrorEnable) {
			Log.e(tag, log, t);
		}
	}
}
