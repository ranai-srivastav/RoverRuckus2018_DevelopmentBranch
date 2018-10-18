package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name = "Auto: Crater", group = "Autonomous")
public class CraterAuto extends LinearOpMode {

    DcMotor MotorFrontX;
    DcMotor MotorFrontY;
    DcMotor MotorBackX;
    DcMotor MotorBackY;

    @Override
    public void runOpMode() throws InterruptedException
    {

        initialize();

        waitForStart();

        //start opMode loop. Main Code Goes Here.
        while (opModeIsActive())
        {
            double d = 2.0;
            telemetry.addData("d declared", "nothing");
            telemetry.update();

            moveForward(d);
            telemetry.addData("moved forward 12 inches", "nothing");
            telemetry.update();

            slideRight(d);
            telemetry.addData("slid right 12 inches", "nothing");
            telemetry.update();

            moveBackward(d);
            telemetry.addData("moved backwards 12 inches", "nothing");
            telemetry.update();

            slideLeft(d);
            telemetry.addData("slid left 12 inches", "nothing");
            telemetry.update();



            turnClockwise(d);
            telemetry.addData("turned 12 inches", "nothing");
            telemetry.update();


            turnAntiClockwise(d);
            telemetry.addData("turned 12 inches", "nothing");
            telemetry.update();

            sleep(1000);

            setPower(0);

            //Stop opMode
            requestOpModeStop();
            break;
        }
    }

