package com.loancalculator.finance.manager.room.tildao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import com.loancalculator.finance.manager.room.tildata.TilBaseData

interface TilBaseDao<T : TilBaseData> {
    /**
     * Delete the specified content from the database.
     * @return Number of rows affected. If greater than 0, the deletion was successful.
     */
    @Delete
    fun deleteContent(data: T): Int

    /**
     * Update the specified content in the database.
     * @return Number of rows affected. If greater than 0, the update was successful.
     */
    @Update
    fun updateContent(data: T): Int

    /**
     * Insert the specified content into the database.
     * @return The row ID of the newly inserted row, or -1 if the insertion failed.
     */
    @Insert
    fun insertContent(data: T): Long

    /**
     * Insert a list of contents into the database.
     * @return A list of row IDs of the newly inserted rows, where -1 indicates an insertion failure.
     */
    @Insert
    fun insertContent(data: MutableList<T>): MutableList<Long>
}
