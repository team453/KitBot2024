package frc.robot.commands.LimeLightVision;


import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.LimeLight;
import frc.robot.Constants.DrivetrainConstants;
import frc.robot.subsystems.CANDrivetrain;


public class MoveWithTag extends Command{
    @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
    private final LimeLight m_LimeLight;
    private final CANDrivetrain m_Drivetrain;
  
    /**
     * Creates a new AutoDrive.
     *
     * @param subsystem The subsystem used by this command.
     */
    public MoveWithTag(LimeLight subsystem1, CANDrivetrain subsystem2) {
        m_LimeLight = subsystem1;
      // Use addRequirements() here to declare subsystem dependencies.
      addRequirements(subsystem1);

      m_Drivetrain = subsystem2;

      addRequirements(subsystem2);
    }
  
    // Called when the command is initially scheduled.
    @Override
    public void initialize() {}
  
    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        // Check if there's a valid target
        if (!m_LimeLight.m_ValidTarget) {
            return; // No valid target, do nothing
        }
    
        double turnAdjust = 0.0;
        double moveAdjust = 0.0;
        double desiredTa = 1; /* set the desired 'ta' value that corresponds to 3 feet distance */;
    
        // Proportional control for turning
        if (m_LimeLight.tx > 0) {
            // Robot needs to turn right
            turnAdjust = -DrivetrainConstants.k_AutoCorrectTurn;
        } else if (m_LimeLight.tx < 0) {
            // Robot needs to turn left
            turnAdjust = DrivetrainConstants.k_AutoCorrectTurn;
        }
    
        // Proportional control for moving
        if (m_LimeLight.ta < desiredTa) {
            // Robot is too far, needs to move forward
            moveAdjust = DrivetrainConstants.k_AutoCorrectSpeed;
        } else if (m_LimeLight.ta > desiredTa) {
            // Robot is too close, needs to move backward
            moveAdjust = -DrivetrainConstants.k_AutoCorrectSpeed;
        }
    
        // Command the drivetrain to adjust position and orientation
        SmartDashboard.putNumber("move", moveAdjust);
        SmartDashboard.putNumber("turn", turnAdjust);
        m_Drivetrain.arcadeDrive(moveAdjust, turnAdjust);
    }
    

  
    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {}
  
    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
      return false;
    }
}