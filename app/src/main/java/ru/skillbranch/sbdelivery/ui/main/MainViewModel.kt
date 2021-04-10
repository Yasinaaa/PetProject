package ru.skillbranch.sbdelivery.ui.main


import ru.skillbranch.sbdelivery.core.BaseViewModel
import ru.skillbranch.sbdelivery.data.mapper.IDishesMapper
import ru.skillbranch.sbdelivery.data.repository.IDishRepository

class MainViewModel(
    private val repository: IDishRepository,
    private val dishesMapper: IDishesMapper,
    //private val notifier: BasketNotifier
): BaseViewModel() {

//        private val _text = MutableLiveData<String>().apply {
//        value = "This is gallery Fragment"
//    }
//    val text: LiveData<String> = _text
}