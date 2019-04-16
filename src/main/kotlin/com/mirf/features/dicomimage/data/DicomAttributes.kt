package com.mirf.features.dicomimage.data

import com.mirf.core.data.attribute.AttributeTagType
import com.mirf.core.data.attribute.DataAttributeMockup

/**
 * Stores [DataAttributeMockup] for all well-known DICOM data attributes
 */
object DicomAttributes {

    /**
     * todo: (avlomakin) add link to doc
     */
    val ONE_PIXEL_VOLUME_MM3 = DataAttributeMockup<Double>("onepixelvolume", "(1234, 1234)",
            AttributeTagType.Dicom)
}

