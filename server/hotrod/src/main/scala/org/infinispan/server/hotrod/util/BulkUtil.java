package org.infinispan.server.hotrod.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.infinispan.Cache;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.distexec.mapreduce.Collator;
import org.infinispan.distexec.mapreduce.Collector;
import org.infinispan.distexec.mapreduce.MapReduceTask;
import org.infinispan.distexec.mapreduce.Mapper;
import org.infinispan.distexec.mapreduce.Reducer;

/**
 * 
 * @author <a href="mailto:rtsang@redhat.com">Ray Tsang</a>
 * @since 5.2
 *
 */
public final class BulkUtil {
	public static final int DEFAULT_SCOPE = 0;
	public static final int GLOBAL_SCOPE = 1;
	public static final int LOCAL_SCOPE = 2;
	
	public static final Set<byte[]> getAllKeys(Cache<byte[], byte[]> cache, int scope) {
		CacheMode cacheMode = cache.getAdvancedCache().getCacheConfiguration().clustering().cacheMode(); 
		boolean keysAreLocal = !cacheMode.isClustered() || cacheMode.isReplicated();
		if (keysAreLocal || scope == LOCAL_SCOPE) {
			return cache.keySet();
		} else {
			MapReduceTask<byte[], byte[], byte[], Object> task =
               new MapReduceTask<byte[], byte[], byte[], Object>(cache)
					.mappedWith(new KeyMapper())
					.reducedWith(new KeyReducer());
			return task.execute(new KeysCollator());
		}
	}
	
	static class KeyMapper implements Mapper<byte[], byte[], byte[], Object> {
		private static final long serialVersionUID = -5054573988280497412L;

		@Override
		public void map(byte[] key, byte[] value,
				Collector<byte[], Object> collector) {
			collector.emit(key, null);
		}
	}
	
	static class KeyReducer implements Reducer<byte[], Object> {
		private static final long serialVersionUID = -8199097945001793869L;

		@Override
		public Boolean reduce(byte[] reducedKey, Iterator<Object> iter) {
			return iter.hasNext();
		}
	}
	
	static class KeysCollator implements Collator<byte[], Object, Set<byte[]>> {
		@Override
		public Set<byte[]> collate(Map<byte[], Object> reducedResults) {
			return reducedResults.keySet();
		}
		
	}
}
