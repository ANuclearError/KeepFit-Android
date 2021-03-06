package com.aidanogrady.keepfit.data.source;

import com.aidanogrady.keepfit.data.model.Update;

import java.util.List;

/**
 * Interface for accessing updates.
 *
 * @author Aidan O'Grady
 * @since 0.2
 */
public interface UpdatesDataSource extends DataSource {
    /**
     * Interface for the callback when an update are loaded or unavailable.
     */
    interface LoadUpdatesCallback {
        void onUpdatesLoaded(List<Update> updates);

        void onDataNotAvailable();
    }

    /**
     * Interface for the callback when updates are loaded or is unavailable.
     */
    interface GetUpdateCallback {
        void onUpdateLoaded(Update update);

        void onDataNotAvailable();
    }


    /**
     * Gets all updates from the data source.
     *
     * @param callback the callback to use when data retrieved.
     */
    void getUpdates(LoadUpdatesCallback callback);

    /**
     * Gets all updates with the given date from the data source.
     *
     * @param date the date to get updates for
     * @param callback the callback to use when data retrieved.
     */
    void getUpdatesForDate(long date, LoadUpdatesCallback callback);

    /**
     * Inserts the given update into the database.
     *
     * @param update the update to be added
     */
    void insertUpdate(Update update);

    /**
     * Removes all updates from the data source.
     */
    void deleteAllUpdates();
}
