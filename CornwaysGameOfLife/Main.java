package umr.dbs.oop.cornwaysGameOfLife;


public class Main {

    public static void main(String[] args) {
        GameOfLife f = new GameOfLife(); //define a new world with the given ammount of cells (in this case use default size (100 * 200))
        //GameOfLife f = new GameOfLife(100, 200, true);    //use this for a random generated field

        //if you want to use random generated fields, you need to remove f.init()
        int[] glider = {1,202,400,401,402};
        int[] gliderFactory = {225,423,425,613,614,621,622,635,636,812,816,821,822,835,836,1001,1002,1011,1017,1021,1022,1201,1202,1211,1215,1217,1218,1223,1225,1411,1417,1425,1612,1616,1813,1814};
        f.init(gliderFactory);


        Window w = new Window(f.getField(), "Cornways Game of life", 5); //show output window



        final int MAXITERATIONS = 1000; //maybe increase MAXITERATIONS for a better impression

        for (int i = 0; i < MAXITERATIONS; i++) {
            System.out.println(f.toString());//print state
            f.evolve(); //generate next generation
            w.update(); //update output window

            try {
                Thread.sleep(0);    //change for slower itertion
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(f.toString());

        f.close();
        System.out.println("\n\n" + MAXITERATIONS + " iterations reached!");
        System.exit(0);
    }
    
}