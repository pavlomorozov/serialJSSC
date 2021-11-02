package org.example;

import jssc.*;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws SerialPortException {
        System.out.println("Serial port jssc demo");
        String[] portNames = SerialPortList.getPortNames();
        if (portNames.length == 0) {
            System.out.println("There are no serial-ports :( You can use an emulator, such ad VSPE, to create a virtual serial port.");
            System.out.println("Press Enter to exit...");
            try {
                System.in.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        System.out.println("> Found ports:");
        for (int i = 0; i < portNames.length; i++) {
            System.out.println(portNames[i]);
        }

        String portName = args[0];
        String message = args[1];

        SerialPort serialPort = new SerialPort(portName);
        try {
            System.out.println("> Will print to port: " + portName);
            serialPort.openPort();
            System.out.println("Port opened");

            serialPort.setParams(SerialPort.BAUDRATE_9600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);

            Thread.sleep(300);

            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN |
                    SerialPort.FLOWCONTROL_RTSCTS_OUT);

            Thread.sleep(300);

            serialPort.addEventListener(new SerialPortEventListener() {
                @Override
                public void serialEvent(SerialPortEvent serialPortEvent) {
                    System.out.println("got an event");
                }
            }, SerialPort.MASK_RXCHAR);

            Thread.sleep(1000);

            System.out.println("> Will send the message: " + message);
            serialPort.writeString(message);
            System.out.println("Message sent");

            Thread.sleep(1000);
        }
        catch (Throwable t) {
            t.printStackTrace();
        } finally {
            System.out.println("> Will close port");
            serialPort.closePort();
            System.out.println("Port closed");
        }
    }
}
