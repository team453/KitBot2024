package frc.robot;

import static frc.robot.Constants.LauncherConstants.kHighLaunchFeederSpeed;
import static frc.robot.Constants.LauncherConstants.kHighLauncherSpeed;
import static frc.robot.Constants.LauncherConstants.kLowLaunchFeederSpeed;
import static frc.robot.Constants.LauncherConstants.kLowLauncherSpeed;

import static frc.robot.Robot.OperatorLauncherSpeed;
import static frc.robot.Robot.DriverDriveSpeed;

import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import java.lang.reflect.Method;
import java.util.Arrays;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
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
import frc.robot.Constants.OperatorConstants;
//import frc.robot.subsystems.CANLauncher;

public class RobotContainer {
    // Subsystems
  private final CANDrivetrain m_drivetrain = new CANDrivetrain();
  private final CANLauncher m_launcher = new CANLauncher();
  private final SendableChooser<Command> autoChooser = new SendableChooser<>();

  // Controllers
  private final Joystick m_driver = new Joystick(OperatorConstants.kDriverControllerPort);
  private final Joystick m_operator = new Joystick(OperatorConstants.kOperatorControllerPort);
   

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
                    -m_driver.getTwist()*Constants.OperatorConstants.kDriverSpeedMultiplier),
            m_drivetrain));

    //When holding the trigger, use the driverDriveSpeed
    /*
    new JoystickButton(m_driver, OperatorConstants.kDriverSpeedButton) // Create a new JoystickButton binding for button 9 on m_driver joystick
    .whileTrue(
        new RunCommand(
            () ->
                m_drivetrain.arcadeDrive(
                    -m_driver.getY()*DriverDriveSpeed,
                    -m_driver.getTwist()*DriverDriveSpeed),
            m_drivetrain));*/

   /*Create an inline sequence to run when the operator presses and holds the appropriate button. Run the PrepareLaunch
     * command for 1 seconds and then run the LaunchNote command */
    new JoystickButton(m_driver, OperatorConstants.kHighSpeedShootButton) // Create a new JoystickButton binding for button 9 on m_driver joystick
    .whileTrue(
        new PrepareLaunch(m_launcher, kHighLauncherSpeed) // Start with preparing the launch
            .withTimeout(LauncherConstants.kLauncherDelay) // Set the timeout for the preparation
            .andThen(new LaunchNote(m_launcher, kHighLauncherSpeed, kHighLaunchFeederSpeed)) // Follow up with launching the note
            .handleInterrupt(() -> m_launcher.stop())); // Handle any interruption by stopping the launcher

    new JoystickButton(m_driver, OperatorConstants.kLowSpeedShootButton) // Create a new JoystickButton binding for button 9 on m_driver joystick
    .whileTrue(
        new PrepareLaunch(m_launcher,kLowLauncherSpeed) // Start with preparing the launch
            .withTimeout(LauncherConstants.kLauncherDelay) // Set the timeout for the preparation
            .andThen(new LaunchNote(m_launcher, kLowLauncherSpeed, kLowLaunchFeederSpeed)) // Follow up with launching the note
            .handleInterrupt(() -> m_launcher.stop())); // Handle any interruption by stopping the launcher

   //Launch with set speeds
new JoystickButton(m_driver, OperatorConstants.kOperatorControlledShootButton) // Create a new JoystickButton binding for button 9 on m_driver joystick
.whileTrue(
     new RunCommand(() -> SmartDashboard.putNumber("running speed", -1 * OperatorLauncherSpeed)) // Print the operator set speed
    .andThen(new PrepareLaunch(m_launcher, -1 * OperatorLauncherSpeed) // Start with preparing the launch
        .withTimeout(LauncherConstants.kLauncherDelay) // Set the timeout for the preparation
        .andThen(new LaunchNote(m_launcher, -1 * OperatorLauncherSpeed, -1 * OperatorLauncherSpeed)) // Follow up with launching the note
        .handleInterrupt(() -> m_launcher.stop()))); // Handle any interruption by stopping the launcher

    new JoystickButton(m_driver, OperatorConstants.kIntakeButton) // Binding for trigger on m_operatorController joystick
    .whileTrue(m_launcher.getIntakeCommand()); // Bind the intake command to be executed while trigger is held

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
