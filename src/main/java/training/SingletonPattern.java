package training;

/**
 * Created by oleksij.onysymchuk@gmail on 25.11.2016.
 */
public class SingletonPattern {

    interface Singleton{}

    static class SimpleThreadSafeSingleton implements Singleton {
        private static final SimpleThreadSafeSingleton instance = new SimpleThreadSafeSingleton();

        private SimpleThreadSafeSingleton() {
        }

        public Singleton getInstance(){
            return instance;
        }
    }

    static class LazyThreadSafeSingleton implements Singleton {

        static private class InstanceHolder {
            private static final LazyThreadSafeSingleton instance = new LazyThreadSafeSingleton();}

        private LazyThreadSafeSingleton() {
        }

        public Singleton getInstance(){
            return InstanceHolder.instance;
        }
    }




}
