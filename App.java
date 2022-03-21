import javax.swing.JFrame;

public class App extends JFrame{

    panel p;

    App(int gridsize) {
        p = new panel(this, gridsize);
        add(p);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
    }

    public void restart(int gridsize) {
        
        remove(p);
        p = new panel(this, gridsize);
        add(p);
        revalidate();
    }

    public static void main(String[] args) throws Exception {
        int gs;
        if (args.length==0 || Integer.valueOf(args[0])<4) {gs=40; System.err.println("Insert at least 4, or it will default to 40");}
        else gs = Integer.valueOf(args[0]);
        new App(gs);
    }
}
