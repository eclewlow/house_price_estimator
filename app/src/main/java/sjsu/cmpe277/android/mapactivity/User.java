package sjsu.cmpe277.android.mapactivity;

import android.content.Context;
import android.location.Address;

public class User {
    private static User sUser;

    private Address mAddress;
    private double mHomeSize;
    private double mLotSize;
    private int mBeds;
    private int mBathrooms;

    public static User get(Context context) {
        if (sUser == null) {
            sUser = new User(context);
        }
        return sUser;
    }

    private User(Context context) {
        mBathrooms = 0;
        mBeds = 0;
        mHomeSize = 0;
        mLotSize = 0;
    }

    public Address getAddress() {
        return mAddress;
    }

    public void setAddress(Address address) {
        this.mAddress = address;
    }

    public double getHomeSize() {
        return mHomeSize;
    }

    public void setHomeSize(double homeSize) {
        mHomeSize = homeSize;
    }

    public double getLotSize() {
        return mLotSize;
    }

    public void setLotSize(double lotSize) {
        mLotSize = lotSize;
    }

    public int getBeds() {
        return mBeds;
    }

    public void setBeds(int beds) {
        mBeds = beds;
    }

    public int getBathrooms() {
        return mBathrooms;
    }

    public void setBathrooms(int bathrooms) {
        mBathrooms = bathrooms;
    }
}
