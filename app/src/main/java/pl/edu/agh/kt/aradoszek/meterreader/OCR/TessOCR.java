package pl.edu.agh.kt.aradoszek.meterreader.OCR;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.googlecode.tesseract.android.TessBaseAPI;
import org.opencv.android.OpenCVLoader;


public class TessOCR {
	private TessBaseAPI tessBaseAPI;

	public TessOCR() {

		if (!OpenCVLoader.initDebug()) {

			Log.d("Error", "Failed to INIT \n OpenCV Failure");
		} else {
			Log.d("Error", "OpenCV INIT Succes");
		}

		tessBaseAPI = new TessBaseAPI();
//
//		tessBaseAPI.setPageSegMode(TessBaseAPI.PageSegMode.PSM_SINGLE_LINE);
//		tessBaseAPI.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST, "1234567890");
	//	tessBaseAPI.setVariable(TessBaseAPI.VAR_CHAR_BLACKLIST, "!@#$%^&*()_+=-qwertyuiop[]}{POIU" +
		//	"YTREWQasdASDfghFGHjklJKLl;L:'\"\\|~`xcvXCVbnmBNM,./<>?");
//
		String dataPath = Environment.getExternalStorageDirectory() + "/tesseract/";
		String language = "eng";
////		File dir = new File(dataPath + "tessdata/");
////		if (!dir.exists())
////			dir.mkdirs();
//	tessBaseAPI.init(dataPath, language);


		tessBaseAPI.setPageSegMode(TessBaseAPI.OEM_TESSERACT_CUBE_COMBINED);
		tessBaseAPI.setPageSegMode(TessBaseAPI.PageSegMode.PSM_AUTO_OSD);
		tessBaseAPI.setPageSegMode(TessBaseAPI.PageSegMode.PSM_SINGLE_LINE);
		tessBaseAPI.setDebug(true);

		tessBaseAPI.init(dataPath, language);
		tessBaseAPI.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST, "1234567890");


	}

	public String getOCRResult(Bitmap bitmap) {

		tessBaseAPI.setImage(bitmap);
		String recognizedText = tessBaseAPI.getUTF8Text();

		recognizedText = recognizedText.replaceAll( " ", "" );
		String resultTxt = recognizedText;


		return resultTxt;
	}

	public void onDestroy() {
		if (tessBaseAPI != null)
			tessBaseAPI.end();
	}
}





