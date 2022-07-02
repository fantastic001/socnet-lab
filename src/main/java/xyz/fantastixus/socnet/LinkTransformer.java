package xyz.fantastixus.socnet;

public interface LinkTransformer<V> {
    /**
     * Should return number giving weight to link between vertices x and y
     * @param x source vertex
     * @param y destination vertex
     * @return link weight. Negative valuesindicate disagreement and positive values indicate collaboration. 
     */
    public int transform(V x, V y); 
}
