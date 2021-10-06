package LemonCode.LemonPrograms2021;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@TeleOp

public class TestingTeleOp extends OpMode {
    public DcMotor leftDrive;
    public DcMotor rightDrive;
    public DcMotor grabbyGrabby;
    public DcMotor armControl;
    
    double leftDrivePower = 0;
    double rightDrivePower = 0;
        
    public void init() {
        //motors
        leftDrive = hardwareMap.get(DcMotor.class, "leftDrive");
        rightDrive = hardwareMap.get(DcMotor.class, "rightDrive");
        grabbyGrabby = hardwareMap.get(DcMotor.class, "grabbyGrabby");
        armControl = hardwareMap.get(DcMotor.class, "armControl");
        
        //motor changes
        leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        grabbyGrabby.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armControl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        grabbyGrabby.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        armControl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        grabbyGrabby.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        armControl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftDrive.setDirection(DcMotor.Direction.REVERSE);
    }
    public void controller1 () {
        leftDrivePower = 0;
        rightDrivePower = 0;
        if (-gamepad1.left_stick_y > 0.1) {
            rightDrivePower = (0.5);
            leftDrivePower = (0.5);
        }
        if (-gamepad1.left_stick_y < -0.1) {
            rightDrivePower = (-0.5);
            leftDrivePower = (-0.5);
        }
        if (-gamepad1.left_stick_y > -0.1 && -gamepad1.left_stick_y < 0.1) {
            rightDrivePower = (0.0);
            leftDrivePower = (0.0);
        }
        leftDrive.setPower(leftDrivePower);
        rightDrive.setPower(rightDrivePower);
        }
    public void loop () {
        controller1 ();
        telemetry();
        grabbyGrabby.setPower(0);
        armControl.setPower(0);
    }
    public void telemetry () {
    telemetry.addData("leftDrive", leftDrivePower);
    telemetry.addData("rightDrive", rightDrivePower);
    telemetry.addData("left_stick_y", -gamepad1.left_stick_y);
    telemetry.update();
    }
}
