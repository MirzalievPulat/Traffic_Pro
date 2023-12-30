package uz.frodo.trafficpro

import uz.frodo.trafficpro.databinding.ItemBinding
import uz.frodo.trafficpro.models.Sign

interface OnClick {
    fun onClick(sign: Sign)
    fun onEdit(sign: Sign,position:Int)
    fun onDelete(sign: Sign)
    fun onLike(sign: Sign,b: ItemBinding)
}