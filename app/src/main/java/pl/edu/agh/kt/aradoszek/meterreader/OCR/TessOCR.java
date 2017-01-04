package pl.edu.agh.kt.aradoszek.meterreader.OCR;

import android.content.res.AssetManager;
import android.graphics.Bitmap;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class TessOCR {
	private TessBaseAPI tessBaseAPI;
	private String dataPath = "";
	private AssetManager assetManager;

	public TessOCR(File filesDir, AssetManager assetManager) {
		tessBaseAPI = new TessBaseAPI();
		this.assetManager = assetManager;
		dataPath = filesDir + "/tesseract/";
		checkFile(new File(dataPath + "tessdata/"));

		String language = "eng";

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

	private void copyFiles() {
		try {
			String filepath = dataPath + "/tessdata/eng.traineddata";

			InputStream instream = assetManager.open("tessdata/eng.traineddata");
			OutputStream outstream = new FileOutputStream(filepath);

			byte[] buffer = new byte[1024];
			int read;
			while ((read = instream.read(buffer)) != -1) {
				outstream.write(buffer, 0, read);
			}
			outstream.flush();
			outstream.close();
			instream.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void checkFile(File dir) {
		if (!dir.exists()&& dir.mkdirs()){
			copyFiles();
		}

		if(dir.exists()) {
			String datafilepath = dataPath + "/tessdata/eng.traineddata";
			File datafile = new File(datafilepath);
			if (!datafile.exists()) {
				copyFiles();
			}
		}
	}
}





