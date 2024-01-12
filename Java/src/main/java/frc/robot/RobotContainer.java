package frc.robot;

import static frc.robot.Constants.AutonomousTypes.kDangerAuto;
import static frc.robot.Constants.AutonomousTypes.kDefaultAuto;
import static frc.robot.Constants.AutonomousTypes.kDriveForwardAuto;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.commands.Autos;
import frc.robot.subsystems.CANDrivetrain;
//import frc.robot.subsystems.CANLauncher;

public class RobotContainer {
  private final CANDrivetrain m_drivetrain = new CANDrivetrain();
  //private final CANLauncher m_launcher = new CANLauncher();

  // Assuming these port numbers are correct for your setup.
  private final Joystick m_driver = new Joystick(0);
  private final Joystick m_operator = new Joystick(1);

  public RobotContainer() {
    configureBindings();
  }
  
  private void configureBindings() {
    m_drivetrain.setDefaultCommand(
        new RunCommand(
            () ->
                m_drivetrain.arcadeDrive(
                    -m_driver.getY(), // Assuming getY() and getX() are correct methods
                    -m_driver.getX()),
            m_drivetrain));

    // Commenting out the following as m_operatorController is not defined
    /*
    m_operatorController
        .a()
        .whileTrue(
            new PrepareLaunch(m_launcher)
                .withTimeout(LauncherConstants.kLauncherDelay)
                .andThen(new LaunchNote(m_launcher))
                .handleInterrupt(() -> m_launcher.stop()));

    m_operatorController.leftBumper().whileTrue(m_launcher.getIntakeCommand());
    */
  }

  public Command getAutonomousCommand(String type) {
    if(type == kDefaultAuto)
    {
return Autos.exampleAuto(m_drivetrain);
    }

    else if(type == kDriveForwardAuto)
    {
      return Autos.driveforwardAuto(m_drivetrain);
    }

    else if(type == kDangerAuto)
    {
       return Autos.dangerAuto(m_drivetrain);
    }

    //send out an error
    System.out.println("AUTO NOT DEFINED OF TYPE: " + type);
    return null;
  }


}
