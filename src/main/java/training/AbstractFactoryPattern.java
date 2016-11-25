package training;

/**
 * Created by oleksij.onysymchuk@gmail on 25.11.2016.
 */
public class AbstractFactoryPattern {
    // is using Factory Method for concrete particular product,
    // but it is using same functionality with others, which also built by this particular factory

    public static void main(String[] args) {

        AbstractFactory factory;

        factory = new SpecificFactoryOne();     // one specific category of products
        getFinalProducysAndDoSomeThings(factory);

        factory = new SpecificFactoryTwo();     // another specific category of products
        getFinalProducysAndDoSomeThings(factory);

    }

    private static void getFinalProducysAndDoSomeThings(AbstractFactory factory) {
        System.out.println();
        factory.getSomeThingA().doSomethingA();
        factory.getSomeThingB().doSomethingB();
    }

    interface AbstractFactory {

        SomeThingA getSomeThingA();

        SomeThingB getSomeThingB();
    }

    interface SomeThingA {
        void doSomethingA();
    }

    interface SomeThingB {
        void doSomethingB();
    }

    static class SpecificFactoryOne implements AbstractFactory {     // usual should be Singleton
        private String way = "way One";

        @Override
        public SomeThingA getSomeThingA() {
            return new SomeThingAImplOne(way);
        }

        @Override
        public SomeThingB getSomeThingB() {
            return new SomeThingBImplOne(way);
        }
    }

    static class SpecificFactoryTwo implements AbstractFactory {     // usual should be Singleton
        private String way = "way Two";

        @Override
        public SomeThingA getSomeThingA() {
            return new SomeThingAImplTwo(way);
        }

        @Override
        public SomeThingB getSomeThingB() {
            return new SomeThingBImplTwo(way);
        }
    }

    static class SomeThingAImplOne implements SomeThingA {
        private String way;

        public SomeThingAImplOne(String way) {
            this.way = way;
        }

        @Override
        public void doSomethingA() {
            System.out.println("Doing something AAA by way ONE.");

        }
    }

    static class SomeThingBImplOne implements SomeThingB {
        private String way;

        public SomeThingBImplOne(String way) {
            this.way = way;
        }

        @Override
        public void doSomethingB() {
            System.out.println("Doing something BBB by " + way + ".");
        }
    }

    static class SomeThingAImplTwo implements SomeThingA {
        private String way;

        public SomeThingAImplTwo(String way) {
            this.way = way;
        }

        @Override
        public void doSomethingA() {
            System.out.println("Doing something AAA by " + way + ".");
        }
    }

    static class SomeThingBImplTwo implements SomeThingB {
        private String way;

        public SomeThingBImplTwo(String way) {
            this.way = way;
        }

        @Override
        public void doSomethingB() {
            System.out.println("Doing something BBB by " + way + ".");

        }
    }


}
