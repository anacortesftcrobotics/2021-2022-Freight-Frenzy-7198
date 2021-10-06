/*
package LemonCode.LemonPrograms2020;
import java.lang.Math;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
import com.qualcomm.hardware.bosch.BNO055IMU;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


public class Lembot {
        public static DcMotor lBack;
        public static DcMotor lFront;
        public static DcMotor rBack;
        public static DcMotor rFront;
        public static DcMotor move;
        public static DcMotor collector;
        public static DcMotor shooter;
        public static Servo grab;
        public static Servo basket;
        public static Servo ringTouchArm;
        public static HardwareMap hardwareMap;
        //imu stuff
        BNO055IMU imu;
    public static void motorChange () {
        //imu stuff
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        
        parameters.mode = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled = false;
        
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        
        imu.initialize(parameters);
        
        while(!linearOpMode.isStopRequested() && !imu.isGyroCalibrated()) {
            sleep(50);
            idle();
        }
        
        //makes changes to motors, meant for runOpMode
        //drive motor changes
        lBack = hardwareMap.get(DcMotor.class, "leftBack");
        lBack.setDirection(DcMotor.Direction.REVERSE);
        lFront = hardwareMap.get(DcMotor.class, "leftFront");
        lFront.setDirection(DcMotor.Direction.REVERSE);
        rBack = hardwareMap.get(DcMotor.class, "rightBack");
        rFront = hardwareMap.get(DcMotor.class, "rightFront");
        lBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //arm changes
        move = hardwareMap.get(DcMotor.class, "move");
        move.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        move.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        move.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        grab = hardwareMap.get(Servo.class, "grab");
        //shooter changes
        collector = hardwareMap.get(DcMotor.class, "collector");
        collector.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        collector.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        collector.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        shooter = hardwareMap.get(DcMotor.class, "shooter");
        shooter.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        shooter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        basket = hardwareMap.get(Servo.class, "basket");
        //toucher arm
        ringTouchArm = hardwareMap.get(Servo.class, "ringTouchArm");
        //imu stuff
        
        
    }
    public static void forward (double inches) {
        //moves forward x inches
        int power = 62 * inches;
        lFront.setTargetPosition((int)(power));
        lBack.setTargetPosition((int)(power));
        rFront.setTargetPosition((int)(power));
        rBack.setTargetPosition((int)(power));
        //sleep((int)(250 * inches));
        resetEncoder();
    }
    public static void backward (double inches) {
        //moves backwards x inches
        lFront.setTargetPosition((int)(-power));
        lBack.setTargetPosition((int)(-power));
        rFront.setTargetPosition((int)(-62 * inches));
        rBack.setTargetPosition((int)(-62 * inches));
        //sleep((int)(250 * inches));
        resetEncoder();
    }
    public static void strafeLeft (double inches) {
        //moves left x inches
        lFront.setTargetPosition((int)(-62 * inches));
        lBack.setTargetPosition((int)(62 * inches));
        rFront.setTargetPosition((int)(62 * inches));
        rBack.setTargetPosition((int)(-62 * inches));
        //sleep((int)(250 * inches));
        resetEncoder();
    }
    public static void strafeRight (double inches) {
        //moves right x inches
        lFront.setTargetPosition((int)(62 * inches));
        lBack.setTargetPosition((int)(-62 * inches));
        rFront.setTargetPosition((int)(-62 * inches));
        rBack.setTargetPosition((int)(62 * inches));
        //sleep((int)(250 * inches));
        resetEncoder();
    }
    public static void flDiag (double inches) {
        //moves forward left diagonally x inches
        lFront.setTargetPosition(0);
        lBack.setTargetPosition((int)(62 * inches));
        rFront.setTargetPosition((int)(62 * inches));
        rBack.setTargetPosition(0);
        //sleep((int)(250 * inches));
        resetEncoder();
    }
    public static void frDiag (double inches) {
        //moves forward right diagonally x inches
        lFront.setTargetPosition((int)(62 * inches));
        lBack.setTargetPosition(0);
        rFront.setTargetPosition(0);
        rBack.setTargetPosition((int)(62 * inches));
        //sleep((int)(250 * inches));
        resetEncoder();
    }
    public static void blDiag (double inches) {
        //moves backwards left diagonally x inches
        lFront.setTargetPosition((int)(-62 * inches));
        lBack.setTargetPosition(0);
        rFront.setTargetPosition(0);
        rBack.setTargetPosition((int)(-62 * inches));
        //sleep((int)(250 * inches));
        resetEncoder();
    }
    public static void brDiag (double inches) {
        //moves backwards right diagonally x inches
        lFront.setTargetPosition(0);
        lBack.setTargetPosition((int)(-62 * inches));
        rFront.setTargetPosition((int)(-62 * inches));
        rBack.setTargetPosition(0);
        //sleep((int)(250 * inches));
        resetEncoder();
    }
    public static void turnRight (double degree) {
        //turns right x degrees

    }
    public static void turnLeft (double degree) {
        //turns left x degrees

    }
    public static void grab () {
        // grabs the wobble goal or sets a wobble goal down. Must already be positioned.
        //starting position is grabbed.
    }
    public static void setDown () {
        //sets the wobble goal down. Must already be positioned.
    }
    public static void check () {
        //uses the limiter switch arm to check the number of rings. 
        //returns an int for the height, 0 = 1, 1 = 2, 4 = 3.
    }
    public static void shoot () {
        //shoot all rings in storage
    }
    public static void reverse () {
        //reset all commands thus forth
        //I do not know how to do this yet
    }
    public static void resetEncoder() {
        lBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
}
*/
/*
wheel circumference: 314.15 MM
motor tick thingy: 767.2 T
mm per inch is: 25.4
so, ~62 ticks per inch
*/
