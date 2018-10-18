package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

public class Movement {
    private DcMotor topLeft = null;
    private DcMotor topRight = null;
    private DcMotor bottomLeft = null;
    private DcMotor bottomRight = null;

    public void forward(double power, int sleepTime) throws InterruptedException{
        bottomLeft.setPower(power);
        topRight.setPower(power);
        Thread.sleep(sleepTime);
    }

    public void backward(double power, int sleepTime) throws InterruptedException{
        bottomLeft.setPower(power);
        topRight.setPower(power);
        Thread.sleep(sleepTime);
    }

    public void leftStrafe(double power, int sleepTime) throws InterruptedException{
        topLeft.setPower(power);
        bottomRight.setPower(power);
        Thread.sleep(sleepTime);
    }

    public void rightStrafe(double power, int sleepTime) throws InterruptedException{
        topLeft.setPower(power);
        bottomRight.setPower(power);
        Thread.sleep(sleepTime);
    }

    public void leftTurn(double power, int sleepTime) throws InterruptedException{
        topLeft.setPower(power);
        topRight.setPower(power);
        bottomLeft.setPower(power);
        bottomRight.setPower(power);
        Thread.sleep(sleepTime);
    }

    public void rightTurn(double power, int sleepTime) throws InterruptedException{
        topLeft.setPower(power);
        topRight.setPower(power);
        bottomLeft.setPower(power);
        bottomRight.setPower(power);
        Thread.sleep(sleepTime);
    }
}
