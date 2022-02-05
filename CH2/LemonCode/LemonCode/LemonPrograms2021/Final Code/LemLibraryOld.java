package org.firstinspires.ftc.teamcode;
import java.lang.Math;
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

public class LemLibraryOld 
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
    public LemLibraryOld (HardwareMap hwMap, OpMode opModeIn, Telemetry telemetryIn)
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
    }
    
    public void strafeLeft (int inches) 
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
    }
    
    public void strafeRight (int inches) 
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
        
        /*
        poseTracker.resetAngle();
        telemetry.addData("Target:", degrees);
        telemetry.addData("Angle:", poseTracker.getAngle());
        telemetry.update();
        
        lFront.setPower(motorPower);
        lBack.setPower(motorPower);
        rFront.setPower(-motorPower);
        rBack.setPower(-motorPower);
        
        while (poseTracker.getAngle() < degrees && linearOpMode.opModeIsActive())
        {
            double correction = 1 - poseTracker.getAngle() / degrees;
            if (correction < 0.25) correction = 0.25;
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
        */
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
        
        /*
        lBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        
        poseTracker.resetAngle();
        
        lFront.setPower(-motorPower * 0.5);
        lBack.setPower(-motorPower * 0.5);
        rFront.setPower(motorPower * 0.5);
        rBack.setPower(motorPower * 0.5);
        
        while (poseTracker.getAngle() > (-degrees) && linearOpMode.opModeIsActive())
        {
            double correction = 1 - poseTracker.getAngle() / (-degrees);
            if (correction < 0.25) correction = 0.25;
            lFront.setPower(-motorPower * correction * 0.5);
            lBack.setPower(-motorPower * correction * 0.5);
            rFront.setPower(motorPower * correction * 0.5);
            rBack.setPower(motorPower * correction * 0.5);
            
            telemetry.addData("Angle:", poseTracker.getAngle());
            telemetry.addData("Correction:", correction);
            telemetry.update();
        }
        
        lFront.setPower(0);
        lBack.setPower(0);
        rFront.setPower(0);
        rBack.setPower(0);
        */
    }
    
    public void rotateRightGyro(int degrees) {
        poseTracker.resetAngle();
        telemetry.addData("Target:", degrees);
        telemetry.addData("Angle:", poseTracker.getAngle());
        telemetry.update();
        
        lFront.setPower(motorPower);
        lBack.setPower(motorPower);
        rFront.setPower(-motorPower);
        rBack.setPower(-motorPower);
        
        while (poseTracker.getAngle() < degrees && linearOpMode.opModeIsActive())
        {
            double correction = 1 - poseTracker.getAngle() / degrees;
            if (correction < 0.25) correction = 0.25;
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
    
    public void rotateLeftGyro(int degrees) {
        lBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        
        poseTracker.resetAngle();
        
        lFront.setPower(-motorPower * 0.5);
        lBack.setPower(-motorPower * 0.5);
        rFront.setPower(motorPower * 0.5);
        rBack.setPower(motorPower * 0.5);
        
        while (poseTracker.getAngle() > (-degrees) && linearOpMode.opModeIsActive())
        {
            double correction = 1 - poseTracker.getAngle() / (-degrees);
            if (correction < 0.25) correction = 0.25;
            lFront.setPower(-motorPower * correction * 0.5);
            lBack.setPower(-motorPower * correction * 0.5);
            rFront.setPower(motorPower * correction * 0.5);
            rBack.setPower(motorPower * correction * 0.5);
            
            telemetry.addData("Angle:", poseTracker.getAngle());
            telemetry.addData("Correction:", correction);
            telemetry.update();
        }
        
        lFront.setPower(0);
        lBack.setPower(0);
        rFront.setPower(0);
        rBack.setPower(0);
    }
    
    public void resetEncoder() 
    {
        lBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
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
    
    public void blockgrab(){
        armHand.setPosition(0.7);
        armControl.setPower(0.175);
        linearOpMode.sleep(100);
    }
    
    public void opengrab(){
        armHand.setPosition(0.1);
    }
    /*
    public int check()
    {
        telemetry.addLine("front: " + (touchSensorFront.getState()));
        telemetry.addLine("back: " + (touchSensorBack.getState()));
        telemetry.update();
        linearOpMode.sleep(2000);
        switchArm.setPosition(1);
        linearOpMode.sleep(500);
        if (!touchSensorBack.getState())
        {
            
            level = 1;
        }
        else if  (!touchSensorFront.getState())
        {
            level = 2;

        }
        else if  (touchSensorBack.getState() && touchSensorFront.getState())
        {
            level = 3;
        }
        telemetry.addLine("front: " + (touchSensorFront.getState()));
        telemetry.addLine("back: " + (touchSensorBack.getState()));
        telemetry.update();
        linearOpMode.sleep(800);
        switchArm.setPosition(0);
        linearOpMode.sleep(800);
        return level;
    }
    */
    public void depositUp()
    {
        switchArm.setPosition(0);
        linearOpMode.sleep(300);
         
        if (level == 1) 
        {

                    
            strafeRight(10);
        
            rotateRight(6);
            forward(14);
            armControl.setPower(0.25);
            linearOpMode.sleep(40);
            strafeLeft(10);
            opengrab();
            linearOpMode.sleep(500);
            strafeRight(10);
            rotateLeft(6);
            backward(40);
            
        }
        else if (level == 2) 
        {
            strafeRight(10);
            armControl.setPower(0.8);
            linearOpMode.sleep(200);
            rotateRight(6);
            forward(14);
            strafeLeft(10);
            opengrab();
            linearOpMode.sleep(500);
            strafeRight(10);
            rotateLeft(6);
            backward(40);
        }
        else if (level == 3) 
        {
            strafeRight(10);

        }
        else if (level == 0)
        {
            //nothing
        }
    }

    public void armLiftTest(int a)
    {
        resetEncoder();
        
        armControl.setTargetPosition(a);
        /*
        about 500 is top 
        
        700 hits the floor
        */
        
        armControl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        
        armControl.setPower(0.75);
        
        linearOpMode.sleep(Math.abs(a*7));
    }
}
