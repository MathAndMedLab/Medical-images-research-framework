import com.mirf.core.data.FileData
import com.mirf.core.data.medimage.ImageSeries
import com.mirf.core.pipeline.PipelineBlock
import com.mirf.core.pipeline.PipelineKeeper
import pdfLayouts.BrainPdfReportCreator
import pdfLayouts.BrainPdfReportSpecBuilder
import pdfLayouts.PatientInfo

/**
 * ReportBuilderBlock is a MIRF PipelineBlock for report generation of the segmentation example.
 * It gathers together all the information about the masks and generates a pdf report based on this data
 *
 * It has 4 listeners from pipeline execution that must pass in the data: wholeMasks, coreMasks, edemaMasks and baseImage.
 */
class ReportBuilderBlock(val patientInfo: PatientInfo,
                         pipelineKeeper: PipelineKeeper) : PipelineBlock<ImageSeries, FileData>("pdfCreator", pipelineKeeper) {

    private var masksSender: Any? = null
    private var coreMasksSender: Any? = null
    private var edemaMasksSender: Any? = null
    private var seriesSender: Any? = null

    private var masksSet = false
    private var coreMasksSet = false
    private var edemaMasksSet = false
    private var seriesSet = false

    private var masks: ImageSeries? = null
    private var coreMasks: ImageSeries? = null
    private var edemaMasks: ImageSeries? = null
    private var series: ImageSeries? = null

    override fun flush() {
        flushMasks()
        flushCoreMasks()
        flushEdemaMasks()
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

    private fun flushCoreMasks() {
        coreMasksSet = false
        coreMasks = null
        coreMasksSender = null
    }

    private fun flushEdemaMasks() {
        edemaMasksSet = false
        edemaMasks = null
        edemaMasksSender = null
    }

    fun setMasks(sender: PipelineBlock<*, ImageSeries>) {
        flushMasks()
        this.masksSender = sender
        sender.dataReady += this::inputReady
    }

    fun setCoreMasks(sender: PipelineBlock<*, ImageSeries>) {
        flushCoreMasks()
        this.coreMasksSender = sender
        sender.dataReady += this::inputReady
    }

    fun setEdemaMasks(sender: PipelineBlock<*, ImageSeries>) {
        flushEdemaMasks()
        this.edemaMasksSender = sender
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
            coreMasksSender -> {
                coreMasks = input
                coreMasksSet = true
            }
            edemaMasksSender -> {
                edemaMasks = input
                edemaMasksSet = true
            }
            seriesSender -> {
                series = input
                seriesSet = true
            }
            else -> log.warn("[$name] undefined sender signal received from $sender, ignored")
        }


        if(masksSet && coreMasksSet && edemaMasksSet && seriesSet){

            val record = pipelineKeeper.session.addNew("[$name]: algorithm execution")

            val spec = BrainPdfReportSpecBuilder(patientInfo, series!!, masks!!, edemaMasks!!, coreMasks!!).build()
            val report = BrainPdfReportCreator(spec).createReport()
            val fileData = FileData(report.stream.toByteArray(),"report", ".pdf")
            onDataReady(this, fileData)

            record.setSuccess()
        }
    }
}
