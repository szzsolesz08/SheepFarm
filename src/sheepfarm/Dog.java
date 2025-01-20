package sheepfarm;

import sheepfarm.fields.EmptyField;
import sheepfarm.fields.GateField;

import java.util.concurrent.ThreadLocalRandom;

public class Dog {
    private String name;
    private int row;
    private int column;
    private char[] directions;
    private final Object fieldLock = new Object();

    public Dog(String name) {
        this.name = name;
        directions = new char[]{'L', 'R', 'U', 'D'};
    }

    public void performAction(Farm farm) {
        while (!farm.isOver()) {
            synchronized (fieldLock) {
                moveDog(farm, ThreadLocalRandom.current().nextInt(0, 4));
            }
        }
    }

    private void moveDog(Farm farm, int direction) {
        Object[][] farmArray = farm.getFarm();
        switch (directions[direction]) {
            case 'L':
                if (farmArray[row][column - 1].getClass() == EmptyField.class) {
                    if ((row < 5 || row > 8) && column - 1 != 8) {
                        farm.moveAnimal(row, column, row, column - 1);
                        column = column - 1;
                    }
                }
                break;
            case 'R':
                if (farmArray[row][column + 1].getClass() == EmptyField.class) {
                    if ((row < 5 || row > 8) && column + 1 != 5) {
                        farm.moveAnimal(row, column, row, column + 1);
                        column = column + 1;
                    }
                }
                break;
            case 'U':
                if (farmArray[row - 1][column].getClass() == EmptyField.class) {
                    if ((column < 5 || column > 8) && row - 1 != 8) {
                        farm.moveAnimal(row, column, row - 1, column);
                        row = row - 1;
                    }
                }
                break;
            case 'D':
                if (farmArray[row + 1][column].getClass() == EmptyField.class) {
                    if ((column < 5 || column > 8) && row + 1 != 8) {
                        farm.moveAnimal(row, column, row + 1, column);
                        row = row + 1;
                    }
                }
                break;
        }

        sleepForMsec(200);
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    protected void sleepForMsec(final int msec){
        try {
            Thread.sleep(msec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
