package com.example.noteapp.domain.utils

sealed class OrderType{
    object Ascending: OrderType()
    object Descending: OrderType()
}
