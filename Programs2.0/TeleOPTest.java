package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;


import static android.os.SystemClock.sleep;



/**
 * TeleOp
 */
@TeleOp(name = "Test Mode")
public class TeleOPTest extends OpMode {
    //Initialising all necessary variables

    DcMotor MotorFrontY;
    DcMotor MotorFrontX;
    DcMotor MotorBackX;
    DcMotor MotorBackY;

    DcMotor MotorArm;
    DcMotor MotorExtend;

    int armTurnPosition;
    int armExtendPosition;

    @Override
    public void init()
    {
        telemetry.addData("Initialised" ,"nothing");
        telemetry.update();

        // defining all the hardware
        MotorFrontX = hardwareMap.dcMotor.get("fx");
        MotorBackX = hardwareMap.dcMotor.get("bx");
        MotorBackY = hardwareMap.dcMotor.get("by");
        MotorFrontY = hardwareMap.dcMotor.get("fy");

        //MotorExtend = hardwareMap.dcMotor.get("extend");
        //MotorArm = hardwareMap.dcMotor.get("arm");

        MotorFrontX.setDirection(DcMotorSimple.Direction.REVERSE);
        MotorBackX.setDirection(DcMotorSimple.Direction.FORWARD);
        MotorFrontY.setDirection(DcMotorSimple.Direction.REVERSE);
        MotorBackY.setDirection(DcMotorSimple.Direction.FORWARD);

        //MotorExtend.setDirection(DcMotorSimple.Direction.FORWARD);
        //MotorArm.setDirection(DcMotorSimple.Direction.FORWARD);

        //MotorExtend.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //MotorArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //armTurnPosition = MotorArm.getCurrentPosition();
        //armExtendPosition = MotorExtend.getCurrentPosition();
    }


    @Override
    public void loop() {
        //telemetry.addData("Enters loop to define power" ,"nothing");
        //telemetry.update();

        float y = gamepad1.left_stick_y;
        float x = gamepad1.right_stick_x;
        float extend = gamepad2.right_stick_y;
        float retract = gamepad2.right_stick_y;
        float turnforward = gamepad2.left_stick_y;
        float turnbackward = gamepad2.left_stick_y;
        boolean rb = gamepad1.right_bumper;
        boolean lb = gamepad1.left_bumper;

        // Reset variables
        float powerXWheels = 0;
        float powerYWheels = 0;

        // Handle regular movement
        powerYWheels += y;

        // Handle sliding movement
        powerXWheels += x;

        // Handle turning movement
        double maxX = (double)powerXWheels;
        double maxY = (double)powerYWheels;

        MotorBackX.setPower(maxX);
        MotorFrontX.setPower(maxX);
        telemetry.addData("Power X" ,maxX);
        telemetry.update();

        MotorBackY.setPower(maxY);
        MotorFrontY.setPower(maxY);
        telemetry.addData("Power Y" ,maxY);
        telemetry.update();

        if(rb)
        {
            MotorFrontX.setPower(0.5);
            MotorFrontY.setPower(0.5);
            MotorBackX.setPower(-0.5);
            MotorBackY.setPower(-0.5);
        }
        if(lb)
        {
            MotorFrontX.setPower(-0.5);
            MotorFrontY.setPower(-0.5);
            MotorBackX.setPower(0.5);
            MotorBackY.setPower(0.5);
        }
/*
        if(turnforward > 0)
        {
            turnArmForward(0.1);

        }
        if(turnbackward > 0)
        {
            turnArmBackward(0.1);

        }
        if(extend > 0)
        {
            armExtend(0.1);


        }
        if(retract > 0)
        {
            armRetract(0.1);

        }

*/

        // Here you set the motors' power to their respected power double.
        // start of controller movements

    }


    public int distanceToCounts(double rotations1){
        int rotations = (int) Math.round (rotations1 * 100);
        return Math.round(rotations);
    }

    public void moveForwardOdometry(double power, int length) {
        MotorFrontY.setPower(-power);
        MotorBackY.setPower(power);
        sleep(length);
    }

    public void moveBackwardOdometry(double power, int length){
        MotorFrontY.setPower(power);;
        MotorBackY.setPower(-power);
        sleep(length);

    }

    public void moveLeftOdometry(double power, int length){
        MotorFrontX.setPower(-power);
        MotorBackX.setPower(power);
        sleep(length);
    }

    public void moveRightOdometry(double power, int length){
        MotorFrontX.setPower(power);
        MotorBackX.setPower(-power);
        sleep(length);
    }

    public void turnArmForward(double distance)
    {
        MotorArm.setPower(0.45);

        MotorArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        telemetry.addData("Calling count", "");
        telemetry.update();

        int COUNTS = distanceToCounts(distance);

        telemetry.addData("Counts", COUNTS);
        telemetry.update();

        telemetry.addData("MotorArm", MotorArm.getCurrentPosition());
        telemetry.update();

        MotorArm.setTargetPosition((MotorArm.getCurrentPosition() + (COUNTS)));


        while ( MotorArm.isBusy() )
        {
            telemetry.addData("Turning motor arm forward", "Encoders");
            telemetry.update();
        }
    }

    public void turnArmBackward(double distance)
    {
        MotorArm.setPower(-0.45);

        MotorArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        telemetry.addData("Calling count", "");
        telemetry.update();

        int COUNTS = distanceToCounts(distance);

        telemetry.addData("Counts", COUNTS);
        telemetry.update();

        telemetry.addData("MotorArm", MotorArm.getCurrentPosition());
        telemetry.update();

        MotorArm.setTargetPosition((MotorArm.getCurrentPosition() - (COUNTS)));

        while ( MotorArm.isBusy())
        {
            telemetry.addData("Turning motor arm backward", "Encoders");
            telemetry.update();
        }
    }

    public void armExtend(double distance)
    {
        MotorExtend.setPower(0.45);

        MotorExtend.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        telemetry.addData("Calling count", "");
        telemetry.update();

        int COUNTS = distanceToCounts(distance);

        telemetry.addData("Counts", COUNTS);
        telemetry.update();

        telemetry.addData("MotorArm", MotorExtend.getCurrentPosition());
        telemetry.update();

        MotorExtend.setTargetPosition((MotorExtend.getCurrentPosition() + (COUNTS)));


        while ( MotorExtend.isBusy())
        {
            telemetry.addData("Extending motor arm", "Encoders");
            telemetry.update();
        }
    }

    public void armRetract(double distance)
    {
        MotorExtend.setPower(-0.45);

        MotorExtend.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        telemetry.addData("Calling count", "");
        telemetry.update();

        int COUNTS = distanceToCounts(distance);

        telemetry.addData("Counts", COUNTS);
        telemetry.update();

        telemetry.addData("MotorArm", MotorExtend.getCurrentPosition());
        telemetry.update();

        MotorExtend.setTargetPosition((MotorExtend.getCurrentPosition() - (COUNTS)));


        while ( MotorExtend.isBusy())
        {
            telemetry.addData("Retracting motor arm", "Encoders");
            telemetry.update();
        }
    }

    @Override
    public void stop() {
    }
    double scaleInput(double dVal) {
        double[] scaleArray = {0.0, 0.04, 0.08, 0.9, 0.11, 0.14, 0.17, 0.23, 0.29, 0.35, 0.42, 0.49, 0.59, 0.71, 0.84, 0.99, 1.00};
        int index = (int) (dVal * 16.0);
        if (index < 0) {
            index = -index;
        }
        if (index > 16) {
            index = 16;
        }
        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }
        return dScale;
    }
}



