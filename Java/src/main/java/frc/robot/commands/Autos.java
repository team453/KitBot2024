// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
// import frc.robot.subsystems.PWMDrivetrain;
import frc.robot.subsystems.CANDrivetrain;

public final class Autos {
  public static Command exampleRequirementAuto(CANDrivetrain drivetrain) {
    return new RunCommand(() -> drivetrain.arcadeDrive(-.5, 0), drivetrain).withTimeout(5)
        .andThen(new RunCommand(() -> drivetrain.arcadeDrive(0, 0), drivetrain).withTimeout(1))
        .andThen(new RunCommand(() -> drivetrain.arcadeDrive(0, 0.5), drivetrain).withTimeout(2))
        .andThen(new RunCommand(() -> drivetrain.arcadeDrive(0, 0), drivetrain)); 
  }

  public static Command exampleFactoryAuto(CANDrivetrain drivetrain) {
    return drivetrain.createArcadeDriveCommand(-.5, 0, 5)
            .andThen(drivetrain.createArcadeDriveCommand(0, 0, 1))
            .andThen(drivetrain.createArcadeDriveCommand(0, 0.5, 2))
            .andThen(drivetrain.createArcadeDriveCommand(0, 0, 1)); 
  }

   public static Command ourFirstAuto(CANDrivetrain drivetrain) {
    return drivetrain.createArcadeDriveCommand(-.5, 0, 1)
            .andThen(drivetrain.createArcadeDriveCommand(0, 0, 1))
            .andThen(drivetrain.createArcadeDriveCommand(0, 0.5,3))
            .andThen(drivetrain.createArcadeDriveCommand(0, 0,1))
            .andThen(drivetrain.createArcadeDriveCommand(0.4, 0,1))
            .andThen(drivetrain.createArcadeDriveCommand(-0.45, 0.45, 4))
             .andThen(drivetrain.createArcadeDriveCommand(0, 0, 1)); 
             
  }
  private Autos() {
    throw new UnsupportedOperationException("This is a utility class!");
  }
}
/*
 *
  public static Command exampleAuto(CANDrivetrain drivetrain) {
   
    return new RunCommand(() -> drivetrain.arcadeDrive(-.5, 0))
        .withTimeout(5)
        .andThen(new RunCommand(() -> drivetrain.arcadeDrive(0, 0)))
        .withTimeout(1) // run for 10 seconds
        .andThen(new RunCommand(() -> drivetrain.arcadeDrive(0, 0.5)))
         .withTimeout(2) // run for 10 seconds
        .andThen(new RunCommand(() -> drivetrain.arcadeDrive(0, 0))); 
  }

  public static Command dangerAuto(CANDrivetrain drivetrain) {

     return new RunCommand(() -> drivetrain.arcadeDrive(0, 0.5)) // 0 forward speed, maximum turning speed
        .withTimeout(10) // run for 10 seconds
        .andThen(new RunCommand(() -> drivetrain.arcadeDrive(0, 0))); 
  }

  public static Command driveforwardAuto(CANDrivetrain drivetrain) {

     return new RunCommand(() -> drivetrain.arcadeDrive(0.5, 0)) // 0 forward speed, maximum turning speed
        .withTimeout(5) // run for 5 seconds
        .andThen(new RunCommand(() -> drivetrain.arcadeDrive(0, 0))); 
  }
 */