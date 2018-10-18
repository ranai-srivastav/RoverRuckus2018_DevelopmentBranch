package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import static android.os.SystemClock.sleep;


/**
 * TeleOp
 */
@TeleOp(name = "Demo Robot")
public class TeleOPDemoRobot extends OpMode {
    //Initialising all necessary variables

    DcMotor Motor1;
    DcMotor Motor2;

    @Override
    public void init()
    {
        telemetry.addData("Initialised" ,"nothing");
        telemetry.update();

        // defining all the hardware
        Motor1 = hardwareMap.dcMotor.get("fx");
        Motor2 = hardwareMap.dcMotor.get("fy");

        Motor1.setDirection(DcMotorSimple.Direction.REVERSE);
        Motor2.setDirection(DcMotorSimple.Direction.FORWARD);

        //armExtendPosition = MotorExtend.getCurrentPosition();
    }


    @Override
    public void loop() {
        //telemetry.addData("Enters loop to define power" ,"nothing");
        //telemetry.update();

        float y = gamepad1.left_stick_y;

        // Reset variables
        float powerWheels = 0;


        // Handle regular movement
        powerWheels += y;


        // Handle turning movement
        double maxX = (double)powerWheels;


        Motor1.setPower(maxX);
        Motor2.setPower(maxX);
        telemetry.addData("Power X" ,maxX);
        telemetry.update();

    }


}



