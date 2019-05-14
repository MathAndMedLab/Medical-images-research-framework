package com.example.mirfskincancer

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

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.view.View;
import android.widget.TextView;
import android.view.View.OnClickListener;

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button = findViewById(R.id.btnRunPipe) as Button
        val image = findViewById(R.id.imageView1) as ImageView
        val text = findViewById(R.id.resText) as TextView;

        button.setOnClickListener {
            val modelName = "skin_cancer_model.pb"
            val tensorflowModel = TensorflowModel(
                    getAssets(), modelName, "conv2d_1_input_1", "activation_5_1/Sigmoid", 1, 1
            )
            val pipe = Pipeline("Detect moles")
            var picName = "test.png"
            val assetsBlock = AlgorithmHostBlock<Data, AssetsData>(
                    { AssetsData(getAssets()) },
                    pipelineKeeper = pipe
            )
            val imageReader = AlgorithmHostBlock<AssetsData, BitmapRawImage>(
                    {
                        val rawImg = it.openImageInAssets(picName)
                        val img = BitmapRawImage(rawImg)
                        image.setImageBitmap(rawImg)
                        return@AlgorithmHostBlock img
                    },
                    pipelineKeeper = pipe
            )
            val tensorflowModelRunner = AlgorithmHostBlock<BitmapRawImage, ParametrizedData<Int>>(
                    {
                        val res = tensorflowModel.runModel(
                        it.getFloatImageArray(128, 128),
                        1,
                        128,
                        128,
                        3
                    )[0].roundToInt()
                        text.setText("The mole is " + formatResult(res))
                        return@AlgorithmHostBlock ParametrizedData<Int>(res)

                    },
                    pipelineKeeper = pipe
            )
            //run
            val root = PipeStarter()
            root.dataReady += assetsBlock::inputReady
            assetsBlock.dataReady += imageReader::inputReady
            imageReader.dataReady += tensorflowModelRunner::inputReady

            pipe.rootBlock = root
            pipe.run(MirfData.empty)
        }

    }

    private fun formatResult(res: Int): String {
        if (res == 1) {
            return "malignant"
        } else {
            return "benign"
        }
    }

}
