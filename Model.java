package src;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Objects;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;


public class Model extends JPanel implements ActionListener {

    private Dimension d;
    private final Font smallFont = new Font("Arial", Font.BOLD, 14);
    private boolean inGame = false;
    private boolean dying = false;
    public boolean v1 = true;
    public boolean v2 = true;
    public boolean v3 = true;
    public boolean v4 = true;
    public boolean v5 = true;
    public boolean v6 = true;
    public boolean undefeatable=false;
    public  boolean[] drawGhostFlag;
    //public boolean[] ghostDead;

    private final int BLOCK_SIZE = 24;
    private final int N_BLOCKS = 20;
    private final int SCREEN_SIZE = N_BLOCKS * BLOCK_SIZE;
    private final int PACMAN_SPEED = 4;
    private int N_GHOSTS = 5;
    private int lives, score;
    private int[] dx, dy;
    private int[] ghost_x, ghost_y, ghost_dx, ghost_dy, ghostSpeed;

    private Image heart, ghost, pacm;
    private Image up, down, left, right;

    private int pacman_x, pacman_y, pacmand_x, pacmand_y;
    private int req_dx, req_dy;

    private final short levelData[] = {
            0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 21,  0,  0,  0,
            0, 19, 26, 26, 18, 26, 18, 26, 26, 26, 26, 18, 26, 18, 26, 26, 24, 26, 22,  0,
            0, 21,  0,  0, 21,  0, 21,  0,  0,  0,  0, 21,  0, 21,  0,  0,  0,  0, 21,  0,
            0, 21,  0,  0, 21,  0, 17, 26, 26, 22,  0, 21,  0, 21,  0,  0,  0,  0, 21,  0,
            0, 21,  0,  0, 21,  0, 21,  0,  0, 21,  0, 21,  0, 21,  0,  0,  0,  0, 21,  0,
            0, 21,  0,  0, 21,  0, 17, 26, 26, 28,  0, 17, 26, 24, 26, 26, 26, 26, 20,  0,
            0, 21,  0,  0, 21,  0, 21,  0,  0,  0,  0, 21,  0,  0,  0,  0,  0,  0, 21,  0,
            0, 21,  0,  0, 21,  0, 17, 26, 26, 26, 26, 24, 26, 26, 26, 26, 22,  0, 21,  0,
            0, 21,  0,  0, 21,  0, 21,  0,  0,  0,  0,  0,  0,  0,  0,  0, 21,  0, 21,  0,
            0, 21,  0,  0, 21,  0, 17, 26, 26, 26, 26, 26, 26, 26, 26, 18, 28,  0, 21,  0,
            0, 21,  0,  0, 21,  0, 21,  0,  0,  0,  0,  0,  0,  0,  0, 21,  0,  0, 21,  0,
            0, 21,  0,  0, 21,  0, 25, 26, 26, 26, 26, 26, 18, 26, 26, 24, 26, 26, 20,  0,
            0, 21,  0,  0, 21,  0,  0,  0,  0,  0,  0,  0, 21,  0,  0,  0,  0,  0, 21,  0,
            0, 21,  0,  0, 17, 26, 26, 26, 26, 26, 26, 26, 24, 26, 26, 26, 26, 26, 20,  0,
            26,16, 26, 26, 28,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 17, 26,
            0, 21,  0,  0,  0,  0, 19, 26, 18, 26, 26, 26, 26, 26, 18, 26, 30,  0, 21,  0,
            0, 17, 26, 26, 30,  0, 21,  0, 21,  0,  0,  0,  0,  0, 21,  0,  0,  0, 21,  0,
            0, 21,  0,  0,  0,  0, 21,  0, 21,  0,  0,  0,  0,  0, 21,  0,  0,  0, 21,  0,
            0, 25, 26, 26, 26, 26, 24, 26, 24, 26, 26, 26, 26, 26, 24, 26, 18, 26, 28,  0,
            0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 21,  0,  0,  0,
    };

