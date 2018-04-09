package com.waliahimanshu.canadia.ui.home

import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.PropertyName


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

    lateinit var year: String

    constructor() {
        // Default constructor required for calls to DataSnapshot.getValue(XX.class)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ExpressEntryModel) return false

        if (crsDrawDate != other.crsDrawDate) return false
        if (crsScore != other.crsScore) return false
        if (totalItaIssued != other.totalItaIssued) return false
        if (tieBreakerDate != other.tieBreakerDate) return false
        if (year != other.year) return false

        return true
    }

    override fun hashCode(): Int {
        var result = crsDrawDate.hashCode()
        result = 31 * result + crsScore.hashCode()
        result = 31 * result + totalItaIssued.hashCode()
        result = 31 * result + tieBreakerDate.hashCode()
        result = 31 * result + year.hashCode()
        return result
    }


}
