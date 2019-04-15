package com.example.mirfskincancer

import android.content.res.AssetManager
import core.data.Data
import core.data.AttributeCollection
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.InputStream

class AssetsData : Data{
    override val attributes: AttributeCollection = AttributeCollection()
    public var assetMgr: AssetManager
    constructor(assetMgr: AssetManager) :super() {
        this.assetMgr = assetMgr
    }

    public fun openImageInAssets(imageName: String): Bitmap? {
        var fileStream: InputStream? = null
        fileStream = this.assetMgr.open(imageName)
        if (fileStream != null) {
            val bitmap = BitmapFactory.decodeStream(fileStream)
            return bitmap
        }
        return null
    }
}