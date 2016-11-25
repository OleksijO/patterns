package training;

/**
 * Created by oleksij.onysymchuk@gmail on 25.11.2016.
 */
public class BuilderWithDirectorStrategyPatterns {

    public static void main(String[] args) {
        // builder implements default strategy
        ComplicateProductAbstractBuilder defaultBuilder = new ComplicateProductDefaultBuilderImpl();
        // builder implements specific strategy
        ComplicateProductAbstractBuilder specialBuilder = new ComplicateProductSpecialBuilderImpl();

        // getting default product
        Director director = new Director(defaultBuilder);
        ComplicateProduct product = director.buildProduct();
        System.out.println(product);

        //specialized builder (setting another strategy)
        director.setBuilder(specialBuilder);
        product = director.buildProduct();
        System.out.println(product);
    }

    static class Director {
        private ComplicateProductAbstractBuilder builder;

        public Director(ComplicateProductAbstractBuilder builder) {
            this.builder = builder;
        }

        public ComplicateProduct buildProduct() {
            return builder
                    .createComplicateProduct()
                    .configureService1()
                    .configureService2()
                    .configureService3()
                    .build();
        }

        public void setBuilder(ComplicateProductAbstractBuilder builder) {
            this.builder = builder;
        }
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

        @Override
        public String toString() {
            return "ComplicateProductImpl{" +
                    "service1=" + service1 +
                    ", service2=" + service2 +
                    ", service3=" + service3 +
                    '}';
        }
    }

    static abstract class ComplicateProductAbstractBuilder {
        ComplicateProductImpl product;

        public ComplicateProductAbstractBuilder createComplicateProduct() {
            product = new ComplicateProductImpl();
            return this;
        }

        public abstract ComplicateProductAbstractBuilder configureService1();

        public abstract ComplicateProductAbstractBuilder configureService2();

        public abstract ComplicateProductAbstractBuilder configureService3();

        public ComplicateProduct build() {
            return product;
        }
    }

    static class ComplicateProductDefaultBuilderImpl extends ComplicateProductAbstractBuilder {

        public ComplicateProductAbstractBuilder configureService1() {
            product.setService1(new Service1DefaultImpl());
            return this;
        }

        public ComplicateProductAbstractBuilder configureService2() {
            product.setService2(new Service2DefaultImpl());
            return this;
        }

        public ComplicateProductAbstractBuilder configureService3() {
            product.setService3(new Service3DefaultImpl());
            return this;
        }
    }

    static class ComplicateProductSpecialBuilderImpl extends ComplicateProductAbstractBuilder {

        public ComplicateProductAbstractBuilder configureService1() {
            product.setService1(new Service1(){String field="redefined service 1";

                @Override
                public String toString() {
                    return "$classname{" +
                            "field='" + field + '\'' +
                            '}';
                }
            });
            return this;
        }

        public ComplicateProductAbstractBuilder configureService2() {
            product.setService2(new Service2(){String field="redefined service 2";

                @Override
                public String toString() {
                    return "$classname{" +
                            "field='" + field + '\'' +
                            '}';
                }
            });
            return this;
        }

        public ComplicateProductAbstractBuilder configureService3() {
            product.setService3(new Service3(){String field="redefined service 3";

                @Override
                public String toString() {
                    return "$classname{" +
                            "field='" + field + '\'' +
                            '}';
                }
            });
            return this;
        }
    }


}
