
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;
//划重点,整个游戏运行过程看序号1234567
//划重点,整个游戏运行过程看序号1234567
//划重点,整个游戏运行过程看序号1234567
public class Board extends JPanel implements ActionListener {//面板底板实现ActionListener组件监听器接口
	private static final long serialVersionUID = 1L;
//游戏数据
	private final int B_WIDTH = 300;//底板X轴
    private final int B_HEIGHT = 300;//底板Y轴
    private final int DOT_SIZE = 10;//图占用的像素大小
    private final int ALL_DOTS = 900;//最大点数300 * 300/10 * 10
    private final int RAND_POS = 29;//随机种子0~29
    private final int DELAY = 300;//定时间隔
//单点坐标数据
    private final int x[] = new int[ALL_DOTS];//900
    private final int y[] = new int[ALL_DOTS];//900

    private int dots;

//苹果坐标变量
    private int apple_x;
    private int apple_y;

//方向状态数据变量
    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
//是否还在游戏变量
    private boolean inGame = true;
//定时器变量
    private Timer timer;

//贴图变量
    private Image ball;//身体
    private Image apple;//苹果食物
    private Image head;//蛇头
//加载贴图函数
    private void loadImages() {

        ImageIcon iid = new ImageIcon("src/resources/dot.png");
        ball = iid.getImage();

        ImageIcon iia = new ImageIcon("src/resources/apple.png");
        apple = iia.getImage();

        ImageIcon iih = new ImageIcon("src/resources/head.png");
        head = iih.getImage();
    }
//1.初始化游戏背景板函数
    public Board() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {//实现键盘按下未释放的处理方法
    //接收键盘事件并根据事件更改方向状态数据
                int key = e.getKeyCode();
                if ((key == KeyEvent.VK_LEFT) && (!rightDirection)) {//如果按左键且当前状态不为右才能改变
                    leftDirection = true;
                    upDirection = false;
                    downDirection = false;
                }
                if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)) {//如果按右键且当前状态不为左才能改变
                    rightDirection = true;
                    upDirection = false;
                    downDirection = false;
                }
                if ((key == KeyEvent.VK_UP) && (!downDirection)) {//如果按上键且当前状态不为下才能改变
                    upDirection = true;
                    rightDirection = false;
                    leftDirection = false;
                }
                if ((key == KeyEvent.VK_DOWN) && (!upDirection)) {//如果按下键且当前状态不为上才能改变
                    downDirection = true;
                    rightDirection = false;
                    leftDirection = false;
                }
            }
        });
//接收键盘事件并根据事件更改方向状态数据
        setBackground(Color.black);//黑色背景
        setFocusable(true);//启动直接获得焦点
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));//随窗口变化
        loadImages();//加载图片
        initGame();//初始化游戏函数
    }
//2.初始化游戏对象数据的函数
    private void initGame() {
//3.初始化10个点的蛇
        dots = 10;
//第一次生成的一条蛇的坐标数据
        for (int z = 0; z < dots; z++) {
            x[z] = 50 - z * 10;
            y[z] = 50;
        }
//4.随机生成苹果坐标
        locateApple();
//5.设置定时器,时间到了就触发一个事件
        timer = new Timer(DELAY, this);
        timer.start();
    }

//6.发生事件时调用,按键,定时器是一个触发事件,什么都不按也会按时触发并执行下面的操作
//                  有没有和苹果碰撞
//                  有没有撞到自己或墙壁
//                  然后移动
//                  然后重绘
@Override
public void actionPerformed(ActionEvent e) {//ActionListener接口定义的空方法,一旦发生事件就执行这个方法,比如定时器,按键

    if (inGame) {
//7.检测吃苹果
        checkApple();//如果苹果与头部碰撞, 就会增加蛇的关节数量,调用随机定位新苹果的方法
//8.检测碰撞
        checkCollision();//碰撞检测
//9.没碰撞按既定方向移动身体数据
        move();
    }
//10.重绘
    repaint();//重绘,在这里打上断点,点击继续,蛇每次只移动一格就停下来等待重绘
}
    @Override
    public void paintComponent(Graphics g) {//11.重绘组件自己
        super.paintComponent(g);//完全重绘不保留原来组件,也就是重绘一次Board,然后界面全黑,然后再画出画出蛇与苹果
//12.重绘完背景板后把苹果和蛇画上去
        doDrawing(g);
    }
