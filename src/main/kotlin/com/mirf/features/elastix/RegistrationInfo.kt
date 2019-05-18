package com.mirf.features.elastix

import com.mirf.core.data.medimage.ImageSeries

class RegistrationInfo(val fixedSeries: ImageSeries, val movingSeries: ImageSeries, val params: String = PARAMS) {

    companion object {
        private const val PARAMS = "" +
                "(Registration \"MultiResolutionRegistration\")\n" +
                "(Metric \"AdvancedMattesMutualInformation\")\n" +
                "(ImageSampler \"RandomCoordinate\")\n" +
                "(Interpolator \"LinearInterpolator\")\n" +
                "(ResampleInterpolator \"FinalBSplineInterpolator\")\n" +
                "(Resampler \"DefaultResampler\")\n" +
                "(Transform \"AffineTransform\")\n" +
                "(Optimizer \"AdaptiveStochasticGradientDescent\")\n" +
                "(FixedImagePyramid \"FixedSmoothingImagePyramid\")\n" +
                "(MovingImagePyramid \"MovingSmoothingImagePyramid\")"
    }
}