package main.java.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * This class allow to manage all external functionalities : error massage, properties...
 * 
 * @author Florian Causse
 * @created 07-04-2011
 * 
 */
/**
 * @author Utilisateur
 * 
 */
public class Utils {

	private String errorMessage = null;
	private File errorMessageFile = null;

	// static variable
	public static final String UNKNOWN_DATA = "unknownData";
	public static final List<String> ratings = new ArrayList<String>();
	public static final List<String> ratingContext = new ArrayList<String>();
	public static final String YES = "yes";
	public static final String NO = "no";
	public static final String HEADER_TAG = "h";
	public static final String OTHER_TAG = "o";
	public static final String WARNING_TAG = "w";
	public static final String STATISTIC_TAG = "s";
	public static final int DEFAULT_WEIGHT = 3;

	// properties file
	public static ResourceBundle bundleConf = ResourceBundle.getBundle("main.resources.conf");
	public static ResourceBundle bundleConfOverridable = ResourceBundle
			.getBundle("main.resources.confOverridable");

	// buffer size
	public static int BUFFER = 2048;

	// file prefix
	public static final String KEY = "key_";
	public static final String ERROR = "error_";

	// file type
	public static final String TXT = "txt";
	public static final String HTML = "html";
	public static final String INTERACTIVE_HTML = "interactivehtml";
	public static final String PDF = "pdf";
	public static final String SDD = "sdd";
	public static final String WIKI = "wiki";
	public static final String SPECIESIDWIKISTATEMENT = "speciesidwikistatement";
	public static final String SPECIESIDWIKIQUESTIONANSWER = "speciesidwikiquestionanswer";
	public static final String DOT = "dot";
	public static final String ZIP = "zip";

	// specific file extension
	public static final String GV = "gv";

	// representation type
	public static final String TREE = "tree";
	public static final String FLAT = "flat";

	// score method type
	public static final String XPER = "xper";
	public static final String JACCARD = "jaccard";
	public static final String SOKALANDMICHENER = "sokalAndMichener";

	// weight type
	public static final String GLOBAL_CHARACTER_WEIGHT = "global";
	public static final String CONTEXTUAL_CHARACTER_WEIGHT = "contextual";

	// options
	private String format = Utils.TXT;
	private String representation = Utils.TREE;
	private boolean fewStatesCharacterFirst = false;
	private boolean mergeCharacterStatesIfSameDiscimination = false;
	private boolean pruning = false;
	private String verbosity = "";
	private String scoreMethod = Utils.XPER;
	private String weightContext = "";
	private String weightType = Utils.GLOBAL_CHARACTER_WEIGHT;

	/**
	 * Constructor
	 */
	public Utils() {
		super();
		// initialize the list of rating values
		ratings.add("Rating1of5");
		ratings.add("Rating2of5");
		ratings.add("Rating3of5");
		ratings.add("Rating4of5");
		ratings.add("Rating5of5");

		// initialize the list of rating contexts
		ratingContext.add("ObservationConvenience");
		ratingContext.add("Availability");
		ratingContext.add("Repeatability");
		ratingContext.add("CostEffectiveness");
		ratingContext.add("PhylogeneticWeighting");
		ratingContext.add("RequiredExpertise");
	}

