package com.mirf.features.elastix

import com.mirf.core.data.medimage.ImageSeries
import com.mirf.core.pipeline.DummyPipeKeeper
import com.mirf.core.pipeline.PipelineBlock
import com.mirf.core.pipeline.PipelineKeeper

class ElastixBlock(
        pipelineKeeper: PipelineKeeper = DummyPipeKeeper()) :
        PipelineBlock<ImageSeries, ImageSeries>("Elastix", pipelineKeeper) {

    private var fixedSender: Any? = null
    private var movingSender: Any? = null
    private var fixedSet = false
    private var movingSet = false

    private var fixedSeries: ImageSeries? = null
    private var movingSeries: ImageSeries? = null


    override fun flush() {
        flushFixed()
        flushMoving()
    }

    fun flushMoving() {
        movingSender = null
        movingSet = false
        movingSeries = null
        log.info("[Elastix] moving image info flushed")
    }

    fun flushFixed() {
        fixedSet = false
        fixedSeries = null
        fixedSender = null
        log.info("[Elastix] fixed image info flushed")
    }

    override fun inputReady(sender: Any, input: ImageSeries) {
        when (sender) {
            fixedSender -> {
                fixedSeries = input
                fixedSet = true
            }
            movingSender -> {
                movingSeries = input
                movingSet = true
            }
            else -> log.warn("[Elastix] undefined sender signal received")
        }

        if (fixedSet && movingSet) {
            log.info("[Elastix] fixed and moving series received, executing alg")
            val regInfo = RegistrationInfo(fixedSeries!!, movingSeries!!)
            val result = ElastixAlg().execute(regInfo)
            onDataReady(this, result)
        }
    }

    fun setFixedSender(sender: PipelineBlock<*, ImageSeries>) {
        flushFixed()
        this.fixedSender = sender
        sender.dataReady += this::inputReady
    }

    fun setMovingSender(sender: PipelineBlock<*, ImageSeries>) {
        flushMoving()
        this.movingSender = sender
        sender.dataReady += this::inputReady
    }
}