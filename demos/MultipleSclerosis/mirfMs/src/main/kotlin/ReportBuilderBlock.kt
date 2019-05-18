import com.mirf.core.data.Data
import com.mirf.core.data.FileData
import com.mirf.core.data.medimage.ImageSeries
import com.mirf.core.pipeline.PipelineBlock
import com.mirf.core.pipeline.PipelineKeeper
import pdfLayouts.MsPdfReportCreator
import pdfLayouts.MsPdfReportParagraphsBuilder
import pdfLayouts.MsVolumeInfo
import pdfLayouts.PatientInfo

class ReportBuilderBlock(val patientInfo: PatientInfo,
                         pipelineKeeper: PipelineKeeper) : PipelineBlock<Data, FileData>("pdfCreator", pipelineKeeper) {

    private var masksSender: Any? = null
    private var seriesSender: Any? = null
    private var baselineVolumeSender: Any? = null

    private var masksSet = false
    private var seriesSet = false
    private var baselineVolumeSet = false

    private var masks: ImageSeries? = null
    private var series: ImageSeries? = null
    private var basleineVolume: MsVolumeInfo? = null

    override fun flush() {
        flushMasks()
        flushSeries()
        flushBaselineVolume()
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

    private fun flushBaselineVolume(){
        baselineVolumeSet = false
        baselineVolumeSender = null
        basleineVolume = null
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

    fun setBaselineVolue(sender: PipelineBlock<*, MsVolumeInfo>){
        flushBaselineVolume()
        this.baselineVolumeSender = sender
        sender.dataReady += this::inputReady
    }

    override fun inputReady(sender: Any, input: Data) {

        when (sender) {
            masksSender -> {
                masks = input as ImageSeries
                masksSet = true
            }
            seriesSender -> {
                series = input as ImageSeries
                seriesSet = true
            }
            baselineVolumeSender ->{
                basleineVolume = input as MsVolumeInfo
                baselineVolumeSet = true
            }
            else -> log.warn("[$name] undefined sender signal received from $sender, ignored")
        }


        if(masksSet && seriesSet && baselineVolumeSet){

            val record = pipelineKeeper.session.addNew("[$name]: algorithm execution")

            val spec = MsPdfReportParagraphsBuilder(patientInfo, series!!, masks!!, basleineVolume!!).build()
            val report = MsPdfReportCreator(spec).createReport()

            val fileData = FileData(report.stream.toByteArray(),"report", ".pdf")
            onDataReady(this, fileData)

            record.setSuccess()
        }
    }
}
