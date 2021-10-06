
package LemonCode.LemonPrograms2020;
import java.lang.Math;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
import com.qualcomm.hardware.bosch.BNO055IMU;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class Lembot2 {
        public static double globalAngle;
        Orientation lastAngles = new Orientation();
    
        public static DcMotor lBack;
        public static DcMotor lFront;
        public static DcMotor rBack;
        public static DcMotor rFront;
        public static DcMotor move;
        public static DcMotor collector;
        public static DcMotor shooter;
        public static Servo grab;
        public static Servo touchArm;
        public static Servo basket;
        public static HardwareMap hardwareMap;
        public LinearOpMode linearOpMode;
        public static TouchSensor touch;
        public static int tpi = 62;
        public static Telemetry telemetry;
        
        //imu stuff
        BNO055IMU imu;
        
        public Lembot2(HardwareMap hwMap, OpMode opModeIn, Telemetry telemetryIn){
            hardwareMap = hwMap;
            telemetry = telemetryIn;
            if (opModeIn instanceof LinearOpMode) {
                linearOpMode = (LinearOpMode) opModeIn;
            }
        }
        
    public void init () {
        //imu stuff
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        
        parameters.mode = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled = false;
        
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        
        imu.initialize(parameters);
        
        while(!linearOpMode.isStopRequested() && !imu.isGyroCalibrated()) {
            linearOpMode.sleep(50);
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
        lBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //arm changes
        move = hardwareMap.get(DcMotor.class, "move");
        move.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        move.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        grab = hardwareMap.get(Servo.class, "grab");
        //shooter changes
        collector = hardwareMap.get(DcMotor.class, "collector");
        collector.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        collector.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        shooter = hardwareMap.get(DcMotor.class, "shooter");
        shooter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        basket = hardwareMap.get(Servo.class, "basket");
        //toucher arm
        touchArm = hardwareMap.get(Servo.class, "toucharm");

    }
    public void forward (int inches) {
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
        
        lFront.setPower(0.75);
        lBack.setPower(0.75);
        rFront.setPower(0.75);
        rBack.setPower(0.75);
        
        linearOpMode.sleep(x*75);
    }
    
    public void backward (int inches) {
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
        
        lFront.setPower(-0.75);
        lBack.setPower(-0.75);
        rFront.setPower(-0.75);
        rBack.setPower(-0.75);
        
        linearOpMode.sleep(x*75);
    }
    public void strafeLeft (int inches) {
        //moves left x inches
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
        
        lFront.setPower(-0.75);
        lBack.setPower(0.75);
        rFront.setPower(0.75);
        rBack.setPower(-0.75);
        
        linearOpMode.sleep(x*75);
    }
    public void strafeRight (int inches) {
        //moves right x inches
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
        
        lFront.setPower(0.75);
        lBack.setPower(-0.75);
        rFront.setPower(-0.75);
        rBack.setPower(0.75);
        
        linearOpMode.sleep(x*75);
    }
    public void flDiag (int inches) {
        //moves forward left diagonally x inches
        int x = inches;
        resetEncoder();
        
        lFront.setTargetPosition(0);
        lBack.setTargetPosition(x*tpi);
        rFront.setTargetPosition(x*tpi);
        rBack.setTargetPosition(0);
        
        lFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        
        lFront.setPower(0);
        lBack.setPower(0.75);
        rFront.setPower(0.75);
        rBack.setPower(0);
        
        linearOpMode.sleep(x*75);
    }
    public void frDiag (int inches) {
        //moves forward right diagonally x inches
        int x = inches;
        resetEncoder();
        
        lFront.setTargetPosition(x*tpi);
        lBack.setTargetPosition(0);
        rFront.setTargetPosition(0);
        rBack.setTargetPosition(x*tpi);
        
        lFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        
        lFront.setPower(0.75);
        lBack.setPower(0);
        rFront.setPower(0);
        rBack.setPower(0.75);
        
        linearOpMode.sleep(x*75);
    }
    public void blDiag (int inches) {
        //moves backwards left diagonally x inches
        int x = inches;
        resetEncoder();
        
        lFront.setTargetPosition(0);
        lBack.setTargetPosition(-x*tpi);
        rFront.setTargetPosition(-x*tpi);
        rBack.setTargetPosition(0);
        
        lFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        
        lFront.setPower(0);
        lBack.setPower(-0.75);
        rFront.setPower(-0.75);
        rBack.setPower(0);
        
        linearOpMode.sleep(x*250);
    }
    public void brDiag (int inches) {
        //moves backwards right diagonally x inches
        int x = inches;
        resetEncoder();
        
        lFront.setTargetPosition(-x*tpi);
        lBack.setTargetPosition(0);
        rFront.setTargetPosition(0);
        rBack.setTargetPosition(-x*tpi);
        
        lFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        
        lFront.setPower(-0.75);
        lBack.setPower(0);
        rFront.setPower(0);
        rBack.setPower(-0.75);
        
        linearOpMode.sleep(x*250);
    }
    public void turnRight (double degree) {
        //turns right x degrees
        degree = -degree;
        lBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        resetAngle();
        lFront.setPower(0.75);
        lBack.setPower(0.75);
        rFront.setPower(-0.75);
        rBack.setPower(-0.75);
        boolean turning = false;
        while (!turning) {
            telemetry.addData("current angle", getAngle());
            telemetry.addData("objective angle", degree);
            telemetry.update();
            if (getAngle() <= degree) {
                turning = true;
                lFront.setPower(0);
                lBack.setPower(0);
                rFront.setPower(0);
                rBack.setPower(0);
            }
        }
        
    }
    public void turnLeft (double degree) {
        //turns left x degrees
        lBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        resetAngle();
        lFront.setPower(-0.75);
        lBack.setPower(-0.75);
        rFront.setPower(0.75);
        rBack.setPower(0.75);
        boolean turning = false;
        while (!turning) {
            telemetry.addData("current angle", getAngle());
            telemetry.addData("objective angle", degree);
            telemetry.update();
            if (getAngle() >= degree) {
                turning = true;
                lFront.setPower(0);
                lBack.setPower(0);
                rFront.setPower(0);
                rBack.setPower(0);
            }
        }
        
    }
    public void grab () {
        // grabs the wobble goal. Must already be positioned.
        //starting position is grabbed.
        //move.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //move.setTargetPosition(-2800);
        //move.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //move.setPower(-0.75);
        
        grabOpen();
        grabClose();
        
        grabOpen();
        grabClose();
        
        grabOpen();
        grabClose();
        
        grabOpen();
        grabClose();
        
        grabOpen();
        grabClose();
    }
    public void grabOpen () {
        grab.setPosition(grab.getPosition() - 1);
        linearOpMode.sleep(250);
    }
    public void grabClose () {
        grab.setPosition(grab.getPosition() + 1);
        linearOpMode.sleep(250);
    }
    public static void setDown () {
        //sets the wobble goal down. Must already be positioned.
    }
    public int check () {
        //uses the limiter switch arm to check the number of rings. 
        //returns an int for the height, 0 = 1, 1 = 2, 4 = 3.
        
        
        //below is copied directly from bens, with the toucharm renamed. it 
        //does not work. From this point forward, the check method is bens job.
        int rings = 0;
        while (!touch.isPressed()) {
            touchArm.setPosition(touchArm.getPosition()-0.02);
            linearOpMode.sleep(25);
        }
        if (touchArm.getPosition() > 0.3) 
            rings = 3;
        else if (touchArm.getPosition() > 0.02)
            rings = 2;
        else 
            rings = 1;

        touchArm.setPosition(1);
        linearOpMode.sleep(500);
        return rings;
    }
    public static void shoot () {
        //shoot all rings in storage
    }
    public static void resetEncoder() {
        lBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
    public double getAngle () {
        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        double deltaAngle = angles.firstAngle - lastAngles.firstAngle;
        if (deltaAngle < -180)
            deltaAngle +=360;
        else if (deltaAngle > 180)
            deltaAngle -= 360;
            
        globalAngle += deltaAngle;
        lastAngles = angles;
        return globalAngle;
    }
    public void resetAngle() {
        lastAngles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        globalAngle = 0;
    }
}
/*
wheel circumference: 314.15 MM
motor tick thingy: 767.2 T
mm per inch is: 25.4
so, ~62 ticks per inch
*/
