package com.waliahimanshu.canadia.ui.home

import com.waliahimanshu.canadia.ui.home.DataItemsModel.Companion.CONTENT_ITEM_VIEW
import java.text.DateFormatSymbols
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ExpressEntryMapper @Inject constructor() {
    fun map(expressEntryDTO: ArrayList<ExpressEntryDTO>): ArrayList<ExpressEntryModel> {

        val simpleDateFormat = SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH)

        val expressEntryModel = arrayListOf<ExpressEntryModel>()
        var crsDate: Date? = null

        for (dto in expressEntryDTO) {
                try {
                    crsDate = simpleDateFormat.parse(dto.crsDrawDate)
                } catch (ex: ParseException) {
                    ex.message
                }
                expressEntryModel.add(
                        DataItemsModel("2018",
                                crsDate, dto.crsScore, dto.tieBreakerDate, dto.totalItaIssued))

        }

        val result = expressEntryModel.groupBy { t -> getType(t).date?.month }

        expressEntryModel.clear();
        for (entry in result) {

            val filter = entry.value.filter { x -> getType(x).year == "2017" }
            expressEntryModel.add(MonthModel(getMonthForInt(entry.key)))
            expressEntryModel.addAll(filter)

        }
        return expressEntryModel
    }

    private fun getType(t: ExpressEntryModel): DataItemsModel {
        if (t.getItemViewType() == CONTENT_ITEM_VIEW) {
            return t as DataItemsModel
        }
        throw IllegalArgumentException("Invalid type " + t.getItemViewType())
    }

    private fun getMonthForInt(num: Int?): String {
        var month = ""
        val dfs = DateFormatSymbols()
        val months = dfs.months
        if (num != null) {
            if (num in 0..11) {
                month = months[num]
            }
        }
        return month
    }
}


