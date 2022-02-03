package com.eunwoo.contactlensmanagement.repo

import android.app.Application
import com.eunwoo.contactlensmanagement.database.Lens
import com.eunwoo.contactlensmanagement.database.LensDao
import com.eunwoo.contactlensmanagement.database.LensDatabase

class LensInfoRepository(application: Application) {
    private val lensDao: LensDao

    init {
        var lensDatabase = LensDatabase.getInstance(application)
        lensDao = lensDatabase!!.lensDao()
    }

    companion object {

    }

    fun insert(lens: Lens) {
        lensDao.insert(lens)
    }

    fun delete(lens: Lens) {
        lensDao.delete(lens)
    }

    fun modify(lens: Lens) {
        lensDao.update(lens)
    }

    fun getList(index: Int): Lens{
        return lensDao.getList()[index]
    }

}