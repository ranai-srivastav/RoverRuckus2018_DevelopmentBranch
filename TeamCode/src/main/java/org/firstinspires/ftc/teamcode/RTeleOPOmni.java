package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;


import static android.os.SystemClock.sleep;
import static java.lang.Math.max;
import static java.lang.Math.min;


/**
 * TeleOp
 */
@TeleOp(name = "TeleOp Mode 1")
public class RTeleOPOmni extends OpMode
{
    //Initialising all necessary variables


    DcMotor MotorFrontY;
    DcMotor MotorFrontX;
    DcMotor MotorBackX;
    DcMotor MotorBackY;

    DcMotor MotorArm;
    DcMotor MotorExtend;
    DcMotor MotorLand;


    int turnMaxPosition;
    int turnMinPosition;

    int extendMaxPosition;
    int extendMinPosition;

    int landMaxPosition;
    int landMinPosition;

    float des_arm_position = 0;

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

        MotorExtend = hardwareMap.dcMotor.get("extend");
        MotorArm = hardwareMap.dcMotor.get("arm");
        MotorLand = hardwareMap.dcMotor.get("land");

        MotorFrontX.setDirection(DcMotorSimple.Direction.REVERSE);
        MotorBackX.setDirection(DcMotorSimple.Direction.FORWARD);
        MotorFrontY.setDirection(DcMotorSimple.Direction.REVERSE);
        MotorBackY.setDirection(DcMotorSimple.Direction.FORWARD);

        MotorExtend.setDirection(DcMotorSimple.Direction.REVERSE);
        MotorArm.setDirection(DcMotorSimple.Direction.FORWARD);
        MotorLand.setDirection(DcMotorSimple.Direction.REVERSE);


        MotorExtend.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        MotorExtend.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        MotorArm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        MotorArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        MotorLand.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        MotorLand.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //Check difference, here starting position is at middle and will move both the sides
        turnMinPosition = MotorArm.getCurrentPosition() - 120;
        turnMaxPosition = MotorArm.getCurrentPosition() + 450;

        //This Motor is running in reverse direction hence values subtracted
        extendMinPosition = MotorExtend.getCurrentPosition() + 500;
        //Difference is 9000
        extendMaxPosition = MotorExtend.getCurrentPosition() + 7500;


        landMinPosition = MotorLand.getCurrentPosition() - 200;
        //Difference is 25000
        landMaxPosition = MotorLand.getCurrentPosition() - 22000;

        telemetry.addData("Arm Extend: ", MotorExtend.getCurrentPosition());
        telemetry.addData("Arm Land: ", MotorLand.getCurrentPosition());
        telemetry.addData("Arm Land: ", landMaxPosition);
        telemetry.addData("Arm Turn: ", MotorArm.getCurrentPosition());

