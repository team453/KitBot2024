package frc.robot.commands.LimeLightVision;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CANDrivetrain;
import frc.robot.subsystems.LimeLight;

public class AlignWithTag extends Command {

    private final CANDrivetrain drivetrain;
    private final LimeLight limelight;
    private static final double Kp = -0.1;
    private static final double min_command = 0.05;

    // Constructor
    public AlignWithTag(CANDrivetrain drivetrain, LimeLight limelight) {
        this.drivetrain = drivetrain;
        this.limelight = limelight;
        addRequirements(drivetrain);
        addRequirements(limelight);
    }


    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        // Initialization code, if any
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        
    System.out.println("Executing Align CMD");
            limelight.updateLimeLightTracking();
            double heading_error = -limelight.tx;
            System.out.println("Executing with error " + heading_error);
            double steering_adjust = 0.0;
            if (Math.abs(heading_error) > 1.0) {
                steering_adjust = Kp * heading_error + (heading_error > 0 ? -min_command : min_command);
            }
            drivetrain.arcadeDrive(0, steering_adjust);
        
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        drivetrain.arcadeDrive(0, 0); //Stop the drivetrain when the command ends
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false; // Modify this as needed based on your command's ending condition
    }
}
