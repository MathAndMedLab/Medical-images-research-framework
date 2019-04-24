package com.mirf.features.machinelearning.tensorflow

import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.DoubleBuffer
import java.nio.FloatBuffer
import java.nio.IntBuffer
import java.nio.LongBuffer
import java.util.ArrayList
import org.tensorflow.Graph
import org.tensorflow.Operation
import org.tensorflow.Session
import org.tensorflow.Tensor
import org.tensorflow.Tensors
import org.tensorflow.types.UInt8

// TODO(musatian): uncomment standart logs and replace them with custom MIRF logs
/**
 * Wrapper over the TensorFlow API ([Graph], [Session]) providing a smaller API surface
 * for inference.
 */
class TensorFlowInferenceInterface {

    // Immutable state.
    private val modelName: String
    private val g: Graph
    private val sess: Session

    // State reset on every call to run.
    private var runner: Session.Runner? = null
    private val feedNames = ArrayList<String>()
    private val feedTensors = ArrayList<Tensor<*>>()
    private val fetchNames = ArrayList<String>()
    private var fetchTensors: MutableList<Tensor<*>> = ArrayList()

    /*
     * Load a TensorFlow model from the InputStream.
     *
     * @param input The InputStream to use to load the model file.
     * @param model The filepath to the GraphDef proto representing the model.
     */
    constructor(input: InputStream?, model: String) {

        this.modelName = model
        this.g = Graph()
        this.sess = Session(g)
        this.runner = sess.runner()

        var `is`: InputStream? = null
        `is` = input
        if (`is` == null) {
            try {
                `is` = FileInputStream(model)
            } catch (e: IOException) {
                throw RuntimeException("Failed to load model from '$model'", e)
            }

        }

        try {
            val graphDef = ByteArray(`is`.available())
            val numBytesRead = `is`.read(graphDef)
            if (numBytesRead != graphDef.size) {
                throw IOException(
                        "read error: read only "
                                + numBytesRead
                                + " of the graph, expected to read "
                                + graphDef.size)
            }


            loadGraph(graphDef, g)
            `is`.close()
            //            Log.i(TAG, "Successfully loaded model from '" + model + "'");

        } catch (e: IOException) {
            throw RuntimeException("Failed to load model from '$model'", e)
        }

    }

    /*
     * Load a TensorFlow model from provided InputStream.
     * Note: The InputStream will not be closed after loading model, users need to
     * close it themselves.
     *
     * @param is The InputStream to use to load the model.
     */
    constructor(`is`: InputStream) {
        // modelName is redundant for model loading from input stream, here is for
        // avoiding error in initialization as modelName is marked final.
        this.modelName = ""
        this.g = Graph()
        this.sess = Session(g)
        this.runner = sess.runner()

        try {

            val baosInitSize = if (`is`.available() > 16384) `is`.available() else 16384
            val baos = ByteArrayOutputStream(baosInitSize)
            var numBytesRead: Int
            val buf = ByteArray(16384)
            while (true) {
                numBytesRead = `is`.read(buf, 0, buf.size)
                if (numBytesRead < 0) break
                baos.write(buf, 0, numBytesRead)
            }
            val graphDef = baos.toByteArray()

            loadGraph(graphDef, g)
            //            Log.i(TAG, "Successfully loaded model from the input stream");
        } catch (e: IOException) {
            throw RuntimeException("Failed to load model from the input stream", e)
        }

    }

    /*
     * Construct a TensorFlowInferenceInterface with provided Graph
     *
     * @param g The Graph to use to construct this interface.
     */
    constructor(g: Graph) {

        // modelName is redundant here, here is for
        // avoiding error in initialization as modelName is marked final.
        this.modelName = ""
        this.g = g
        this.sess = Session(g)
        this.runner = sess.runner()
    }

    /** An overloaded version of runInference that allows supplying targetNodeNames as well  */
    @JvmOverloads
    fun run(outputNames: Array<String>, enableStats: Boolean = false, targetNodeNames: Array<String> = arrayOf()) {
        // Release any Tensors from the previous run calls.
        closeFetches()

        // Add fetches.
        for (o in outputNames) {
            fetchNames.add(o)
            val tid = TensorId.parse(o)
            runner!!.fetch(tid.name, tid.outputIndex)
        }

        // Add targets.
        for (t in targetNodeNames) {
            runner!!.addTarget(t)
        }

        // Run the session.
        try {
            if (enableStats) {
                val r = runner!!.setOptions(byteArrayOf(0x08, 0x03)).runAndFetchMetadata()
                fetchTensors = r.outputs

            } else {
                fetchTensors = runner!!.run()
            }
        } catch (e: RuntimeException) {
            throw e
        } finally {
            // Always release the feeds (to save resources) and reset the runner, this run is
            // over.
            closeFeeds()
            runner = sess.runner()
        }
    }

