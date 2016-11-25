package training;

/**
 * Created by oleksij.onysymchuk@gmail on 25.11.2016.
 */
public class FactoryMethodPattern {
    // is using in Abstract Factory for concrete particular product
    
    interface Factory {

        Product getProduct();

    }

    interface Product {
        void doSomething();
    }

    static class FactoryImpl implements Factory {     // usual should be Singleton
        @Override
        public Product getProduct() {
            Product product = new ProductImpl();
            // set up product
            return product;
        }
    }

    static class ProductImpl implements Product {

        @Override
        public void doSomething() {
            System.out.println("Doing something");

        }
    }

}
