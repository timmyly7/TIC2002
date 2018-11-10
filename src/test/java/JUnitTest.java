import com.sun.media.sound.InvalidDataException;
import exception.InvalidCommandException;
import exception.InvalidDateRangeException;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;

public class JUnitTest {
    @Test
    public void testScannerProcess() {

        try {
            String showCommand = "show -todo";
            String addCommand = "add -schedule -test add schedule|20180101 08:30|20180108 09:50";
            String editCommand = "edit -schedule -1 -test edit|x|x";
            String deleteCommand ="delete -schedule -1";
            String exit = "exit";
            InputStream stdin = System.in;
            System.setIn(new ByteArrayInputStream(showCommand.getBytes()));
            System.setIn(new ByteArrayInputStream(addCommand.getBytes()));
            System.setIn(new ByteArrayInputStream(editCommand.getBytes()));
            System.setIn(new ByteArrayInputStream(deleteCommand.getBytes()));
            System.setIn(new ByteArrayInputStream(exit.getBytes()));
            Scanner scanner = new Scanner(System.in);
            System.setIn(stdin);
            ScannerProcess.processIncomingMessage(scanner);
        } catch (InvalidDataException e) {
            e.printStackTrace();
        } catch (InvalidDateRangeException e) {
            e.printStackTrace();
        } catch (InvalidCommandException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAddSchedule() {

        try {
            String data = "add -schedule -test add schedule|20180101 08:30|20180108 09:50";
            String exit = "exit";
            InputStream stdin = System.in;
            System.setIn(new ByteArrayInputStream(data.getBytes()));
            System.setIn(new ByteArrayInputStream(exit.getBytes()));
            Scanner scanner = new Scanner(System.in);
            System.setIn(stdin);
            ScannerProcess.processIncomingMessage(scanner);
        } catch (InvalidDataException e) {
            e.printStackTrace();
        } catch (InvalidDateRangeException e) {
            e.printStackTrace();
        } catch (InvalidCommandException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testShowTodo() {

        try {
            String data = "show -todo";
            String exit = "exit";
            InputStream stdin = System.in;
            System.setIn(new ByteArrayInputStream(data.getBytes()));
            System.setIn(new ByteArrayInputStream(exit.getBytes()));
            Scanner scanner = new Scanner(System.in);
            System.setIn(stdin);
            ScannerProcess.processIncomingMessage(scanner);
        } catch (InvalidDataException e) {
            e.printStackTrace();
        } catch (InvalidDateRangeException e) {
            e.printStackTrace();
        } catch (InvalidCommandException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testExport() {

        try {
            String data = "export -C:\\Users\\Timmy\\Desktop\\School\\2018\\Sem 1\\TIC 2002 Java -text2";
            String exit = "exit";
            InputStream stdin = System.in;
            System.setIn(new ByteArrayInputStream(data.getBytes()));
            System.setIn(new ByteArrayInputStream(exit.getBytes()));
            Scanner scanner = new Scanner(System.in);
            System.setIn(stdin);
            ScannerProcess.processIncomingMessage(scanner);
        } catch (InvalidDataException e) {
            e.printStackTrace();
        } catch (InvalidDateRangeException e) {
            e.printStackTrace();
        } catch (InvalidCommandException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testImport() {

        try {
            String data = "import -C:\\Users\\Timmy\\Desktop\\School\\2018\\Sem 1\\TIC 2002 Java -text2";
            String exit = "exit";
            InputStream stdin = System.in;
            System.setIn(new ByteArrayInputStream(data.getBytes()));
            System.setIn(new ByteArrayInputStream(exit.getBytes()));
            Scanner scanner = new Scanner(System.in);
            System.setIn(stdin);
            ScannerProcess.processIncomingMessage(scanner);
        } catch (InvalidDataException e) {
            e.printStackTrace();
        } catch (InvalidDateRangeException e) {
            e.printStackTrace();
        } catch (InvalidCommandException e) {
            e.printStackTrace();
        }
    }
}
