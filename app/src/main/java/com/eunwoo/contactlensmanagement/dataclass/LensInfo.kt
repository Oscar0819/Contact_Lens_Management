package com.eunwoo.contactlensmanagement.dataclass

data class LensInfo(var name: String,
                    var leftSight: Double,
                    var rightSight: Double,
                    var productName: String,
                    var initialDate: String,
                    var expirationDate: String,
                    var pushCheck: Boolean,
                    var memo: String
)
