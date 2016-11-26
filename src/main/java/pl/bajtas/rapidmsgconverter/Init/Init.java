package pl.bajtas.rapidmsgconverter.Init;

import org.apache.log4j.Logger;
import pl.bajtas.rapidmsgconverter.Controller.ConverterController;

import java.util.Arrays;

/**
 * Created by Bajtas on 26.11.2016.
 */
public class Init {
    private static final Logger LOG = Logger.getLogger(Init.class);

    public static void main(String[] args) {
        LOG.info("Converting files: " + Arrays.toString(args));
        if (args != null) {
            ConverterController ctrl = new ConverterController(args);
            ctrl.convertToPdf();
        }
        LOG.info("Files converted.");
    }
}
