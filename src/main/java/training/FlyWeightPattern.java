package training;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by oleksij.onysymchuk@gmail on 26.11.2016.
 */
public class FlyWeightPattern {


    public static void main(String[] args) throws InterruptedException {
        long startTime;
        long finishTime;

        int size = 500000;

        List<SimpleObject> list = new LinkedList<>();
        Type[] types = Type.values();
        FlyWeightFactory factory = new FlyWeightFactory();

        System.out.println("Direct creation without flyweight pattern");
        startTime = System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            list.add(new SimpleObject(types[(int) (Math.random() * types.length)]));
        }
        finishTime = System.currentTimeMillis();

        System.out.println("Time of creation: " + (finishTime - startTime) + " ms");
        System.out.println();


        System.out.println("Creation with flyweight pattern");

        list.clear();

        startTime = System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            list.add(factory.getSimpleObject(types[(int) (Math.random() * types.length)]));
        }
        finishTime = System.currentTimeMillis();

        System.out.println("Time of creation: " + (finishTime - startTime) + " ms");
    }

    /*
       OUTPUT:

       Direct creation without flyweight pattern        // created 500000 simple objects
       Time of creation: 895 ms

       Creation with flyweight pattern
       Time of creation: 35 ms                           // created 5 simple objects

    */


    enum Type {A, B, C, D, E}

    static class SimpleObject {
        private Type type;
        private BigDecimal[] memoryConsumption = new BigDecimal[100];

        public SimpleObject(Type type) {
            this.type = type;
        }

        public Type getType() {
            return type;
        }
    }

    static class FlyWeightFactory {
        private Map<Type, SimpleObject> cash = new HashMap<>();

        public SimpleObject getSimpleObject(Type type) {
            SimpleObject obj = cash.get(type);
            if (obj == null) {
                obj = new SimpleObject(type);
                cash.put(type, obj);
            }
            return obj;
        }
    }


}
