# Microsemi_SmartFusion2_RoboticArm
Example Microsemi SmartFusion 2 FPGA Application demo: operation of a sample manipulating robotic arm and accompanying Android control application using UART over Bluetooth Interfacing

Example Application note for MicroSemi SmartFusion 2 - Robotic Sample Manipulator

Created by Michael Klopfer, Ph.D., Crystal Lai, Zihan (Bronco) Chen, Jonathan Tang, Priscilla Luu, & Prof. G.P. Li

University of California, Irvine - California Plug Load Research Center (CalPlug)

Copyright (C) The Regents of the University of California, 2017

Released into the public domain. 


Demonstration video:  https://www.youtube.com/watch?v=K9a-UCFHEOE&feature=youtu.be

Installation:

This folder contains an update to the most recent Libero project for the Embedded World robotic arm demo in addition to an application note explaining the operation of the project.

Prior to Release on Github, the following changes were made to the project:
1. CoreGPIO was modified to use individual interrupts for the General Purpose Inputs (GPI[2:0]) instead of the OR'd implementation
   used in the previous version of the design.  GPI[0] is connected to SW1 on the Future Electronics Creative board.  INT[0] is 
   connected to the Cortex-M3 NVIC interrupt 1 (MSS_INT_F2M[1]).  GPI[1] is connected to SW2 on the Future Electronics Creative board.    INT[1] is  connected to the Cortex-M3 NVIC interrupt 2 (MSS_INT_F2M[2]).  GPI[2] is connected to pin 3 of the Mikro Bus Adapter 5 connector (MIKRO_CS).
2. CorePWM was updated to the latest version (v4.4.101).
The pin assignments were unchanged from the previous release.

A SoftConsole project based on the CoreGPIO Interrupt Blink sample project (SF2_GNU_SC4_interrupt_blink) was used to test the interrrupts.
The followign changes were made to the original sample project.
1. Driver configuration files and firmware drivers exported from Libero were imported into the sample project.
2. The fabric GPIO block was configured as follows:
   - GPIO_0: inout; GPIO interrupt set as a level triggered interrupt (high)
   - GPIO_1: input; GPIO interrupt set as an edge triggered interrupt (high)
    Interrupt service routines were added for the General Purpose Input interrupts (INT[0] and INT[1]).
3. The Cortex-M3 NVIC was configured so that IRQ2 had a higher priority than IR1.  If both interrupts are received IRQ2 is serviced.
4. Semihosting was used to send messages to a terminal to indicate switches were pressed.

Operation (Usng Open OCD Debugger in SoftConsole or flashed into eNVM using production linker scripts):

After launching the debugger the SoftConsole terminal will display: 
"SF2_GNU_SC4_interrupt_blink_project
CoreGPIO Initialized"
LED1 will blink together (LED1 will be red and LED2 will be green).  

If SW1 is pressed, the terminal will display 
"Switch 1 pressed - FabricIRq1"
LED1 will turn off and LED2 will be green.  The LEDs will continue this way if SW1 remains depressed.  When SW1 is released
LED1 and LED2 will toggle rather than blinking together as previously (LED1 will be red; LED2 will be green). One must reset the board to reestablish normal operation.

If SW2 is pressed (with SW1 depressed or released), the terminal will display
"Switch 2 pressed - FabricIRq2; Restart the debugger to run the application"
LED1 and LED2 will both be red.  The ISR loops forever in this state.  The debugger must be relaunched to get out of this state.
(This was to simulate pressing the emergency stop button).  One must reset the board to reestablish normal operation.

The Android application was tested on a Samsung Galaxy Tab SM-T230NU and uses classic Bluetooth communication.

The project folder contains an update of the design for the 2017 Embedded World demo created by UC Irvine.
The sample projects in this folder have been modified to match the hardware.
In addition, the SF2_GNU_SC4_interrupt_blink and SF2_GNU_SC4_pwm_slow_blink projects have been
further modified to support Semihosting as described in the SoftConsole 4.0 Release notes.

Tim McCarthy
February 14, 2017

Note:  This folder contains the most recent Libero and SoftConsole projects for the Embedded World robotic arm demo. The following changes were made to the project:
1. The Libero SoC project was renamed Servo-arm2 (the project was previously named PWM_8ch_16b_Creative).
2. The SoftConsole project was renamed servoarm  (the project was originally named SF2_GNU_SC4_pwm_slow_blink).

No changes were made to the Libero project or SoftConsole project other than renaming the projects.  Please see the revision notes file about potential issues with hard coded path that may need to be updated.

Tim McCarthy
March 6, 2017

Tim McCarthy + Michael Klopfer
Final Stable Release: April 18, 2017