	/**
	 * Convert a String value to a Double value
	 * 
	 * @param String
	 *            , the String to convert
	 * @return Double, the Double value
	 */
	public static Double convertStringToDouble(String str) {

		try {
			Double doubleValue = new Double(Double.parseDouble(str));
			return doubleValue;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Getter for configuration elements or messages
	 * 
	 * @return String the element corresponding to the key
	 */
	public static String getBundleConfElement(String key) {
		return Utils.bundleConf.getString(key);
	}

	/**
	 * Getter for overridable configuration elements or messages
	 * 
	 * @return String the element corresponding to the key
	 */
	public static String getBundleConfOverridableElement(String key) {
		return Utils.bundleConfOverridable.getString(key);
	}

	/**
	 * setter for configuration ResourceBundle
	 * 
	 * @param bundle
	 */
	public static void setBundleConf(ResourceBundle bundleConf) {
		Utils.bundleConf = bundleConf;
	}

	/**
	 * setter for overridable configuration ResourceBundle
	 * 
	 * @param bundle
	 */
	public static void setBundleConfOverridable(ResourceBundle bundleConfOverridable) {
		Utils.bundleConfOverridable = bundleConfOverridable;
	}

	/**
	 * getter for the error message
	 * 
	 * @return String, the error file name
	 */
	public String getErrorMessage() {
		return this.errorMessage;
	}

	/**
	 * setter for the error message
	 * 
	 * @param msg
	 */
	public void setErrorMessage(String msg) {
		if (getErrorMessage() == null) {
			errorMessage = msg;
			setErrorMessageFile(createErrorFile());
		}
	}

	/**
	 * setter for the error message with Throwable object
	 * 
	 * @param msg
	 *            , the readable message
	 * @param t
	 *            , the exception
	 */
	public void setErrorMessage(String msg, Throwable t) {
		if (getErrorMessage() == null) {
			errorMessage = msg + ": " + t.getMessage();
			setErrorMessageFile(createErrorFile());
		}
	}

	/**
	 * getter for file message
	 * 
	 * @return File, the error file
	 */
	public File getErrorMessageFile() {
		return this.errorMessageFile;
	}

	/**
	 * setter for file message
	 * 
	 * @param errorMessageFile
	 *            , the error file
	 */
	public void setErrorMessageFile(File errorMessageFile) {
		this.errorMessageFile = errorMessageFile;
	}

	/**
	 * method creating the error message
	 * 
	 * @return File, the error file
	 */
	public File createErrorFile() {
		String path = Utils.getBundleConfOverridableElement("generatedKeyFiles.prefix")
				+ Utils.getBundleConfOverridableElement("generatedKeyFiles.folder");

		String lineReturn = System.getProperty("line.separator");
		File erroFile = null;
		try {
			erroFile = File.createTempFile(Utils.ERROR, "." + Utils.TXT, new File(path));

			FileOutputStream fileOutputStream = new FileOutputStream(erroFile);
			fileOutputStream.write(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF });
			BufferedWriter txtFileWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream,
					"UTF-8"));

			txtFileWriter.append(this.errorMessage);
			txtFileWriter.append(lineReturn + lineReturn + Utils.getBundleConfElement("message.webmaster")
					+ Utils.getBundleConfOverridableElement("email.webmaster"));
			txtFileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return erroFile;
	}

	/**
	 * get the format of the generated key
	 * 
	 * @return String, the format
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * 
	 * set the format of the generated key
	 * 
	 * @param format
	 */
	public void setFormat(String format) {
		this.format = format;
	}

	/**
	 * get the representation of the generated key
	 * 
	 * @return String, the representation
	 */
	public String getRepresentation() {
		return representation;
	}

	/**
	 * set the representation of the generated key
	 * 
	 * @param representation
	 */
	public void setRepresentation(String representation) {
		this.representation = representation;
	}

	/**
	 * test if the fewStatesCharacterFirst option is active
	 * 
	 * @return true if fewStatesCharacterFirst is selected
	 */
	public boolean isFewStatesCharacterFirst() {
		return fewStatesCharacterFirst;
	}

	/**
	 * set the fewStatesCharacterFirst option
	 * 
	 * @param fewStatesCharacterFirst
	 */
	public void setFewStatesCharacterFirst(boolean fewStatesCharacterFirst) {
		this.fewStatesCharacterFirst = fewStatesCharacterFirst;
	}

	/**
	 * test if the mergeCharacterStatesIfSameDiscimination option is active
	 * 
	 * @return true if mergeCharacterStatesIfSameDiscimination is selected
	 */
	public boolean isMergeCharacterStatesIfSameDiscimination() {
		return mergeCharacterStatesIfSameDiscimination;
	}

	/**
	 * set the mergeCharacterStatesIfSameDiscimination option
	 * 
	 * @param mergeCharacterStatesIfSameDiscimination
	 */
	public void setMergeCharacterStatesIfSameDiscimination(boolean mergeCharacterStatesIfSameDiscimination) {
		this.mergeCharacterStatesIfSameDiscimination = mergeCharacterStatesIfSameDiscimination;
	}

	/**
	 * test if the pruning option is active
	 * 
	 * @return true if pruning is selected
	 */
	public boolean isPruning() {
		return pruning;
	}

	/**
	 * set the pruning option
	 * 
	 * @param pruning
	 */
	public void setPruning(boolean pruning) {
		this.pruning = pruning;
	}

