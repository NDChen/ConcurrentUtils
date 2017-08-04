package hotel.settlement.common.cache;

/**
 * Created by chen_zhi on 2017/8/4.
 */
public interface Computable<A, V> {
    V compute(A arg) throws Exception;
}