    private int currentSpeed = 3;
    private short[] screenData;
    private Timer timer;
    private Timer timer_enemies;
    private int counter[];

    public Model() {

        loadImages();
        initVariables();
        addKeyListener(new TAdapter());
        setFocusable(true);
        initGame();
    }


    private void loadImages() {

        try
        {
            down = new ImageIcon(Objects.requireNonNull(getClass().getResource("images/down.gif"))).getImage();
            up = new ImageIcon(Objects.requireNonNull(getClass().getResource("images/up.gif"))).getImage();
            left = new ImageIcon(Objects.requireNonNull(getClass().getResource("images/left.gif"))).getImage();
            right = new ImageIcon(Objects.requireNonNull(getClass().getResource("images/right.gif"))).getImage();
            ghost = new ImageIcon(Objects.requireNonNull(getClass().getResource("images/ghost.gif"))).getImage();
            pacm = new ImageIcon(Objects.requireNonNull(getClass().getResource("images/pacman.png"))).getImage();
            heart = new ImageIcon(Objects.requireNonNull(getClass().getResource("images/heart.png"))).getImage();
        }
        catch (Exception e){
            System.out.println("Exception occured"+e);
        }

    }

    private void initVariables() {

        screenData = new short[N_BLOCKS * N_BLOCKS];
        d = new Dimension(480, 540);
        ghost_x = new int[N_GHOSTS];
        ghost_dx = new int[N_GHOSTS];
        ghost_y = new int[N_GHOSTS];
        ghost_dy = new int[N_GHOSTS];
        ghostSpeed = new int[N_GHOSTS];
        dx = new int[N_GHOSTS];
        dy = new int[N_GHOSTS];

        counter = new int[5];
        for (int i=0;i<5;i++)
        {
            counter[i] = 0;
        }

        drawGhostFlag = new boolean[5];
        for (int i=0;i<5;i++){
            drawGhostFlag[i]=true;
        }
//        ghostDead = new boolean[5];
//        for (int i=0;i<5;i++){
//            ghostDead[i]=false;
//        }

        timer = new Timer(60, this);
        timer.start();

        timer_enemies = new Timer(1000,this);

    }

    private void initGame() {

        lives = 3;
        score = 0;
        initLevel();
        N_GHOSTS = 5;
        currentSpeed = 3;
    }

    private void initLevel() {

        int i;
        for (i = 0; i < N_BLOCKS * N_BLOCKS; i++) {
            screenData[i] = levelData[i];
        }

        continueLevel();
    }

    private void continueLevel() {

        int dx = 1;

        for (int i = 0; i <5; i++) {

            ghost_y[i] =11 * BLOCK_SIZE; //start position
            ghost_x[i] = 10 * BLOCK_SIZE;
            ghost_dy[i] = 0;
            ghost_dx[i] = dx;
            dx = -dx;

            ghostSpeed[i] = currentSpeed;
        }

        pacman_x = 7 * BLOCK_SIZE;  //start position
        pacman_y = 11 * BLOCK_SIZE;
        pacmand_x = 0;	//reset direction move
        pacmand_y = 0;
        req_dx = 0;		// reset direction controls
        req_dy = 0;
        dying = false;
    }

    private void drawPowerUp(Graphics2D g2d) {
        if(this.v1){
            g2d.drawImage(heart, 312, 72,this);//1st power up
        }
        if(this.v2){
            g2d.drawImage(heart, 192, 168,this);//2nd power up
        }
        if(this.v3){
            g2d.drawImage(heart, 432, 240,this);//3rd power up
        }
        if(this.v4){
            g2d.drawImage(heart, 96, 336,this);//4th power up
        }
        if(this.v5){
            g2d.drawImage(heart, 96, 384,this);//5th power up
        }
        if(this.v6){
            g2d.drawImage(heart, 288, 360,this);//6th power up
        }

    }

