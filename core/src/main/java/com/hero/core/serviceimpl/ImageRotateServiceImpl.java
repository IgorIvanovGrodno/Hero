package com.hero.core.serviceimpl;

import com.day.image.Layer;
import com.hero.core.service.ImageRotateService;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.osgi.service.event.EventConstants;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Dictionary;
import java.util.Hashtable;

@Component(immediate = true, service = ImageRotateService.class)
public class ImageRotateServiceImpl implements ImageRotateService {

    @Reference
    protected EventAdmin eventAdmin;

//    @Property(label = "Shop consumer mini basket endpoint",
//            value = "https://shop.ci.ee.co.uk/cart/rollover/MiniCart/0",
//            description =
//                    "The endpoint for the EE Shop mini basket used by consumers.")
    private static final String ENDPOINT_CONSUMER_URL_NAME =
            "minibasket.consumer.endpoint.url";
    private static final String IMAGE_ACTION = "image.action";
    private  String imageAction;

    protected final void activate(final ComponentContext context) {
        final Dictionary<String, Object> properties = context.getProperties();

        if (properties != null) {
            this.imageAction = String.valueOf(properties.get(IMAGE_ACTION));
        }
    }

    @Override
    public Layer rotateNodeImage(Node imageNode) throws IOException {
        InputStream inputStream = null;
        Layer layer = null;
        try {
            inputStream = imageNode.getProperty("jcr:data").getBinary().getStream();
            layer = new Layer(inputStream);
            layer.rotate(180);
        } catch (RepositoryException | IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return  layer;
    }

    @Override
    public Layer grayscaleNodeImage(Node imageNode) throws IOException {
        Dictionary properties = new Hashtable();
        properties.put("title", "title");
        properties.put("path", "/some/path");
        Event event = new Event("com/hero", properties);
        if (event != null) {
            eventAdmin.postEvent(event);
        }
        InputStream inputStream = null;
        Layer layer = null;
        try {
            inputStream = imageNode.getProperty("jcr:data").getBinary().getStream();
            layer = new Layer(inputStream);
            layer.grayscale();
        } catch (RepositoryException | IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return  layer;
    }

    @Override
    public void writeImage(Layer image, OutputStream outputStream, String mimeType, double quality) throws IOException {
        image.write(mimeType, quality, outputStream);
    }

    @Override
    public Layer imageAction(Node imageNode) throws IOException {
        Layer resultImage;
        switch (this.imageAction) {
            case "rotate":
                resultImage = rotateNodeImage(imageNode);
                break;
            case "grayscale":
                resultImage = grayscaleNodeImage(imageNode);

                break;
            default:
                resultImage = null;
        }
        return resultImage;
    }
}
