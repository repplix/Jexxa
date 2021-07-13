package io.jexxa.infrastructure.drivenadapterstrategy.persistence;

import java.util.List;

/**
 *
 * @param <T> Type of the managed object
 * @param <S> Type of the metadata that is used to find the objects
 */
public interface IObjectQuery<T, S >
{
    /**
     * Get all values which fulfill: {@code value <= metadata of the object}
     *
     * @param value concrete value that is used for comparison
     * @return list of all objects that fulfill the condition {@code value <= metadata of the object}
     */
    List<T> getGreaterOrEqualThan(S value);

    /**  get all values which fulfill: {@code value < returnedValue}
     */
    List<T> getGreaterThan(S value);

    /** get all values which fulfill:  {@code value <= endValue}
     */
    List<T> getLessOrEqualThan(S endValue);

    /** get all values which fulfill:  {@code value < endValue}
     */
    List<T> getLessThan(S endValue);

    /**
     * Returns all elements equal to S
     * @param value specifies comparison value
     * @return list of elements that are equal to value
     */
    List<T> getEqualTo(S value);


    /** get all values which fulfill: {@code startValue <= value <= endValue}
     */
    List<T> getRangeClosed(S startValue, S endValue);

    /** get all values which fulfill: {@code startValue <= value < endValue}
     */
    List<T> getRange(S startValue, S endValue);

    /**
     * Sorts the entries by S in ascending order and returns the defined amount of elements
     * @param amount specifies the number of recent added aggregates that should be returned.
     * @return list of elements limited by the given amount.
     *         If less then requested aggregates are managed, all aggregates are returned.
     *         If amount is &lt; 0 then an empty list ist returned
     */
    List<T> getAscending(int amount);

    /**
     * Sorts the entries by S in descending order and returns the defined amount of elements
     * @param amount specifies the number of recent added aggregates that should be returned.
     * @return list of elements limited by the given amount.
     *         If less then requested aggregates are managed, all aggregates are returned.
     *         If amount is &lt; 0 then an empty list ist returned
     */
    List<T> getDescending(int amount);
}