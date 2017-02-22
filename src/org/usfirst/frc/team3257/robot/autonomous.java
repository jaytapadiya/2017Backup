package org.usfirst.frc.team3257.robot;

import edu.wpi.first.wpilibj.Ultrasonic;

public class autonomous {

	public static void main(Ultrasonic distL, Ultrasonic distR) {
		while ((distL.getRangeInches() < 118) && (distR.getRangeInches() < 118)) {
			drive.setLeftSpeed(.2);
			drive.setRightSpeed(-.2);
			if(distL.getRangeInches() + 2 < distR.getRangeInches()) {
				drive.setLeftSpeed(-0.2);
				drive.setRightSpeed(-0.2);
			}

			if(distR.getRangeInches() + 2 < distL.getRangeInches()) {
				drive.setLeftSpeed(0.2);
				drive.setRightSpeed(0.2);
			}

			
		}
		
		drive.setLeftSpeed(0);
		drive.setRightSpeed(0);

	}

}