//13.绘制苹果和蛇,或者游戏结束时绘制游戏结束
    private void doDrawing(Graphics g) {
        
        if (inGame) {

            g.drawImage(apple, apple_x, apple_y, this);

            for (int z = 0; z < dots; z++) {
                if (z == 0) {
                    g.drawImage(head, x[z], y[z], this);//绘制蛇头
                } else {
                    g.drawImage(ball, x[z], y[z], this);//绘制蛇身,蛇头减1
                }
            }

            Toolkit.getDefaultToolkit().sync();//同步确保绘制完成

        } else {

            String msg = "Game Over";
            Font small = new Font("Helvetica", Font.BOLD, 14);//字体属性
            FontMetrics metr = getFontMetrics(small);//字体规格对象
            g.setColor(Color.white);//设置画笔颜色为白色
            g.setFont(small);//设置画笔字体
            g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2);//画在底板中间
        }        
    }
//检测是否吃到苹果函数
    private void checkApple() {//如果苹果与头部碰撞, 就会增加蛇的关节数量,调用随机定位新苹果的方法

        if ((x[0] == apple_x) && (y[0] == apple_y)) {

            dots++;
            locateApple();//随机生成苹果坐标
        }
    }

    private void move() {
//蛇的移动函数,根据当前方向状态移动,移动的只是数据,还没有画出来
        //注意,是身体推着头走,不是头带着身体走
        //处理身体移动
        //第二个关节移动在第一个位置,第三个关节移动在第二个关节所在的位置,相当于最后一点去掉,其余点向头部移动
        for (int z = dots; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }
        //处理头部移动
        if (leftDirection) {//将头部向左移动
            x[0] -= DOT_SIZE;
        }
        if (rightDirection) {//将头部向右移动
            x[0] += DOT_SIZE;
        }
        if (upDirection) {//将头部向上移动
            y[0] -= DOT_SIZE;
        }
        if (downDirection) {//将头部向下移动
            y[0] += DOT_SIZE;
        }
    }

    private void checkCollision() {
//碰撞检测函数,检测到碰撞则计时器终止不再触发计时事件
        //身体碰撞检测
        for (int z = dots; z > 0; z--) {
            //大于等于4个点且要加等号,否则有bug,检测头有没有跟身体碰撞
            if ((z >= 4) && (x[0] == x[z]) && (y[0] == y[z])) {
                inGame = false;
            }
        }
        //边缘碰撞检测
        if (y[0] >= B_HEIGHT) {//碰到下边游戏结束
            inGame = false;
        }
        if (y[0] < 0) {//碰到上边游戏结束
            inGame = false;
        }
        if (x[0] >= B_WIDTH) {//碰到右边墙壁游戏结束
            inGame = false;
        }
        if (x[0] < 0) {//碰到左边墙壁游戏结束
            inGame = false;
        }
        if (!inGame) {
            timer.stop();//计时器终止
        }
    }

    private void locateApple() {
//随机生成苹果坐标函数
        //Math.random() 0~1之间的浮点,每个像素点10,窗口大小300,只有0~29
        int r = (int) (Math.random() * RAND_POS);//随机种子
        apple_x = ((r * DOT_SIZE));
        r = (int) (Math.random() * RAND_POS);
        apple_y = ((r * DOT_SIZE));
    }
//接收键盘事件的抽象类(内部类,已在程序开始处改写为匿名类)
/*private class TAdapter extends KeyAdapter {
    @Override
    public void keyPressed(KeyEvent e) {//实现键盘按下未释放的处理方法
//接收键盘事件并根据事件更改方向状态数据
        int key = e.getKeyCode();
        if ((key == KeyEvent.VK_LEFT) && (!rightDirection)) {//如果按左键且当前状态不为右才能改变
            leftDirection = true;
            upDirection = false;
            downDirection = false;
        }
        if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)) {//如果按右键且当前状态不为左才能改变
            rightDirection = true;
            upDirection = false;
            downDirection = false;
        }
        if ((key == KeyEvent.VK_UP) && (!downDirection)) {//如果按上键且当前状态不为下才能改变
            upDirection = true;
            rightDirection = false;
            leftDirection = false;
        }
        if ((key == KeyEvent.VK_DOWN) && (!upDirection)) {//如果按下键且当前状态不为上才能改变
            downDirection = true;
            rightDirection = false;
            leftDirection = false;
        }
    }
}*/
}