    private void playGame(Graphics2D g2d) {

        if (dying) {

            death();

        } else {

            movePacman();
            drawPacman(g2d);
            moveGhosts(g2d);
            drawPowerUp(g2d);
            checkMaze();
        }
    }

    private void showIntroScreen(Graphics2D g2d) {

        String start = "Press SPACE to start";
        g2d.setColor(Color.yellow);
        g2d.drawString(start, 180, 210);
    }

    private void drawScore(Graphics2D g) {
        g.setFont(smallFont);
        g.setColor(new Color(5, 181, 79));
        String s = "Score: " + score;
        g.drawString(s, SCREEN_SIZE / 2 + 96, SCREEN_SIZE + 16);

        for (int i = 0; i < lives; i++) {
            g.drawImage(pacm, i * 28 + 8, SCREEN_SIZE + 1, this);
        }
    }

    private void checkMaze() {

        int i = 0;
        boolean finished = true;

        while (i < N_BLOCKS * N_BLOCKS && finished) {

            if ((screenData[i]) != 0) {
                finished = false;
            }

            i++;
        }

        if (finished) {
            initLevel();
        }
    }

    private void death() {

        lives--;

        if (lives == 0) {
            inGame = false;
        }

        continueLevel();
        v1=true;
        v2=true;
        v3=true;
        v4=true;
        v5=true;
        v6=true;

    }

    private void moveGhosts(Graphics2D g2d) {

        int pos;
        int count;

            for (int i = 0; i < 5; i++)
            {
                if(drawGhostFlag[i])
                {
                    if (ghost_x[i] % BLOCK_SIZE == 0 && ghost_y[i] % BLOCK_SIZE == 0)
                    {
                        pos = ghost_x[i] / BLOCK_SIZE + N_BLOCKS * (ghost_y[i] / BLOCK_SIZE);

                        count = 0;

                        if ((screenData[pos] & 1) == 0 && ghost_dx[i] != 1)
                        {
                            dx[count] = -1;
                            dy[count] = 0;
                            count++;
                        }

                        if ((screenData[pos] & 2) == 0 && ghost_dy[i] != 1)
                        {
                            dx[count] = 0;
                            dy[count] = -1;
                            count++;
                        }

                        if ((screenData[pos] & 4) == 0 && ghost_dx[i] != -1)
                        {
                            dx[count] = 1;
                            dy[count] = 0;
                            count++;
                        }

                        if ((screenData[pos] & 8) == 0 && ghost_dy[i] != -1)
                        {
                            dx[count] = 0;
                            dy[count] = 1;
                            count++;
                        }

                        if (count == 0)
                        {

                            if ((screenData[pos] & 15) == 15)
                            {
                                ghost_dx[i] = 0;
                                ghost_dy[i] = 0;
                            } else
                            {
                                ghost_dx[i] = -ghost_dx[i];
                                ghost_dy[i] = -ghost_dy[i];
                            }

                        } else
                        {

                            count = (int) (Math.random() * count);

                            if (count > 3)
                            {
                                count = 3;
                            }

                            ghost_dx[i] = dx[count];
                            ghost_dy[i] = dy[count];
                        }

                    }

                    ghost_x[i] = ghost_x[i] + (ghost_dx[i] * ghostSpeed[i]);
                    ghost_y[i] = ghost_y[i] + (ghost_dy[i] * ghostSpeed[i]);
                    drawGhost(g2d, ghost_x[i] + 1, ghost_y[i] + 1);
                }
                if (ghost_x[i] > 456)
                {
                    ghost_x[i] = 0;
                } else if (ghost_x[i] < 0)
                {
                    ghost_x[i] = 456;
                }
                if (ghost_y[i] > 456)
                {
                    ghost_y[i] = 0;
                } else if (ghost_y[i] < 0)
                {
                    ghost_y[i] = 456;
                }

                if (pacman_x > (ghost_x[i] - 12) && pacman_x < (ghost_x[i] + 12)
                        && pacman_y > (ghost_y[i] - 12) && pacman_y < (ghost_y[i] + 12)
                        && inGame && !undefeatable)
                {

                    dying = true;
                } else if (pacman_x > (ghost_x[i] - 12) && pacman_x < (ghost_x[i] + 12)
                        && pacman_y > (ghost_y[i] - 12) && pacman_y < (ghost_y[i] + 12)
                        && inGame && undefeatable)
                {

                    // enemy dies
                    drawGhostFlag[i] = false;
                    timer_enemies.start();
                    if(timer_enemies.isRunning() && counter[i]!=300){
                        counter[i] = counter[i] + 60;
                        System.out.println(counter[i]);
                    }

                    if(counter[i]==300){
                        drawGhostFlag[i] = true;
                        ghost_y[i] =11 * BLOCK_SIZE; //start position
                        ghost_x[i] = 10 * BLOCK_SIZE;
                        counter[i]=0;
                       // timer_enemies.stop();
                    }

                }
            }

    }

