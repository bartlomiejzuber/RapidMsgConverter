package pl.bajtas.rapidmsgconverter.Service;

import com.auxilii.msgparser.Message;
import com.auxilii.msgparser.MsgParser;
import com.auxilii.msgparser.attachment.Attachment;
import com.auxilii.msgparser.attachment.FileAttachment;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.ElementList;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Bajtas on 26.11.2016.
 */
public class ConverterService {
    private static final Logger LOG = Logger.getLogger(ConverterService.class);

    private MsgParser msgp = new MsgParser();
    private String fromEmail = StringUtils.EMPTY;
    private String fromName = StringUtils.EMPTY;
    private String subject = StringUtils.EMPTY;
    private String body = StringUtils.EMPTY;
    private List<Attachment> atts = null;
    private Document document = new Document();

    public void convertToPdf(String fileSrc) {
        try {
            getData(fileSrc);
            createPdf(fileSrc);
            saveAttachments(fileSrc);
        } catch (IOException | UnsupportedOperationException ex) {
            LOG.error("Error: ", ex);
        }
    }

    private void getData(String fileSrc) throws IOException{
        Message msg = msgp.parseMsg(fileSrc);
        fromEmail = msg.getFromEmail();
        fromName = msg.getFromName();
        subject = msg.getSubject();
        body = msg.getConvertedBodyHTML();
        atts = msg.getAttachments();
    }

    private void createPdf(String fileSrc) {
        fileSrc = fileSrc.substring(0, fileSrc.lastIndexOf('.')) + ".pdf";
        try {
            PdfWriter.getInstance(document, new FileOutputStream(fileSrc));
            document.open();
            document.add(new Paragraph("From: " + fromEmail + " [" + fromName + "]"));
            document.add(new Paragraph("Subject: " + subject));
            document.add(new Paragraph("Body: "));
            ElementList list = XMLWorkerHelper.parseToElementList(body, null);
            for (Element element : list) {
                document.add(element);
            }
        } catch (IOException | DocumentException ex) {
            LOG.error("Error: ", ex);
        } finally {
            document.close();
        }
    }

    private void saveAttachments(String fileSrc) throws IOException {
        fileSrc = fileSrc.substring(0, fileSrc.lastIndexOf('.')) + "-att";
        for (Attachment att : atts) {
            if (att instanceof FileAttachment) {
                FileAttachment file = (FileAttachment) att;
                fileSrc += file.getExtension();

                FileOutputStream out = new FileOutputStream(fileSrc);
                out.write(file.getData());
                out.close();
            }
        }
    }
}
