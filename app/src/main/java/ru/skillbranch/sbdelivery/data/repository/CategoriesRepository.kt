package ru.skillbranch.sbdelivery.data.repository

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.skillbranch.sbdelivery.data.local.dao.CategoryDao
import ru.skillbranch.sbdelivery.data.local.entity.CategoryEntity
import ru.skillbranch.sbdelivery.data.local.pref.PrefManager
import ru.skillbranch.sbdelivery.data.mapper.ICategoriesMapper
import ru.skillbranch.sbdelivery.data.remote.RestService

interface ICategoryRepository {
    fun getParentCategories(offset: Int, limit: Int): Single<MutableList<CategoryEntity>>
    fun findCategoriesByName(searchText: String): Observable<MutableList<CategoryEntity>>
}

class CategoriesRepository(
    private val prefs: PrefManager,
    private val api: RestService,
    private val mapper: ICategoriesMapper,
    private val categoryDao: CategoryDao
) : ICategoryRepository {

    override fun getParentCategories(offset: Int, limit: Int): Single<MutableList<CategoryEntity>> =
        api.getCategories(offset, limit,
            "${PrefManager.BEARER} ${prefs.accessToken}")
            .map {
                val items = mapper.mapDtoToEntity(it)
                categoryDao.insert(items)
                items
            }
            .flattenAsObservable {it}
            .filter { it.parent == null }
            .toList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    override fun findCategoriesByName(searchText: String): Observable<MutableList<CategoryEntity>> =
        categoryDao.findCategoriesByName(searchText)
            .map {
                it.sortByDescending { c -> c.parent.isNullOrBlank()}
                it
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

}