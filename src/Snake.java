
import javax.swing.JFrame;

public class Snake extends JFrame {
	private static final long serialVersionUID = 1L;

	public Snake() {
        
        add(new Board());
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args)throws Exception {
        //不使用任务队列
        //SwingUtilities.invokeLater(new Runnable(){
			//public void run(){
				JFrame ex = new Snake();
                ex.setVisible(true);
			//}
        //});
    }
}