package frc.robot;

import static frc.robot.Constants.AutonomousTypes.kDangerAuto;
import static frc.robot.Constants.AutonomousTypes.kDefaultAuto;
import static frc.robot.Constants.AutonomousTypes.kDriveForwardAuto;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import java.lang.reflect.Method;
import java.util.Arrays;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.Constants.LauncherConstants;
import frc.robot.commands.Autos;
import frc.robot.commands.LaunchNote;
import frc.robot.commands.PrepareLaunch;
import frc.robot.subsystems.CANDrivetrain;
import frc.robot.subsystems.CANLauncher;
import frc.robot.subsystems.PWMLauncher;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
//import frc.robot.subsystems.CANLauncher;

public class RobotContainer {
  private final CANDrivetrain m_drivetrain = new CANDrivetrain();
  private final CANLauncher m_launcher = new CANLauncher();
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
                    -m_driver.getY()*Constants.OperatorConstants.kDriverSpeedMultiplier,
                    -m_driver.getX()*Constants.OperatorConstants.kDriverSpeedMultiplier),
            m_drivetrain));

   /*Create an inline sequence to run when the operator presses and holds the 9 button. Run the PrepareLaunch
     * command for 1 seconds and then run the LaunchNote command */
    new JoystickButton(m_driver, 12) // Create a new JoystickButton binding for button 9 on m_driver joystick
    .whileTrue(
        new PrepareLaunch(m_launcher) // Start with preparing the launch
            .withTimeout(LauncherConstants.kLauncherDelay) // Set the timeout for the preparation
            .andThen(new LaunchNote(m_launcher)) // Follow up with launching the note
            .handleInterrupt(() -> m_launcher.stop())); // Handle any interruption by stopping the launcher

            new JoystickButton(m_driver, 10) // Binding for button 10 on m_operatorController joystick
            .whileTrue(m_launcher.getIntakeCommand()); // Bind the intake command to be executed while button 10 is held


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
        return m_drivetrain.run(() -> {});
    }

    public String getSelectedAutoName() {
      return autoChooser.getSelected().getName();
  }

}
