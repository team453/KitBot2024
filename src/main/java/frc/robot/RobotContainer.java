package frc.robot;

import static frc.robot.Constants.LauncherConstants.kHighLaunchFeederSpeed;
import static frc.robot.Constants.LauncherConstants.kHighLauncherSpeed;
import static frc.robot.Constants.LauncherConstants.kLowLaunchFeederSpeed;
import static frc.robot.Constants.LauncherConstants.kLowLauncherSpeed;

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
   
//Shuffleboard
    private  double operatorLauncherSpeed = 0;
    private  double driverDriveSpeed = 0;

  public RobotContainer() {
    configureBindings();
    populateAutoCommands();
    updateSpeeds();
    setupShuffleboard();
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
    new JoystickButton(m_driver, OperatorConstants.kDriverSpeedButton) // Create a new JoystickButton binding for button 9 on m_driver joystick
    .whileTrue(
        new RunCommand(
            () ->
                m_drivetrain.arcadeDrive(
                    -m_driver.getY()*driverDriveSpeed,
                    -m_driver.getTwist()*driverDriveSpeed),
            m_drivetrain));

   /*Create an inline sequence to run when the operator presses and holds the appropriate button. Run the PrepareLaunch
     * command for 1 seconds and then run the LaunchNote command */
    new JoystickButton(m_operator, OperatorConstants.kHighSpeedShootButton) // Create a new JoystickButton binding for button 9 on m_driver joystick
    .whileTrue(
        new PrepareLaunch(m_launcher, kHighLauncherSpeed) // Start with preparing the launch
            .withTimeout(LauncherConstants.kLauncherDelay) // Set the timeout for the preparation
            .andThen(new LaunchNote(m_launcher, kHighLauncherSpeed, kHighLaunchFeederSpeed)) // Follow up with launching the note
            .handleInterrupt(() -> m_launcher.stop())); // Handle any interruption by stopping the launcher

    new JoystickButton(m_operator, OperatorConstants.kLowSpeedShootButton) // Create a new JoystickButton binding for button 9 on m_driver joystick
    .whileTrue(
        new PrepareLaunch(m_launcher,kLowLauncherSpeed) // Start with preparing the launch
            .withTimeout(LauncherConstants.kLauncherDelay) // Set the timeout for the preparation
            .andThen(new LaunchNote(m_launcher, kLowLauncherSpeed, kLowLaunchFeederSpeed)) // Follow up with launching the note
            .handleInterrupt(() -> m_launcher.stop())); // Handle any interruption by stopping the launcher

    //Launch with preset speeds
     new JoystickButton(m_operator, OperatorConstants.kOperatorControlledShootButton) // Create a new JoystickButton binding for button 9 on m_driver joystick
    .whileTrue(
        new PrepareLaunch(m_launcher,operatorLauncherSpeed) // Start with preparing the launch
            .withTimeout(LauncherConstants.kLauncherDelay) // Set the timeout for the preparation
            .andThen(new LaunchNote(m_launcher, operatorLauncherSpeed, operatorLauncherSpeed)) // Follow up with launching the note
            .handleInterrupt(() -> m_launcher.stop())); // Handle any interruption by stopping the launcher


    new JoystickButton(m_operator, OperatorConstants.kIntakeButton) // Binding for trigger on m_operatorController joystick
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

  //user set launcher speed stuff
  private void setupShuffleboard() {
    ShuffleboardTab tab = Shuffleboard.getTab("SmartDashboard");

    tab.addNumber("Operator Set Launcher Speed", () -> {
        return Math.round(operatorLauncherSpeed* 1000.0) / 1000.0; // Round to 3 decimal places
    });

    tab.addNumber("Drive Speed Multiplier", () -> {
        return Math.round(driverDriveSpeed * 1000.0) / 1000.0; // Round to 3 decimal places
    });
}

private void updateSpeeds()
{
     operatorLauncherSpeed= m_operator.getRawAxis(4); // Get the value from axis 4
     //value from 0 to 1 to act as a multiplier

        driverDriveSpeed = map(m_driver.getRawAxis(4), -1.0, 1.0, 0.0, 1.0);
}

double map(double x, double in_min, double in_max, double out_min, double out_max) {
    return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
  }
}