    protected void initialize()
    {
        MotorFrontX = (DcMotor) hardwareMap.dcMotor.get("fx");
        MotorFrontY = (DcMotor) hardwareMap.dcMotor.get("fy");
        MotorBackX = (DcMotor) hardwareMap.dcMotor.get("bx");
        MotorBackY = (DcMotor) hardwareMap.dcMotor.get("by");


        // Reverse the motor that runs backwards when connected directly to the battery
        MotorFrontX.setDirection(DcMotor.Direction.REVERSE);
        MotorFrontY.setDirection(DcMotor.Direction.REVERSE);
        MotorBackX.setDirection(DcMotor.Direction.FORWARD);
        MotorBackY.setDirection(DcMotor.Direction.FORWARD);

        //Motors set to run with encoders
        MotorBackX.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        MotorBackY.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        MotorFrontX.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        MotorFrontY.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void moveForward(double distance)
    {
        MotorFrontY.setPower(-0.35);
        MotorBackY.setPower(-0.35);

        MotorFrontY.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        MotorBackY.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        telemetry.addData("Calling count", "");
        telemetry.update();

        int COUNTS = distanceToCounts(distance);

        telemetry.addData("Counts", COUNTS);
        telemetry.update();

        telemetry.addData("MotorFrontY", MotorFrontY.getCurrentPosition());
        telemetry.addData("MotorBackY", MotorBackY.getCurrentPosition());
        telemetry.update();

        MotorFrontY.setTargetPosition((MotorFrontY.getCurrentPosition() - (COUNTS)));
        MotorBackY.setTargetPosition((MotorBackY.getCurrentPosition() - (COUNTS)));


        while (opModeIsActive() && MotorBackY.isBusy() && MotorFrontY.isBusy())
        {
            telemetry.addData("Running motor Y front and back", "Encoders");
            telemetry.update();
        }
        setPower(0);
        sleep(100);
    }


    public void moveBackward(double distance)
    {
        MotorFrontY.setPower(0.35);
        MotorBackY.setPower(0.35);

        MotorFrontY.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        MotorBackY.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        int COUNTS = distanceToCounts (distance);

        MotorFrontY.setTargetPosition((MotorFrontY.getCurrentPosition() + (COUNTS)));
        MotorBackY.setTargetPosition((MotorBackY.getCurrentPosition() + (COUNTS)));


        while (opModeIsActive() && MotorBackY.isBusy() && MotorFrontY.isBusy())
        {
            telemetry.addData("Running motor Y front and Back", "Encoders");
            telemetry.update();
        }
        setPower(0);
        sleep(100);
    }


    public void slideRight(double distance)
    {
        MotorFrontX.setPower(0.35);
        MotorBackX.setPower(0.35);

        MotorFrontX.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        MotorBackX.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        int COUNTS = distanceToCounts(distance);

        MotorFrontX.setTargetPosition((MotorFrontX.getCurrentPosition() + (COUNTS)));
        MotorBackX.setTargetPosition((MotorBackX.getCurrentPosition() + (COUNTS)));

        while (opModeIsActive() && MotorBackX.isBusy() && MotorFrontX.isBusy())
        {
            telemetry.addData("Running motor X front and back", "Encoders");
            telemetry.update();
        }
        setPower(0);
        sleep(100);
    }

    public void slideLeft (double distance)
    {
        MotorFrontX.setPower(-0.35);
        MotorBackX.setPower(-0.35);


        telemetry.addData("Reached SlideLeft, Ready to  setMode", "none");
        telemetry.update();

        MotorFrontX.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        telemetry.addData("Front Motor X mode set", "Nothing");
        telemetry.update();

        MotorBackX.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        telemetry.addData("Back Motor X", "");
        telemetry.update();

        int COUNTS = distanceToCounts(distance);

        MotorFrontX.setTargetPosition((MotorFrontX.getCurrentPosition() - (COUNTS)));
        MotorBackX.setTargetPosition((MotorBackX.getCurrentPosition() - (COUNTS)));

        while (opModeIsActive() && MotorBackX.isBusy() && MotorFrontX.isBusy())
        {
            telemetry.addData("Running Motor X Front and Back", "Encoders");
            telemetry.update();
        }
        setPower(0);
        sleep(100);
    }

    public void turnClockwise(double distance)
    {

        MotorFrontX.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        MotorFrontY.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        MotorBackX.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        MotorBackY.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        int COUNTS = distanceToCounts(distance);

        MotorFrontY.setTargetPosition((MotorFrontY.getCurrentPosition() + (COUNTS)));
        MotorBackY.setTargetPosition((MotorBackY.getCurrentPosition() - (COUNTS)));
        MotorFrontX.setTargetPosition((MotorFrontX.getCurrentPosition() + (COUNTS)));
        MotorBackX.setTargetPosition((MotorBackX.getCurrentPosition() - (COUNTS)));

        MotorFrontY.setPower(0.35);
        MotorBackY.setPower(-0.35);
        MotorFrontX.setPower(-0.35);
        MotorBackX.setPower(0.35);


        while (opModeIsActive() && MotorBackY.isBusy() && MotorBackX.isBusy() &&
                MotorFrontX.isBusy() && MotorFrontY.isBusy())
        {
            telemetry.addData("Running", "Encoders");
            telemetry.update();
        }
        setPower(0);
        sleep(100);

    }
    public void turnAntiClockwise(double distance)
    {

        MotorFrontX.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        MotorFrontY.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        MotorBackX.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        MotorBackY.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        int COUNTS = distanceToCounts(distance);

        MotorFrontY.setTargetPosition((MotorFrontY.getCurrentPosition() - (COUNTS)));
        MotorBackY.setTargetPosition((MotorBackY.getCurrentPosition() + (COUNTS)));
        MotorFrontX.setTargetPosition((MotorFrontX.getCurrentPosition() - (COUNTS)));
        MotorBackX.setTargetPosition((MotorBackX.getCurrentPosition() + (COUNTS)));

        MotorFrontY.setPower(-0.35);
        MotorBackY.setPower(0.35);
        MotorFrontX.setPower(0.35);
        MotorBackX.setPower(-0.35);


        while (opModeIsActive() && MotorBackY.isBusy() && MotorBackX.isBusy() &&
                MotorFrontX.isBusy() && MotorFrontY.isBusy())
        {
            telemetry.addData("Running", "Encoders");
            telemetry.update();
        }
        setPower(0);
        sleep(100);

    }

    public void setPower(double power)
    {
        MotorFrontX.setPower(power);
        MotorFrontY.setPower(power);
        MotorBackX.setPower(power);
        MotorBackY.setPower(power);
    }

    public int distanceToCounts(double distance)
    {
        int rotations = (int) Math.round(distance*1000);
        return Math.round(rotations);
    }


}
