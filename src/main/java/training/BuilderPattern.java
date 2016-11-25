package training;

/**
 * Created by oleksij.onysymchuk@gmail on 25.11.2016.
 */
public class BuilderPattern {

    public static void main(String[] args) {
        ComplicateProductBuilder builder = new ComplicateProductBuilderImpl();

        // default product
        ComplicateProduct product = builder.build();

        //specialized builder
        product = builder
                .configureService1(new Service1(){ /* redefined */ })
                .configureService2(new Service2(){ /* redefined */ })
                .configureService3(new Service3(){ /* redefined */ })
                .build();

    }

    interface ComplicateProduct {
        void doSomethingImportant();
    }

    interface Service1 {
    }

    interface Service2 {
    }

    interface Service3 {
    }

    static class Service1DefaultImpl implements Service1 {
    }

    static class Service2DefaultImpl implements Service2 {
    }

    static class Service3DefaultImpl implements Service3 {
    }

    static class ComplicateProductImpl implements ComplicateProduct {
        private Service1 service1;
        private Service2 service2;
        private Service3 service3;

        public void doSomethingImportant() {
            // heavy logic using all services
        }

        public void setService1(Service1 service1) {
            this.service1 = service1;
        }

        public void setService2(Service2 service2) {
            this.service2 = service2;
        }

        public void setService3(Service3 service3) {
            this.service3 = service3;
        }
    }

    interface ComplicateProductBuilder {
        ComplicateProductBuilder configureService1(Service1 service1);

        ComplicateProductBuilder configureService2(Service2 service2);

        ComplicateProductBuilder configureService3(Service3 service3);

        ComplicateProduct build();
    }

    static abstract class ComplicateProductAbstractBuilder implements ComplicateProductBuilder {
        ComplicateProductImpl product;
        protected Service1 service1 = new Service1DefaultImpl();
        protected Service2 service2 = new Service2DefaultImpl();
        protected Service3 service3 = new Service3DefaultImpl();

        public ComplicateProductImpl createComplicateProduct() {
            return new ComplicateProductImpl();
        }

        public abstract ComplicateProductBuilder configureService1(Service1 service1);

        public abstract ComplicateProductBuilder configureService2(Service2 service2);

        public abstract ComplicateProductBuilder configureService3(Service3 service3);

        public ComplicateProduct build() {
            product = createComplicateProduct();
            product.setService1(service1);
            product.setService2(service2);
            product.setService3(service3);
            return product;
        }
    }

    static class ComplicateProductBuilderImpl extends ComplicateProductAbstractBuilder {

        @Override
        public ComplicateProductBuilder configureService1(Service1 service1) {
            this.service1 = service1;
            return this;
        }

        @Override
        public ComplicateProductBuilder configureService2(Service2 service2) {
            this.service2 = service2;
            return this;
        }

        @Override
        public ComplicateProductBuilder configureService3(Service3 service3) {
            this.service3 = service3;
            return this;
        }
    }


}
