package org.usfirst.frc.team3257.robot;

import java.text.DecimalFormat;

import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class rangeFinder extends Robot {

	public static void main() {
		// TODO Auto-generated method stub
		double rangeL = (distL.getRangeInches()) / 12; // reads the range on the
		// ultrasonic sensor
		double rangeR = (distR.getRangeInches()) / 12;
		// Timer.delay(.5);

		DecimalFormat myFormat = new DecimalFormat("0.0");
		String rangeFinalL = myFormat.format(rangeL);
		String rangeFinalR = myFormat.format(rangeR);

		SmartDashboard.putNumber("Front Left Distance (ft): ", distL.getRangeInches() / 12);
		SmartDashboard.putNumber("front Right Distance (ft): ", distR.getRangeInches() / 12);
		SmartDashboard.putNumber("Back Left Distance (ft): ", backDistL.getRangeInches() / 12);
		SmartDashboard.putNumber("Back Right Distance (ft): ", backDistR.getRangeInches() / 12);

	}
	
	public static void approachPeg() {
		frontAlign();
		
		while ((distL.getRangeInches() > 38.0) && (distR.getRangeInches() > 38)) {
			setLeftSpeed(.25);
			setRightSpeed(-.25);
		}
		while ((distL.getRangeInches() > 28.0) && (distL.getRangeInches() < 39) && (distR.getRangeInches() > 28)
	&& (distR.getRangeInches() < 39)) {
			setLeftSpeed(.2);
			setRightSpeed(-.2);
		}
		stop();

	}
	
	public static void approach() {
		align();

		while ((backDistL.getRangeInches() > 20.0) && (backDistR.getRangeInches() > 20)) {
			setLeftSpeed(-.3);
			setRightSpeed(.3);
		}
		while ((backDistL.getRangeInches() > 9.0) && (backDistL.getRangeInches() < 21) && (backDistR.getRangeInches() > 9)
	&& (backDistR.getRangeInches() < 21)) {
			setLeftSpeed(-.25);
			setRightSpeed(.25);
		}
		
		align();
		stop();
	}

	public static void stop() {
		setLeftSpeed(0);
		setRightSpeed(0);
	}

	public static void align() {
		while (backDistL.getRangeInches() + 1 < backDistR.getRangeInches()) {
			setLeftSpeed(0.3);
			setRightSpeed(0.3);
		}

		while (backDistR.getRangeInches() + 1 < backDistL.getRangeInches()) {
			setLeftSpeed(-0.3);
			setRightSpeed(-0.3);
		}
		stop();

	}
	
	public static void frontAlign() {
		while (distL.getRangeInches() + 1 < distR.getRangeInches()) {
			setLeftSpeed(-0.2);
			setRightSpeed(-0.2);
		}

		while (distR.getRangeInches() + 1 < distL.getRangeInches()) {
			setLeftSpeed(0.2);
			setRightSpeed(0.2);
		}
		stop();

	}



}
