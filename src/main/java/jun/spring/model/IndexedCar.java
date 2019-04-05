package jun.spring.model;

import lombok.Data;

import java.util.*;

@Data
public class IndexedCar {

    private Car[]  array;

    private Collection<?> collection;

    private List list;

    private Set<? super Object> set;

    private SortedSet<? super Object> sortedSet;

    private Map map;

    private SortedMap sortedMap;

    public IndexedCar() {
        this(true);
    }

    public IndexedCar(boolean populate) {
        if (populate) {
            populate();
        }
    }

    public void populate() {
        Avante avante = new Avante("아방이");
        Avante avante1 = new Avante("아방이1");
        Avante avante2 = new Avante("아방이2");
        Avante avante3 = new Avante("아방이3");
        Avante avante4 = new Avante("아방이4");
        Avante avante5 = new Avante("아방이5");
        Avante avante6 = new Avante("아방이6");
        Avante avante7 = new Avante("아방이7");
        Avante avanteX = new Avante("아방이X");
        Avante avanteY = new Avante("아방이Y");
        this.array = new Avante[]{avante, avante1};
        this.list = new ArrayList<Object>();
        this.list.add(avante2);
        this.list.add(avante3);
        this.set = new TreeSet<Object>();
        this.set.add(avante6);
        this.set.add(avante7);
        this.map = new HashMap<Object, Object>();
        this.map.put("key1", avante4);
        this.map.put("key2", avante5);
        this.map.put("key.3", avante5);
        List list = new ArrayList();
        list.add(avanteX);
        list.add(avanteY);
        this.map.put("key4", list);

    }
}
