package com.aidanogrady.keepfit.base;

/**
 * The BaseView of the application.
 *
 * @author Aidan O'Grady
 * @since 0.1
 */
public interface BaseView<T> {
    /**
     * Sets the presenter to the given presenter.
     *
     * @param presenter the presenter to be set
     */
    void setPresenter(T presenter);
}
