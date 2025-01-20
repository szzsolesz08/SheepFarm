package sheepfarm;

import sheepfarm.fields.EmptyField;
import sheepfarm.fields.GateField;
import sheepfarm.fields.WallField;

import java.util.concurrent.ThreadLocalRandom;

public class Sheep {

    private String name;
    private int row;
    private int column;

    private char[] directions;

    private final Object fieldLock = new Object();

    public Sheep(String name) {
        this.name = name;
    }

    public void performAction(Farm farm) {
        while (!farm.isOver()) {
            synchronized (fieldLock) {
                switch (isDogNearby(farm)) {
                    case 0:
                        directions = new char[]{'L', 'R', 'U', 'D'};
                        moveSheep(farm, ThreadLocalRandom.current().nextInt(0, directions.length));
                    case 1:
                        directions = new char[]{'R', 'D'};
                        moveSheep(farm, ThreadLocalRandom.current().nextInt(0, directions.length));
                    case 2:
                        directions = new char[]{'D'};
                        moveSheep(farm, ThreadLocalRandom.current().nextInt(0, directions.length));
                    case 3:
                        directions = new char[]{'L', 'D'};
                        moveSheep(farm, ThreadLocalRandom.current().nextInt(0, directions.length));
                    case 4:
                        directions = new char[]{'R'};
                        moveSheep(farm, ThreadLocalRandom.current().nextInt(0, directions.length));
                    case 5:
                        directions = new char[]{'L'};
                        moveSheep(farm, ThreadLocalRandom.current().nextInt(0, directions.length));
                    case 6:
                        directions = new char[]{'R', 'U'};
                        moveSheep(farm, ThreadLocalRandom.current().nextInt(0, directions.length));
                    case 7:
                        directions = new char[]{'U'};
                        moveSheep(farm, ThreadLocalRandom.current().nextInt(0, directions.length));
                    case 8:
                        directions = new char[]{'L', 'U'};
                        moveSheep(farm, ThreadLocalRandom.current().nextInt(0, directions.length));
                }
            }
        }
    }

    private void moveSheep(Farm farm, int direction) {
        Object[][] farmArray = farm.getFarm();
        switch (directions[direction]) {
            case 'L':
                if (farmArray[row][column - 1].getClass() == GateField.class) {
                    farm.sheepEscaped(row, column);
                    farm.setOver();
                    System.out.println("Sheep: " + name + " got out!");
                } else if (farmArray[row][column - 1].getClass() == EmptyField.class) {
                    farm.moveAnimal(row, column, row, column - 1);
                    column = column - 1;
                }
                break;
            case 'R':
                if (farmArray[row][column + 1].getClass() == GateField.class) {
                    farm.sheepEscaped(row, column);
                    farm.setOver();
                    System.out.println("Sheep: " + name + " got out!");
                } else if (farmArray[row][column + 1].getClass() == EmptyField.class) {
                    farm.moveAnimal(row, column, row, column + 1);
                    column = column + 1;
                }
                break;
            case 'U':
                if (farmArray[row - 1][column].getClass() == GateField.class) {
                    farm.sheepEscaped(row, column);
                    farm.setOver();
                    System.out.println("Sheep: " + name + " got out!");
                } else if (farmArray[row - 1][column].getClass() == EmptyField.class) {
                    farm.moveAnimal(row, column, row - 1, column);
                    row = row - 1;
                }
                break;
            case 'D':
                if (farmArray[row + 1][column].getClass() == GateField.class) {
                    farm.sheepEscaped(row, column);
                    farm.setOver();
                    System.out.println("Sheep: " + name + " got out!");
                } else if (farmArray[row + 1][column].getClass() == EmptyField.class) {
                    farm.moveAnimal(row, column, row + 1, column);
                    row = row + 1;
                }
                break;
        }

        sleepForMsec(200);
    }

    private int isDogNearby(Farm farm) {
        synchronized (fieldLock) {
            Object[][] farmArray = farm.getFarm();
            if (farmArray[row - 1][column - 1].getClass() == Dog.class) {
                return 1;
            }
            else if (farmArray[row - 1][column].getClass() == Dog.class) {
                return 2;
            }
            else if (farmArray[row - 1][column + 1].getClass() == Dog.class) {
                return 3;
            }
            else if (farmArray[row][column - 1].getClass() == Dog.class) {
                return 4;
            }
            else if (farmArray[row][column + 1].getClass() == Dog.class) {
                return 5;
            }
            else if (farmArray[row + 1][column - 1].getClass() == Dog.class) {
                return 6;
            }
            else if (farmArray[row + 1][column].getClass() == Dog.class) {
                return 7;
            }
            else if (farmArray[row + 1][column + 1].getClass() == Dog.class) {
                return 8;
            }
            else {
                return 0;
            }
        }
    }

    protected void sleepForMsec(final int msec){
        try {
            Thread.sleep(msec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    @Override
    public String toString() {
        return name;
    }
}
