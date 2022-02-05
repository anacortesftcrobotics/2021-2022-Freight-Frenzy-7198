package org.firstinspires.ftc.teamcode;

import java.lang.Math;
import java.util.Locale;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import com.qualcomm.hardware.bosch.BNO055IMU;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.vuforia.Frame;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.util.ElapsedTime;
import java.util.ArrayDeque;

@TeleOp

public class LemTeleOpRed extends OpMode 
{
    //Variables
    double lBackPower;
    double lFrontPower;
    double rBackPower;
    double rFrontPower;
    double speedCoefficient = 1;
    double actualSpeedCoefficient;
    boolean speedChanged = false;
    double denominator;
    double allottedTime = 10;
    boolean carouselPower;
    double a;
    double deadzoneVar = 0.05;
    double y = 0;
    double x = 0;
    double rx = 0;
    double oldTime = 0;
    double executionTime = 0;
    double loopsPerSecond = 0;
    double avTime;
    double armPower = 0;
    double parity;
    double oldStick;
    double leftAverage = 3;
    double rightAverage = 5;
    
    //Weird Inits 
    private ArrayDeque<Double> queue;
    
    //Hardware Inits
    private Blinker control_Hub;
    private Blinker expansion_Hub_1;
    private HardwareDevice webcam_1;
    private DcMotor armControl;
    private Servo armHand;
    private Servo carousel;
    private Servo switchArm;
    private DistanceSensor leftColor;
    private DistanceSensor rightColor;
    BNO055IMU imu;
    private DcMotor lBack;
    private DcMotor lFront;
    private DcMotor rBack;
    private DcMotor rFront;
    Orientation angles;
    private DigitalChannel armStop;
    
    public void init() 
    {
        //IMU
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);
        
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
        armControl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armControl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        
        //Sub-Assembly Servos
        armHand = hardwareMap.get(Servo.class, "armHand");
        carousel = hardwareMap.get(Servo.class, "carousel");
        switchArm = hardwareMap.get(Servo.class, "switchArm");
        
        //Other 
        queue = new ArrayDeque<Double>(20);
        leftColor = hardwareMap.get(DistanceSensor.class, "leftColor");
        rightColor = hardwareMap.get(DistanceSensor.class, "rightColor");
        
