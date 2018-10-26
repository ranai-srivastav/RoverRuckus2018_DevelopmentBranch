package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * TeleOp
 */
@TeleOp(name = "TeleOp Mode 1")
public class RTeleOPOmni extends OpMode
{
    //Initialising all necessary variables
    float y;
    float x;
    boolean land;
    boolean raise;
    float des_arm_rotation;
    float des_arm_extension;
    float armRotation_posError;
    float armExtension_posError;
    float landingArm_PosError;
    float des_landingArm_position;

    DcMotor MotorFrontY;
    DcMotor MotorFrontX;
    DcMotor MotorBackX;
    DcMotor MotorBackY;

    DcMotor MotorArm;
    DcMotor MotorExtend;
    DcMotor MotorLand;

    @Override
    public void init()
    {

        telemetry.addData("Inside Init() method",null);
        y = gamepad1.left_stick_y;
        x = gamepad1.right_stick_x;
        land = gamepad2.a;
        raise = gamepad2.b;
        des_arm_rotation = 0;
        des_arm_extension = 0;
        armRotation_posError = 0;
        armExtension_posError = 0;
        landingArm_PosError = 0;
        des_landingArm_position = 0;


        telemetry.addData("Initialised", "nothing");
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
        MotorLand.setDirection(DcMotorSimple.Direction.FORWARD);


        MotorExtend.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        MotorExtend.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        MotorArm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        MotorArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        MotorLand.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        MotorLand.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


    }


    @Override
    public void loop()
    {
        telemetry.addData("Inside loop",null);
        telemetry.update();

        // Reset variables
        float powerXWheels = 0;
        float powerYWheels = 0;


        // Handle regular movement
        powerYWheels += gamepad1.left_stick_y;

        // Handle sliding movement
        powerXWheels += gamepad1.right_stick_x;


        telemetry.addData("Power X wheels",powerXWheels);
        telemetry.addData("Power Y wheels",powerXWheels);
        telemetry.update();


        // Handle turning movement
        double maxX = (double) powerXWheels;
        double maxY = (double) powerYWheels;
        telemetry.addData("Power X wheels", maxX);
        telemetry.addData("Power Y wheels", maxY);
        telemetry.update();

        MotorBackX.setPower(maxX);
        MotorFrontX.setPower(maxX);


        MotorBackY.setPower(maxY);
        MotorFrontY.setPower(maxY);


        if (gamepad2.right_bumper)
        {
            MotorFrontX.setPower(0.5);
            MotorFrontY.setPower(0.5);
            MotorBackX.setPower(-0.5);
            MotorBackY.setPower(-0.5);
        }
        if (gamepad2.left_bumper)
        {
            MotorFrontX.setPower(-0.5);
            MotorFrontY.setPower(-0.5);
            MotorBackX.setPower(0.5);
            MotorBackY.setPower(0.5);
        }


        //Moving the arm forwards and backwards
        des_arm_rotation += 12 * gamepad2.left_stick_y;                                                  //how fast we accumulate. Speed
        des_arm_rotation = (float) (min(max(des_arm_rotation, -120.0), 1150.0));
        armRotation_posError = des_arm_rotation - MotorArm.getCurrentPosition();
        armRotation_posError = (float) (min(max(.0025 * armRotation_posError, -1.0), 1.0));             //how much we accumulate. Sensitivity
        MotorArm.setPower(armRotation_posError);
        //Arm Movements over

        //Arm Extension and retraction
        des_arm_extension += 15 * gamepad2.right_stick_x;                                                //how fast we accumulate. Speed
        des_arm_extension = (float) (min(max(des_arm_extension, 0), 7000.0));
        armExtension_posError = des_arm_extension - MotorExtend.getCurrentPosition();
        armExtension_posError = (float) (min(max(.003 * armExtension_posError, -1.0), 1.0));
        telemetry.addData("Power for extension is set to : ",armExtension_posError);            //how much we accumulate. Sensitivity
        MotorExtend.setPower(armExtension_posError);
        //Arm extension and retraction over


        //raising and lowering the landing arm
        if(gamepad2.dpad_up)
        {
            telemetry.addData("Data", MotorLand.getCurrentPosition());
            telemetry.update();
            if(MotorLand.getCurrentPosition()<= 2000)
            {
                MotorLand.setPower(1.0);
            }
            else
                MotorLand.setPower(0);
        }

        if(gamepad2.dpad_down)
        {
            telemetry.addData("Data",MotorLand.getCurrentPosition());
            telemetry.update();
            if(MotorLand.getCurrentPosition()>=50)
            {
                MotorLand.setPower(-1.0);
            }
            else
                MotorLand.setPower(0);
        }
        //end of raising and landing arm code



    }

    public int distanceToCounts(double rotations1)
    {
        int rotations = (int) Math.round(rotations1 * 100);
        return Math.round(rotations);
    }

    @Override
    public void stop() {}
}


/*//Check difference, here starting position is at middle and will move both the sides
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

        telemetry.update();*/
/*
    public int distanceToCounts(double rotations1) {
        int rotations = (int) Math.round(rotations1 * 100);
        return Math.round(rotations);
    }
    public void turnArmForward(double distance)
    {

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

    public static void main()
    {
        Log.i("Trial","This is a trial message" );
    }
    */

