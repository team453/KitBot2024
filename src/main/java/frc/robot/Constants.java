// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;
import edu.wpi.first.math.util.Units;
/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
  public static class OperatorConstants {
    // Port numbers for driver and operator gamepads. These correspond with the numbers on the USB
    // tab of the DriverStation
    public static final int kDriverControllerPort = 0;
    public static final int kOperatorControllerPort = 1;

    //Speed multipliers/ limits
    public static final double kDriverSpeedMultiplier = 0.60;

    //Button Mappings
    public static final int kIntakeButton = 1;
    public static final int kHighSpeedShootButton = 8;
    public static final int kLowSpeedShootButton  = 10;
  }

  public static class DrivetrainConstants {
    // PWM ports/CAN IDs for motor controllers
    public static final int kLeftRearID = 2;
    public static final int kLeftFrontID = 1;
    public static final int kRightRearID = 7;
    public static final int kRightFrontID = 10;

    // Current limit for drivetrain motors

    public static final int kCurrentLimit = 60;
    public static final int kEncoderCPR = 1024;
    public static final double kWheelDiameterMeters = Units.inchesToMeters(6);
    public static final double kEncoderDistancePerPulse =
      (kWheelDiameterMeters * Math.PI) / (double) kEncoderCPR;
  }

  public static class LauncherConstants {
    // PWM ports/CAN IDs for motor controllers
   public static final int kFeederID = 5;
    public static final int kLauncherID = 6;

    // Current limit for launcher and feed wheels
    public static final int kLauncherCurrentLimit = 80;
    public static final int kFeedCurrentLimit = 80;

    // Speeds for wheels when intaking and launching. Intake speeds are negative to run the wheels
    // in reverse
    public static final double kLauncherSpeed = -1;
    public static final double kLaunchFeederSpeed = -1;
    public static final double kIntakeLauncherSpeed = 1;
    public static final double kIntakeFeederSpeed = .2;

    public static final double kLauncherDelay = 0.5;
  }

  public static class AutonomousTypes
  {
      public static final String kDefaultAuto = "Default";
      public static final String kDriveForwardAuto = "Drive Forward";
      public static final String kDangerAuto = "DANGEROUS MODE!!";
  }
}
