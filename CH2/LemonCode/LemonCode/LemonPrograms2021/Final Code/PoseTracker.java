package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

// This is a class that provides useful methods using the REV IMU
public class PoseTracker {

    // Define IMU object
    BNO055IMU imu;

    // Define necessary fields
    Orientation lastAngles;
    double globalAngle, correction;

    // Constructs a PoseTracker object
    public PoseTracker(BNO055IMU imu) {

        // Set robot references
        this.imu = imu;
    }

    // Reset current tracked angle, and store the current true heading
    public void resetAngleX() {
        lastAngles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        globalAngle = 0;
    }
    
    // Reset current tracked angle, and store the current true heading
    public void resetAngle() {
        lastAngles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        globalAngle = 0;
    }

    // Calculate and return the current tracked angle using the previous true angle and the current true angle
    public double getAngleX() {
        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        double deltaAngle = 0;
        if (angles != null) {
            deltaAngle = angles.firstAngle - lastAngles.firstAngle;
        } else {
            //r.telemetry.addLine("Err0r");
            //r.telemetry.update();
        }

        if (deltaAngle < -180) {
            deltaAngle += 360;
        } else if (deltaAngle > 180) {
            deltaAngle -= 360;
        }

        globalAngle += deltaAngle;

        lastAngles = angles;

        return globalAngle;
    }
    
    // Calculate and return the current tracked angle using the previous true angle and the current true angle
    public double getAngle() {
        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        double deltaAngle = 0;
        if (angles != null) {
            deltaAngle = angles.firstAngle - lastAngles.firstAngle;
        } else {
            //r.telemetry.addLine("Err0r");
            //r.telemetry.update();
        }

        if (deltaAngle < -180) {
            deltaAngle += 360;
        } else if (deltaAngle > 180) {
            deltaAngle -= 360;
        }

        globalAngle += deltaAngle;

        lastAngles = angles;

        return globalAngle;
    }

    // Returns the amount to correct a straight movement to avoid slight power imbalance
    public double checkDirection() {
        double correction, angle, gain = .1;

        angle = getAngle();

        if (angle == 0) {
            correction = 0;
        } else {
            correction = -angle;
        }

        correction = correction * gain;

        return correction;
    }
}