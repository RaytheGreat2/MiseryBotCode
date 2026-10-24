package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class teleop extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        // By setting these values to new Gamepad(), they will default to all
        // boolean values as false and all float values as 0
        Gamepad currentGamepad1 = new Gamepad();
        Gamepad currentGamepad2 = new Gamepad();

        Gamepad previousGamepad1 = new Gamepad();
        Gamepad previousGamepad2 = new Gamepad();

        // other initialization code goes here
        // Declare our motors
        // Make sure your ID's match your configuration
        DcMotor frontLeftMotor = hardwareMap.dcMotor.get("frontLeftMotor");
        DcMotor backLeftMotor = hardwareMap.dcMotor.get("backLeftMotor");
        DcMotor frontRightMotor = hardwareMap.dcMotor.get("frontRightMotor");
        DcMotor backRightMotor = hardwareMap.dcMotor.get("backRightMotor");
        DcMotor leftIntakeMotor = hardwareMap.dcMotor.get("leftIntakeMotor");
        DcMotor rightIntakeMotor = hardwareMap.dcMotor.get("rightIntakeMotor");
        DcMotor armMotor = hardwareMap.dcMotor.get("armMotor");
        CRServo sungSungSungServo = hardwareMap.crservo.get("evan");
        // Reverse the right side motors. This may be wrong for your setup.
        // If your robot moves backwards when commanded to go forwards,
        // reverse the left side instead.
        // See the note about this earlier on this page.
        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            // Store the gamepad values from the previous loop iteration in
            // previousGamepad1/2 to be used in this loop iteration.
            // This is equivalent to doing this at the end of the previous
            // loop iteration, as it will run in the same order except for
            // the first/last iteration of the loop.
            previousGamepad1.copy(currentGamepad1);
            previousGamepad2.copy(currentGamepad2);

            // Store the gamepad values from this loop iteration in
            // currentGamepad1/2 to be used for the entirety of this loop iteration.
            // This prevents the gamepad values from changing between being
            // used and stored in previousGamepad1/2.
            currentGamepad1.copy(gamepad1);
            currentGamepad2.copy(gamepad2);

            // Main teleop loop goes here
            double y = -gamepad1.left_stick_y; // Remember, Y stick value is reversed
            double x = gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
            double rx = gamepad1.right_stick_x;

            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio,
            // but only if at least one is out of the range [-1, 1]
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = (y + x + rx) / denominator;
            double backLeftPower = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower = (y + x - rx) / denominator;

            // for arm rotation
            if (gamepad2.left_stick_y > 0) {
                armMotor.setPower(0.4 * gamepad2.left_stick_y);
            } else if (gamepad2.left_stick_y < 0) {
                armMotor.setPower(0.4 * gamepad2.left_stick_y);
            } else {
                armMotor.setPower(0.1);
            }

            // intake
            if (currentGamepad2.a && !previousGamepad2.a) {
                leftIntakeMotor.setPower(0.3);
                rightIntakeMotor.setPower(-0.3);
            }

            // stop intake/outtake
            if (currentGamepad2.b && !previousGamepad1.b) {
                leftIntakeMotor.setPower(0);
                rightIntakeMotor.setPower(0);
            }

            // start outtake
            if (currentGamepad2.x && !previousGamepad1.x) {
                leftIntakeMotor.setPower(-0.48);
                rightIntakeMotor.setPower(0.48);

            }

            // Move spindexer down one slot
            if (currentGamepad2.dpad_left && !previousGamepad2.dpad_left) {
                sungSungSungServo.setPower(0.28);
                sleep(300);
                sungSungSungServo.setPower(0);
            }

            // Move spindexer up one slot
            if (currentGamepad2.dpad_right && !previousGamepad2.dpad_right) {
                sungSungSungServo.setPower(-0.28);
                sleep(300);
                sungSungSungServo.setPower(0);
            }

            // rotate spindexer down
            sungSungSungServo.setPower(0.3 * gamepad2.left_trigger);

            // rotate spindexer up
            sungSungSungServo.setPower(-0.3 * gamepad2.right_trigger);


            frontLeftMotor.setPower(frontLeftPower);
            backLeftMotor.setPower(-backLeftPower);
            frontRightMotor.setPower(frontRightPower);
            backRightMotor.setPower(backRightPower);
        }
    }
}