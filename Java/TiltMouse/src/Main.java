import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JButton;

public class Main extends JFrame{
	private static TiltMouse mouse;
	public Main() {	
		this.addWindowStateListener(new WindowStateListener() {	
				@Override
				public void windowStateChanged(WindowEvent e) {
					if(e.getID() == WindowEvent.WINDOW_CLOSING) {
						mouse.stop();
					}
				}
		});
		
		setTitle("Tilt Mouse");
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		getContentPane().add(panel);
		
		JLabel lblSetSensitivity = new JLabel("Set sensitivity:");
		panel.add(lblSetSensitivity);
		
		
		JSlider slider = new JSlider();
		panel.add(slider);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.setMaximum(50);
		slider.setMinimum(0);
		slider.setValue((int)(mouse.getSensitivity()*100.0));
		slider.setMajorTickSpacing(10);
		slider.setMinorTickSpacing(1);
		
		
		try {
			BufferedImage myPicture;
			myPicture = ImageIO.read(new File("mouse.png"));
			JLabel picLabel = new JLabel(new ImageIcon(myPicture));
			getContentPane().add(picLabel, BorderLayout.NORTH);
			
			JButton startBtn = new JButton("Set");
			startBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println(slider.getValue()/100.0);
					mouse.setSensitivity(slider.getValue()/100.0);
				}
			});
			
			getContentPane().add(startBtn, BorderLayout.SOUTH);
		} catch (IOException e1) {
			System.out.println("Cannot find image");
		}
		
	}
	public static void main(String[] args) {
		try {
			mouse = new TiltMouse("com3", 9600);
		} catch (AWTException e1) {
			System.out.println("Cannot use robot in this system");
		}
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {     	
                JFrame frame = new Main();
                frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
                frame.pack();
        		frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
		 Runnable r = new Runnable() {
	         public void run() {
	             mouse.start();
	         }
	     };

	     new Thread(r).start();
	}
}
