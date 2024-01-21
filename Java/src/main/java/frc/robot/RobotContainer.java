package frc.robot;

import java.lang.reflect.Method;
import java.util.Arrays;

import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.*;
import edu.wpi.first.wpilibj2.command.*;
import frc.robot.commands.*;
import frc.robot.commands.LimeLightVision.*;
import frc.robot.subsystems.*;
import frc.robot.Constants.*;


public class RobotContainer {
    //subsystems
  private final CANDrivetrain m_drivetrain = new CANDrivetrain();
  private final LimeLight m_limelight = new LimeLight();
 //private final CANLauncher m_launcher = new CANLauncher();

  //commands
  private final AlignWithTag m_align = new AlignWithTag(m_drivetrain, m_limelight);
    private final MoveWithTag m_move = new MoveWithTag(m_limelight, m_drivetrain);

  //shuffleboard 
  private final SendableChooser<Command> autoChooser = new SendableChooser<>();


  private final Joystick m_driver = new Joystick(Constants.OIConstants.kDriverControllerPort);
  private final Joystick m_operator = new Joystick(Constants.OIConstants.kOperatorControllerPort);

  public RobotContainer() {
     m_drivetrain.setDefaultCommand(
        new RunCommand(
            () -> m_drivetrain.arcadeDrive(
                    -m_driver.getY() * Constants.OIConstants.kDriverSpeedMultiplier, 
                    -m_driver.getX() * Constants.OIConstants.kDriverSpeedMultiplier),
            m_drivetrain));
    configureBindings();
    populateAutoCommands();
  }
  
private void configureBindings() {
    //Move with the vision target
   final JoystickButton autoMoveButton = new JoystickButton(m_driver, OIConstants.kDriverAutoMoveButton);        
    autoMoveButton.onTrue(m_move);   
    autoMoveButton.onFalse(
        new RunCommand(
            () -> m_drivetrain.arcadeDrive(
                    -m_driver.getY() * Constants.OIConstants.kDriverSpeedMultiplier, 
                    -m_driver.getX() * Constants.OIConstants.kDriverSpeedMultiplier),
            m_drivetrain));   
  
    //Align with the vision target
    final JoystickButton autoAlignButton = new JoystickButton(m_driver, OIConstants.kDriverAlignmentButton);
    autoAlignButton.onTrue(m_align);
    autoAlignButton.onFalse(
        new RunCommand(
            () -> m_drivetrain.arcadeDrive(
                    -m_driver.getY() * Constants.OIConstants.kDriverSpeedMultiplier, 
                    -m_driver.getX() * Constants.OIConstants.kDriverSpeedMultiplier),
            m_drivetrain));
}
            

   

    // Commented out the following as we do not have a launcher yet.
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
