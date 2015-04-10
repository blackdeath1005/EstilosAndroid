package com.app.appandroid.menu.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.appandroid.R;
import com.app.appandroid.widgets.LoadImage;

public class ImagePagerAdapter extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;
    String[] mImages;
    ProgressDialog pDialog;
    LoadImage mLoadImage;

    public ImagePagerAdapter(Context context,String[] images,ProgressDialog dial) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mImages = images;
        pDialog = dial;
    }

    @Override
    public int getCount() {
        return mImages.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_image, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageEstablecimiento);

        //imageView.setImageResource(mImages[position]);
        mLoadImage = new LoadImage(itemView.getContext(),pDialog,imageView);
        mLoadImage.execute(mImages[position]);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

}