    /** Returns a reference to the Graph describing the computation run during inference.  */
    fun graph(): Graph {
        return g
    }

    fun graphOperation(operationName: String): Operation {
        val operation = g.operation(operationName) ?: throw RuntimeException(
                "Node '$operationName' does not exist in model '$modelName'")
        return operation
    }

    /**
     * Cleans up the state associated with this Object.
     *
     *
     * The TenosrFlowInferenceInterface object is no longer usable after this method returns.
     */
    fun close() {
        closeFeeds()
        closeFetches()
        sess.close()
        g.close()
    }

    @Throws(Throwable::class)
    protected fun finalize() {
    }

    // Methods for taking a native Tensor and filling it with values from Kotlin arrays.

    /**
     * Given a source array with shape [dims] and content [src], copy the contents into
     * the input Tensor with name [inputName]. The source array [src] must have at least
     * as many elements as that of the destination Tensor. If [src] has more elements than the
     * destination has capacity, the copy is truncated.
     */
    fun feed(inputName: String, src: BooleanArray, vararg dims: Long) {
        val b = ByteArray(src.size)

        for (i in src.indices) {
            b[i] = if (src[i]) 1.toByte() else 0.toByte()
        }

        addFeed(inputName, Tensor.create(Boolean::class.java, dims, ByteBuffer.wrap(b)))
    }

    /**
     * Given a source array with shape [dims] and content [src], copy the contents into
     * the input Tensor with name [inputName]. The source array [src] must have at least
     * as many elements as that of the destination Tensor. If [src] has more elements than the
     * destination has capacity, the copy is truncated.
     */
    fun feed(inputName: String, src: FloatArray, vararg dims: Long) {
        addFeed(inputName, Tensor.create(dims, FloatBuffer.wrap(src)))
    }

    /**
     * Given a source array with shape [dims] and content [src], copy the contents into
     * the input Tensor with name [inputName]. The source array [src] must have at least
     * as many elements as that of the destination Tensor. If [src] has more elements than the
     * destination has capacity, the copy is truncated.
     */
    fun feed(inputName: String, src: IntArray, vararg dims: Long) {
        addFeed(inputName, Tensor.create(dims, IntBuffer.wrap(src)))
    }

    /**
     * Given a source array with shape [dims] and content [src], copy the contents into
     * the input Tensor with name [inputName]. The source array [src] must have at least
     * as many elements as that of the destination Tensor. If [src] has more elements than the
     * destination has capacity, the copy is truncated.
     */
    fun feed(inputName: String, src: LongArray, vararg dims: Long) {
        addFeed(inputName, Tensor.create(dims, LongBuffer.wrap(src)))
    }

    /**
     * Given a source array with shape [dims] and content [src], copy the contents into
     * the input Tensor with name [inputName]. The source array [src] must have at least
     * as many elements as that of the destination Tensor. If [src] has more elements than the
     * destination has capacity, the copy is truncated.
     */
    fun feed(inputName: String, src: DoubleArray, vararg dims: Long) {
        addFeed(inputName, Tensor.create(dims, DoubleBuffer.wrap(src)))
    }

    /**
     * Given a source array with shape [dims] and content [src], copy the contents into
     * the input Tensor with name [inputName]. The source array [src] must have at least
     * as many elements as that of the destination Tensor. If [src] has more elements than the
     * destination has capacity, the copy is truncated.
     */
    fun feed(inputName: String, src: ByteArray, vararg dims: Long) {
        addFeed(inputName, Tensor.create(UInt8::class.java, dims, ByteBuffer.wrap(src)))
    }

    /**
     * Copy a byte sequence into the input Tensor with name [inputName] as a string-valued
     * scalar tensor. In the TensorFlow type system, a "string" is an arbitrary sequence of bytes, not
     * a Java `String` (which is a sequence of characters).
     */
    fun feedString(inputName: String, src: ByteArray) {
        addFeed(inputName, Tensors.create(src))
    }

