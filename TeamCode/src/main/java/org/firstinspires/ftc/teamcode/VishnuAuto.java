package org.firstinspires.ftc.teamcode;
import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.DogeCV;
import com.disnodeteam.dogecv.detectors.roverrukus.SamplingOrderDetector;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Autonomous(name = "YeEt")
public class VishnuAuto extends LinearOpMode {
    float y;
    float x;
    boolean lb;
    boolean rb;
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
    public void runOpMode() throws InterruptedException {
        SamplingOrderDetector detector = new SamplingOrderDetector();
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

        MotorFrontX = hardwareMap.dcMotor.get("fx");
        MotorBackX = hardwareMap.dcMotor.get("bx");
        MotorBackY = hardwareMap.dcMotor.get("by");
        MotorFrontY = hardwareMap.dcMotor.get("fy");

        MotorExtend = hardwareMap.dcMotor.get("extend");
        MotorArm = hardwareMap.dcMotor.get("arm");
        MotorLand = hardwareMap.dcMotor.get("land");

        MotorFrontX.setDirection(DcMotorSimple.Direction.FORWARD);
        MotorBackX.setDirection(DcMotorSimple.Direction.REVERSE);
        MotorFrontY.setDirection(DcMotorSimple.Direction.FORWARD);
        MotorBackY.setDirection(DcMotorSimple.Direction.REVERSE);

        MotorExtend.setDirection(DcMotorSimple.Direction.REVERSE);
        MotorArm.setDirection(DcMotorSimple.Direction.FORWARD);
        MotorLand.setDirection(DcMotorSimple.Direction.REVERSE);

        MotorExtend.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        MotorExtend.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        MotorArm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        MotorArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        MotorLand.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        MotorLand.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
/*
        MotorLand.setTargetPosition(MotorLand.getCurrentPosition() - 2000);
        MotorLand.setPower(0.3);
        MotorLand.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        while (MotorLand.isBusy()){
            telemetry.addData("StartMotorLand:", MotorLand.getCurrentPosition());
            telemetry.update();
        }
        MotorLand.setPower(0);

*/
            MotorLand.setTargetPosition(MotorLand.getCurrentPosition() + 50000);
            MotorLand.setPower(1.0);
            if (MotorLand.isBusy() && opModeIsActive()){
                telemetry.addData("Lander Pos: ",MotorLand.getCurrentPosition());
                telemetry.update();
            }
            MotorLand.setPower(0);

            MotorFrontY.setPower(0.35);
            MotorBackY.setPower(0.35);
            sleep(500);

            if (detector.getCurrentOrder().toString() == "LEFT")
            {
                telemetry.addData("Left", null);
                telemetry.update();

                //slides left
                MotorBackX.setPower(1.0);
                sleep(50);
                MotorFrontX.setPower(1.0);
                sleep(500);

                //move forward
                MotorBackY.setPower(1.0);
                MotorFrontY.setPower(1.0);
                sleep(500);
            }
            else

                if (detector.getCurrentOrder().toString() == "RIGHT")
                {
                telemetry.addData("Right", null);
                telemetry.update();

                //slides right
                MotorFrontX.setPower(-1.0);
                sleep(500);
                MotorBackX.setPower(-1.0);
                sleep(500);

                MotorBackY.setPower(1.0);
                MotorFrontY.setPower(1.0);
                sleep(500);
            }
            else
                if (detector.getCurrentOrder().toString() == "CENTER")
                {
                telemetry.addData("Center",null);
                telemetry.update();
                //go backwards
            }
            else
                if (detector.getCurrentOrder().toString() == "UNKNOWN")
            {
                MotorBackX.setPower(0);
                MotorBackY.setPower(0);
                MotorFrontX.setPower(0);
                MotorFrontY.setPower(0);
            }
/*
            MotorFrontX.setPower(0.5);
            MotorFrontY.setPower(0.5);
            MotorBackX.setPower(-0.5);
            MotorBackY.setPower(-0.5);
            sleep(2000);*/
    }
}
