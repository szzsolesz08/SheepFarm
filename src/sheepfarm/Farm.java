package sheepfarm;

import sheepfarm.fields.EmptyField;
import sheepfarm.fields.GateField;
import sheepfarm.fields.WallField;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Farm {

    private Object[][] farm;

    private final Object moveLock = new Object();

    private boolean over;

    public Farm() {
        over = false;
        loadFarm();
    }

    private void loadFarm() {
        farm = new Object[14][14];
        for (int i = 0; i < 14; i++) {
            for (int j = 0; j < 14; j++) {
                if (i == 0 || i == 13 || j == 0 || j == 13) {
                    farm[i][j] = new WallField();
                }
                else {
                    farm[i][j] = new EmptyField();
                }
            }
        }

        ThreadLocalRandom randomNumber = ThreadLocalRandom.current();

        farm[0][randomNumber.nextInt(1, 13)] = new GateField();
        farm[13][randomNumber.nextInt(1, 13)] = new GateField();
        farm[randomNumber.nextInt(1, 13)][0] = new GateField();
        farm[randomNumber.nextInt(1, 13)][13] = new GateField();
    }

    public void placeSheep(Sheep sheep) {
        ThreadLocalRandom randomNumber = ThreadLocalRandom.current();
        int row = randomNumber.nextInt(5, 9);
        int column = randomNumber.nextInt(5, 9);
        while (farm[row][column].getClass() != EmptyField.class) {
            row = randomNumber.nextInt(5, 9);
            column = randomNumber.nextInt(5, 9);
        }
        farm[row][column] = sheep;
        sheep.setRow(row);
        sheep.setColumn(column);
    }

    public void placeDog(Dog dog) {
        ThreadLocalRandom randomNumber = ThreadLocalRandom.current();
        int row = randomNumber.nextInt(1, 13);
        int column = randomNumber.nextInt(1, 13);
        while ((farm[row][column].getClass() != EmptyField.class) ||
                ((row >= 5 && row <= 8) &&
                (column >= 5 && column <= 8))){
            row = randomNumber.nextInt(1, 13);
            column = randomNumber.nextInt(1, 13);
        }
        farm[row][column] = dog;
        dog.setRow(row);
        dog.setColumn(column);
    }

    public void moveAnimal(int originalRow, int originalColumn, int newRow, int newColumn) {
        synchronized (moveLock) {
            farm[newRow][newColumn] = farm[originalRow][originalColumn];
            farm[originalRow][originalColumn] = new EmptyField();
        }
    }

    public void sheepEscaped(int row, int column)  {
        synchronized (moveLock) {
            farm[row][column] = new EmptyField();
        }
    }

    public void setOver() {
        over = true;
    }

    public boolean isOver() {
        return over;
    }

    public void printTable() {
        while (!over) {
            System.out.println("\033[H\033[2J");
            System.out.println("\u001B[0;0H");
            for (int i = 0; i < 14; i++) {
                for (int j = 0; j < 14; j++) {
                    System.out.print(farm[i][j].toString());
                }
                System.out.println();
            }
            sleepForMsec(200);
        }
    }

    protected void sleepForMsec(final int msec){
        try {
            Thread.sleep(msec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private int getZone(int row, int column) {
        if (row > 0 && row <= 4) {
            if (column > 0 && column <= 4) {
                return 1;
            }
            else if (column > 4 && column <= 8) {
                return 2;
            }
            else if (column > 8 && column <= 12){
                return 3;
            }
        }
        else if (row > 4 && row <= 8) {
            if (column > 0 && column <= 4) {
                return 4;
            }
            else if (column > 4 && column <= 8) {
                return 5;
            }
            else if (column > 8 && column <= 12){
                return 6;
            }
        }
        else if (row > 8 && row <= 13) {
            if (column > 0 && column <= 4) {
                return 7;
            }
            else if (column > 4 && column <= 8) {
                return 8;
            }
            else if (column > 8 && column <= 12){
                return 9;
            }
        }
        return 0;
    }

    public Object[][] getFarm() {
        return farm;
    }
}