    /**
     * Copy an array of byte sequences into the input Tensor with name [inputName] as a
     * string-valued one-dimensional tensor (vector). In the TensorFlow type system, a "string" is an
     * arbitrary sequence of bytes, not a Java `String` (which is a sequence of characters).
     */
    fun feedString(inputName: String, src: Array<ByteArray>) {
        addFeed(inputName, Tensors.create(src))
    }

    // Methods for taking a native Tensor and filling it with src from Java native IO buffers.

    /**
     * Given a source buffer with shape [dims] and content [src], both stored as
     * **direct** and **native ordered** java.nio buffers, copy the contents into the input
     * Tensor with name [inputName]. The source buffer [src] must have at least as many
     * elements as that of the destination Tensor. If [src] has more elements than the
     * destination has capacity, the copy is truncated.
     */
    fun feed(inputName: String, src: FloatBuffer, vararg dims: Long) {
        addFeed(inputName, Tensor.create(dims, src))
    }

    /**
     * Given a source buffer with shape [dims] and content [src], both stored as
     * **direct** and **native ordered** java.nio buffers, copy the contents into the input
     * Tensor with name [inputName]. The source buffer [src] must have at least as many
     * elements as that of the destination Tensor. If [src] has more elements than the
     * destination has capacity, the copy is truncated.
     */
    fun feed(inputName: String, src: IntBuffer, vararg dims: Long) {
        addFeed(inputName, Tensor.create(dims, src))
    }

    /**
     * Given a source buffer with shape [dims] and content [src], both stored as
     * **direct** and **native ordered** java.nio buffers, copy the contents into the input
     * Tensor with name [inputName]. The source buffer [src] must have at least as many
     * elements as that of the destination Tensor. If [src] has more elements than the
     * destination has capacity, the copy is truncated.
     */
    fun feed(inputName: String, src: LongBuffer, vararg dims: Long) {
        addFeed(inputName, Tensor.create(dims, src))
    }

    /**
     * Given a source buffer with shape [dims] and content [src], both stored as
     * **direct** and **native ordered** java.nio buffers, copy the contents into the input
     * Tensor with name [inputName]. The source buffer [src] must have at least as many
     * elements as that of the destination Tensor. If [src] has more elements than the
     * destination has capacity, the copy is truncated.
     */
    fun feed(inputName: String, src: DoubleBuffer, vararg dims: Long) {
        addFeed(inputName, Tensor.create(dims, src))
    }

    /**
     * Given a source buffer with shape [dims] and content [src], both stored as
     * **direct** and **native ordered** java.nio buffers, copy the contents into the input
     * Tensor with name [inputName]. The source buffer [src] must have at least as many
     * elements as that of the destination Tensor. If [src] has more elements than the
     * destination has capacity, the copy is truncated.
     */
    fun feed(inputName: String, src: ByteBuffer, vararg dims: Long) {
        addFeed(inputName, Tensor.create(UInt8::class.java, dims, src))
    }

    /**
     * Read from a Tensor named [outputName] and copy the contents into a Java array. [ ] must have length greater than or equal to that of the source Tensor. This operation will
     * not affect dst's content past the source Tensor's size.
     */
    fun fetch(outputName: String, dst: FloatArray) {
        fetch(outputName, FloatBuffer.wrap(dst))
    }

    /**
     * Read from a Tensor named [outputName] and copy the contents into a Java array. [ ] must have length greater than or equal to that of the source Tensor. This operation will
     * not affect dst's content past the source Tensor's size.
     */
    fun fetch(outputName: String, dst: IntArray) {
        fetch(outputName, IntBuffer.wrap(dst))
    }

    /**
     * Read from a Tensor named [outputName] and copy the contents into a Java array. [ ] must have length greater than or equal to that of the source Tensor. This operation will
     * not affect dst's content past the source Tensor's size.
     */
    fun fetch(outputName: String, dst: LongArray) {
        fetch(outputName, LongBuffer.wrap(dst))
    }

    /**
     * Read from a Tensor named [outputName] and copy the contents into a Java array. [ ] must have length greater than or equal to that of the source Tensor. This operation will
     * not affect dst's content past the source Tensor's size.
     */
    fun fetch(outputName: String, dst: DoubleArray) {
        fetch(outputName, DoubleBuffer.wrap(dst))
    }

    /**
     * Read from a Tensor named [outputName] and copy the contents into a Java array. [ ] must have length greater than or equal to that of the source Tensor. This operation will
     * not affect dst's content past the source Tensor's size.
     */
    fun fetch(outputName: String, dst: ByteArray) {
        fetch(outputName, ByteBuffer.wrap(dst))
    }

