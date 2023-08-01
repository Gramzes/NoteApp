package com.gramzin.noteapp.domain.utils

sealed class OrderType{
    object Ascending: OrderType()
    object Descending: OrderType()
}
