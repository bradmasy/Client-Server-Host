package PhotoInterface;

import PhotoInterface.Exceptions.WrongInputException;

<<<<<<< HEAD
import javax.sound.sampled.Port;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.InetAddress;
=======
>>>>>>> f30fe0b705d90a4ae9957f07b3765fe38d80ba39
import java.util.Scanner;

public class PhotoUploader {

<<<<<<< HEAD
    Socket connection;
    String host = "http://localhost:8081/";

=======
>>>>>>> f30fe0b705d90a4ae9957f07b3765fe38d80ba39

    PhotoUploader() {


    }

<<<<<<< HEAD
    public void makeConnectionToServer() throws IOException
    {
        System.out.println("Attempting Connection...");
        URL url = new URL(host);
        InetAddress IP = InetAddress.getByName(url.getHost());
        System.out.println(IP);
        String Address = IP.getHostAddress();
        System.out.println(Address);
        // need to connect with a socket...
        // need IP address of server, the port number,
        int port = url.getPort();
        System.out.println("port number is: " + port);
        connection = new Socket(IP,port);
        System.out.println("Connection made to: " + connection.getInetAddress().getHostName());
        
=======

    public void makeConnectionToServer()
    {

>>>>>>> f30fe0b705d90a4ae9957f07b3765fe38d80ba39

    }

    /**
     *
     * @return
     */
<<<<<<< HEAD
    public boolean uploadPhoto() throws IOException {

        makeConnectionToServer();


        return true;
=======
    public boolean uploadPhoto()
    {
        boolean continueProgram = false;



        return continueProgram;
>>>>>>> f30fe0b705d90a4ae9957f07b3765fe38d80ba39
    }

    /**
     *
     * @param userInput
     * @return
     */
<<<<<<< HEAD
    public boolean chooseOption(final int userInput) throws IOException {
=======
    public boolean chooseOption(final int userInput)
    {
>>>>>>> f30fe0b705d90a4ae9957f07b3765fe38d80ba39
        boolean continueProgram = true;

        switch (userInput){
            case 1:
                continueProgram = uploadPhoto();
            case 2:
                continueProgram = false; // will trigger an exit event.
        }

        return continueProgram;
    }

<<<<<<< HEAD
    public void displayMenu() throws WrongInputException, IOException {
=======
    public void displayMenu() throws WrongInputException {
>>>>>>> f30fe0b705d90a4ae9957f07b3765fe38d80ba39
        boolean continueProgram = true;
        int userDecision;
        Scanner userScanner = new Scanner(System.in);

<<<<<<< HEAD
        makeConnectionToServer(); // connect to the server.

        while (continueProgram) {

=======
        while (continueProgram) {
>>>>>>> f30fe0b705d90a4ae9957f07b3765fe38d80ba39
            initiateInterface();

            try {
                if (userScanner.hasNextInt()) {

                    userDecision = userScanner.nextInt();
                    continueProgram = chooseOption(userDecision);
                }
                else {
                    throw new WrongInputException("\nIncorrect Input. Please Try Again\n");
                }
<<<<<<< HEAD
            } catch (WrongInputException | IOException e) {
=======
            } catch (WrongInputException e) {
>>>>>>> f30fe0b705d90a4ae9957f07b3765fe38d80ba39
                System.out.println(e.getMessage());
                userScanner.next();
            }
        }

        System.out.println("Exiting The Program...2");
    }

    public void initiateInterface() {
        System.out.println("Welcome To The Photo Uploader!");
        System.out.println("------------------------------");
        System.out.println("Please Choose an Option:");
        System.out.println("1. Upload Photo");
        System.out.println("2. Exit");
    }


}