        telemetry.update();
    }


    @Override
    public void loop() {
        //telemetry.addData("Enters loop to define power" ,"nothing");
        //telemetry.update();


        float y = gamepad1.left_stick_y;
        float x = gamepad1.right_stick_x;
        float extend = gamepad2.right_stick_y;
        float turnforward = gamepad2.left_stick_y;
        boolean rb = gamepad1.right_bumper;
        boolean lb = gamepad1.left_bumper;
        boolean a = gamepad2.a;
        boolean b = gamepad2.b;
        float pos_error;

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


        //Moving the arm forwards and backwards

        des_arm_position += 8*gamepad2.left_stick_y;
        des_arm_position = (float)(min(max(des_arm_position,-150.0),1200.0));
        telemetry.addData("LY: ",gamepad2.left_stick_y);
        telemetry.addData("DA", des_arm_position);
        telemetry.update();
        pos_error = des_arm_position - MotorArm.getCurrentPosition();
        pos_error = (float)(min(max(.0015 * pos_error,-1.0),1.0));
        MotorArm.setPower(pos_error);
        //Arm Movements over

        if(extend > 0)
        {
            armRetract(10);
        }
        if(extend < 0)
        {
            armExtend(10);

        }

        if(b)
        {
            land(15);
        }

        if(a)
        {
            raise(15);

        }

    }





    public int distanceToCounts(double rotations1){
        int rotations = (int) Math.round (rotations1 * 100);
        return Math.round(rotations);
    }

    public void turnArmForward(double distance) {

        MotorArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        int COUNTS = distanceToCounts(distance);

        MotorArm.setTargetPosition((MotorArm.getCurrentPosition() + COUNTS));

        if (turnMaxPosition > MotorArm.getCurrentPosition()){
            MotorArm.setPower(0.6);
            while (MotorArm.isBusy()) {
                telemetry.addData("Turning motor arm forward", MotorArm.getCurrentPosition());
                telemetry.update();
            }
        }
        MotorArm.setPower(0);
        telemetry.addData("Turning motor arm Forward: ", MotorArm.getCurrentPosition());
        telemetry.addData("Turning motor arm Forward Max: ", turnMaxPosition);
        telemetry.update();
    }

    public void turnArmBackward(double distance)
    {
        MotorArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        int COUNTS = distanceToCounts(distance);
        MotorArm.setTargetPosition((MotorArm.getCurrentPosition() - COUNTS));

        if(MotorArm.getCurrentPosition()> turnMinPosition) {
            MotorArm.setPower(-0.6);
            while (MotorArm.isBusy()) {
                telemetry.addData("Turning motor arm backward", MotorArm.getCurrentPosition());
                telemetry.update();
            }
        }
        MotorArm.setPower(0);
        telemetry.addData("Turning motor arm backward", MotorArm.getCurrentPosition());
        telemetry.addData("Turning motor arm backward Min: ", turnMinPosition);
        telemetry.update();
    }

    public void armExtend(double distance)
    {
        MotorExtend.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        int COUNTS = distanceToCounts(distance);

        MotorExtend.setTargetPosition((MotorExtend.getCurrentPosition() + (COUNTS)));

        if(MotorExtend.getCurrentPosition() < extendMaxPosition) {
            MotorExtend.setPower(1);
            while (MotorExtend.isBusy()) {
                telemetry.addData("Extending motor arm", MotorExtend.getCurrentPosition());
                telemetry.update();
            }
        }
        MotorExtend.setPower(0);
        telemetry.addData("Extending motor arm", MotorExtend.getCurrentPosition());
        telemetry.addData("Extending motor arm Max: ", extendMaxPosition);
        telemetry.update();
    }

    public void armRetract(double distance)
    {
        MotorExtend.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        int COUNTS = distanceToCounts(distance);

        MotorExtend.setTargetPosition((MotorExtend.getCurrentPosition() - (COUNTS)));

        if(MotorExtend.getCurrentPosition() > extendMinPosition) {
            MotorExtend.setPower(-1);
            while (MotorExtend.isBusy()) {
                telemetry.addData("Retracting motor arm", MotorExtend.getCurrentPosition());
                telemetry.update();
            }
        }
        MotorExtend.setPower(0);
        telemetry.addData("Retracting motor arm", MotorExtend.getCurrentPosition());
        telemetry.addData("Retracting motor arm Min: ", extendMinPosition);
        telemetry.update();
    }

    public void raise(double distance)
    {
        MotorLand.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        int COUNTS = distanceToCounts(distance);

        MotorLand.setTargetPosition((MotorLand.getCurrentPosition() + (COUNTS)));

        int currentPosition = MotorLand.getCurrentPosition();

        if((MotorLand.getCurrentPosition() < landMinPosition)) {
            MotorLand.setPower(1);
            while (MotorLand.isBusy()) {
                telemetry.addData("Raising the robot", MotorLand.getCurrentPosition());
                telemetry.update();
            }
        }
        MotorLand.setPower(0);
        telemetry.addData("Lowering the robot", MotorLand.getCurrentPosition());
        telemetry.addData("Lowering the robot min: ", landMinPosition);
        telemetry.update();

    }

    public void land (double distance)
    {
        MotorLand.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        int COUNTS = distanceToCounts(distance);

        MotorLand.setTargetPosition((MotorLand.getCurrentPosition() - (COUNTS)));

        if((MotorLand.getCurrentPosition() > landMaxPosition)) {
            MotorLand.setPower(-1);
            while (MotorLand.isBusy()) {
                telemetry.addData("Lowering the robot", MotorLand.getCurrentPosition());
                telemetry.update();
            }
        }
        MotorLand.setPower(0);
        telemetry.addData("Lowering the robot", MotorLand.getCurrentPosition());
        telemetry.addData("Lowering the robot Max: ", landMaxPosition);
        telemetry.update();
    }

    @Override
    public void stop() {
    }

}
