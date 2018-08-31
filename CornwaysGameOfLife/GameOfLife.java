package umr.dbs.oop.cornwaysGameOfLife;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.*;

public class GameOfLife {
    private boolean[][] field;
    private ExecutorService eService;

    public GameOfLife(boolean[][] field) {
        this.field = field;
        this.eService = Executors.newWorkStealingPool();
    }

    public GameOfLife(int[] alive, int fieldWith, int fieldHeight) {
        this.field = new boolean[fieldHeight][fieldHeight];
        init(alive);
        this.eService = Executors.newWorkStealingPool();
    }

    public GameOfLife() {
        this.field = new boolean[100][200];
        this.eService = Executors.newWorkStealingPool();
    }

    public GameOfLife(int fieldHeight, int fieldWidth, boolean random) {
        this.field = new boolean[fieldHeight][fieldWidth];
        if(random) {
            Random r = new Random();
            for (int i = 0; i < this.field.length; i++) {
                for (int j = 0; j < this.field[0].length; j++) {
                    this.field[i][j] = r.nextBoolean();
                }
            }
        }
        this.eService = Executors.newWorkStealingPool();
    }

    public void init(int[] alive) {
        if(alive.length > this.field.length * this.field[0].length) {
            throw new UnsupportedOperationException("to many cells to revive");
        }
        int counter = 0;
        for (int i = 0; i < this.field.length; i++) {
            for (int j = 0; j < this.field[0].length; j++) {
                if (counter < alive.length && alive[counter] == i * this.field[0].length + j) {
                    this.field[i][j] = true;
                    counter++;
                }
            }
        }
    }

    public void evolve() {
        Future<boolean[]>[] list = new Future[this.field.length];
        for (int i = 0; i < this.field.length; i++) {
            list[i] = this.eService.submit(new RowEvolution(this, i));
        }
        boolean isDone;
        boolean nextIsDone = true;
        do {
            isDone = true;
            nextIsDone = true;
            for (int i = 0; i < list.length; i++) {
                //check if the current line is finished and the row above and below. If they are not finished thread-based errors could occour.
                if(list[i].isDone() && list[this.convertY(i - 1)].isDone() && list[convertY(i + 1)].isDone()) {
                    try {
                        this.field[i] = list[i].get();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }   
                } else {
                    isDone = false;
                }
            }
        } while(!isDone);

    }

    public int convertX(int x) {
        if (x > this.field[0].length - 1) {
            return 0;
        } else if (x < 0) {
            return this.field[0].length - 1;
        } else {
            return x;
        }
    }

    public synchronized int convertY(int y) {
        if (y > this.field.length - 1) {
            return 0;
        } else if (y < 0) {
            return this.field.length - 1;
        } else {
            return y;
        }
    }

    public boolean[][] getField() {
        return this.field;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.field.length; i++) {
            for (int j = 0; j < this.field[0].length; j++) {
                sb.append((this.field[i][j]) ? 1 : 0);
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public void close() {
        this.eService.shutdown();
    }



    private class RowEvolution implements Callable<boolean[]> { // used to speed up the evolution
        private boolean[][] field;
        private int row;
        private GameOfLife f;

        public RowEvolution(final GameOfLife f, final int row) {
            this.f = f;
            this.field = this.f.getField();
            this.row = row;
        }


        @Override
        public boolean[] call() throws Exception {
            int neighborCount;
            boolean[] temp = new boolean[this.field[0].length];
            for (int i = 0; i < this.field[0].length; i++) {
                neighborCount = 0;
                if(this.field[this.f.convertY(row - 1)][this.f.convertX(i - 1)]) { //top left neighbor
                    neighborCount++;
                }

                if(this.field[this.f.convertY(row - 1)][this.f.convertX(i)]) { //top neighbor
                    neighborCount++;
                }

                if(this.field[this.f.convertY(row - 1)][this.f.convertX(i + 1)]) { // rop right neighbor
                    neighborCount++;
                }

                if(this.field[this.f.convertY(row)][this.f.convertX(i - 1)]) { //left neightbor
                    neighborCount++;
                }

                if(this.field[this.f.convertY(row)][this.f.convertX(i + 1)]) {  //right neighbor
                    neighborCount++;
                }

                if(this.field[this.f.convertY(row + 1)][this.f.convertX(i - 1)]) {  //bottom left neighbor
                    neighborCount++;
                }

                if(this.field[this.f.convertY(row + 1)][this.f.convertX(i)]) {  //bottom neighbor
                    neighborCount++;
                }

                if(this.field[this.f.convertY(row + 1)][this.f.convertX(i + 1)]) {  //bottom right neighbor
                    neighborCount++;
                }


                if(neighborCount == 3) {
                    temp[i] = true;    //respawn (perfect conditions)
                } else if(neighborCount < 2) {
                    temp[i] = false;    //dies of loneliness
                } else if(neighborCount == 2) { // || neighborCount == 3) {
                    temp[i] = this.field[row][i];   //conditions are ok. stays alive
                } else {
                    temp[i] = false;    //dies of overpopulation
                }

            }
            return temp;
        }
    }


}
