import java.util.ArrayList;
import java.util.Random;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;

public class panel extends JPanel implements KeyListener, Runnable{
    
    private int GRIDSIZE = 40;
    Thread t;
    App parent;
    ArrayList<int[]> snake = new ArrayList<>();    // 1
    int[][] grid = new int[GRIDSIZE][GRIDSIZE];   // 0: x and 1: y
    int[] apple = new int[2];    // 2
    int[] dir = new int[2]; // x var, y var

    int BS = 400/GRIDSIZE;
    final int FRAMESPEED = 5;

    boolean running = true;

    public panel(App p, int gs){

        p.addKeyListener(this);
        setPreferredSize(new Dimension(400, 400));
        parent=p;
        GRIDSIZE = gs;
        BS = (int) 400/GRIDSIZE;

        t = new Thread(this);
        t.start();
        start();
    }

    private void start() {

        int[] pos = {4,2};
        snake.add(pos);
        newApple();
    }

    private void newApple() {

        Random rand = new Random();
        do {
            apple[0]= rand.nextInt(GRIDSIZE-1);
            apple[1]= rand.nextInt(GRIDSIZE-1);
        } while (snake.stream().anyMatch(e -> e[0]==apple[0] && e[1]==apple[1]));
    }

    private void eat() {

        if (snake.get(0)[0]==apple[0] && snake.get(0)[1]==apple[1]) {
            System.out.println("eaten");
            newApple();

            int[] pos = {snake.get(snake.size()-1)[0]+1, snake.get(snake.size()-1)[1]};
            snake.add(snake.size(), pos);
        }
    }

    private void update(){

        if ((snake.get(0)[0]==0 && dir[0]==-1) || (snake.get(0)[0]==GRIDSIZE-1 && dir[0]==1) || (snake.get(0)[1]==0 && dir[1]==-1) || (snake.get(0)[1]==GRIDSIZE-1 && dir[1]==1)) kill();
        eat();
        
        grid = new int[GRIDSIZE][GRIDSIZE];

        for (int i=snake.size()-1; i>=0; i--){

            if (snake.get(i).equals(snake.get(0))) continue;
            snake.get(i)[0] = snake.get(i-1)[0];
            snake.get(i)[1] = snake.get(i-1)[1];
        } 
        snake.get(0)[0]+=dir[0];
        snake.get(0)[1]+=dir[1];

        for (int i=1; i<snake.size(); i++){

            if(snake.get(0)[0]==snake.get(i)[0] && snake.get(0)[1]==snake.get(i)[1]) kill();
        } 
        grid[apple[0]][apple[1]]=2;
        snake.stream().forEach(e -> {grid[e[0]][e[1]] =1;});
        repaint();
    }

    private void kill() {

        System.out.println("u dead");
        dir = new int[2];
        running=false;
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {}
        parent.restart(GRIDSIZE);
    }

    public void paintComponent(Graphics g){

        super.paintComponent(g);
        for (int i =0; i<GRIDSIZE; i++) {
            for (int j =0; j<GRIDSIZE; j++) {
                if (grid[i][j]==2) {
                    g.setColor(Color.red);
                    g.fillRect(i*BS+1, j*BS+1, BS-1, BS-1);
                } else if (grid[i][j]==1) {
                    g.setColor(Color.green);
                    g.fillRect(i*BS+1, j*BS+1, BS-1, BS-1);
                } else if (grid[i][j]==0) {
                    g.setColor(Color.white);
                    g.fillRect(i*BS+1, j*BS+1, BS-1, BS-1);
                }  
            }
        }
    }

    @Override
    public void run(){

        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        final double ns = 1000000000.0/FRAMESPEED;
        double delta = 0;
        // int frame =0;
       
        while(running){
            long currentTime = System.nanoTime();
            delta += (currentTime-lastTime)/ns;
            lastTime = currentTime;
            while(delta>=1){
                delta--;
                update();
                // frame++;
            }

            if(System.currentTimeMillis()-timer>1000){
                timer+=1000;
                // f.setTitle("current fps: "+frame);
                // frame=0;
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        
        if ((e.getKeyCode()==KeyEvent.VK_LEFT || e.getKeyCode()==KeyEvent.VK_A) && dir[0]==0) {dir[0]=-1; dir[1]=0;}
        else if ((e.getKeyCode()==KeyEvent.VK_RIGHT || e.getKeyCode()==KeyEvent.VK_D) && dir[0]==0) {dir[0]=1; dir[1]=0;}
        else if ((e.getKeyCode()==KeyEvent.VK_DOWN || e.getKeyCode()==KeyEvent.VK_S) && dir[1]==0) {dir[0]=0; dir[1]=1;}
        else if ((e.getKeyCode()==KeyEvent.VK_UP || e.getKeyCode()==KeyEvent.VK_W) && dir[1]==0) {dir[0]=0; dir[1]=-1;}
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}
