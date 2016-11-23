package pl.edu.agh.kt.aradoszek.meterreader.OCR;

import java.io.File;

import android.graphics.Bitmap;
import android.os.Environment;

import com.googlecode.tesseract.android.TessBaseAPI;

public class TessOCR {
	private TessBaseAPI tessBaseAPI;
	
	public TessOCR() {
		tessBaseAPI = new TessBaseAPI();
		String dataPath = Environment.getExternalStorageDirectory() + "/tesseract/";
		String language = "eng";
		tessBaseAPI.setDebug(true);
		tessBaseAPI.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST, "1234567890");
		tessBaseAPI.setVariable(TessBaseAPI.VAR_CHAR_BLACKLIST, "!@#$%^&*()_+=-qwertyuiop[]}{POIU" +
				"YTREWQasdASDfghFGHjklJKLl;L:'\"\\|~`xcvXCVbnmBNM,./<>?");

		File dir = new File(dataPath + "tessdata/");
		if (!dir.exists()) 
			dir.mkdirs();
		tessBaseAPI.init(dataPath, language);
	}
	
	public String getOCRResult(Bitmap bitmap) {

		tessBaseAPI.setImage(bitmap);
		String result = tessBaseAPI.getUTF8Text();

		return result;
    }
	
	public void onDestroy() {
		if (tessBaseAPI != null)
			tessBaseAPI.end();
	}
	
}