	/**
	 * get the verbosity value
	 * 
	 * @return String, the verbosity string
	 */
	public String getVerbosity() {
		return verbosity;
	}

	/**
	 * set the verbosity value
	 * 
	 * @param verbosity
	 */
	public void setVerbosity(String verbosity) {
		this.verbosity = verbosity;
	}

	/**
	 * get the score method
	 * 
	 * @return String, the score method selected
	 */
	public String getScoreMethod() {
		return scoreMethod;
	}

	/**
	 * set the score method
	 * 
	 * @param scoreMethod
	 */
	public void setScoreMethod(String scoreMethod) {
		this.scoreMethod = scoreMethod;
	}

	/**
	 * get the weight context
	 * 
	 * @return String, the weight context
	 */
	public String getWeightContext() {
		return weightContext;
	}

	/**
	 * set the weight context
	 * 
	 * @param weightContext
	 *            , the weight context
	 */
	public void setWeightContext(String weightContext) {
		for (String ratingContext : Utils.ratingContext) {
			if (weightContext.equalsIgnoreCase(ratingContext)) {
				this.weightContext = ratingContext;
			}
		}
	}

	/**
	 * get the weight type
	 * 
	 * @return String, the weight type
	 */
	public String getWeightType() {
		return weightType;
	}

	/**
	 * set the weight type
	 * 
	 * @param weightType
	 */
	public void setWeightType(String weightType) {
		this.weightType = weightType;
	}

	/**
	 * This method returns the intersection of two Lists
	 * 
	 * @param list1
	 * @param list2
	 * @return List<?>
	 */
	public static List<?> intersection(List<?> list1, List<?> list2) {
		List<Object> list = new ArrayList<Object>();
		for (Object o : list1) {
			if (list2.contains(o))
				list.add(o);
		}
		return list;
	}

	/**
	 * This method returns a list containing the elements of a primary list that do not appear in a list of
	 * excluded elements
	 * 
	 * @param primaryList
	 *            , the list which elements are to be retained
	 * @param excludedList
	 *            , the list which elements shall not remain in the final list
	 * @return List<?>
	 */
	public static List<?> exclusion(List<?> primaryList, List<?> excludedList) {
		List<Object> list = new ArrayList<Object>();
		for (Object o : primaryList) {
			if (!excludedList.contains(o))
				list.add(o);
		}
		return list;
	}

	/**
	 * This method returns the union of two Lists
	 * 
	 * @param list1
	 * @param list2
	 * @return List<?>
	 */
	public static List<?> union(List<?> list1, List<?> list2) {
		Set<Object> set = new HashSet<Object>();
		set.addAll(list1);
		set.addAll(list2);
		return new ArrayList<Object>(set);
	}

	/**
	 * This method round a float value
	 * 
	 * @param floatToRound
	 *            the float number that will be rounded
	 * @param roundFactor
	 *            the power of 10 used to round the float, e.g. if roundFactor = 3, the float number will be
	 *            rounded with 10^3 as a multiplier
	 * @return float, the rounded value
	 */
	public static float roundFloat(float floatToRound, int roundFactor) {
		double roundedFloat = 0;
		double multiplier = Math.pow((double) 10, (double) roundFactor);

		roundedFloat = multiplier * floatToRound;
		roundedFloat = (int) (roundedFloat + .5);
		roundedFloat /= multiplier;
		return (float) roundedFloat;
	}

	/**
	 * This method round a double value
	 * 
	 * @param score
	 *            the double number that will be rounded
	 * @param roundFactor
	 *            the power of 10 used to round the float, e.g. if roundFactor = 3, the float number will be
	 *            rounded with 10^3 as a multiplier
	 * @return double, the rounded value
	 */
	public static double roundDouble(double score, int roundFactor) {
		double roundedDouble = 0;
		double multiplier = Math.pow((double) 10, (double) roundFactor);

		roundedDouble = multiplier * score;
		roundedDouble = (int) (roundedDouble + .5);
		roundedDouble /= multiplier;
		return (float) roundedDouble;
	}

	/**
	 * This method delete accent containing in a string
	 * 
	 * @param String
	 *            , the string candidate to delete accents
	 * @return String, the string without accents
	 */
	public static String unAccent(String s) {
		String temp = Normalizer.normalize(s, Normalizer.Form.NFC);
		return temp.replaceAll("[^\\p{ASCII}]", "");
	}
}
