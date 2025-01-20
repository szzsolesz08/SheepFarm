import sheepfarm.Dog;
import sheepfarm.Farm;
import sheepfarm.Sheep;

import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final List<Sheep> sheepList = new ArrayList<>();
    private static final List<Dog> dogList = new ArrayList<>();

    private static final List<Thread> threads = new ArrayList<>();

    public static void main(String[] args) {
        Farm farm = new Farm();
        Object[][] farmArray = farm.getFarm();

        sheepList.add(new Sheep("A"));
        sheepList.add(new Sheep("B"));
        sheepList.add(new Sheep("C"));
        sheepList.add(new Sheep("D"));
        sheepList.add(new Sheep("E"));
        sheepList.add(new Sheep("F"));
        sheepList.add(new Sheep("G"));
        sheepList.add(new Sheep("H"));
        sheepList.add(new Sheep("I"));
        sheepList.add(new Sheep("J"));

        dogList.add(new Dog("1"));
        dogList.add(new Dog("2"));
        dogList.add(new Dog("3"));
        dogList.add(new Dog("4"));
        dogList.add(new Dog("5"));

        sheepList.forEach(s -> {
            Thread t = new Thread(() -> {
                farm.placeSheep(s);
                s.performAction(farm);
            });
            threads.add(t);
            t.start();
        });

        dogList.forEach(d -> {
            Thread t = new Thread(() -> {
                farm.placeDog(d);
                d.performAction(farm);
            });
            threads.add(t);
            t.start();
        });

        farm.printTable();

        threads.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}
