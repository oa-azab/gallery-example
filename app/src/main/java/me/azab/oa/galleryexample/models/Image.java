package me.azab.oa.galleryexample.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by omar on 23/03/2017.
 */

public class Image implements Parcelable {

    private String url;
    private String title;

    public Image(String url, String title) {
        this.url = url;
        this.title = title;
    }

    protected Image(Parcel in) {
        url = in.readString();
        title = in.readString();
    }

    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * This is test method of static data
     *
     * @return List of images
     */
    public static List<Image> getImages() {
        List<Image> mList = new ArrayList<>();

        mList.add(new Image("https://farm4.staticflickr.com/3848/33608700335_816752705e_b.jpg", "Wild Duck"));
        mList.add(new Image("https://farm3.staticflickr.com/2894/32765424984_417cb3b317_b.jpg", "DSC03299"));
        mList.add(new Image("https://farm4.staticflickr.com/3928/32794237203_52cbf661d0_b.jpg", "DSC03428"));
        mList.add(new Image("https://farm3.staticflickr.com/2857/33479589161_b7dee4a645_b.jpg", "Canada Goose (Branta canadensis)"));
        mList.add(new Image("https://farm3.staticflickr.com/2929/33608594505_fcd8e47430_b.jpg", "herring gull"));
        mList.add(new Image("https://farm3.staticflickr.com/2927/33479554531_7436cbe8d2_b.jpg", "Moorhen (Gallinula chlorops)"));
        mList.add(new Image("https://farm3.staticflickr.com/2851/33608570965_074e8a7e90_b.jpg", "Cuddle"));
        mList.add(new Image("https://farm3.staticflickr.com/2916/33451979072_b8174bab3d_b.jpg", "THE ROLLING PEOPLE"));
        mList.add(new Image("https://farm3.staticflickr.com/2912/33479519731_d4b22cba4d_b.jpg", "Stork#2"));
        mList.add(new Image("https://farm4.staticflickr.com/3698/33225309650_51be6114c2_b.jpg", "Phainopepla"));

        return mList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(url);
        parcel.writeString(title);
    }
}
