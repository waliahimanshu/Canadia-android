package com.waliahimanshu.canadia.util


interface BaseView<in T : BasePresenter> {

    fun setPresenter(presenter: T)

}