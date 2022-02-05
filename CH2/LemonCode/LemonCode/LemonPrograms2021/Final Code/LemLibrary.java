package org.firstinspires.ftc.teamcode;

import java.lang.Math;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import java.util.Locale;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.hardware.HardwareMap;
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
level3 - arm goes to level 3 on the hub
allStop - the drive motors all stop attempting to do anything.
keepRunning - keeps the program open for a minute. Good for looking at telemetry.
rotateRightGyro - turns with imu
rotateLeftGyro - turns with imu
resetArm() - resets arm to bottom

Auto Naming Convention:

LemAuto[Color][Position][Task1][Task2][Task...][Points]

Tasks:
Duck Delivery
Park in storage
Park completely in storage
Park in warehouse
Park completely in warehouse
Freight in storage
Freight in hub
Preload on hub
TSL on hub
*/
public class LemLibrary 
{
    //variables
    boolean grabbed = false;
    public static int tpi = 62;
    public int level = 0;
    public static double motorPower = 0.5;
    
    //weird init stuff
    public static HardwareMap hardwareMap;
    public LinearOpMode linearOpMode;
    public static Telemetry telemetry;
    public LemLibrary (HardwareMap hwMap, OpMode opModeIn, Telemetry telemetryIn)
    {
        hardwareMap = hwMap;
        telemetry = telemetryIn;
        if (opModeIn instanceof LinearOpMode) 
        {
            linearOpMode = (LinearOpMode) opModeIn;
        }
    }
    //actual hardware init
    private Blinker control_Hub;
    private Blinker expansion_Hub_1;
    private HardwareDevice webcam_1;
    private DcMotor armControl;
    private Servo armHand;
    private Servo carousel;
    private Servo switchArm;
    private DcMotor lBack;
    private DcMotor lFront;
    private DcMotor rBack;
    private DcMotor rFront;
    private DistanceSensor leftColor;
    private DistanceSensor rightColor;
    BNO055IMU imu;
    PoseTracker poseTracker;
    
    private DigitalChannel armStop;
    
    
    public void init() 
    {
        //Drive Motors
        lBack = hardwareMap.get(DcMotor.class, "leftBack");
        lBack.setDirection(DcMotor.Direction.REVERSE);
        lBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        lFront = hardwareMap.get(DcMotor.class, "leftFront");
        lFront.setDirection(DcMotor.Direction.REVERSE);
        lFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        rBack = hardwareMap.get(DcMotor.class, "rightBack");
        rBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        
        rFront = hardwareMap.get(DcMotor.class, "rightFront");
        rFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        
        //Sub-Assembly Motors 
        armControl = hardwareMap.get(DcMotor.class, "armControl");
        
        //Sub-Assembly Servos
        armHand = hardwareMap.get(Servo.class, "armHand");
        carousel = hardwareMap.get(Servo.class, "carousel");
        switchArm = hardwareMap.get(Servo.class, "switchArm");
        
        //Sub-Assembly Sensors
        leftColor = hardwareMap.get(DistanceSensor.class, "leftColor");
        rightColor = hardwareMap.get(DistanceSensor.class, "rightColor");
        
        //Start Position Stuff 
        switchArm.setPosition(0);
        
        //IMU Stuff 
        // Construct a Parameters object and modify from default
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.mode = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled = false;

        // Map imu to hardware
        imu = hardwareMap.get(BNO055IMU.class, "imu");

        // Initialize IMU
        imu.initialize(parameters);
        poseTracker = new PoseTracker(imu);
        
        //armStop
        armStop = hardwareMap.get(DigitalChannel.class, "armStop");
    }
    
