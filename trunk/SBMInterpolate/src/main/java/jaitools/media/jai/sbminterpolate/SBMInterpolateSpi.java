/*
 * Copyright 2009 Michael Bedward
 * 
 * This file is part of jai-tools.

 * jai-tools is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.

 * jai-tools is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public 
 * License along with jai-tools.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package jaitools.media.jai.sbminterpolate;

import java.awt.image.renderable.RenderedImageFactory;
import javax.media.jai.OperationDescriptor;
import javax.media.jai.OperationRegistry;
import javax.media.jai.OperationRegistrySpi;
import javax.media.jai.registry.RenderedRegistryMode;

/**
 * THIS IS NOT READY FOR USE YET !!!
 *
 * OperationRegistrySpi implementation to register the "SBMInterpolate"
 * operation and its associated image factories.
 */
public class SBMInterpolateSpi implements OperationRegistrySpi {

    /** The name of the product to which these operations belong. */
    private String productName = "jaitools.media.jai.sbminterpolate";
 
    /** Default constructor. */
    public SBMInterpolateSpi() {}

    /**
     * Registers the SBMInterpolate operation and its
     * associated image factories across all supported operation modes.
     *
     * @param registry The registry with which to register the operations
     * and their factories.
     */
    public void updateRegistry(OperationRegistry registry) {
        OperationDescriptor op = new SBMInterpolateDescriptor();
        registry.registerDescriptor(op);
        String descName = op.getName();
        
        RenderedImageFactory rif = new SBMInterpolateRIF();

        registry.registerFactory(RenderedRegistryMode.MODE_NAME,
                                 descName,
                                 productName,
                                 rif);

    }
}