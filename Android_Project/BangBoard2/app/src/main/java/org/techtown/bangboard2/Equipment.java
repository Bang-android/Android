package org.techtown.bangboard2;

import android.os.Parcel;
import android.os.Parcelable;

public class Equipment implements Parcelable {
    String name;
    String point;
    String position;
    String method;
    String image;
    String youtube_id;

    public Equipment (String name, String point, String position, String method, String image, String youtube_id) {
        this.name = name;
        this.point = point;
        this.position = position;
        this.method = method;
        this.image = image;
        this.youtube_id = youtube_id;
    }

    public Equipment (Parcel src) {
        this.name = src.readString();
        this.point = src.readString();
        this.position = src.readString();
        this.method = src.readString();
        this.image = src.readString();
        this.youtube_id = src.readString();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

        @Override
        public Equipment createFromParcel(Parcel parcel) {
            return new Equipment(parcel);
        }

        @Override
        public Equipment[] newArray(int i) {
            return new Equipment[i];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getYoutube_id() {
        return youtube_id;
    }

    public void setYoutube_id(String youtube_id) {
        this.youtube_id = youtube_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(point);
        parcel.writeString(position);
        parcel.writeString(method);
        parcel.writeString(image);
        parcel.writeString(youtube_id);
    }
}
