package org.firstinspires.ftc.teamcode;
import java.lang.Math;
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

@Autonomous (name = "LemAutoOpRed2RHPW", group = "Autos")

public class LemAutoOpRed2RHPW extends LinearOpMode 
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
    
    /*
    Function Library:

    init - intializes all the motors and the like. Must be run at start
    reset encoder - sets encoders on the motors back to zero. Only used within other functions
    forward - Moves the robot forward x amount.
    backward- Moves the robot backward x amount.
    strafeLeft - Moves the robot to the left x amount without rotating.
    strafeRight - Moves the robot to the left x amount without rotating.
    turnLeft - Turns the robot x amount to the left
    turnRight - Turns the robot x amount to the right
    armTest - Used for testing the arm
    check - checks where the team element is
    carouselMove - runs the carousel. Direction and length must be specified
    grab - makes the hand close or open when run.
    level1 - arm goes to level 1 on the hub
    level2 - arm goes to level 2 on the hub
    l  evel3 - arm goes to level 3 on the hub
    allStop - the drive motors all stop attempting to do anything.
    keepRunning - keeps the program open for a minute. Good for looking at telemetry.
    rotateRightGyro - turns with imu
    rotateLeftGyro - turns with imu
    */
    
    public void runOpMode() 
    {
        robob = new LemLibrary(hardwareMap, this, telemetry);
        robob.init();
        waitForStart();
        
        robob.grab();
        robob.grab();
        robob.backward(11);
        robob.strafeRight(3);
        level = robob.check(-1,20);
        robob.rotateLeftGyro(45);
        robob.forward(2);
        if (level == 1)
        {
            //1
            robob.rotateRightGyro(180);
            robob.armTest(40);
            robob.linearOpMode.sleep(Math.abs(2000));
            robob.strafeLeft(3);
            robob.forward(12);
            robob.linearOpMode.sleep(500);
            robob.grab();
            robob.backward(9);
            robob.strafeRight(3);
            robob.rotateRightGyro(90);
            robob.rotateRightGyro(45);
        }
        else if (level == 2) 
        {
            //2
            robob.rotateRightGyro(180);
            robob.armTest(100);
            robob.linearOpMode.sleep(Math.abs(2000));
            robob.forward(10);
            robob.linearOpMode.sleep(500);
            robob.grab();
            robob.backward(8);
            robob.rotateRightGyro(90);
            robob.rotateRightGyro(45);
        }
        else 
        {
            //3
            robob.armTest(530);
            robob.linearOpMode.sleep(Math.abs(2800));
            robob.backward(9);
            robob.linearOpMode.sleep(500);
            robob.grab();
            robob.moveArm(-700,1200);
            robob.forward(9);
            robob.rotateLeftGyro(45);
        }
        robob.rotateLeftGyro(180);
        robob.strafeLeft(16);
        robob.backward(25);
        
        
        robob.allStop();
        //basically just Lemtonomous. just a spot for writing out commands from lemlibrary
    }
}
