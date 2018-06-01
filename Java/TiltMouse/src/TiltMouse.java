import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.util.Scanner;

import com.fazecast.jSerialComm.SerialPort;


public class TiltMouse {
	private SerialPort comPort;
	private String portDescriptor;
	private int baudRate;
	private Robot robot;
	private float x, y, z;
	private double sensitivity = 0.05;
	private Scanner scanner;
	private boolean stop;
	
	public TiltMouse(String portDescriptor, int baudRate) throws AWTException {
		this.portDescriptor = portDescriptor;
		this.baudRate = baudRate;
		this.robot = new Robot();
		this.x = 0;
		this.y = 0;
		this.z = 0;
		this.stop = false;
		
		comPort = SerialPort.getCommPort(portDescriptor);
		comPort.setBaudRate(baudRate);
		comPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 100, 0);
	}
	public void start() {
		comPort.openPort();
		scanner = new Scanner(comPort.getInputStream());
		while(scanner.hasNext() && !stop) {
			String readString = scanner.nextLine();
			String [] splitted = readString.split(" ");
			String [] splittedLR = null;
			if(splitted.length < 3) {
				continue;
			}
			try {
				x = (float) -(Integer.parseInt(splitted[0]));
				y = (float) -(Integer.parseInt(splitted[1]));
				splittedLR = splitted[2].split(":");
				z = (float) -(Integer.parseInt(splittedLR[0]));
			} catch(NumberFormatException e) {
				continue;
			}

			PointerInfo a = MouseInfo.getPointerInfo();
			Point b = a.getLocation();
			int mousex = (int) b.getX();
			int mousey = (int) b.getY();

			if(splittedLR.length == 3) {
			    if(splittedLR[2].equals("r[p]")) {
					robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
					robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
				} else if(splittedLR[2].equals("r[r]")) {
					
				}
				
				if(splittedLR[1].equals("l[p]")) {
					robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
				} else if(splittedLR[1].equals("l[r]")) {
					robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
				}
			}
			
		    robot.mouseMove(mousex + (int)(y*sensitivity), mousey + (int)(x*sensitivity));
		}
		scanner.close();
		comPort.closePort();
	}
	public double getSensitivity() {
		return sensitivity;
	}
	public void setSensitivity(double sensitivity) {
		this.sensitivity = sensitivity;
	}
	public void stop() {
		stop = true;
	}
}
