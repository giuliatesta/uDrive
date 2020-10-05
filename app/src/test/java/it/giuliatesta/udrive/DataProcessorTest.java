package it.giuliatesta.udrive;

import org.junit.Test;

import it.giuliatesta.udrive.accelerometer.AccelerometerDataEvent;

import static org.junit.Assert.*;

public class DataProcessorTest {

    @Test
    public void name() {

        DataProcessor dataProcessor = new DataProcessor();
        System.out.println("dataProcessor = " + dataProcessor);
        AccelerometerDataEvent res = dataProcessor.calculateData(0.0, -4.0, 2.0);

        System.out.println("res = " + res);
    }
}