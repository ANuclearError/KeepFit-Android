package com.aidanogrady.keepfit.data.source;

import com.aidanogrady.keepfit.data.model.Goal;

import java.util.List;

/**
 * Interface for accessing goals data.
 *
 * @author Aidan O'Grady
 * @since 0.2
 */
public interface GoalsDataSource extends DataSource {
    /**
     * Interface for the callback when goals are loaded or unavailable.
     */
    interface LoadGoalsCallback {
        void onGoalsLoaded(List<Goal> goals);

        void onDataNotAvailable();
    }

    /**
     * Interface for the callback when a goal is loaded or is unavailable.
     */
    interface GetGoalCallback {
        void onGoalLoaded(Goal goal);

        void onDataNotAvailable();
    }


    /**
     * Gets all goals and performs the given callback.
     *
     * @param callback the callback to enact when goals retrieved
     */
    void getGoals(LoadGoalsCallback callback);

    /**
     * Gets the goal with the given ID and enacts the given callback.
     *
     * @param id the ID of the goal
     * @param deleted retrieve goal user has deleted if true
     * @param callback the callback to enact when goal is retrieved
     */
    void getGoal(String id, boolean deleted, GetGoalCallback callback);

    /**
     * Inserts this goal to the data source.
     *
     * @param goal the goal to be inserted
     */
    void insertGoal(Goal goal);

    /**
     * Updates the given goal to the database. The id and name of the goal are used to determine if
     * the goal can be updated.
     *
     * @param goal the goal to be updated
     */
    void updateGoal(Goal goal, String oldId);

    /**
     * Refreshes the data source.
     */
    void refreshGoals();

    /**
     * Deletes all goals from the data source
     */
    void deleteAllGoals();

    /**
     * Deletes the goal with the given ID from the data source.
     *
     * @param id the goal to be deleted from data source
     */
    void deleteGoal(String id);
}