    /**
     * Read from a Tensor named [outputName] and copy the contents into the **direct** and
     * **native ordered** java.nio buffer [dst]. [dst] must have capacity greater than
     * or equal to that of the source Tensor. This operation will not affect dst's content past the
     * source Tensor's size.
     */
    fun fetch(outputName: String, dst: FloatBuffer) {
        getTensor(outputName).writeTo(dst)
    }

    /**
     * Read from a Tensor named [outputName] and copy the contents into the **direct** and
     * **native ordered** java.nio buffer [dst]. [dst] must have capacity greater than
     * or equal to that of the source Tensor. This operation will not affect dst's content past the
     * source Tensor's size.
     */
    fun fetch(outputName: String, dst: IntBuffer) {
        getTensor(outputName).writeTo(dst)
    }

    /**
     * Read from a Tensor named [outputName] and copy the contents into the **direct** and
     * **native ordered** java.nio buffer [dst]. [dst] must have capacity greater than
     * or equal to that of the source Tensor. This operation will not affect dst's content past the
     * source Tensor's size.
     */
    fun fetch(outputName: String, dst: LongBuffer) {
        getTensor(outputName).writeTo(dst)
    }

    /**
     * Read from a Tensor named [outputName] and copy the contents into the **direct** and
     * **native ordered** java.nio buffer [dst]. [dst] must have capacity greater than
     * or equal to that of the source Tensor. This operation will not affect dst's content past the
     * source Tensor's size.
     */
    fun fetch(outputName: String, dst: DoubleBuffer) {
        getTensor(outputName).writeTo(dst)
    }

    /**
     * Read from a Tensor named [outputName] and copy the contents into the **direct** and
     * **native ordered** java.nio buffer [dst]. [dst] must have capacity greater than
     * or equal to that of the source Tensor. This operation will not affect dst's content past the
     * source Tensor's size.
     */
    fun fetch(outputName: String, dst: ByteBuffer) {
        getTensor(outputName).writeTo(dst)
    }

    @Throws(IOException::class)
    private fun loadGraph(graphDef: ByteArray, g: Graph) {
        val startMs = System.currentTimeMillis()

        try {
            g.importGraphDef(graphDef)
        } catch (e: IllegalArgumentException) {
            throw IOException("Not a valid TensorFlow Graph serialization: " + e.message)
        }


        val endMs = System.currentTimeMillis()
        //        Log.i(
        //                TAG,
        //                "Model load took " + (endMs - startMs) + "ms, TensorFlow version: " + TensorFlow.version());
    }

    private fun addFeed(inputName: String, t: Tensor<*>) {
        // The string format accepted by TensorFlowInferenceInterface is node_name[:output_index].
        val tid = TensorId.parse(inputName)
        runner!!.feed(tid.name, tid.outputIndex, t)
        feedNames.add(inputName)
        feedTensors.add(t)
    }

    private class TensorId {
        lateinit internal var name: String
        internal var outputIndex: Int = 0

        companion object {

            // Parse output names into a TensorId.
            //
            // E.g., "foo" --> ("foo", 0), while "foo:1" --> ("foo", 1)
            fun parse(name: String): TensorId {
                val tid = TensorId()
                val colonIndex = name.lastIndexOf(':')
                if (colonIndex < 0) {
                    tid.outputIndex = 0
                    tid.name = name
                    return tid
                }
                try {
                    tid.outputIndex = Integer.parseInt(name.substring(colonIndex + 1))
                    tid.name = name.substring(0, colonIndex)
                } catch (e: NumberFormatException) {
                    tid.outputIndex = 0
                    tid.name = name
                }

                return tid
            }
        }
    }

    private fun getTensor(outputName: String): Tensor<*> {
        var i = 0
        for (n in fetchNames) {
            if (n == outputName) {
                return fetchTensors[i]
            }
            ++i
        }
        throw RuntimeException(
                "Node '$outputName' was not provided to run(), so it cannot be read")
    }

    private fun closeFeeds() {
        for (t in feedTensors) {
            t.close()
        }
        feedTensors.clear()
        feedNames.clear()
    }

    private fun closeFetches() {
        for (t in fetchTensors) {
            t.close()
        }
        fetchTensors.clear()
        fetchNames.clear()
    }

    companion object {
        private val TAG = "TensorFlowInferenceInterface"
    }

}

