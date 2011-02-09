/*
 * Copyright 2011 Michael Bedward
 * 
 * This file is part of jai-tools.
 *
 * jai-tools is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 *
 * jai-tools is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public 
 * License along with jai-tools.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package jaitools.jiffle.runtime;

import java.util.Map;
import javax.media.jai.TiledImage;

import jaitools.CollectionFactory;
import jaitools.imageutils.ImageUtils;
import jaitools.jiffle.Jiffle;

import static org.junit.Assert.*;

/**
 * Base class for unit tests of evaluation of simple statements with a single source
 * and destination image.
 * 
 * @author Michael Bedward
 * @since 1.1
 * @source $URL$
 * @version $Id$
 */
public abstract class StatementsTestBase {

    protected static final int WIDTH = 10;
    protected static final int NUM_PIXELS = WIDTH * WIDTH;
    protected static final double TOL = 1.0e-8;
    
    private final JiffleProgressListener nullListener = new NullProgressListener();
    
    protected Map<String, Jiffle.ImageRole> imageParams;
    protected JiffleDirectRuntime runtimeInstance;

    public interface Evaluator {
        double eval(double val);
    }
    
    protected TiledImage createSequenceImage() {
        TiledImage img = ImageUtils.createConstantImage(WIDTH, WIDTH, 0.0);
        int k = 0;
        for (int y = 0; y < WIDTH; y++) {
            for (int x = 0; x < WIDTH; x++) {
                img.setSample(x, y, 0, k++);
            }
        }
        return img;
    }

    protected void testScript(String script, Evaluator evaluator) throws Exception {
        TiledImage srcImg = createSequenceImage();
        testScript(script, srcImg, evaluator);
    }

    protected void testScript(String script, TiledImage srcImg, Evaluator evaluator) throws Exception {
        imageParams = CollectionFactory.map();
        imageParams.put("dest", Jiffle.ImageRole.DEST);
        imageParams.put("src", Jiffle.ImageRole.SOURCE);

        Jiffle jiffle = new Jiffle(script, imageParams);
        runtimeInstance = (JiffleDirectRuntime) jiffle.getRuntimeInstance();

        testRuntime(srcImg, runtimeInstance, evaluator);
    }

    protected void testRuntime(TiledImage srcImg, JiffleDirectRuntime runtime, Evaluator evaluator) {
        runtime.setSourceImage("src", srcImg);

        TiledImage destImg = ImageUtils.createConstantImage(
                srcImg.getMinX(), srcImg.getMinY(), WIDTH, WIDTH, 0.0);
        runtime.setDestinationImage("dest", destImg);

        runtime.evaluateAll(nullListener);
        assertImage(srcImg, destImg, evaluator);
    }

    protected void assertImage(TiledImage srcImg, TiledImage destImg, Evaluator evaluator) {
        for (int y = srcImg.getMinY(), iy=0; iy < WIDTH; y++, iy++) {
            for (int x = srcImg.getMinX(), ix = 0; ix < WIDTH; x++, ix++) {
                assertEquals(evaluator.eval(srcImg.getSampleDouble(x, y, 0)), destImg.getSampleDouble(x, y, 0), TOL);
            }
        }
    }

}