/* package LemonCode.LemonPrograms2020;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ColorSensor;


@Autonomous

public class LemonTest extends LinearOpMode {
    
    public DcMotor lBack;
    public DcMotor lFront;
    public DcMotor rBack;
    public DcMotor rFront;
    
    public void runOpMode() {
    
    lBack = hardwareMap.get(DcMotor.class, "leftBack");
    lBack.setDirection(DcMotor.Direction.REVERSE);
    lFront = hardwareMap.get(DcMotor.class, "leftFront");
    lFront.setDirection(DcMotor.Direction.REVERSE);
    rBack = hardwareMap.get(DcMotor.class, "rightBack");
    rFront = hardwareMap.get(DcMotor.class, "rightFront");
    
    lFront.setTargetPosition(383);
    rFront.setTargetPosition(383);
    lBack.setTargetPosition(383);
    rBack.setTargetPosition(383);
    
    lFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    rFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    lBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    rBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
}
*/
