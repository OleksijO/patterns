package training;

/**
 * Created by oleksij.onysymchuk@gmail on 25.11.2016.
 */
public class DecoratorPattern {

    public static void main(String[] args) {
        //do something
        new Client(new FrameworkImpl()).doSomething();

        System.out.println();

        //do something and somthing more
        new Client(new Decorator(new FrameworkImpl())).doSomething();
    }

    static class Client {
        private Framework framework;

        public Client(Framework framework) {
            this.framework = framework;
        }

        void doSomething() {
            framework.doSomething();

        }
    }

    interface Framework {
        void doSomething();
    }

    static class FrameworkImpl implements Framework {
        @Override
        public void doSomething() {
            System.out.println("Doing something.");
        }
  }

    static class Decorator implements Framework {
        private Framework original;

        public Decorator(Framework original) {
            this.original = original;
        }

        @Override
        public void doSomething() {
            System.out.println("Do something useful before original method");
            original.doSomething();
            System.out.println("Do something useful after original method");
        }
    }
}
