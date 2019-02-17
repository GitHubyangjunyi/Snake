
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
//���ص�,������Ϸ���й��̿����1234567
//���ص�,������Ϸ���й��̿����1234567
//���ص�,������Ϸ���й��̿����1234567
public class Board extends JPanel implements ActionListener {//���װ�ʵ��ActionListener����������ӿ�
	private static final long serialVersionUID = 1L;
//��Ϸ����
	private final int B_WIDTH = 300;//�װ�X��
    private final int B_HEIGHT = 300;//�װ�Y��
    private final int DOT_SIZE = 10;//ͼռ�õ����ش�С
    private final int ALL_DOTS = 900;//������300 * 300/10 * 10
    private final int RAND_POS = 29;//�������0~29
    private final int DELAY = 300;//��ʱ���
//������������
    private final int x[] = new int[ALL_DOTS];//900
    private final int y[] = new int[ALL_DOTS];//900

    private int dots;

//ƻ���������
    private int apple_x;
    private int apple_y;

//����״̬���ݱ���
    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
//�Ƿ�����Ϸ����
    private boolean inGame = true;
//��ʱ������
    private Timer timer;

//��ͼ����
    private Image ball;//����
    private Image apple;//ƻ��ʳ��
    private Image head;//��ͷ
//������ͼ����
    private void loadImages() {

        ImageIcon iid = new ImageIcon("src/resources/dot.png");
        ball = iid.getImage();

        ImageIcon iia = new ImageIcon("src/resources/apple.png");
        apple = iia.getImage();

        ImageIcon iih = new ImageIcon("src/resources/head.png");
        head = iih.getImage();
    }
//1.��ʼ����Ϸ�����庯��
    public Board() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {//ʵ�ּ��̰���δ�ͷŵĴ�����
    //���ռ����¼��������¼����ķ���״̬����
                int key = e.getKeyCode();
                if ((key == KeyEvent.VK_LEFT) && (!rightDirection)) {//���������ҵ�ǰ״̬��Ϊ�Ҳ��ܸı�
                    leftDirection = true;
                    upDirection = false;
                    downDirection = false;
                }
                if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)) {//������Ҽ��ҵ�ǰ״̬��Ϊ����ܸı�
                    rightDirection = true;
                    upDirection = false;
                    downDirection = false;
                }
                if ((key == KeyEvent.VK_UP) && (!downDirection)) {//������ϼ��ҵ�ǰ״̬��Ϊ�²��ܸı�
                    upDirection = true;
                    rightDirection = false;
                    leftDirection = false;
                }
                if ((key == KeyEvent.VK_DOWN) && (!upDirection)) {//������¼��ҵ�ǰ״̬��Ϊ�ϲ��ܸı�
                    downDirection = true;
                    rightDirection = false;
                    leftDirection = false;
                }
            }
        });
//���ռ����¼��������¼����ķ���״̬����
        setBackground(Color.black);//��ɫ����
        setFocusable(true);//����ֱ�ӻ�ý���
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));//�洰�ڱ仯
        loadImages();//����ͼƬ
        initGame();//��ʼ����Ϸ����
    }
//2.��ʼ����Ϸ�������ݵĺ���
    private void initGame() {
//3.��ʼ��10�������
        dots = 10;
//��һ�����ɵ�һ���ߵ���������
        for (int z = 0; z < dots; z++) {
            x[z] = 50 - z * 10;
            y[z] = 50;
        }
//4.�������ƻ������
        locateApple();
//5.���ö�ʱ��,ʱ�䵽�˾ʹ���һ���¼�
        timer = new Timer(DELAY, this);
        timer.start();
    }

//6.�����¼�ʱ����,����,��ʱ����һ�������¼�,ʲô������Ҳ�ᰴʱ������ִ������Ĳ���
//                  ��û�к�ƻ����ײ
//                  ��û��ײ���Լ���ǽ��
//                  Ȼ���ƶ�
//                  Ȼ���ػ�
@Override
public void actionPerformed(ActionEvent e) {//ActionListener�ӿڶ���Ŀշ���,һ�������¼���ִ���������,���綨ʱ��,����

    if (inGame) {
//7.����ƻ��
        checkApple();//���ƻ����ͷ����ײ, �ͻ������ߵĹؽ�����,���������λ��ƻ���ķ���
//8.�����ײ
        checkCollision();//��ײ���
//9.û��ײ���ȶ������ƶ���������
        move();
    }
//10.�ػ�
    repaint();//�ػ�,��������϶ϵ�,�������,��ÿ��ֻ�ƶ�һ���ͣ�����ȴ��ػ�
}
    @Override
    public void paintComponent(Graphics g) {//11.�ػ�����Լ�
        super.paintComponent(g);//��ȫ�ػ治����ԭ�����,Ҳ�����ػ�һ��Board,Ȼ�����ȫ��,Ȼ���ٻ�����������ƻ��
//12.�ػ��걳������ƻ�����߻���ȥ
        doDrawing(g);
    }
