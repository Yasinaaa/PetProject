package ru.skillbranch.sbdelivery.data.remote.err

class EmptyDishesError(val messageDishes: String = "") : Throwable(messageDishes) {
}