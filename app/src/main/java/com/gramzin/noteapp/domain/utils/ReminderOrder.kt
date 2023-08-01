package com.gramzin.noteapp.domain.utils

sealed class ReminderOrder(open val orderType: OrderType){

    class Title(orderType: OrderType): ReminderOrder(orderType)

    class CreationDate(orderType: OrderType): ReminderOrder(orderType)

    class ReminderDate(orderType: OrderType): ReminderOrder(orderType)

    fun copy(orderType: OrderType): ReminderOrder {
        return when(this){
            is Title -> Title(orderType)
            is CreationDate -> CreationDate(orderType)
            is ReminderDate -> ReminderDate(orderType)
        }
    }
}