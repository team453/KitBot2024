// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
  
  public static class OIConstants {
    // Port numbers for driver and operator gamepads. These correspond with the numbers on the USB
    // tab of the DriverStation
    public static final int kDriverControllerPort = 1;
    public static final int kOperatorControllerPort = 0;

    //Speed multipliers/ limits
    public static final double kDriverSpeedMultiplier = 0.60;

    //Button Mapping
    public static final int kDriverAutoMoveButton = 9;
    public static final int kDriverAlignmentButton = 10;

  }

  public static class DrivetrainConstants {
    // PWM ports/CAN IDs for motor controllers
    public static final int kLeftRearID = 2;
    public static final int kLeftFrontID = 1;
    public static final int kRightRearID = 7;
    public static final int kRightFrontID = 10;

    // Current limit for drivetrain motors
    public static final int kCurrentLimit = 60;

    //Auto correction constants
     public static final double k_AutoCorrectSpeed = 0.25;
    public static final double k_AutoCorrectDist = 1.0;
    public static final double k_AutoCorrectTurn = 0.1;

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
    public static final double kLauncherSpeed = 1;
    public static final double kLaunchFeederSpeed = 1;
    public static final double kIntakeLauncherSpeed = -1;
    public static final double kIntakeFeederSpeed = -.2;

    public static final double kLauncherDelay = 1;
  }

  /*
   *  public static final class DriveConstants {
        public static final int kLeftFrontPort = 1;
        public static final int kLeftRearPort = 2;
        public static final int kRightFrontPort = 3;
        public static final int kRightRearPort = 4;

        public static final double k_AutoCorrectSpeed = 0.25;
        public static final double k_AutoCorrectDist = 1.0;
        public static final double k_AutoCorrectTurn = 0.1;

        public static final double kTrackWidth = 0.68;
        // Distance between centers of right and left wheels on robot
        public static final double kWheelBase = 0.68;
        // Distance between centers of front and back wheels on robot

        public static final int kGearRatio = 16;

        public static final MecanumDriveKinematics kDriveKinematics =
            new MecanumDriveKinematics(
                new Translation2d(kWheelBase / 2, kTrackWidth / 2),
                new Translation2d(kWheelBase / 2, -kTrackWidth / 2),
                new Translation2d(-kWheelBase / 2, kTrackWidth / 2),
                new Translation2d(-kWheelBase / 2, -kTrackWidth / 2));

        public static final int kEncoderCPR = 2048;
        public static final double kWheelDiameterMeters = 0.1524;
        public static final double kEncoderDistancePerPulse =
            (kWheelDiameterMeters * Math.PI) / ((double) kEncoderCPR * kGearRatio);
        // These are example values only - DO NOT USE THESE FOR YOUR OWN ROBOT!
        // These characterization values MUST be determined either experimentally or theoretically
        // for *your* robot's drive.
        // The SysId tool provides a convenient method for obtaining these values for your robot.
        public static final SimpleMotorFeedforward kFeedforward =
        new SimpleMotorFeedforward(1, 0.8, 0.15);

        // Example value only - as above, this must be tuned for your drive!
        public static final double kPFrontLeftVel = 0.5;
        public static final double kPRearLeftVel = 0.5;
        public static final double kPFrontRightVel = 0.5;
        public static final double kPRearRightVel = 0.5;

        public static final double kNormSpeedMult = 0.66;
        public static final double kTurboSpeedMult = 1.0;

        //Button Mapping
        public static final int kTurboButtonMap = 3;
        public static final int kIntakeInMap = 9;
        public static final int kIntakeOutMap = 11;
        public static final int kLifterUpMap = 10;
        public static final int kLifterDownMap = 12;
        public static final int kClimberUpMap = 7;
        public static final int kClimberDownMap = 8;
    }
   */
}
