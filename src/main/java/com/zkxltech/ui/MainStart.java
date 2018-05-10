package com.zkxltech.ui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Panel;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.App;
import com.sun.awt.AWTUtilities;
import com.zkxltech.config.Global;

@SuppressWarnings("restriction")
public class MainStart {

	public JFrame frame;
	private Panel panel;
	private String title = Global.VERSION; //窗口标题
	int Window_Width = Toolkit.getDefaultToolkit().getScreenSize().width;
	int Window_Height = Toolkit.getDefaultToolkit().getScreenSize().height;
	int Frame_Width = 42;
	int Frame_Height = 42;
	int shellX,shellY;
	int x, y;
	int x1, y1;// 鼠标释放位置
	int mouse_X = 0, mouse_Y = 0;
	public static boolean isShow = false;
	private MainPage mainPage ;
	private ImageIcon imageIcon, icon;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		MainStart window = new MainStart();
	}

	/**
	 * Create the application.
	 */
	public MainStart() {
		initImage();
		initialize();
	}

	public void initImage() {
		/*任务栏图片*/
		imageIcon = new ImageIcon(this.getClass().getResource("/com/zkxltech/image/zhuomian.png"));
		icon = new ImageIcon(this.getClass().getResource("/com/zkxltech/image/zhuomian_close.png"));

	}
	
	public void changeImage(){
		if (isShow) {
			icon = new ImageIcon(this.getClass().getResource("/com/zkxltech/image/zhuomian_open.png"));
		}else {
			icon = new ImageIcon(this.getClass().getResource("/com/zkxltech/image/zhuomian_close.png"));
		}
	}
	
	public void initData(){
		shellX = 0;
		GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Rectangle maximumWindowBounds = graphicsEnvironment.getMaximumWindowBounds();
		shellY = maximumWindowBounds.height - (int) (Toolkit.getDefaultToolkit().getScreenSize().width/2.4) * 560 / 800 - Frame_Height-10;
		x = shellX;
		y = shellY;
		x1 =shellX;
		y1 =shellY;
	}
	
	public void exit() {
		frame.setVisible(false);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings("restriction")
	private void initialize() {
		initData();// 初始化窗口位置
		frame = new JFrame();
		frame.setUndecorated(true); // 不装饰
		AWTUtilities.setWindowOpaque(frame, false);
		frame.getContentPane().setLayout(null);// 设置布局
		frame.setBounds(shellX, shellY, Frame_Width, Frame_Height);
		frame.setAlwaysOnTop(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		/* 默认显示的圆形 */
		panel = new Panel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void paint(Graphics g) {
				super.paint(g);
				Graphics2D g2d = (Graphics2D) g;
				icon.paintIcon(this, g2d, 0, 0);
				g.dispose();
			}
		};
		panel.setBounds(0, 0, Frame_Width, Frame_Height);
		/*鼠标拖拽事件*/
		panel.addMouseListener(new MouseAdapter() {
         	@Override
         	public void mousePressed(MouseEvent e) {
         		if (!isShow) {
         			mouse_X =e.getXOnScreen();
             		mouse_Y =e.getYOnScreen();
				}
         	}
        	@Override
        	public void mouseReleased(MouseEvent e) {
        		if (!isShow) {
            		x1 =x;
            		y1 =y;
        		}
        	}
			@Override
			public void mouseClicked(MouseEvent e) {
				frame.setBounds(shellX,shellY, Frame_Width, Frame_Height);
				x = shellX;
				y = shellY;
				x1 =shellX;
				y1 =shellY;
				if (!isShow) {
					Display.getDefault().syncExec(new Runnable() {
					    public void run() {
					    	mainPage.showShell();
					    	}
					    });
				}else {
					Display.getDefault().syncExec(new Runnable() {
					    public void run() {
					    	mainPage.closeShell();
					    	}
					    }); 
				}
				isShow = !isShow;
				changeImage();
				frame.repaint();
			}
        });
		panel.addMouseMotionListener(new MouseMotionListener() {
        	
			@Override
			public void mouseDragged(MouseEvent e) {
				if (!isShow) {
					x=x1 + e.getXOnScreen()-mouse_X;
					y=y1 +e.getYOnScreen()-mouse_Y;
					frame.setBounds(x,y, Frame_Width, Frame_Height);
				}
			}
	
			@Override
			public void mouseMoved(MouseEvent e) {
			}
    	});
		frame.getContentPane().add(panel);

		frame.setIconImage(imageIcon.getImage()); // 任务栏图片
		frame.setTitle(title);
		frame.setVisible(true);
		frame.setAlwaysOnTop(true);

		// //显示手状
		panel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
	
		mainPage = new MainPage(new Shell());
		mainPage.setMainStart(this);
		mainPage.open();
	}

}
