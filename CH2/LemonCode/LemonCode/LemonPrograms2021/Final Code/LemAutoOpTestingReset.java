package org.firstinspires.ftc.teamcode;
import java.lang.Math;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Gyroscope;

@Autonomous
@Disabled

public class LemAutoOpTestingReset extends LinearOpMode 
{
    private Blinker control_Hub;
    private Blinker expansion_Hub_1;
    private HardwareDevice webcam_1;
    private DcMotor armControl;
    private Servo armHand;
    private Servo carousel;
    private Servo switchArm;
    private DistanceSensor leftColor;
    private DistanceSensor rightColor;
    private Gyroscope imu;
    private DcMotor leftBack;
    private DcMotor leftFront;
    private DcMotor rightBack;
    private DcMotor rightFront;
    private LemLibrary robob;
    public int level = 0;

    public void runOpMode() 
    {
        robob = new LemLibrary(hardwareMap, this, telemetry);
        robob.init();
        waitForStart();
        
        robob.resetArm();
        
        robob.allStop();
        robob.keepRunning(60000);
        //basically just Lemtonomous. just a spot for writing out commands from lemlibrary
    }
}
