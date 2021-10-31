package com.eunwoo.contactlensmanagement

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "lensTable")
data class Lens(
    @PrimaryKey
    var id: Long?,

    @ColumnInfo(name = "name")
    var name: String?,

    @ColumnInfo(name = "contents")
    var contents: String?
)
