package com.gu.toolargetool

import android.os.Parcel
import android.os.Parcelable

class ParcelableByteArray(size: Int): Parcelable {
    val array = ByteArray(size)

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeByteArray(array)
    }

    override fun describeContents() = 0
}
