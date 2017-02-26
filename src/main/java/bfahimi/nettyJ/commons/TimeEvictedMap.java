package bfahimi.nettyJ.commons;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.RemovalListener;

import lombok.extern.slf4j.Slf4j;

/**
 * Cache with time based eviction to handle join operations for the stream.
 * If a tuple fails during the stream processing, a normal map could
 * contain zombie entries. The time based cache will evict entries
 * which wait longer than the time a tuple is allowed to be in the
 * topology.
 * <p>
 * In case of an temporary error Storm will internally retry the whole
 * tuple after the tuple time-out. Thus we must ensure the faulty state is
 * removed from the cache to avoid wasting memory.
 *
 * @param <K> Key type under which to store a value.
 * @param <V> Value type to store in the cache.
 */
@Slf4j
public class TimeEvictedMap<K, V> {

    private final LoadingCache<K, V> cache;

    /**
     * Cache with time based eviction.
     *
     * @param duration    Time after which the entries should be removed.
     * @param unit        Specifies in which unit the given duration is.
     * @param cacheLoader Supplier to create new instances of &lt;V&gt;.
     */
    public TimeEvictedMap(long duration, TimeUnit unit, Function<K, V> cacheLoader, RemovalListener<K, V> removalListener) {
        this.cache = Caffeine
                .newBuilder()
                .expireAfterAccess(duration, unit)
                .removalListener(removalListener)
                .build(cacheLoader::apply);
    }

    /**
     * Get the currently stored information from the cache. If no entry was stored yet
     * for the given identifier a new entry is created automatically.
     *
     * @param key Cache lookup key.
     * @return Created or cached instance associated with the given cache key. Could
     * artificially return null if lookup or creation fails.
     */
    public V get(K key) {
        return cache.get(key);
    }

    public void put(K key, V value){
        cache.put(key, value);
    }

    /**
     * Returns the associated entry for a given key if it has been assigned before.
     *
     * @param key Cache key lookup.
     * @return The associated cache entry, null otherwise.
     */
    public V getIfPresent(K key) {
        return cache.getIfPresent(key);
    }

    /**
     * Remove an entry from the cache prematurely.
     *
     * @param key Cache lookup key to remove date for.
     */
    public void evict(K key) {
        cache.invalidate(key);
    }

    /**
     * Get a snapshot view onto the current state of the cache. The returned data structure is immutable.
     * <p>
     * <i>NOTE</i> that the map view onto the cache is only <i>weakly consistent</i>.
     * See {@link LoadingCache#asMap()} for details.
     *
     * @return Immutable map view onto the current state of the cache.
     */
    public Map<K, V> getSnapshot() {
        return Collections.unmodifiableMap(cache.asMap());
    }

}
