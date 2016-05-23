package sunyung.kr.photoCacheRecover;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by tbzm on 16. 4. 28.
 */
public class ImageGridViewAdapter extends BaseAdapter implements View.OnClickListener {
    private static final String TAG = "ImageGridViewAdapter";
    private LayoutInflater mInflater;
    private ArrayList<GridViewData> mImagePath;
    private Context mContext;


    public ImageGridViewAdapter(Context context, ArrayList<GridViewData> imagePath) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService((Context.LAYOUT_INFLATER_SERVICE));
        this.mImagePath = imagePath;
    }

    private static class ViewHolder {
        public ImageView mImageView;
        public ImageView mImageNone;
        public ImageView mImageViewSelected;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.grid_row, null);
            holder = new ViewHolder();
            holder.mImageView = (ImageView) convertView.findViewById(R.id.image);
            holder.mImageView.setOnClickListener(this);
            holder.mImageNone = (ImageView) convertView.findViewById(R.id.image_none);
            holder.mImageNone.setOnClickListener(this);
            holder.mImageViewSelected = (ImageView) convertView.findViewById(R.id.image_selected);
            holder.mImageViewSelected.setOnClickListener(this);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        GridViewData data = mImagePath.get(position);
        holder.mImageNone.setTag(data);
        holder.mImageViewSelected.setTag(data);
        if (data.getSelected()) {
            holder.mImageViewSelected.setVisibility(View.VISIBLE);
        } else {
            holder.mImageViewSelected.setVisibility(View.INVISIBLE);
        }
        Glide.with(mContext).load(data.getPath()).centerCrop().into(holder.mImageView);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image: {

                break;
            }
            case R.id.image_none: {
                GridViewData data = (GridViewData) v.getTag();
                Logger.d(TAG, "data = " + data.getPath());
                if (data.getSelected()) {
                    data.setSelcted(false);
                } else {
                    data.setSelcted(true);
                }
                ((MainActivity) mContext).setButtonText(getSelectedItem().size());
                notifyDataSetChanged();
                break;
            }
            case R.id.image_selected: {
                GridViewData data = (GridViewData) v.getTag();
                Logger.d(TAG, "data = " + data.getPath());
                if (data.getSelected()) {
                    data.setSelcted(false);
                } else {
                    data.setSelcted(true);
                }
                ((MainActivity) mContext).setButtonText(getSelectedItem().size());
                notifyDataSetChanged();
                break;
            }
        }
    }


    public ArrayList<GridViewData> getSelectedItem() {
        ArrayList<GridViewData> data = new ArrayList<>();
        int count = 0;
        if (mImagePath != null) {
            for (int i = 0; i < mImagePath.size(); i++) {
                if (mImagePath.get(i).getSelected()) {
                    data.add(mImagePath.get(i));
                }
            }
        }
        return data;
    }

    public void setAllDataDeSeleted() {
        if (mImagePath != null) {
            for (int i = 0; i < mImagePath.size(); i++) {
                if (mImagePath.get(i).getSelected()) {
                    mImagePath.get(i).setSelcted(false);
                }
            }
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return mImagePath.get(position);
    }

    @Override
    public int getCount() {
        return mImagePath.size();
    }
}
