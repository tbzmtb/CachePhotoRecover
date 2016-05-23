package sunyung.kr.photoCacheRecover;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by tbzm on 16. 5. 13.
 */
public class PictureRepairTask extends AsyncTask<String, String, String> {
    private Context mContext;
    private Handler mHandler;
    private final String TAG = getClass().getName();
    private ArrayList<GridViewData> data;
    private ProgressDialog mProgressDialog;

    public PictureRepairTask(Context context, ArrayList<GridViewData> data, Handler handler) {
        mContext = context;
        mHandler = handler;
        this.data = data;
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
    protected String doInBackground(String... params) {
        for (int i = 0; i < data.size(); i++) {
            File src = new File(data.get(i).getPath());
            File file = new File(Config.NEW_IMAGE_DIRECTORY);
            File dst = new File(Config.NEW_IMAGE_DIRECTORY + File.separator + getFileName(i));

            try {
                if (!dst.exists()) {
                    file.createNewFile();
                }
                copy(src, dst);
                new MediaScanning(mContext, dst);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void copy(File src, File dst) throws IOException {
        FileInputStream inStream = new FileInputStream(src);
        FileOutputStream outStream = new FileOutputStream(dst);
        FileChannel inChannel = inStream.getChannel();
        FileChannel outChannel = outStream.getChannel();
        inChannel.transferTo(0, inChannel.size(), outChannel);
        inStream.close();
        outStream.close();
    }


    public String getFileName(int index) {
        Date day = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HH_mm_ss", Locale.KOREA);
        return String.valueOf(sdf.format(day) + index + ".png");
    }

    @Override
    protected void onPostExecute(String result) {
        if (mProgressDialog != null) {
            mProgressDialog.cancel();
            mProgressDialog = null;
        }
        if (mHandler != null) {
            Message msg = Message.obtain();
            msg.what = Config.FILE_REPAIR_HANDLER;
            msg.obj = result;
            mHandler.sendMessage(msg);
        }
        super.onPostExecute(result);
    }

}
