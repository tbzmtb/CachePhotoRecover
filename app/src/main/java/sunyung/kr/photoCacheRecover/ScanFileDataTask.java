package sunyung.kr.photoCacheRecover;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by tbzm on 16. 4. 25.
 */
public class ScanFileDataTask extends AsyncTask<String, Void, ArrayList<GridViewData>> {
    private Context mContext;
    private Handler mHandler;
    private final String TAG = getClass().getName();
    private ArrayList<GridViewData> data;
    private ProgressDialog mProgressDialog;

    public ScanFileDataTask(Context context, Handler handler) {
        mContext = context;
        mHandler = handler;
        data = new ArrayList<>();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage(mContext.getResources().getString(R.string.photo_restoring));
        mProgressDialog.show();
    }

    @Override
    protected ArrayList<GridViewData> doInBackground(String... args) {
        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        checkFileOfDirectory(getFileList(root));
        return data;
    }


    public void checkFileOfDirectory(File[] fileList) {
        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].isDirectory()) {
                checkFileOfDirectory(getFileList(fileList[i].getPath()));
            } else {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(fileList[i].getPath(), options);
                if (options.outWidth != -1 && options.outHeight != -1) {
                    data.add(new GridViewData(fileList[i].getPath(), false));

                }
            }
        }
    }

    public File[] getFileList(String strPath) {
        File fileRoot = new File(strPath);
        if (!fileRoot.isDirectory()) {
            return null;
        }

        return fileRoot.listFiles();
    }

    @Override
    protected void onPostExecute(ArrayList<GridViewData> result) {
        if (mProgressDialog != null) {
            mProgressDialog.cancel();
            mProgressDialog = null;
        }
        if (mHandler != null) {
            Message msg = Message.obtain();
            msg.what = Config.FILE_SCAN_HANDLER;
            msg.obj = result;
            mHandler.sendMessage(msg);
        }
        super.onPostExecute(result);
    }
}
