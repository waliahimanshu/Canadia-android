package com.waliahimanshu.canadia.ui.home

import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.PropertyName
import java.util.*


@IgnoreExtraProperties
class ExpressEntryModel {

    @PropertyName("crs_draw_date")
    lateinit var crsDrawDate: String

    @PropertyName("crs_score")
    lateinit var crsScore: String

    @PropertyName("ita_issues")
    lateinit var totalItaIssued: String

    @PropertyName("tie_breaker_date")
    lateinit var tieBreakerDate: String

    constructor() {
        // Default constructor required for calls to DataSnapshot.getValue(XX.class)
    }

    /**
     * A map of sample (dummy) items, by ID.
     */
    val ITEM_MAP: MutableMap<Int, ExpressEntryModel> = HashMap()
}
