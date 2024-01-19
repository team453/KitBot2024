package frc.robot;

import static frc.robot.Constants.AutonomousTypes.kDangerAuto;
import static frc.robot.Constants.AutonomousTypes.kDefaultAuto;
import static frc.robot.Constants.AutonomousTypes.kDriveForwardAuto;

import java.lang.reflect.Method;
import java.util.Arrays;

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
  private final SendableChooser<Command> autoChooser = new SendableChooser<>();
  //private final CANLauncher m_launcher = new CANLauncher();

  // Assuming these port numbers are correct for your setup.
  private final Joystick m_driver = new Joystick(Constants.OperatorConstants.kDriverControllerPort);
  private final Joystick m_operator = new Joystick(Constants.OperatorConstants.kOperatorControllerPort);

  public RobotContainer() {
    configureBindings();
    populateAutoCommands();
  }
  
  private void configureBindings() {
    m_drivetrain.setDefaultCommand(
        new RunCommand(
            () ->
                m_drivetrain.arcadeDrive(
                    -m_driver.getY()*Constants.OperatorConstants.kDriverSpeedMultiplier, // Assuming getY() and getX() are correct methods
                    -m_driver.getX()*Constants.OperatorConstants.kDriverSpeedMultiplier),
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
 private void populateAutoCommands() {
        Method[] methods = Autos.class.getDeclaredMethods();
        Arrays.stream(methods)
              .filter(method -> Command.class.isAssignableFrom(method.getReturnType()) && method.getParameterCount() == 1)
              .forEach(method -> {
                  try {
                      Command command = (Command) method.invoke(null, m_drivetrain);
                      autoChooser.addOption(method.getName(), command);
                  } catch (Exception e) {
                      e.printStackTrace();
                  }
              });
        SmartDashboard.putData("Auto Mode", autoChooser);
    }

    public Command getAutonomousCommand() {
        return autoChooser.getSelected();
    }

    public String getSelectedAutoName() {
      return autoChooser.getSelected().getName();
  }

}
