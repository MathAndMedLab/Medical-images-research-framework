package core.data.medimage

/**
 * Abstract class to store image pixels data.
 * It is made to assist cross-platform integration for mobile platforms
 * RawImageData is used in MedImage and all algorithms that use it
 */
// TODO(musatian): migrate all calls to image to rawImage and according addons
abstract class RawImageData  {
    abstract fun getHeight(): Int
    abstract fun getWidth(): Int
}