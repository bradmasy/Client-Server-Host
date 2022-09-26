package PhotoInterface;

import PhotoInterface.Exceptions.WrongInputException;

<<<<<<< HEAD
import java.io.IOException;

public class Main {

    public static void main(final String[] args){
        PhotoUploader photoUploader = new PhotoUploader();

        try
        {
            photoUploader.displayMenu();
        }
        catch(IOException|WrongInputException e)
        {
            System.out.println(e.getMessage());
        }
=======
public class Main {

    public static void main(final String[] args) throws WrongInputException {
        PhotoUploader photoUploader = new PhotoUploader();
        photoUploader.displayMenu();
>>>>>>> f30fe0b705d90a4ae9957f07b3765fe38d80ba39

    }
}
