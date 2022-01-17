package com.eunwoo.contactlensmanagement.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface LensDao {
    @Query("SELECT * FROM lensTable ORDER BY id ASC")
    fun getAll(): LiveData<List<Lens>>

    @Query("SELECT * FROM lensTable ORDER BY id ASC")
    fun getList(): List<Lens>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(lens: Lens)

    @Update
    fun update(lens: Lens)

    @Delete
    fun delete(lens: Lens)

    @Query("DELETE FROM lensTable")
    fun deleteAll()
}