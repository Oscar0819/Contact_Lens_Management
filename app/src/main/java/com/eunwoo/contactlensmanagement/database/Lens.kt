package com.eunwoo.contactlensmanagement.database

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

    @ColumnInfo(name = "left_sight")
    var leftSight: Double?,

    @ColumnInfo(name = "right_sight")
    var rightSight: Double?,

    @ColumnInfo(name = "product_name")
    var productName: String?,

    @ColumnInfo(name = "initial_date")
    var initialDate: String?,

    @ColumnInfo(name = "recommended_lenses_wear_time")
    var expirationDate: String?,

    @ColumnInfo(name = "push_check")
    var pushCheck: Boolean?,

    @ColumnInfo(name = "memo")
    var memo: String?
)
