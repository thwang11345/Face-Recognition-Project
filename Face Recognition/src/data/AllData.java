package data;

import java.io.File;

/**
 * Container for Train and Test data.
 *
 * @author Thomas Hwang
 */
public class AllData {

	Dataset Train = null;
	Dataset Test = null;

	/**
	 * Constructs train and test data.
	 * 
	 * @param folder   the location of the face data
	 * @param faceOnly
	 */
	public AllData(File folder, boolean faceOnly) {
		init(folder, faceOnly);
	}

	/**
	 * Returns train data.
	 * 
	 * @return train data
	 */
	public Dataset getTrain() {
		return Train;
	}

	/**
	 * Returns test data.
	 * 
	 * @return test data
	 */
	public Dataset getTest() {
		return Test;
	}
	/**
	 * helper method to construct train and test data given a file and whether or not this file contains face only data.
	 */
	private void init(File f, boolean faceOnly) {
		for (final File fileEntry : f.listFiles()) {
			if (fileEntry.isDirectory()) {
				String path = fileEntry.getAbsolutePath();
				int faceIdx = path.toLowerCase().indexOf("face");

				if (faceOnly && faceIdx >= 0) {
					if (path.toLowerCase().indexOf("train") >= 0) {
						Train = new Dataset(fileEntry, faceOnly);
					} else if (path.toLowerCase().indexOf("test") >= 0) {
						Test = new Dataset(fileEntry, faceOnly);
					}
				} else if (!faceOnly && faceIdx < 0) {
					if (path.toLowerCase().indexOf("train") >= 0) {
						Train = new Dataset(fileEntry, faceOnly);
					} else if (path.toLowerCase().indexOf("test") >= 0) {
						Test = new Dataset(fileEntry, faceOnly);
					}
				}
			}
		}
	}
}
