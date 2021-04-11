package ru.skillbranch.sbdelivery.core

import androidx.lifecycle.SavedStateHandle

interface IViewModelState {
    /**
     * override this if need save state in bundle
     */
    fun save(outState: SavedStateHandle){
        //default empty implementation
    }

    /**
     * override this if need restore state from bundle
     */
    fun restore(savedState: SavedStateHandle) : IViewModelState{
        //default empty implementation
        return this
    }
}