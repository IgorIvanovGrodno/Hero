package com.hero.core.service;

import com.day.image.Layer;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.io.IOException;
import java.io.OutputStream;


public interface ImageRotateService {
    Layer rotateNodeImage(Node imageNode) throws RepositoryException, IOException;

    Layer grayscaleNodeImage(Node imageNode) throws IOException;

    void  writeImage(Layer image, OutputStream outputStream, String mimeType, double quality) throws IOException;

    Layer imageAction(Node imageNode) throws IOException;
}
