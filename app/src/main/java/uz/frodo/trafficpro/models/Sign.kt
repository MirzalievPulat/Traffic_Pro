package uz.frodo.trafficpro.models

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
data class Sign(var name: String?, var about: String?, var image: String?, var type: String?, var
liked:Int) :Parcelable {

    @PrimaryKey(autoGenerate = true)
    var id:Int = 0

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt()
    ) {
        id = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(about)
        parcel.writeString(image)
        parcel.writeString(type)
        parcel.writeInt(liked)
        parcel.writeInt(id)
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
