package com.thread.cache;

import java.time.LocalTime;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class SimpleTieredCache<V extends SimpleTieredCache.DataWithId>  {

	private final int maxCapacity;
	private final ConcurrentSkipListMap<DataCacheKey, V> skipListMap; // sorted keys stored here.
	private final ConcurrentHashMap<DataCacheKey, V> cacheDataMap;
	private final DataSourceInstance<V> dataSourceInstance;

	public SimpleTieredCache(int maxCapacity, DataSourceInstance<V> dataSourceInstance) {
		this.maxCapacity = maxCapacity;
		this.dataSourceInstance = dataSourceInstance;

		this.skipListMap = new ConcurrentSkipListMap<>(new Comparator<DataCacheKey>() {
			@Override
			public int compare(DataCacheKey o1, DataCacheKey o2) {
				// lower access time are stored towards end.
				return new Long(o2.lastAccessedTimeNano).compareTo(o1.lastAccessedTimeNano);
			}
		});

		this.cacheDataMap = new ConcurrentHashMap<>();
	}


	public void save(V data) {
		synchronized (data.getId().intern()) {
			checkSizeAndFlushIfNeeded();
			// get existing entry in cache.
			DataCacheKey cacheKey = new DataCacheKey(data.getId());
			cacheKey.lastAccessedTimeNano = LocalTime.now().toNanoOfDay();
			skipListMap.put(cacheKey, data);
			cacheDataMap.put(cacheKey, data);
		}
	}

	private void checkSizeAndFlushIfNeeded() {
		// check size and remove.
		int i = maxCapacity;
		if (i < cacheDataMap.size()) {
			synchronized (this) {// sync on this object since we don't know which key is last.
				int size = cacheDataMap.size();
				while (i < size) {
					Map.Entry<DataCacheKey, V> lastEntry = skipListMap.lastEntry();
					V value = cacheDataMap.get(lastEntry.getKey());
					if (value != null) {
						saveToDB(value);
					}
					cacheDataMap.remove(lastEntry.getKey());
					skipListMap.pollLastEntry();
					i++;
				}
			}
		}
	}

	private void saveToDB(V value) {
		dataSourceInstance.saveToDB();
	}

	private V getFromDB(String id) {
		return dataSourceInstance.getFromDB(id);
	}


	public V get(String id) {
		synchronized (id.intern()) {
			// check if exists in cache.
			DataCacheKey dataCacheKey = new DataCacheKey(id);
			dataCacheKey.lastAccessedTimeNano = LocalTime.now().toNanoOfDay();
			V data = cacheDataMap.get(dataCacheKey);
			if (data == null) {
				data = getFromDB(id);
				cacheDataMap.put(dataCacheKey, data);
				// this will mostly put duplicate data, but cannot see a way around that unless we also save dataCacheKey with V. This is because of the comparator used in skiplist.
				skipListMap.put(dataCacheKey, data);
			}
			return data;
		}
	}

	interface DataSourceInstance<V> {
		void saveToDB();
		V getFromDB(String id);
	}

	class DataWithId {
		String id;

		public String getId() {
			return id;
		}
	}

	class DataCacheKey<V> {
		long lastAccessedTimeNano;
		long lastFlushedTimeNano;
		final String id;

		public DataCacheKey(String id) {
			this.id = id;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			DataCacheKey that = (DataCacheKey) o;
			if (id != null ? !id.equals(that.id) : that.id != null) return false;

			return true;
		}

		@Override
		public int hashCode() {
			return id != null ? id.hashCode() : 0;
		}
	}
}
