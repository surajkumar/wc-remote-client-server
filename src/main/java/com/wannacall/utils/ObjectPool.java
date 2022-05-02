package com.wannacall.utils;

import com.wannacall.utils.logging.Logger;
import io.vertx.core.Vertx;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * An object pool is used to pre-construct objects that can be re-used throughout the life cycle of the program.
 * Such objects are often heavy to construct e.g. database connection objects or worker threads and such are
 * pre-created to be used throughout the application.
 *
 * @param <T> The object that the pool will consist of
 * @author Suraj Kumar
 * @version 1.0
 */
public abstract class ObjectPool<T> {
    /**
     * A queue containing our pooled objects
     */
    protected final Queue<T> pool = new ConcurrentLinkedQueue<>();
    /**
     * The maximum amount of objects that object pool can contain.
     * This exists so that we can control the number of objects being created to prevent an infinite number of
     * objects dynamically being created by the pool.
     */
    private final int capacity;
    /**
     * The number of objects currently available within the pool to be used. This includes objects that are currently
     * on loan.
     */
    private int amountInPool;

    /**
     * Constructs the object pool.
     *
     * @param capacity The maximum number of objects that can be stored within the pool.
     */
    public ObjectPool(int capacity) {
        this.capacity = capacity;
    }

    /**
     * Constructs the objects before being inserted into the pool. This must not be null.
     *
     * @return The fully constructed object to be placed within the pool.
     */
    protected abstract T construct();

    /**
     * A helper function to allow for any operations to be performed before an object is sent for retrieval.
     *
     * @param obj The object that is about to be loaned.
     */
    protected abstract void beforeRetrieval(T obj);

    /**
     * A helper function to allow for any operations before an object is returned back to the pool e.g. cleanup.
     *
     * @param obj The object that is about to be returned to the pool.
     */
    protected abstract void beforeReturn(T obj);

    /**
     * Keeps track of objects currently on loan and the time they were taken from the pool
     */
    private final Map<String, Long> time = new ConcurrentHashMap<>();

    /**
     * Fetches an object from the object pool and dynamically constructs objects whenever the pool does not
     * contain any free objects (until the capacity is reached).
     *
     * @return A next available object within the pool. Null if the capacity is reached and no objects are available.
     */
    public T get() {
        synchronized (pool) {
            if (pool.peek() == null && amountInPool < capacity) {
                var obj = construct();
                if (obj != null) {
                    pool.offer(obj);
                    amountInPool++;
                    Logger.debug("Added object to pool, size now: " + pool.size());
                } else {
                    Logger.error("Object pool constructed a NULL!!! Why??");
                }
                return obj;
            }
            var obj = pool.poll();
            if (obj == null) {
                Logger.error("A null object was polled from pool, consider increasing the pool size.");
            } else {
                beforeRetrieval(obj);
            }
            time.put(Thread.currentThread().getStackTrace()[3].getClassName() + "#" + Thread.currentThread().getStackTrace()[3].getMethodName(), System.currentTimeMillis());
            System.out.println("Lent Object from pool to: " + Thread.currentThread().getStackTrace()[3].getClassName() + "#" + Thread.currentThread().getStackTrace()[3].getMethodName());
            return obj;
        }
    }

    /**
     * This is used to monitor objects that are on loan which have not been returned back to the pool
     * after a certain period of time. Used for debugging potential failures elsewhere in the program where
     * a object may not be returned for some time.
     */
    public void monitor() {
        System.out.println("Starting Monitor");
        Vertx.vertx().setPeriodic(2000, res -> {
            if (time.size() > 1) {
                time.forEach((k, v) -> {
                    if ((System.currentTimeMillis() + 1000) > v) {
                        Logger.warning("Object in pool from " + k + " has been not been returned for a long time... elapsed: " + (System.currentTimeMillis() - v) + "ms");
                    }
                });
            }
        });
    }

    /**
     * Returns an object back to the pool and executes the beforeReturn.
     * As objects are not identifiable by current design, it is possible to return user created objects to by-pass
     * the pool capacity.
     *
     * TODO: Consider disallowing user created objects to be added to the pool
     *
     * @param obj The object to return to the pool.
     */
    public void returnToPool(T obj) {
        synchronized (pool) {
            var name = Thread.currentThread().getStackTrace()[3].getClassName() + "#" + Thread.currentThread().getStackTrace()[3].getMethodName();
            if (pool.contains(obj)) {
                Logger.warning("Object has already been returned to the pool, caller " + name);
                return;
            }
            if(amountInPool >= capacity) {
                Logger.warning("Skipped returning object back, pool is full!");
                return;
            }
            time.remove(name);
            if(obj == null) {
                amountInPool--;
                Logger.warning("Skipped returning null back to pool");
                return;
            }
            beforeReturn(obj);
            pool.offer(obj);
            amountInPool--;
            Logger.debug("Borrowed object has been returned to the pool");
            System.out.println("Returned object to connection pool: " + name);
        }
    }
}