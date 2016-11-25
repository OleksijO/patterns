package training;

/**
 * Created by oleksij.onysymchuk@gmail on 25.11.2016.
 */
public class AbstractFactoryPattern {


    public static void main(String[] args) {

        AbstractFactory factory;

        factory = new SpecificFactoryOne();     // one specific category of products
        getFinalProducysAndDoSomeThings(factory);

        factory = new SpecificFactoryTwo();     // another specific category of products
        getFinalProducysAndDoSomeThings(factory);

    }

    private static void getFinalProducysAndDoSomeThings(AbstractFactory factory){
        System.out.println();
        factory.getSomeThingA().doSomethingA();
        factory.getSomeThingB().doSomethingB();
    }

    interface  AbstractFactory {

        SomeThingA getSomeThingA();

        SomeThingB getSomeThingB();
    }

     interface SomeThingA {
        void doSomethingA();
    }

    interface SomeThingB {
        void doSomethingB();
    }

    static class SpecificFactoryOne implements AbstractFactory{
        @Override
        public SomeThingA getSomeThingA() {
            return new SomeThingAImplOne();
        }

        @Override
        public SomeThingB getSomeThingB() {
            return new SomeThingBImplOne();
        }
    }

    static class SpecificFactoryTwo implements AbstractFactory{
        @Override
        public SomeThingA getSomeThingA() {
            return new SomeThingAImplTwo();
        }

        @Override
        public SomeThingB getSomeThingB() {
            return new SomeThingBImplTwo();
        }
    }

    static class SomeThingAImplOne implements SomeThingA{
        @Override
        public void doSomethingA() {
            System.out.println("Doing something AAA by way ONE.");

        }
    }

    static class SomeThingBImplOne implements SomeThingB{
        @Override
        public void doSomethingB() {
            System.out.println("Doing something BBB by way ONE.");
        }
    }

    static class SomeThingAImplTwo implements SomeThingA{
        @Override
        public void doSomethingA() {
            System.out.println("Doing something AAA by way TWO.");
        }
    }

    static class SomeThingBImplTwo implements SomeThingB{
        @Override
        public void doSomethingB() {
            System.out.println("Doing something BBB by way TWO.");

        }
    }




}