    public void resetEncoder() 
    {
        lBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armControl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
    
    public void forward (int inches) 
    {
        //moves forward x inches
        int x = inches;
        resetEncoder();
        
        lFront.setTargetPosition(x*tpi);
        lBack.setTargetPosition(x*tpi);
        rFront.setTargetPosition(x*tpi);
        rBack.setTargetPosition(x*tpi);
        
        lFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        
        lFront.setPower(motorPower);
        lBack.setPower(motorPower);
        rFront.setPower(motorPower);
        rBack.setPower(motorPower);
        
        linearOpMode.sleep(x*75);
        
        lFront.setPower(0);
        lBack.setPower(0);
        rFront.setPower(0);
        rBack.setPower(0);
    }
    
    public void backward (int inches) 
    {
        //moves backwards x inches
        int x = inches;
        resetEncoder();
        
        lFront.setTargetPosition(-x*tpi);
        lBack.setTargetPosition(-x*tpi);
        rFront.setTargetPosition(-x*tpi);
        rBack.setTargetPosition(-x*tpi);
        
        lFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        
        lFront.setPower(-motorPower);
        lBack.setPower(-motorPower);
        rFront.setPower(-motorPower);
        rBack.setPower(-motorPower);
        
        linearOpMode.sleep(x*75);
        
        lFront.setPower(0);
        lBack.setPower(0);
        rFront.setPower(0);
        rBack.setPower(0);
    }
    
    public void strafeRight (int inches) 
    {
        //moves left x inches
        int x = inches;
        resetEncoder();
        
        lFront.setTargetPosition(x*tpi);
        lBack.setTargetPosition(-x*tpi);
        rFront.setTargetPosition(-x*tpi);
        rBack.setTargetPosition(x*tpi);
        
        lFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        
        lFront.setPower(motorPower);
        lBack.setPower(-motorPower);
        rFront.setPower(-motorPower);
        rBack.setPower(motorPower);
        
        linearOpMode.sleep(x*75);
        
        lFront.setPower(0);
        lBack.setPower(0);
        rFront.setPower(0);
        rBack.setPower(0);
    }
    
    public void strafeLeft (int inches) 
    {
        //moves right x inches
        int x = inches;
        resetEncoder();
        
        lFront.setTargetPosition(-x*tpi);
        lBack.setTargetPosition(x*tpi);
        rFront.setTargetPosition(x*tpi);
        rBack.setTargetPosition(-x*tpi);
        
        lFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        
        lFront.setPower(-motorPower);
        lBack.setPower(motorPower);
        rFront.setPower(motorPower);
        rBack.setPower(-motorPower);
        
        linearOpMode.sleep(x*75);
        
        lFront.setPower(0);
        lBack.setPower(0);
        rFront.setPower(0);
        rBack.setPower(0);
    }
    
    public void rotateRight (int degrees)
    {
        int x = degrees;
        lBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        
        lFront.setPower(motorPower);
        lBack.setPower(motorPower);
        rFront.setPower(-motorPower);
        rBack.setPower(-motorPower);
        
        linearOpMode.sleep(Math.abs(x * 75));
        
        lFront.setPower(0);
        lBack.setPower(0);
        rFront.setPower(0);
        rBack.setPower(0);
    }
    
    public void rotateLeft(int degrees)
    {
        int x = degrees;
        lBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        
        lFront.setPower(-motorPower);
        lBack.setPower(-motorPower);
        rFront.setPower(motorPower);
        rBack.setPower(motorPower);
        
        linearOpMode.sleep(Math.abs(x * 75));
        
        lFront.setPower(0);
        lBack.setPower(0);
        rFront.setPower(0);
        rBack.setPower(0);
    }
    
    public void carouselMove(int x,int y)
    {
        carousel.setPosition(y);
        linearOpMode.sleep(Math.abs(x));
        carousel.setPosition(0.5);
    }
    
    public void grab()
    {
        if (!grabbed)
        {
            armHand.setPosition(0.5);
            grabbed = true;
            linearOpMode.sleep(100);
        }
        else 
        {
            armHand.setPosition(0.7);
            grabbed = false;
            linearOpMode.sleep(100);
        }
    }
    
    public void armTest(int a)
    {
        resetEncoder();
        armControl.setTargetPosition(a);
        armControl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        armControl.setPower(0.5);
        linearOpMode.sleep(Math.abs(a*7));
    }
    
    public int check (int d,int t)
    {   
        int i;
        for (i = t;i > 0;i--) 
        {
            if (d < 0)
            {
                strafeLeft(1);
            } 
            else 
            {
                strafeRight(1);
            }
            if (rightColor.getDistance(DistanceUnit.INCH) <= 3) 
            {
                level = 1;
                telemetry.addData("level",level);
                telemetry.update();
                i = 0;
            } 
            else if (leftColor.getDistance(DistanceUnit.INCH) <= 3)
            {
                level = 2;
                telemetry.addData("level",level);
                telemetry.update();
                i = 0;
            } 
            else if (leftColor.getDistance(DistanceUnit.INCH) >= 3 && rightColor.getDistance(DistanceUnit.INCH) >= 3) 
            {
                level = 3;
                telemetry.addData("level",level);
                telemetry.update();
            }
        }
        return level;
    }
    public void level1 ()
    {
        resetEncoder();
        armControl.setTargetPosition(620);
        armControl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        armControl.setPower(0.5);
    }
    public void level2 ()
    {
        resetEncoder();
        armControl.setTargetPosition(570);
        armControl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        armControl.setPower(0.5);
    }
    public void level3 ()
    {
        resetEncoder();
        armControl.setTargetPosition(20);
        armControl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        armControl.setPower(0.5);
    }
    public void keepRunning (int time) 
    {
        linearOpMode.sleep(Math.abs(time));
    }
    public void allStop () 
    {
        linearOpMode.sleep(500);
        resetEncoder();
        
        lBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lFront.setPower(0);
        lBack.setPower(0);
        rFront.setPower(0);
        rBack.setPower(0);
    }
    
    //HMMM
    
    public void rotateRightGyro (int degree) {
        int degrees = degree - 3;
        resetEncoder();
        poseTracker.resetAngle();
        
        telemetry.addData("Target:", degrees);
        telemetry.addData("Angle:", poseTracker.getAngle());
        telemetry.update();
        
        lBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        
        lFront.setPower(motorPower);
        lBack.setPower(motorPower);
        rFront.setPower(-motorPower);
        rBack.setPower(-motorPower);
        
        while ((poseTracker.getAngle() > (-degrees)) && (linearOpMode.opModeIsActive()))
        {
            double correction = 1 - Math.abs(poseTracker.getAngle() / degrees);
            if (correction < 0.45) correction = 0.45;
            lFront.setPower(motorPower * correction);
            lBack.setPower(motorPower * correction);
            rFront.setPower(-motorPower * correction);
            rBack.setPower(-motorPower * correction);
            
            telemetry.addData("Target:", degrees);
            telemetry.addData("Angle:", poseTracker.getAngle());
            telemetry.addData("Correction:", correction);
            telemetry.update();
        }
        
        lFront.setPower(0);
        lBack.setPower(0);
        rFront.setPower(0);
        rBack.setPower(0);
    }
    
    public void rotateLeftGyro(int degree) {
        int degrees = degree - 3;
        resetEncoder();
        poseTracker.resetAngle();
        
        telemetry.addData("Target:", degrees);
        telemetry.addData("Angle:", poseTracker.getAngle());
        telemetry.update();
        
        lBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        
        lFront.setPower(-motorPower);
        lBack.setPower(-motorPower);
        rFront.setPower(motorPower);
        rBack.setPower(motorPower);
        
        while ((poseTracker.getAngle() < degrees) && (linearOpMode.opModeIsActive()))
        {
            double correction = 1 - Math.abs(poseTracker.getAngle() / degrees);
            if (correction < 0.45) correction = 0.45;
            lFront.setPower(-motorPower * correction);
            lBack.setPower(-motorPower * correction);
            rFront.setPower(motorPower * correction);
            rBack.setPower(motorPower * correction);
            
            telemetry.addData("Target:", degrees);
            telemetry.addData("Angle:", poseTracker.getAngle());
            telemetry.addData("Correction:", correction);
            telemetry.update();
        }
        
        lFront.setPower(0);
        lBack.setPower(0);
        rFront.setPower(0);
        rBack.setPower(0);
    }   
    public void resetArm ()
    {
        resetEncoder();
        armControl.setTargetPosition(-400);
        armControl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        armControl.setPower(0.75);
        linearOpMode.sleep(Math.abs(400*7));
    }
    public void moveArm(int a,int b)
    {
        resetEncoder();
        armControl.setTargetPosition(a);
        armControl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        armControl.setPower(0.5);
        linearOpMode.sleep(Math.abs(b));
    }
}
