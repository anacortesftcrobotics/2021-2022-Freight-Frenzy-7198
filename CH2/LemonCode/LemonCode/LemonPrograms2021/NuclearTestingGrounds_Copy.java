/*
attempt at motor init. Failed
package LemonCode.LemonPrograms2021;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp

public class NuclearTestingGrounds_Copy extends OpMode{
    public DcMotor motorInit(String motorName, boolean reverse) {
        private DcMotor motor;
        motor = hardwareMap.get(DcMotor.class, motorName);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        if (reverse == true) {
            motor.setDirection(DcMotor.Direction.REVERSE);
        }
        return motor;
    }
    public DcMotor grabbyGrabby;
    public void init(){
        grabbyGrabby = motorInit("grabbyGrabby",false);
    }
    public void loop() {
        //doot doot doot
    }
}
*/