package com.example.mirfskincancer

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import core.pipeline.Pipeline
import kotlinx.android.synthetic.main.activity_main.*
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.content.res.AssetManager
import java.io.InputStream
import core.pipeline.AlgorithmHostBlock
import core.pipeline.PipeStarter
import core.data.ParametrizedData
import core.data.Data
import core.data.AttributeCollection
import kotlinx.android.synthetic.main.activity_main.*
import core.data.MirfData
import kotlin.math.roundToInt
import org.tensorflow.Graph
import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

class MainActivity : AppCompatActivity() {
    // TODO: think of a way to implement accumulator for different types of data
    // TODO: make a clear demo with activities and good code
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        val modelName = "skin_cancer_model.pb"
        val tensorflowModel = TensorflowModel(
            getAssets(), modelName, "conv2d_1_input_1", "activation_5_1/Sigmoid", 1, 1
        )
        val graph: Graph = tensorflowModel.inferenceInterface!!.graph()
        graph.operations().forEach {
            println(it.name())
        }
        val pipe = Pipeline("Detect moles")
        var picName = "test.png"
        val assetsBlock = AlgorithmHostBlock<Data, AssetsData>(
            { AssetsData(getAssets()) },
            pipelineKeeper = pipe
        )
        val imageReader = AlgorithmHostBlock<AssetsData, BitmapRawImage>(
            { BitmapRawImage(it.openImageInAssets(picName)) },
            pipelineKeeper = pipe
        )
        val tensorflowModelRunner = AlgorithmHostBlock<BitmapRawImage, ParametrizedData<Int>>(
            {
                ParametrizedData<Int>(
                    tensorflowModel.runModel(
                        it.getFloatImageArray(128, 128),
                        1,
                        128,
                        128,
                        3
                    )[0].roundToInt()
                )
            },
            pipelineKeeper = pipe
        )
        pipe.session.newRecord += { _, b -> println(b) }

        //run
        val root = PipeStarter()
        root.dataReady += assetsBlock::inputReady
        assetsBlock.dataReady += imageReader::inputReady
        imageReader.dataReady += tensorflowModelRunner::inputReady

        pipe.rootBlock = root
        pipe.run(MirfData.empty)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
