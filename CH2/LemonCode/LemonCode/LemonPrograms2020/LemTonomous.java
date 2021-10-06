package LemonCode.LemonPrograms2020;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.robotcore.external.Telemetry;

@Autonomous (name = "LemTonomous", group = "LemonPrograms")

public class LemTonomous extends LinearOpMode {
    //variables go here
    /*
    int ringNum
    */
    private Lembot2 bob;
    
    public void runOpMode() {
        bob = new Lembot2(hardwareMap, this, telemetry);
        bob.init();
        waitForStart();
        //notes 
        /*
         - when strafing, loses about 1 inch over 20 inches
         - depending on weight, the robot may drift
         - most sub-assembly methods are empty. Working on fixing that.
         - diagonal is untested, I think it unlikely that it will go the exact 
         number of inches commanded.
        */
        //work area
        
        bob.forward(2);
        
    }
}
