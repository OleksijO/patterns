package training;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by oleksij.onysymchuk@gmail on 26.11.2016.
 */
public class ObserverPattern {

    public static void main(String[] args) throws InterruptedException {
        Observer waiter1 = new WaiterForCertainNumber(1);
        Observer waiter2 = new WaiterForCertainNumber(3);
        Observable generator = new NumberGenerator(5);
        Thread generatorThread = (Thread) generator;
        generator.addObserver(waiter1);
        generatorThread.setDaemon(true);
        generatorThread.start();
        Thread.sleep(5000);
        generator.addObserver(waiter2);
        Thread.sleep(5000);

    }
    interface Observable {
        void addObserver(Observer observer);
        void removeObserver(Observer observer);
        void notifyObservers();
    }

    interface Observer{
        void handleEvent(int number);
    }

    static class NumberGenerator extends Thread implements Observable {
        List<Observer> observers = new LinkedList<>();
        int lastGeneratedNumber =-1;
        int maxValue;

        public NumberGenerator(int maxValue) {
            this.maxValue = maxValue;
        }

        @Override
        public void addObserver(Observer observer) {
            observers.add(observer);

        }

        @Override
        public void removeObserver(Observer observer) {
           observers.remove(observer);

        }

        @Override
        public void notifyObservers() {
           observers.forEach(observer -> observer.handleEvent(lastGeneratedNumber));
        }

        public void run(){
            while(!isInterrupted()){
                lastGeneratedNumber = generateNumber(5);
                System.out.println();
                System.out.println("Generated value: "+lastGeneratedNumber);
                notifyObservers();
                try {
                    sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private int generateNumber(int max){
            return (int) (Math.random()*max);
        }

    }

    static class WaiterForCertainNumber implements Observer{
        private int myNumber;


        public WaiterForCertainNumber(int myNumber) {
            this.myNumber = myNumber;
        }

        @Override
        public void handleEvent(int number) {
            if (number==myNumber){
                System.out.println("Waiter #"+myNumber+": Yeeeehoooo... It's My number!");
            }

        }
    }

}
