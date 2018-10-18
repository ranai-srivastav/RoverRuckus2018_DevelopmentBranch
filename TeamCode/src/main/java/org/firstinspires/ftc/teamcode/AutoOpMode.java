package org.firstinspires.ftc.teamcode;

import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.DogeCV;
import com.disnodeteam.dogecv.detectors.roverrukus.SamplingOrderDetector;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Autonomous(name = "Auto: Basic", group = "Autonomous")
public class AutoOpMode extends LinearOpMode {

    DcMotor MotorFrontX;
    DcMotor MotorFrontY;
    DcMotor MotorBackX;
    DcMotor MotorBackY;

    DcMotor MotorArm;
    DcMotor MotorExtend;
    DcMotor MotorLand;
    private SamplingOrderDetector detector;

    char goldPosition;

    @Override
    public void runOpMode() throws InterruptedException
    {

        initialize();

        waitForStart();

        //start opMode loop. Main Code Goes Here.
        while (opModeIsActive())
        {
            telemetry.addData("Gold Position: ", goldPosition);
            telemetry.update();

            //for moving distance
            double d = 0;
            //land  robot
            robotLanding ();

            if (goldPosition == 'n'){
                getGoldPosition();
            }
            detector.disable();

            //Place Robominion marker in the safe depot
            placeMarker();

            //If gold position not detected then find again after landing

            telemetry.addData("Gold Position: ", goldPosition);
            telemetry.update();

            //Move forward for dropping marker
            d = 1.5;
            moveForward(d);

            // Drop marker
            placeMarker();

            //Move to the gold minral position
            d=1.25;

            if(goldPosition == 'c'){
                //Do nothing as robot is already at center position
            }else if(goldPosition == 'l'){
                slideLeft(d);
            }else if(goldPosition == 'r'){
                slideRight(d);
            }

            //Knock gold ony if gold position found
            d= 0.8;
            if(goldPosition != 'n'){
                moveForward(d);
                moveBackward(d);
            }

            //Go to crater




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

        MotorExtend = hardwareMap.dcMotor.get("extend");
        MotorArm = hardwareMap.dcMotor.get("arm");
        MotorLand = hardwareMap.dcMotor.get("land");

        // Reverse the motor that runs backwards when connected directly to the battery
        MotorFrontX.setDirection(DcMotor.Direction.REVERSE);
        MotorFrontY.setDirection(DcMotor.Direction.REVERSE);
        MotorBackX.setDirection(DcMotor.Direction.FORWARD);
        MotorBackY.setDirection(DcMotor.Direction.FORWARD);

        MotorExtend.setDirection(DcMotor.Direction.FORWARD);
        MotorArm.setDirection(DcMotor.Direction.FORWARD);
        MotorLand.setDirection(DcMotor.Direction.FORWARD);

        //Motors set to run with encoders
        MotorBackX.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        MotorBackY.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        MotorFrontX.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        MotorFrontY.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        MotorExtend.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        MotorArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        MotorLand.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        detector = new SamplingOrderDetector();
        detector.init(hardwareMap.appContext, CameraViewDisplay.getInstance());
        detector.useDefaults();

        detector.downscale = 0.4; // How much to downscale the input frames

        // Optional Tuning
        detector.areaScoringMethod = DogeCV.AreaScoringMethod.MAX_AREA; // Can also be PERFECT_AREA
        //detector.perfectAreaScorer.perfectArea = 10000; // if using PERFECT_AREA scoring
        detector.maxAreaScorer.weight = 0.001;

        detector.ratioScorer.weight = 15;
        detector.ratioScorer.perfectRatio = 1.0;


        detector.enable();
        getGoldPosition();

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

    public void moveArmForward(double distance)
    {
        MotorArm.setPower(0.65);

        MotorArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        telemetry.addData("Calling count", "");
        telemetry.update();

        int COUNTS = distanceToCounts(distance);

        telemetry.addData("Counts", COUNTS);
        telemetry.update();

        telemetry.addData("MotorArm", MotorArm.getCurrentPosition());
        telemetry.update();

        MotorArm.setTargetPosition((MotorArm.getCurrentPosition() + (COUNTS)));


        while (opModeIsActive() && MotorArm.isBusy())
        {
            telemetry.addData("Running motor arm forward", "Encoders");
            telemetry.update();
        }
        setPower(0);
        sleep(100);
    }

    public void moveArmBackward(double distance)
    {
        MotorArm.setPower(-0.65);

        MotorArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        telemetry.addData("Calling count", "");
        telemetry.update();

        int COUNTS = distanceToCounts(distance);

        telemetry.addData("Counts", COUNTS);
        telemetry.update();

        telemetry.addData("MotorArm", MotorArm.getCurrentPosition());
        telemetry.update();

        MotorArm.setTargetPosition((MotorArm.getCurrentPosition() - (COUNTS)));


        while (opModeIsActive() && MotorArm.isBusy())
        {
            telemetry.addData("Running motor arm backward", "Encoders");
            telemetry.update();
        }
        setPower(0);
        sleep(100);
    }

    public void extendArmForward(double distance)
    {
        MotorExtend.setPower(0.65);

        MotorExtend.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        telemetry.addData("Calling count", "");
        telemetry.update();

        int COUNTS = distanceToCounts(distance);

        telemetry.addData("Counts", COUNTS);
        telemetry.update();

        telemetry.addData("MotorExtend", MotorArm.getCurrentPosition());
        telemetry.update();

        MotorExtend.setTargetPosition((MotorExtend.getCurrentPosition() + (COUNTS)));


        while (opModeIsActive() && MotorExtend.isBusy())
        {
            telemetry.addData("Running motor extend forward", "Encoders");
            telemetry.update();
        }
        setPower(0);
        sleep(100);
    }

    public void extendArmBackward(double distance)
    {
        MotorExtend.setPower(-0.65);

        MotorExtend.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        telemetry.addData("Calling count", "");
        telemetry.update();

        int COUNTS = distanceToCounts(distance);

        telemetry.addData("Counts", COUNTS);
        telemetry.update();

        telemetry.addData("MotorExtend", MotorArm.getCurrentPosition());
        telemetry.update();

        MotorExtend.setTargetPosition((MotorExtend.getCurrentPosition() - (COUNTS)));


        while (opModeIsActive() && MotorExtend.isBusy())
        {
            telemetry.addData("Running motor extend forward", "Encoders");
            telemetry.update();
        }
        setPower(0);
        sleep(100);
    }

    public void robotLanding (){

        //Landing
        MotorLand.setPower(0.35);

        double dist = 0.6;
        int COUNTS = distanceToCounts (dist);

        MotorLand.setTargetPosition((MotorLand.getCurrentPosition() + (COUNTS)));

        while (opModeIsActive() && MotorLand.isBusy())
        {
            telemetry.addData("Robot Landing", "Encoders");
            telemetry.update();
        }
        setPower(0);
        sleep(100);

        //Release handle
        slideLeft(0.3);

        //Get landing arm down
        MotorLand.setPower(-0.6);

        MotorLand.setTargetPosition((MotorLand.getCurrentPosition() - (COUNTS)));

        while (opModeIsActive() && MotorLand.isBusy())
        {
            telemetry.addData("Taking Landing arm down", "Encoders");
            telemetry.update();
        }
        setPower(0);
        sleep(100);

        //Come back to center position
        slideRight(0.3);
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


    public void getGoldPosition() {
        goldPosition = 'n';
        int center = 0;
        int left = 0;
        int right = 0;

        try {
            for (int i = 0; i < 100; i++) {
                if (detector.getCurrentOrder().toString() == "LEFT") {
                    left++;
                } else if (detector.getCurrentOrder().toString() == "RIGHT") {
                    right++;
                } else if (detector.getCurrentOrder().toString() == "CENTER") {
                    center++;
                }
                telemetry.addData("Order", detector.getCurrentOrder().toString());
                telemetry.update();
                sleep (100);
            }

        }catch (Exception e) {
            telemetry.addData("Exception: ", e);
        }

        if(center>left && center>right){
            goldPosition = 'c';
        } else if(left>center && left>right){
            goldPosition = 'l';
        } else if(right>center && right>left){
            goldPosition = 'r';
        }

        telemetry.addData("Gold Position: ", goldPosition);
        telemetry.addData("Last Order", detector.getLastOrder().toString()); // The last known result
        telemetry.addData("Center: ", center);
        telemetry.addData("left: ", left);
        telemetry.addData("Right: ", right);
        telemetry.update();
    }
    public void placeMarker()
    {
        moveArmForward(0.4);
        extendArmForward(3);
        sleep(100);
        moveArmForward(0.5);
        extendArmBackward(0.5);
        sleep(100);
        moveArmBackward(0.9);
        sleep(100);
        extendArmBackward(2.5);
        sleep(100);

        telemetry.addData("Marker Placed", "Encoders");
        telemetry.update();


    }

}
