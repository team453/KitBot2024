// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import static frc.robot.Constants.DrivetrainConstants.*;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import static edu.wpi.first.units.MutableMeasure.mutable;
import static edu.wpi.first.units.Units.Meters;
import static edu.wpi.first.units.Units.MetersPerSecond;
import static edu.wpi.first.units.Units.Volts;
import edu.wpi.first.units.Distance;
import edu.wpi.first.units.Measure;
import edu.wpi.first.units.MutableMeasure;
import edu.wpi.first.units.Velocity;
import edu.wpi.first.units.Voltage;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import frc.robot.Constants;
import frc.robot.Constants.DrivetrainConstants;
import java.util.function.DoubleSupplier;

/* This class declares the subsystem for the robot drivetrain if controllers are connected via CAN. Make sure to go to
 * RobotContainer and uncomment the line declaring this subsystem and comment the line for PWMDrivetrain.
 *
 * The subsystem contains the objects for the hardware contained in the mechanism and handles low level logic
 * for control. Subsystems are a mechanism that, when used in conjuction with command "Requirements", ensure
 * that hardware is only being used by 1 command at a time.
 */
public class CANDrivetrain extends SubsystemBase {
  /*Class member variables. These variables represent things the class needs to keep track of and use between
  different method calls. */
  DifferentialDrive m_drivetrain;
  private final Encoder m_rightEncoder =
      new Encoder(0, 1, true);

  private final Encoder m_leftEncoder =
    new Encoder(2, 3, false);

  // Mutable holder for unit-safe voltage values, persisted to avoid reallocation.
  private final MutableMeasure<Voltage> m_appliedVoltage = mutable(Volts.of(0));
  // Mutable holder for unit-safe linear distance values, persisted to avoid reallocation.
  private final MutableMeasure<Distance> m_distance = mutable(Meters.of(0));
  // Mutable holder for unit-safe linear velocity values, persisted to avoid reallocation.
  private final MutableMeasure<Velocity<Distance>> m_velocity = mutable(MetersPerSecond.of(0));
  /*Constructor. This method is called when an instance of the class is created. This should generally be used to set up
   * member variables and perform any configuration or set up necessary on hardware.
   */

  CANSparkMax leftFront = new CANSparkMax(kLeftFrontID, MotorType.kBrushed);
    CANSparkMax leftRear = new CANSparkMax(kLeftRearID, MotorType.kBrushed);
    CANSparkMax rightFront = new CANSparkMax(kRightFrontID, MotorType.kBrushed);
    CANSparkMax rightRear = new CANSparkMax(kRightRearID, MotorType.kBrushed);


  
  private final SysIdRoutine m_sysIdRoutine =
      new SysIdRoutine(
          // Empty config defaults to 1 volt/second ramp rate and 7 volt step voltage.
          new SysIdRoutine.Config(),
          new SysIdRoutine.Mechanism(
              // Tell SysId how to plumb the driving voltage to the motors.
              (Measure<Voltage> volts) -> {
                leftFront.setVoltage(volts.in(Volts));
                rightFront.setVoltage(volts.in(Volts));
              },
              // Tell SysId how to record a frame of data for each motor on the mechanism being
              // characterized.
              log -> {
                // Record a frame for the left motors.  Since these share an encoder, we consider
                // the entire group to be one motor.
                log.motor("drive-left")
                    .voltage(
                        m_appliedVoltage.mut_replace(
                            leftFront.get() * RobotController.getBatteryVoltage(), Volts))
                    .linearPosition(m_distance.mut_replace(m_leftEncoder.getDistance(), Meters))
                    .linearVelocity(
                        m_velocity.mut_replace(m_leftEncoder.getRate(), MetersPerSecond));
                // Record a frame for the right motors.  Since these share an encoder, we consider
                // the entire group to be one motor.
                log.motor("drive-right")
                    .voltage(
                        m_appliedVoltage.mut_replace(
                            rightFront.get() * RobotController.getBatteryVoltage(), Volts))
                    .linearPosition(m_distance.mut_replace(m_rightEncoder.getDistance(), Meters))
                    .linearVelocity(
                        m_velocity.mut_replace(m_rightEncoder.getRate(), MetersPerSecond));
              },
              // Tell SysId to make generated commands require this subsystem, suffix test state in
              // WPILog with this subsystem's name ("drive")
              this));
  public CANDrivetrain() {
    
    m_leftEncoder.setDistancePerPulse(DrivetrainConstants.kEncoderDistancePerPulse);
    m_rightEncoder.setDistancePerPulse(DrivetrainConstants.kEncoderDistancePerPulse);
    /*Sets current limits for the drivetrain motors. This helps reduce the likelihood of wheel spin, reduces motor heating
     *at stall (Drivetrain pushing against something) and helps maintain battery voltage under heavy demand */
    leftFront.setSmartCurrentLimit(kCurrentLimit);
    leftRear.setSmartCurrentLimit(kCurrentLimit);
    rightFront.setSmartCurrentLimit(kCurrentLimit);
    rightRear.setSmartCurrentLimit(kCurrentLimit);

    // Set the rear motors to follow the front motors.
    leftRear.follow(leftFront);
    rightRear.follow(rightFront);

    // Invert the left side so both side drive forward with positive motor outputs
    leftFront.setInverted(true);
    rightFront.setInverted(false);

    // Put the front motors into the differential drive object. This will control all 4 motors with
    // the rears set to follow the fronts
    m_drivetrain = new DifferentialDrive(leftFront, rightFront);

    
  }

  /*Method to control the drivetrain using arcade drive. Arcade drive takes a speed in the X (forward/back) direction
   * and a rotation about the Z (turning the robot about it's center) and uses these to control the drivetrain motors */
  public void arcadeDrive(double speed, double rotation) {
    m_drivetrain.arcadeDrive(speed, rotation);
  }

  @Override
  public void periodic() {
    /*This method will be called once per scheduler run. It can be used for running tasks we know we want to update each
     * loop such as processing sensor data. Our drivetrain is simple so we don't have anything to put here */
  }

   /**
     * Creates a command to drive the robot in arcade mode for a specified duration.
     *
     * @param speed The speed at which to drive the robot.
     * @param rotation The rotation rate for the robot.
     * @param timeout The duration for which the command should run.
     * @return A new command that drives the robot in arcade mode for the specified duration.
     */
    public Command createArcadeDriveCommand(double speed, double rotation, double timeout) {
        return new RunCommand(() -> this.arcadeDrive(speed, rotation), this)
                .withTimeout(timeout);
    }
  
  public Command sysIdQuasistatic (SysIdRoutine.Direction direction){
    return m_sysIdRoutine.quasistatic(direction);
  }

  public Command sysIdDynamic(SysIdRoutine.Direction direction){
    return m_sysIdRoutine.dynamic(direction);
  }
}
