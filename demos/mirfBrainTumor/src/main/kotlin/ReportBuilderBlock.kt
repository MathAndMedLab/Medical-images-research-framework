import com.mirf.core.data.FileData
import com.mirf.core.data.medimage.ImageSeries
import com.mirf.core.pipeline.PipelineBlock
import com.mirf.core.pipeline.PipelineKeeper
import pdfLayouts.MsPdfReportCreator
import pdfLayouts.BrainPdfReportSpecBuilder
import pdfLayouts.PatientInfo

class ReportBuilderBlock(val patientInfo: PatientInfo,
                         pipelineKeeper: PipelineKeeper) : PipelineBlock<ImageSeries, FileData>("pdfCreator", pipelineKeeper) {

    private var masksSender: Any? = null
    private var seriesSender: Any? = null

    private var masksSet = false
    private var seriesSet = false

    private var masks: ImageSeries? = null
    private var series: ImageSeries? = null

    override fun flush() {
        flushMasks()
        flushSeries()
    }

    private fun flushSeries() {
        seriesSender = null
        seriesSet = false
        series = null
    }

    private fun flushMasks() {
        masksSet = false
        masks = null
        masksSender = null
    }

    fun setMasks(sender: PipelineBlock<*, ImageSeries>) {
        flushMasks()
        this.masksSender = sender
        sender.dataReady += this::inputReady
    }

    fun setSeries(sender: PipelineBlock<*, ImageSeries>) {
        flushSeries()
        this.seriesSender = sender
        sender.dataReady += this::inputReady
    }

    override fun inputReady(sender: Any, input: ImageSeries) {

        when (sender) {
            masksSender -> {
                masks = input
                masksSet = true
            }
            seriesSender -> {
                series = input
                seriesSet = true
            }
            else -> log.warn("[$name] undefined sender signal received from $sender, ignored")
        }


        if(masksSet && seriesSet){

            val record = pipelineKeeper.session.addNew("[$name]: algorithm execution")

            val spec = BrainPdfReportSpecBuilder(patientInfo, series!!, masks!!).build()
            val report = MsPdfReportCreator(spec).createReport()
            val fileData = FileData(report.stream.toByteArray(),"report", ".pdf")
            onDataReady(this, fileData)

            record.setSuccess()
        }
    }
}
