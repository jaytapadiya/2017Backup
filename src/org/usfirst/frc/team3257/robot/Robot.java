
package org.usfirst.frc.team3257.robot;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.interfaces.Accelerometer;
import edu.wpi.first.wpilibj.interfaces.Potentiometer;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.DecimalFormat;

import org.usfirst.frc.team3257.robot.commands.ExampleCommand;
import org.usfirst.frc.team3257.robot.subsystems.ExampleSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	public static final ExampleSubsystem exampleSubsystem = new ExampleSubsystem();
	public static OI oi;

	public static Joystick stick;
	public static Joystick xbox;
	

	static Jaguar BL, BR, FL, FR, winch, arm;
	// AnalogInput pMeter;
	AnalogPotentiometer pot;
	double potDegrees;
	static Ultrasonic distL, backDistL, distR, backDistR;
	double speedMult;
	Accelerometer accel;
	I2C compass;
	double heading;
	byte zRead[];
	
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */

	public void robotInit() {
		oi = new OI();
		xbox = new Joystick(0);
		stick = new Joystick(1);
		winch = new Jaguar(5);
		arm = new Jaguar(0);
		FL = new Jaguar(3);
		FR = new Jaguar(1);
		BL = new Jaguar(2);
		BR = new Jaguar(4);
		//limitSwitch = new DigitalInput(1);
		CameraServer server = CameraServer.getInstance();
		server.startAutomaticCapture();

		speedMult = .5;
		distL = new Ultrasonic(3, 2);
		distR = new Ultrasonic(1, 0); // output, input DIO ports
		backDistR = new Ultrasonic(7, 6);
		backDistL = new Ultrasonic(5, 4);
		//pMeter = new AnalogInput(0);
		
		pot = new AnalogPotentiometer(3);
	
		accel = new BuiltInAccelerometer();
		
		compass = new I2C(I2C.Port.kOnboard, 0x1E);
		
		heading = 0.0;
		zRead = new byte[6];
	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	public void disabledInit() {

	}

	public void disabledPeriodic() {
	}

	public void autonomousInit() {
		distL.setAutomaticMode(true); // turns on automatic mode
		distR.setAutomaticMode(true);
		backDistL.setAutomaticMode(true);
		backDistR.setAutomaticMode(true);
	}

	/**
	 * This function is called periodically during autonomous
	 */
	public void autonomousPeriodic() {
		autonomous.main(backDistL, backDistR);
	}

	public void teleopInit() {
		distL.setAutomaticMode(true); // turns on automatic mode
		distR.setAutomaticMode(true);
		backDistL.setAutomaticMode(true);
		backDistR.setAutomaticMode(true);
//		byte[] test = new byte[6];
//		byte[] init = new byte[] {0x3C, 0x02, 0x00};
//		byte[] address = new byte[] {0x3C, 0x02};
//		
		
		//compass.read(0x10, 5, test);
		//compass.write(2, 0);
//		compass.writeBulk(init);
//		compass.writeBulk(address);
//		compass.read(0x9, 6, test);
//		
		//System.out.println(test);
//		for(int i = 0; i < test.length; i++)
//		{
//			System.out.println(Byte.toUnsignedInt(test[i]));
//		}
//		
//		try {
//			Thread.sleep(250);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		compass.read(0x03, 6, test);
//		
		//Short x = new Short();
		
//		for (int i = 0; i < test.length; i++) {
//			System.out.println(Byte.toUnsignedInt(test[i]));
//		}
		
//		//System.out.println(test);
		compass.write(0x02, 0x00);

	}

	public void teleopPeriodic() {
		// System.out.println("test");
		
		readCompass();
		
		LiveWindow.run();	
		//normalDrive();
		drive.main();
		rangeFinder.main();
		if (xbox.getRawButton(2) == true) { // 2=B 
			rangeFinder.align();
		}
		if (xbox.getRawButton(1) == true) { //1=A
			rangeFinder.approach();
		}
		
		if (xbox.getRawButton(3) == true) {
			rangeFinder.frontAlign();
		}
		
		if (xbox.getRawButton(4)) {
			rangeFinder.approachPeg();
		}
		SmartDashboard.putNumber("X: ", accel.getX());
		SmartDashboard.putNumber("Y: ", accel.getY());
		SmartDashboard.putNumber("Z: ", accel.getZ());
		// SmartDashboard.putNumber("arm: ", pMeter.getVoltage());
		
		potDegrees = pot.get()*100;
		SmartDashboard.putNumber("Potentiometer: ", potDegrees);
		
		
//		SmartDashboard.putNumber("Adjusted Y", .7071*accel.getY());
//		SmartDashboard.putNumber("Adjusted Z: ", -.7071*accel.getZ());
		SmartDashboard.putNumber("X TOTAL G: ", .68199*accel.getY() + -.68199*accel.getZ());
		
		if ((potDegrees < 20) && stick.getY() > 0) {

			arm.set(stick.getY()*.5);		
		
		} else if ((potDegrees > 55) && stick.getY() < 0) {
			arm.set(stick.getY() * .5);
		} else if ((potDegrees > 20) && (potDegrees < 55)) {
			arm.set(stick.getY()*.5);
			
		}else {arm.set(0);}
		
		//winch.set(stick.getY());
		
		
		
	}



	public static void setLeftSpeed(double speed) {
		FL.set(speed);
		// ML.set(speed);
		BL.set(speed);
		//System.out.print("Left: " + speed + ", ");
	}

	public static void setRightSpeed(double speed) {
		FR.set(speed);
		// MR.set(speed);
		BR.set(speed);
		//System.out.println("Right: " + speed);
	}

	/**
	 * This function is called periodically during test mode
	 */
	public void testPeriodic() {
		LiveWindow.run();
	}
	
	public void readCompass() {
		
		if (!compass.read(0x05, 6, zRead)) {
			heading = ((zRead[1] << 8) | zRead[0]);
		}
		SmartDashboard.putNumber("heading ", heading);
	}
	
}