        //armStop
        armStop = hardwareMap.get(DigitalChannel.class, "armStop");
    }
    public void loop()
    {
        controller1();
        controller2();
        //executionTime();
        telemetry();
    }
    public void controller1()
    {
        y = -gamepad1.left_stick_y;
        x = gamepad1.left_stick_x;
        rx = gamepad1.right_stick_x;
        
        lBackPower = 0;
        lFrontPower = 0;
        rBackPower = 0;
        rFrontPower = 0;
        
        denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        lBackPower = (y - x + rx) / denominator;
        lFrontPower = (y + x + rx) / denominator;
        rBackPower = (y + x - rx) / denominator;
        rFrontPower = (y - x - rx) / denominator;
        
        if (Math.abs(lBackPower) < deadzoneVar) 
        {
            lBackPower = 0;
        }
        if (Math.abs(lFrontPower) < deadzoneVar)
        {
            lFrontPower = 0;
        }
        if (Math.abs(rBackPower) < deadzoneVar) 
        {
            rBackPower = 0;
        }
        if (Math.abs(rFrontPower) < deadzoneVar)
        {
            rFrontPower = 0;
        }
        
        lBackPower = (lBackPower / Math.abs(lBackPower)) * Math.min(Math.abs(lBackPower), 0.9);
        lFrontPower = (lFrontPower / Math.abs(lFrontPower)) * Math.min(Math.abs(lFrontPower), 0.9);
        rBackPower = (rBackPower / Math.abs(rBackPower)) * Math.min(Math.abs(rBackPower), 0.9);
        rFrontPower = (rFrontPower / Math.abs(rFrontPower)) * Math.min(Math.abs(rFrontPower), 0.9);
        
        lBackPower = lBackPower - lBackPower / 9;
        lFrontPower = lFrontPower + lFrontPower / 9;
        rBackPower = rBackPower - rBackPower / 9;
        rFrontPower = rFrontPower + rFrontPower / 9;
        
        lBackPower = applyCurve(lBackPower);
        lFrontPower = applyCurve(lFrontPower);
        rBackPower = applyCurve(rBackPower);
        rFrontPower = applyCurve(rFrontPower);
        
        actualSpeedCoefficient = 1.2 - (speedCoefficient / 5);
        
        lBackPower = lBackPower * actualSpeedCoefficient;
        lFrontPower = lFrontPower * actualSpeedCoefficient;
        rBackPower = rBackPower * actualSpeedCoefficient;
        rFrontPower = rFrontPower * actualSpeedCoefficient; 
        /*
        lBackPower = lBack.getPower() + upToSpeed(lBack.getPower(),lBackPower,allottedTime);
        lFrontPower = lFront.getPower() + upToSpeed(lFront.getPower(),lFrontPower,allottedTime);
        rBackPower = rBack.getPower() + upToSpeed(rBack.getPower(),rBackPower,allottedTime);
        rFrontPower =  rFront.getPower() + upToSpeed(rFront.getPower(),rFrontPower,allottedTime);
        */
        lBack.setPower(lBackPower);
        lFront.setPower(lFrontPower);
        rBack.setPower(rBackPower);
        rFront.setPower(rFrontPower);
        
        if (!gamepad1.left_bumper && !gamepad1.right_bumper)
        {
            speedChanged = false;
        }
        if (gamepad1.left_bumper && !speedChanged && speedCoefficient != 5)
        {
            speedCoefficient++;
            speedChanged = true;
        }
        if (gamepad1.right_bumper && !speedChanged && speedCoefficient != 1)
        {
            speedCoefficient--;
            speedChanged = true;
        }
        
        //180 degree turn
    }
    public void controller2()
    {   
        double pos = 0.7;
        //grabbing
        
        if(gamepad2.right_bumper) //switch to 2
        {
            armHand.setPosition(1); 
        }
        if(!gamepad2.right_bumper) //switch to 2
        {
            armHand.setPosition(0.5);
        }
        
        /*
        if(gamepad2.right_trigger > 0.3) //switch to 2
        {
            pos++;
            armHand.setPosition(pos);
        } 
        if (gamepad2.right_trigger < 0.1)
        {
            armHand.setPosition(0.5);
            pos = 0.7;
        }
        */
        //arm moving
        armControl.setPower(-gamepad2.left_stick_y);
        
        
        //stuff goes here
        
        
        /*
        Logans Code
        
        ((DcMotorEx)armControl).setVelocity(-gamepad2.left_stick_y * 400);
        
        if(-gamepad2.left_stick_y == 0 && ((DcMotorEx)armControl).getVelocity() > 0)
        {
            ((DcMotorEx)armControl).setVelocity(200);
        }
            if(-gamepad2.left_stick_y == 0 && ((DcMotorEx)armControl).getVelocity() < 0)
        {
            ((DcMotorEx)armControl).setVelocity(-200);
        }
        */
        
        //carousel
        if (gamepad2.left_trigger > 0) 
        {
            carousel.setPosition(0.1);
        } 
        else 
        {
            carousel.setPosition(0.5);
        }
        
        //switchArmNonsense
        
        if(gamepad2.x)
        {
            switchArm.setPosition(0.8);
        }
        else
        {
            switchArm.setPosition(0);
        }
    }
    public void telemetry()
    {
        angles   = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        //telemetry.addData("lBackPower",lBackPower); 
        //telemetry.addData("lFrontPower",lFrontPower); 
        //telemetry.addData("rBackPower",rBackPower); 
        //telemetry.addData("rFrontPower",rFrontPower); 
        //telemetry.addData("ActualSpeedCoefficient",(1.2 - speedCoefficient / 5));
        //telemetry.addData("speedCoefficient",speedCoefficient);
        telemetry.addData("speed", Math.ceil(100 * actualSpeedCoefficient) + "%");
        //telemetry.addData("Hand Position", armHand.getPosition());
        //telemetry.addData("arm velocity",((DcMotorEx)armControl).getVelocity());
        telemetry.addData("time",time);
        //telemetry.addData("Trigger",gamepad2.right_trigger);
        telemetry.addData("leftDistance",leftColor.getDistance(DistanceUnit.INCH));
        telemetry.addData("rightDistance",rightColor.getDistance(DistanceUnit.INCH));
        //telemetry.addData("leftAverage",leftAverage);
        //telemetry.addData("rightAverage",rightAverage);
        telemetry.addData("heading",formatAngle(angles.angleUnit, angles.firstAngle));
        telemetry.addData("armStop",armStop.getState());
        telemetry.update();
        
        oldStick = gamepad1.right_stick_y;
    }
    //space for functions
    public double upToSpeed(double currentSpeed, double newSpeed, double allottedTime)
    {
        double changeInSpeed = (newSpeed - currentSpeed) / allottedTime;
        return changeInSpeed;
    }
    public void executionTime()
    {
        if (queue.size() >= 10)
        {
            queue.poll();
        }
        avTime = 0;
        for (double time : queue) 
        {
            avTime += time;
        }
        avTime = avTime / queue.size();
        executionTime = avTime - oldTime;
        oldTime = avTime;
        loopsPerSecond = 1/executionTime;
        queue.add(time);
    }
    public double applyCurve(double input)
    {
        parity = input/Math.abs(input);
        return (parity * Math.log10(parity * input + 0.1) + parity) / (Math.log10(1.1) + 1);
    }
    public void nothing ()
    {
        formatAngle(angles.angleUnit, angles.firstAngle);
    }
    String formatAngle(AngleUnit angleUnit, double angle) 
    {
        return formatDegrees(AngleUnit.DEGREES.fromUnit(angleUnit, angle));
    }
    String formatDegrees(double degrees)
    {
        return String.format(Locale.getDefault(), "%.1f", AngleUnit.DEGREES.normalize(degrees));
    }
}
