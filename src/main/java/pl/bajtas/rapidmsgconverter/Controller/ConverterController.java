package pl.bajtas.rapidmsgconverter.Controller;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import pl.bajtas.rapidmsgconverter.Service.ConverterService;

/**
 * Created by Bajtas on 26.11.2016.
 */
public class ConverterController {
    private static final Logger LOG = Logger.getLogger(ConverterController.class);

    private String fileSrc = StringUtils.EMPTY;
    private ConverterService converterService = new ConverterService();

    public ConverterController(String[] args) {
        fileSrc = args[0];
    }

    public void convertToPdf() {
        converterService.convertToPdf(fileSrc);
    }
}