    private void drawGhost(Graphics2D g2d, int x , int y) {
        g2d.drawImage(ghost, x, y, this);
    }

    private void movePacman() {

        int pos;
        short ch;

        if (pacman_x % BLOCK_SIZE == 0 && pacman_y % BLOCK_SIZE == 0) {
            pos = pacman_x / BLOCK_SIZE + N_BLOCKS * (pacman_y / BLOCK_SIZE);
            ch = screenData[pos];

            if ((ch & 16) != 0) {
                screenData[pos] = (short) (ch & 15);
                score++;
            }

            if (req_dx != 0 || req_dy != 0) {
                if (!((req_dx == -1 && req_dy == 0 && (ch & 1) != 0)
                        || (req_dx == 1 && req_dy == 0 && (ch & 4) != 0)
                        || (req_dx == 0 && req_dy == -1 && (ch & 2) != 0)
                        || (req_dx == 0 && req_dy == 1 && (ch & 8) != 0))) {
                    pacmand_x = req_dx;
                    pacmand_y = req_dy;
                }
            }

            // Check for standstill
            if ((pacmand_x == -1 && pacmand_y == 0 && (ch & 1) != 0)
                    || (pacmand_x == 1 && pacmand_y == 0 && (ch & 4) != 0)
                    || (pacmand_x == 0 && pacmand_y == -1 && (ch & 2) != 0)
                    || (pacmand_x == 0 && pacmand_y == 1 && (ch & 8) != 0)) {
                pacmand_x = 0;
                pacmand_y = 0;
            }


            if (((pacman_x > 300 && pacman_x < 324  && pacman_y > 60 && pacman_y < 84) |
                    (pacman_x > 180 && pacman_x < 204  && pacman_y > 156 && pacman_y < 180)|
                    (pacman_x > 420 && pacman_x < 444  && pacman_y > 228 && pacman_y < 252) |
                    (pacman_x > 84 && pacman_x < 108  && pacman_y > 324 && pacman_y < 348)|
                    (pacman_x > 84 && pacman_x < 108  && pacman_y > 372 && pacman_y < 396)|
                    (pacman_x > 276 && pacman_x < 300  && pacman_y > 348    && pacman_y < 372))
                    && inGame){
                //System.out.println("Undefeatable mode on !!");
                undefeatable = true;
            }
        }
        pacman_x = pacman_x + PACMAN_SPEED * pacmand_x;
        pacman_y = pacman_y + PACMAN_SPEED * pacmand_y;

        if(pacman_x == 312 && pacman_y == 72){
            this.v1 = false;
        }
        if(pacman_x == 192 && pacman_y == 168){
            this.v2 = false;
        }
        if(pacman_x == 432 && pacman_y == 240){
            this.v3 = false;
        }
        if(pacman_x == 96 && pacman_y == 336){
            this.v4 = false;
        }
        if(pacman_x == 96 && pacman_y == 384){
            this.v5 = false;
        }
        if(pacman_x == 288 && pacman_y == 360){
            this.v6 = false;
        }

        if(pacman_x > 456){
            pacman_x = 0;
        } else if (pacman_x < 0) {
            pacman_x = 456;
        }
        if(pacman_y > 456){
            pacman_y = 0;
        } else if (pacman_y < 0) {
            pacman_y = 456;
        }
    }

