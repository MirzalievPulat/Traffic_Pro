package uz.frodo.trafficpro.models

import android.os.Parcel
import android.os.Parcelable

data class Sign(var id:Int? = null, var name: String?, var about: String?, var image: String?, var type: String?, var
liked:Int) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(name)
        parcel.writeString(about)
        parcel.writeString(image)
        parcel.writeString(type)
        parcel.writeInt(liked)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Sign> {
        override fun createFromParcel(parcel: Parcel): Sign {
            return Sign(parcel)
        }

        override fun newArray(size: Int): Array<Sign?> {
            return arrayOfNulls(size)
        }
    }
}
