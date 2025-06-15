package com.riezki.cmpcurrencyapp.data.local.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

/**
 * @author riezky maisyar
 */


open class CurrencyEntity : RealmObject {
    @PrimaryKey var _id = ObjectId()
    var code = ""
    var value = 0.0
}