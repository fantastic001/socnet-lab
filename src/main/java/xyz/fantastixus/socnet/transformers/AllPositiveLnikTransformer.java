package xyz.fantastixus.socnet.transformers;

import xyz.fantastixus.socnet.LinkTransformer;

public class AllPositiveLnikTransformer<V> implements LinkTransformer<V> {

    @Override
    public int transform(V x, V y) {
        return 1;
    }
    
}
