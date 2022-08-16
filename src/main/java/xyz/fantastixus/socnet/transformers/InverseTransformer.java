package xyz.fantastixus.socnet.transformers;

import xyz.fantastixus.socnet.LinkTransformer;

public class InverseTransformer<V> implements LinkTransformer<V> {
    
    private LinkTransformer<V> t; 

    public InverseTransformer(LinkTransformer<V> t) {
        this.t = t;
    }

    @Override
    public int transform(V x, V y) {
        if (t.transform(x, y) < 0) return 1; 
        else return -1;
    }
    
}