    private void drawPacman(Graphics2D g2d) {

        if(!undefeatable)
        {

            if (req_dx == -1)
            {
                g2d.drawImage(left, pacman_x + 1, pacman_y + 1, this);
            } else if (req_dx == 1)
            {
                g2d.drawImage(right, pacman_x + 1, pacman_y + 1, this);
            } else if (req_dy == -1)
            {
                g2d.drawImage(up, pacman_x + 1, pacman_y + 1, this);
            } else
            {
                g2d.drawImage(down, pacman_x + 1, pacman_y + 1, this);
            }
        }
        else{
            // render some different color gif
            if (req_dx == -1)
            {
                g2d.drawImage(left, pacman_x + 1, pacman_y + 1, this);
            } else if (req_dx == 1)
            {
                g2d.drawImage(right, pacman_x + 1, pacman_y + 1, this);
            } else if (req_dy == -1)
            {
                g2d.drawImage(up, pacman_x + 1, pacman_y + 1, this);
            } else
            {
                g2d.drawImage(down, pacman_x + 1, pacman_y + 1, this);
            }

        }
    }

    private void drawMaze(Graphics2D g2d) {

        short i = 0;
        int x, y;

        for (y = 0; y < SCREEN_SIZE; y += BLOCK_SIZE) {
            for (x = 0; x < SCREEN_SIZE; x += BLOCK_SIZE) {

                g2d.setColor(new Color(14, 5, 89));
                g2d.setStroke(new BasicStroke(2));

                if ((levelData[i] == 0)) {
                    g2d.fillRect(x, y, BLOCK_SIZE, BLOCK_SIZE);
                }

                if ((screenData[i] & 1) != 0) {
                    g2d.drawLine(x, y, x, y + BLOCK_SIZE - 1);
                }

                if ((screenData[i] & 2) != 0) {
                    g2d.drawLine(x, y, x + BLOCK_SIZE - 1, y);
                }

                if ((screenData[i] & 4) != 0) {
                    g2d.drawLine(x + BLOCK_SIZE - 1, y, x + BLOCK_SIZE - 1,
                            y + BLOCK_SIZE - 1);
                }

                if ((screenData[i] & 8) != 0) {
                    g2d.drawLine(x, y + BLOCK_SIZE - 1, x + BLOCK_SIZE - 1,
                            y + BLOCK_SIZE - 1);
                }

                if ((screenData[i] & 16) != 0) {
                    g2d.setColor(new Color(255,255,0));
                    g2d.fillOval(x + 10, y + 10, 6, 6);
                }

                i++;
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, d.width, d.height);

        drawMaze(g2d);
        drawScore(g2d);

        if (inGame) {
            playGame(g2d);
        } else {
            showIntroScreen(g2d);
        }

        Toolkit.getDefaultToolkit().sync();
        g2d.dispose();
    }


    //controls
    class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if (inGame) {
                if (key == KeyEvent.VK_LEFT) {
                    req_dx = -1;
                    req_dy = 0;
                } else if (key == KeyEvent.VK_RIGHT) {
                    req_dx = 1;
                    req_dy = 0;
                } else if (key == KeyEvent.VK_UP) {
                    req_dx = 0;
                    req_dy = -1;
                } else if (key == KeyEvent.VK_DOWN) {
                    req_dx = 0;
                    req_dy = 1;
                } else if (key == KeyEvent.VK_ESCAPE && timer.isRunning()) {
                    inGame = false;
                }
            } else {
                if (key == KeyEvent.VK_SPACE) {
                    inGame = true;
                    initGame();
                }
            }
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

}

