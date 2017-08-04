package hotel.settlement.common.cache;

import java.util.concurrent.*;

/**
 * Created by chen_zhi on 2017/8/4.
 */
public class Memoizer <A, V> implements Computable<A, V> {

    private final ConcurrentMap<A, Future<V>> cache = new ConcurrentHashMap<>();
    private final Computable<A, V> c;

    public Memoizer(Computable<A, V> c){
        this.c = c;
    }

    @Override
    public V compute(final A arg) throws Exception {

        while(true) {
            Future<V> f = cache.get(arg);
            if(f == null) {
                Callable<V> eval = new Callable<V>() {
                    @Override
                    public V call() throws Exception {
                        return c.compute(arg);
                    }
                };
                FutureTask<V> ft = new FutureTask<>(eval);
                f = cache.putIfAbsent(arg, ft);

                if(f == null) {
                    f = ft;
                    ft.run();
                }
            }
            try {
                return f.get();
            }catch (CancellationException e) {
                cache.remove(arg, f);
            } catch (ExecutionException e) {
                throw new Exception(e.getCause());
            }
        }
    }
}
