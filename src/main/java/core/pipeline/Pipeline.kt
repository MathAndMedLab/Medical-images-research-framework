package core.pipeline

import core.data.Data
import java.time.LocalDateTime

class Pipeline(val name: String, val creationTime: LocalDateTime = LocalDateTime.now()) : PipelineKeeper {

    private var _session: PipelineSession = PipelineSession()

    var rootBlock: PipelineBlock<*, *>? = null

    override val session: IPipelineSession
        get() = _session

    fun <T : Data> run(initialData: T) {
        if (rootBlock != null) {
            //TODO: (avlomakin) check if there is better way to call method of out-projected class
            (rootBlock as PipelineBlock<*, *>)::inputReady.call(this, initialData)
        } else
            throw PipelineException("root node is not initialized")
    }

    fun flush() {
        _session = PipelineSession()
    }

    override fun getCachedDataFor(sender: Any, request: PipelineKeeperRequest): Any? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}