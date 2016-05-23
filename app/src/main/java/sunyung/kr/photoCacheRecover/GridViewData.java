package sunyung.kr.photoCacheRecover;

import android.widget.GridView;

/**
 * Created by tbzm on 16. 5. 13.
 */
public class GridViewData {

    String mPath;
    boolean mSelcted;

    public GridViewData(String path, boolean selected) {
        mPath = path;
        mSelcted = selected;
    }

    public String getPath() {
        return mPath;
    }

    public void setPath(String path) {
        mPath = path;
    }

    public boolean getSelected() {
        return mSelcted;
    }

    public void setSelcted(boolean selected) {
        mSelcted = selected;
    }

}