//13.����ƻ������,������Ϸ����ʱ������Ϸ����
    private void doDrawing(Graphics g) {
        
        if (inGame) {

            g.drawImage(apple, apple_x, apple_y, this);

            for (int z = 0; z < dots; z++) {
                if (z == 0) {
                    g.drawImage(head, x[z], y[z], this);//������ͷ
                } else {
                    g.drawImage(ball, x[z], y[z], this);//��������,��ͷ��1
                }
            }

            Toolkit.getDefaultToolkit().sync();//ͬ��ȷ���������

        } else {

            String msg = "Game Over";
            Font small = new Font("Helvetica", Font.BOLD, 14);//��������
            FontMetrics metr = getFontMetrics(small);//���������
            g.setColor(Color.white);//���û�����ɫΪ��ɫ
            g.setFont(small);//���û�������
            g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2);//���ڵװ��м�
        }        
    }
//����Ƿ�Ե�ƻ������
    private void checkApple() {//���ƻ����ͷ����ײ, �ͻ������ߵĹؽ�����,���������λ��ƻ���ķ���

        if ((x[0] == apple_x) && (y[0] == apple_y)) {

            dots++;
            locateApple();//�������ƻ������
        }
    }

    private void move() {
//�ߵ��ƶ�����,���ݵ�ǰ����״̬�ƶ�,�ƶ���ֻ������,��û�л�����
        //ע��,����������ͷ��,����ͷ����������
        //���������ƶ�
        //�ڶ����ؽ��ƶ��ڵ�һ��λ��,�������ؽ��ƶ��ڵڶ����ؽ����ڵ�λ��,�൱�����һ��ȥ��,�������ͷ���ƶ�
        for (int z = dots; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }
        //����ͷ���ƶ�
        if (leftDirection) {//��ͷ�������ƶ�
            x[0] -= DOT_SIZE;
        }
        if (rightDirection) {//��ͷ�������ƶ�
            x[0] += DOT_SIZE;
        }
        if (upDirection) {//��ͷ�������ƶ�
            y[0] -= DOT_SIZE;
        }
        if (downDirection) {//��ͷ�������ƶ�
            y[0] += DOT_SIZE;
        }
    }

    private void checkCollision() {
//��ײ��⺯��,��⵽��ײ���ʱ����ֹ���ٴ�����ʱ�¼�
        //������ײ���
        for (int z = dots; z > 0; z--) {
            //���ڵ���4������Ҫ�ӵȺ�,������bug,���ͷ��û�и�������ײ
            if ((z >= 4) && (x[0] == x[z]) && (y[0] == y[z])) {
                inGame = false;
            }
        }
        //��Ե��ײ���
        if (y[0] >= B_HEIGHT) {//�����±���Ϸ����
            inGame = false;
        }
        if (y[0] < 0) {//�����ϱ���Ϸ����
            inGame = false;
        }
        if (x[0] >= B_WIDTH) {//�����ұ�ǽ����Ϸ����
            inGame = false;
        }
        if (x[0] < 0) {//�������ǽ����Ϸ����
            inGame = false;
        }
        if (!inGame) {
            timer.stop();//��ʱ����ֹ
        }
    }

    private void locateApple() {
//�������ƻ�����꺯��
        //Math.random() 0~1֮��ĸ���,ÿ�����ص�10,���ڴ�С300,ֻ��0~29
        int r = (int) (Math.random() * RAND_POS);//�������
        apple_x = ((r * DOT_SIZE));
        r = (int) (Math.random() * RAND_POS);
        apple_y = ((r * DOT_SIZE));
    }
//���ռ����¼��ĳ�����(�ڲ���,���ڳ���ʼ����дΪ������)
/*private class TAdapter extends KeyAdapter {
    @Override
    public void keyPressed(KeyEvent e) {//ʵ�ּ��̰���δ�ͷŵĴ�����
//���ռ����¼��������¼����ķ���״̬����
        int key = e.getKeyCode();
        if ((key == KeyEvent.VK_LEFT) && (!rightDirection)) {//���������ҵ�ǰ״̬��Ϊ�Ҳ��ܸı�
            leftDirection = true;
            upDirection = false;
            downDirection = false;
        }
        if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)) {//������Ҽ��ҵ�ǰ״̬��Ϊ����ܸı�
            rightDirection = true;
            upDirection = false;
            downDirection = false;
        }
        if ((key == KeyEvent.VK_UP) && (!downDirection)) {//������ϼ��ҵ�ǰ״̬��Ϊ�²��ܸı�
            upDirection = true;
            rightDirection = false;
            leftDirection = false;
        }
        if ((key == KeyEvent.VK_DOWN) && (!upDirection)) {//������¼��ҵ�ǰ״̬��Ϊ�ϲ��ܸı�
            downDirection = true;
            rightDirection = false;
            leftDirection = false;
        }
    }
}*/
}
